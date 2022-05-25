package com.example.learningmate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.MultipartBody;
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
import android.os.ParcelFileDescriptor;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class TestFileActivity extends AppCompatActivity {

    private String mentorUid;
    private String saveFolder = "/mydown";
    private String savePath;
    private String fileUrl = "http://windry.dothome.co.kr/se_learning_mate/files";
    private String fileName = "657c6223051c6e2e2c48c43de4a3109a.zip";
    private String fileExtend = "zip";
    private ProgressBar progressBar;
    private Button realDownloadButton;
    private Button getFileButton;
    private Button uploadFileButton;
    private DownloadManager downloadManager;
    private long id;

    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            progressBar.setVisibility(View.GONE);
            showDownloadFile();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_file);
        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
//        if(User.currentUser.getIdentity() == 1){
//            mentorUid = User.currentUser.getUserId();
//        }
//        else{
//            mentorUid = User.currentUser.getPairId();
//        }
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        progressBar = findViewById(R.id.loading);
        uploadFileButton = findViewById(R.id.upload_file);
        getFileButton = findViewById(R.id.get_file);
        getFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(intent, "File Select"), 5);
            }
        });
        Button button = findViewById(R.id.download_btn);
        realDownloadButton = findViewById(R.id.real_download);
        realDownloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri download = Uri.parse(fileUrl + "/" + fileName);
                DownloadManager.Request request = new DownloadManager.Request(download);
//                long id = downloadManager.enqueue(request);
                request.setTitle(originalFileName);
                request.setDescription("항목 설명");
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setAllowedOverRoaming(true)
                        .setAllowedOverMetered(true);
                id = downloadManager.enqueue(request);

                IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED);
                notiReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        String extraId = DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS;
                        long[] references = intent.getLongArrayExtra(extraId);
                        for (long reference : references) {

                        }
                    }
                };
                IntentFilter intentFilter1 = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
                BroadcastReceiver complete = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {

                    }
                };

                IntentFilter intentFilter2 = new IntentFilter(DownloadManager.ACTION_VIEW_DOWNLOADS);
//                registerReceiver(notiReceiver, intentFilter);
                registerReceiver(downloadReceiver, intentFilter1);
//                registerReceiver(complete, intentFilter2);

            }
        });
        String ext = Environment.getExternalStorageState();
        if (ext.equals(Environment.MEDIA_MOUNTED)) {
            savePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + saveFolder;
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                java.io.File dir = new java.io.File(savePath);
                if (!dir.exists()) {
                    dir.mkdir();
                }

                if (!new java.io.File(savePath + "/" + fileName).exists()) {
                    progressBar.setVisibility(View.VISIBLE);
                    new DownloadThread(fileUrl + "/" + fileName, savePath + "/" + fileName).start();
                } else {
                    showDownloadFile();
                }
            }
        });
    }

    private BroadcastReceiver notiReceiver;
    private final BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(id);
            Cursor cursor = downloadManager.query(query);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                int status = cursor.getInt(columnIndex);
                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    try {
                        ParcelFileDescriptor file = downloadManager.openDownloadedFile(id);
                        FileInputStream fileInputStream = new ParcelFileDescriptor.AutoCloseInputStream(file);

                        Toast.makeText(TestFileActivity.this, "다운로드 성공", Toast.LENGTH_SHORT).show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    public class DownloadThread extends Thread {
        String serverUrl;
        String localPath;

        public DownloadThread(String serverUrl, String localPath) {
            this.serverUrl = serverUrl;
            this.localPath = localPath;
        }

        @Override
        public void run() {
            URL imgUrl;
            int read;
            try {
                imgUrl = new URL(serverUrl);
                HttpURLConnection connection = (HttpURLConnection) imgUrl.openConnection();
                int len = connection.getContentLength();

                byte[] tmpByte = new byte[len];
                InputStream inputStream = connection.getInputStream();
                java.io.File file = new java.io.File(localPath);
                FileOutputStream fileOutputStream = new FileOutputStream(file);

                while (true) {
                    read = inputStream.read(tmpByte);
                    if (read <= 0) {
                        break;
                    }
                    fileOutputStream.write(tmpByte, 0, read);
                }
                inputStream.close();
                fileOutputStream.close();
                connection.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            handler.sendEmptyMessage(0);
        }
    }

    private void showDownloadFile() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        java.io.File file = new java.io.File(savePath + "/" + fileName);

        if (fileExtend.equals("mp3")) {
            intent.setDataAndType(Uri.fromFile(file), "audio/*");
        } else if (fileExtend.equals("mp4")) {
            intent.setDataAndType(Uri.fromFile(file), "video/*");
        } else if (fileExtend.equals("jpg") || fileExtend.equals("jpeg")
                || fileExtend.equals("JPG") || fileExtend.equals("gif")
                || fileExtend.equals("png") || fileExtend.equals("bmp")) {
            intent.setDataAndType(Uri.fromFile(file), "image/*");
        } else if (fileExtend.equals("txt")) {
            intent.setDataAndType(Uri.fromFile(file), "text/*");
        } else if (fileExtend.equals("doc") || fileExtend.equals("docx")) {
            intent.setDataAndType(Uri.fromFile(file), "application/msword");
        } else if (fileExtend.equals("xls") || fileExtend.equals("xlsx")) {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.ms-excel");
        } else if (fileExtend.equals("ppt") || fileExtend.equals("pptx")) {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.ms-powerpoint");
        } else if (fileExtend.equals("pdf")) {
            intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        } else if (fileExtend.equals("zip")) {
            intent.setDataAndType(Uri.fromFile(file), "application/zip");
        }
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5) {
            Uri uri = data.getData();

            // 파일의 경로 가져오기
            String[] filePathColumn = {MediaStore.MediaColumns.DATA};
            String selection = MediaStore.Files.FileColumns._ID + " = " + id;
            Cursor cursor1 = getContentResolver().query(MediaStore.Files.getContentUri("external"), filePathColumn, selection, null, null);

            String filePath = "";
            if (cursor1.moveToFirst()) {
                int cIndex = cursor1.getColumnIndex(filePathColumn[0]);
                filePath = cursor1.getString(cIndex);
            }

            // 파일 이름 가져오기
            String name = "";
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                name = cursor.getString(nameIndex);
            }
            String finalName = name;

            // 파일 데이터 생성
            File sourceFile = new File(filePath);
            if (!sourceFile.exists()) {
                sourceFile.mkdir();
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("info", "")
                            .addFormDataPart("fileToUpload", finalName, RequestBody.create(MultipartBody.FORM, sourceFile)).build();

                    Request request = new Request.Builder()
                            .url("http://windry.dothome.co.kr/se_learning_mate/controller/file_controller.php")
                            .post(requestBody)
                            .build();
                    OkHttpClient client = new OkHttpClient();
                    try {
                        Response response = client.newCall(request).execute();
                        if (response.isSuccessful()) {
                            ResponseBody body = response.body();
                            if (body != null) {
                                String data = body.string();
                                Log.d("dataaaa", data);
                            }
                        } else {
                            Log.d("request", "failure");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}