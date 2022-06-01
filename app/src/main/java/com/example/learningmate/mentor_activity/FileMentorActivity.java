package com.example.learningmate.mentor_activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learningmate.R;
import com.example.learningmate.model.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileMentorActivity extends AppCompatActivity {

    private TextView selectedFile;
    private String filePath;
    private String fileName;
    private Button submitButton;
    private EditText title;
    private EditText dueDate;
    private EditText body;
    private EditText score;
    public static final int REQUEST_CODE = 50;
    private boolean isSelectFile = false;
    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.arg1 == 1) {
                selectedFile.setText(fileName);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_mentor);
        findViewById(R.id.back_ib).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        selectedFile = findViewById(R.id.file_homework_file_info_tv);
        submitButton = findViewById(R.id.file_submit_b);
        title = findViewById(R.id.file_homework_name_et);
        dueDate = findViewById(R.id.file_homework_duration_et);
        body = findViewById(R.id.file_homework_info_et);
        score = findViewById(R.id.file_homework_score_et);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dueDate.getText().toString().length() != 12) {
                    Toast.makeText(FileMentorActivity.this, "날짜형식이 맞지 않습니다!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isSelectFile) {
                    Toast.makeText(FileMentorActivity.this, "파일을 첨부해주세요!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (title.getText().toString().isEmpty()) {
                    Toast.makeText(FileMentorActivity.this, "과제명을 입력하세요!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (body.getText().toString().isEmpty()) {
                    Toast.makeText(FileMentorActivity.this, "설명을 작성하세요!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (score.getText().toString().isEmpty()) {
                    Toast.makeText(FileMentorActivity.this, "만점 점수를 입력하세요!", Toast.LENGTH_SHORT).show();
                    return;
                }
                File sourceFile = new File(filePath);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        RequestBody requestBody = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("mUser", User.currentUser.getUserId())
                                .addFormDataPart("m_title", title.getText().toString())
                                .addFormDataPart("m_content", body.getText().toString())
                                .addFormDataPart("m_perfect_score", score.getText().toString())
                                .addFormDataPart("m_due_date", formatDate(dueDate.getText().toString()))
                                .addFormDataPart("fileToUpload", fileName, RequestBody.create(MultipartBody.FORM, sourceFile)).build();

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
                                    if (data.equals("Created Assignment")) {
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(FileMentorActivity.this, "새로운 과제가 등록되었습니다.", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        });
                                    } else {
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(FileMentorActivity.this, "과제가 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
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
            }
        });
        selectedFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(intent, "File Select"), REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                filePath = getDriveFilePath(uri, this);
                isSelectFile = true;
                Message message = handler.obtainMessage();
                message.arg1 = 1;
                handler.sendMessage(message);
            } else if (resultCode == RESULT_CANCELED) {
                selectedFile.setText("선택되지 않음");
                isSelectFile = false;
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
        java.io.File file = new File(context.getCacheDir(), name);
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
            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        return file.getPath();
    }

    private String formatDate(String rawDate) {
        String date = rawDate.substring(0, 8);
        String time = rawDate.substring(8);
        String y = date.substring(0, 4);
        String M = date.substring(4, 6);
        String d = date.substring(6, 8);
        String h = time.substring(0, 2);
        String m = time.substring(2);
        return y + "-" + M + "-" + d + " " + h + ":" + m;
    }
}