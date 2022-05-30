package com.example.learningmate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.FormBody;
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
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HomeworkDetailActivity extends AppCompatActivity {

    private TextView title;
    private TextView body;
    private TextView dueDate;
    private TextView postedFileName;
    private Button submittedFileName;
    private Homework homework;
    private File file;
    private Submission submission;
    private long downloadId;
    private DownloadManager downloadManager;
    private TextView grade;
    public static final int REQUEST_CODE = 30;
    private String filePath;
    private Button submitButton;
    private String fileName;
    private boolean isSelectedFile = false;
    private JSONObject submissionData;
    private ArrayList<String> submissionInfo;
    private TextView submitDateTitle;
    private TextView submitDateContent;
    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.arg1 == 3) {
                submittedFileName.setText(fileName);
                return;
            }
            if (msg.arg1 == 1) {
                title.setText(homework.getTitle());
                dueDate.setText(homework.getDueDate());
                body.setText(homework.getBody());
                if (homework.getGradedScore() == -1) {
                    grade.setText("- / " + homework.getPerfectScore());
                } else {
                    grade.setText(homework.getGradedScore() + " / " + homework.getPerfectScore());
                }
                postedFileName.setText(file.getFileName());
                if (homework.getSubmitId() == null || homework.getSubmitId().equals("null")) {
                    submittedFileName.setText("파일 첨부하기");
                    submitDateContent.setVisibility(View.GONE);
                    submitDateTitle.setVisibility(View.GONE);
                }
                postedFileName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), FilePreviewWebViewActivity.class);
                        intent.putExtra("url", file.getFileUrl());
                        startActivity(intent);
                    }
                });
                postedFileName.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        Uri download = Uri.parse(file.getFileUrl());
                        DownloadManager.Request request = new DownloadManager.Request(download);
                        request.setTitle(file.getFileName());
                        request.setDescription("파일 다운로드");
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setAllowedOverRoaming(true);
                        request.setAllowedOverMetered(true);

                        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, file.getFileName());
                        }
                        downloadId = downloadManager.enqueue(request);

                        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
                        registerReceiver(downloadReceiver, intentFilter);
                        return true;
                    }
                });
            }
        }
    };
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
                    Toast.makeText(HomeworkDetailActivity.this, "다운로드 성공", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_detail);
        findViewById(R.id.back_ib).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Intent intent = getIntent();
        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        if (intent != null) {
            homework = (Homework) intent.getSerializableExtra("data");
        }
        title = findViewById(R.id.detail_homework_name_tv);
        dueDate = findViewById(R.id.detail_homework_duration_tv);
        body = findViewById(R.id.detail_homework_content_tv);
        postedFileName = findViewById(R.id.detail_content_file_name_tv);
        submittedFileName = findViewById(R.id.detail_homework_file_b);
        submitButton = findViewById(R.id.detail_submit_b);
        submitDateTitle = findViewById(R.id.detail_general_submit_date_tv);
        submitDateContent = findViewById(R.id.detail_general_submit_date_content_tv);
        submissionInfo = new ArrayList<>();
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!overSubmitDate()) {
                    if (isSelectedFile) {
                        java.io.File file = new java.io.File(filePath);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                RequestBody requestBody = new MultipartBody.Builder()
                                        .setType(MultipartBody.FORM)
                                        .addFormDataPart("submitter", User.currentUser.getUserId())
                                        .addFormDataPart("assign_id_submit", homework.getHomeworkId() + "")
                                        .addFormDataPart("fileToUpload", fileName, RequestBody.create(MultipartBody.FORM, file)).build();

                                Request request = new Request.Builder()
                                        .url("http://windry.dothome.co.kr/se_learning_mate/controller/assignment_controller.php")
                                        .post(requestBody)
                                        .build();

                                OkHttpClient client = new OkHttpClient();

                                try {
                                    Response response = client.newCall(request).execute();
                                    if (response.isSuccessful()) {
                                        ResponseBody body = response.body();
                                        if (body != null) {
                                            String data = body.string();
                                            Log.d("data", data);
                                            if (data.equals("1")) {
                                                handler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(HomeworkDetailActivity.this, "과제 제출 완료", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    }
                                                });

                                            } else {
                                                handler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(HomeworkDetailActivity.this, "과제 제출 실패", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }
                                    } else {
                                        Log.d("request", "failure");
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    } else {
                        Toast.makeText(HomeworkDetailActivity.this, "제출 파일이 선택되지 않았습니다!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(HomeworkDetailActivity.this, "제출기한이 지났습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        submittedFileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!overSubmitDate()) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(Intent.createChooser(intent, "File Select"), REQUEST_CODE);
                } else {
                    Toast.makeText(HomeworkDetailActivity.this, "제출기한이 지났습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        submittedFileName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (homework.getSubmitId() == null || homework.getSubmitId().equals("null")) {
                    return false;
                } else {
                    Uri download = Uri.parse(submissionData.optString("file_url"));
                    DownloadManager.Request request = new DownloadManager.Request(download);
                    request.setTitle(submissionData.optString("file_name"));
                    request.setDescription("파일 다운로드");
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setAllowedOverRoaming(true);
                    request.setAllowedOverMetered(true);

                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, submissionData.optString("file_name"));
                    }
                    downloadId = downloadManager.enqueue(request);

                    IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
                    registerReceiver(downloadReceiver, intentFilter);
                }
                return true;
            }
        });
        grade = findViewById(R.id.detail_homework_grade_tv);
        Log.d("sub", (homework.getSubmitId()));
        new Thread(new Runnable() {
            @Override
            public void run() {
                getFileInfo();
                if (homework.getSubmitId() == null || homework.getSubmitId().equals("null")) {
                } else {
                    getSubmissionInfo();
                }
            }
        }).start();

    }

    public void getSubmissionInfo() {
        try {
            String url = "http://windry.dothome.co.kr/se_learning_mate/controller/assignment_controller.php";
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("submit_id", homework.getSubmitId() + "")
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
                    if (data.contains("Not Found")) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(HomeworkDetailActivity.this, "파일을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        submissionData = new JSONObject(data);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                submittedFileName.setText(submissionData.optString("file_name"));
                                submitDateContent.setText(submissionData.optString("submit_date"));

                            }
                        });
                    }
                }
            } else {
                Log.d("response", "error");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getFileInfo() {
        try {
            String url = "http://windry.dothome.co.kr/se_learning_mate/controller/file_controller.php";
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("mentor_uid", User.currentUser.getPairId())
                    .add("file_id", homework.getFileId() + "")
                    .add("category", "0")
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
                    if (data.equals("Not Found File Data")) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(HomeworkDetailActivity.this, "파일을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    } else {
                        JSONObject jsonObject = new JSONObject(data);
                        file = new File(
                                jsonObject.get("file_id").toString(),
                                jsonObject.get("file_uploader").toString(),
                                Integer.parseInt(jsonObject.get("file_category").toString()),
                                jsonObject.get("upload_date").toString(),
                                jsonObject.get("file_name").toString(),
                                jsonObject.get("file_hash_name").toString(),
                                jsonObject.get("file_url").toString(),
                                jsonObject.get("file_size").toString()
                        );
                        Message message = handler.obtainMessage();
                        message.arg1 = 1;
                        handler.sendMessage(message);
                    }

                    Log.d("data", data);
                }
            } else {
                Log.d("response", "error");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                filePath = getDriveFilePath(uri, this);
                Message message = handler.obtainMessage();
                message.arg1 = 3;
                handler.sendMessage(message);
                isSelectedFile = true;
            } else if (resultCode == RESULT_CANCELED) {
                submittedFileName.setText("선택되지 않음");
                isSelectedFile = false;
            }
        }
    }

    private String getDriveFilePath(Uri uri, Context context) {
        Cursor returnCursor = context.getContentResolver().query(uri, null, null, null, null);
        /*
         * Get the column indexes of the data in the Cursor,
         *     * move to the first row in the Cursor, get the data,
         *     * and display it.
         * */
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();

        String name = (returnCursor.getString(nameIndex));
        fileName = name;
        String size = (Long.toString(returnCursor.getLong(sizeIndex)));
        java.io.File file = new java.io.File(context.getCacheDir(), name);
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(file);
            int read = 0;
            int maxBufferSize = 1 * 1024 * 1024;
            int bytesAvailable = inputStream.available();

            //int bufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            final byte[] buffers = new byte[bufferSize];
            while ((read = inputStream.read(buffers)) != -1) {
                outputStream.write(buffers, 0, read);
            }
            Log.d("File Size", "Size " + file.length());
            inputStream.close();
            outputStream.close();
            Log.d("File Path", "Path " + file.getPath());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        return file.getPath();
    }

    public boolean overSubmitDate() {
        try {
            Date current = new Date();
            Date now;
            Date due;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            now = f.parse(homework.getDueDate());
            due = f.parse(simpleDateFormat.format(current));
            if (now.compareTo(due) > 0) {
                return false;
            } else {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}