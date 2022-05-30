package com.example.learningmate.mentor_activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.learningmate.common_activity.FilePreviewWebViewActivity;
import com.example.learningmate.R;
import com.example.learningmate.adapter.LibraryRVAdapter;
import com.example.learningmate.model.Library;
import com.example.learningmate.model.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class LibraryMentorActivity extends AppCompatActivity {
    ImageButton addButton;
    ImageButton removeButton;
    RecyclerView libraryRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    private ArrayList<Library> libraryArrayList;
    private LibraryRVAdapter libraryRVAdapter;
    private DownloadManager downloadManager;
    private long downloadId;
    private final BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadId);
            Cursor cursor = downloadManager.query(query);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                int status = cursor.getInt(columnIndex);
                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    Toast.makeText(LibraryMentorActivity.this, "다운로드 성공", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.arg1 == 1) {
                libraryRVAdapter = new LibraryRVAdapter(libraryArrayList);
                libraryRecyclerView.setAdapter(libraryRVAdapter);

                libraryRVAdapter.setOnItemClickListener(new LibraryRVAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        Intent intent = new Intent(getApplicationContext(), FilePreviewWebViewActivity.class);
                        intent.putExtra("url", libraryArrayList.get(position).getFileUrl());
                        startActivity(intent);
                    }
                });

                libraryRVAdapter.setOnItemLongClickListener(new LibraryRVAdapter.OnItemLongClickListener() {
                    @Override
                    public void onItemLongClick(View v, int position) {
                        Uri download = Uri.parse(libraryArrayList.get(position).getFileUrl());
                        DownloadManager.Request request = new DownloadManager.Request(download);
                        request.setTitle(libraryArrayList.get(position).getFileName());
                        request.setDescription("파일 다운로드");
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setAllowedOverRoaming(true);
                        request.setAllowedOverMetered(true);

                        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, libraryArrayList.get(position).getFileName());
                        }
                        downloadId = downloadManager.enqueue(request);

                        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
                        registerReceiver(downloadReceiver, intentFilter);
                    }
                });
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_mentor);
        findViewById(R.id.back_ib).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

        libraryRecyclerView = findViewById(R.id.library_mentor_rv);

        libraryRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        libraryRecyclerView.setLayoutManager(layoutManager);
        libraryArrayList = new ArrayList<>();

        addButton = findViewById(R.id.library_add_ib);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LibraryCreateActivity.class));
            }
        });
        removeButton = findViewById(R.id.library_remove_ib);
        new Thread(new Runnable() {
            @Override
            public void run() {
                getLibraryList();
            }
        }).start();
    }


    public void getLibraryList() {
        try {
            String url = "http://windry.dothome.co.kr/se_learning_mate/controller/material_controller.php";
            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder()
                    .add("get_uid", User.currentUser.getUserId())
                    .build();

            Request request = new Request
                    .Builder()
                    .url(url)
                    .post(formBody).build();

            Response response = client
                    .newCall(request)
                    .execute();

            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                if (body != null) {
                    String data = body.string();
                    Log.d("data", data);
                    if (data.equals("None")) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LibraryMentorActivity.this, "자료가 없습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        libraryArrayList.clear();
                        JSONArray jsonArray = new JSONArray(data);
                        for (int i = 0; i < jsonArray.length(); ++i) {
                            JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                            libraryArrayList.add(new Library(jsonObject.optString("title")
                                    , jsonObject.optString("file_name")
                                    , jsonObject.optString("file_url")
                            ));
                        }
                        Message message = handler.obtainMessage();
                        message.arg1 = 1;
                        handler.sendMessage(message);
                    }
                }
            } else {
                Log.d("response", "error");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getLibraryList();
            }
        }).start();
    }
}