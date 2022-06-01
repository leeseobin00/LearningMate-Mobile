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

public class LibraryCreateActivity extends AppCompatActivity {

    private EditText materialName;
    private TextView selectedFile;
    private Button uploadButton;
    private String filePath;
    private String fileName;
    public static final int REQUEST_CODE = 20;
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
        setContentView(R.layout.activity_library_create);
        materialName = findViewById(R.id.file_material_name_et);
        selectedFile = findViewById(R.id.file_homework_file_info_tv);
        uploadButton = findViewById(R.id.material_upload_b);
        findViewById(R.id.back_ib).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isSelectFile){
                    Toast.makeText(LibraryCreateActivity.this, "파일이 선택되지 않았습니다!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(materialName.getText().toString().isEmpty()){
                    Toast.makeText(LibraryCreateActivity.this, "자료명을 입력하세요!", Toast.LENGTH_SHORT).show();
                    return;
                }
                File file = new File(filePath);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        RequestBody requestBody = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("create_uid", User.currentUser.getUserId())
                                .addFormDataPart("title", materialName.getText().toString())
                                .addFormDataPart("fileToUpload", fileName, RequestBody.create(MultipartBody.FORM, file))
                                .build();

                        Request request = new Request.Builder()
                                .url("http://windry.dothome.co.kr/se_learning_mate/controller/material_controller.php")
                                .post(requestBody)
                                .build();

                        OkHttpClient client = new OkHttpClient();

                        try {
                            Response response = client.newCall(request).execute();
                            if (response.isSuccessful()) {
                                ResponseBody body = response.body();
                                if (body != null) {
                                    String data = body.string();
                                    if (data.equals("1")) {
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(LibraryCreateActivity.this, "자료 업로드 완료", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        });
                                    } else {
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(LibraryCreateActivity.this, "자료 업로드에 실패하였습니다.", Toast.LENGTH_SHORT).show();
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
            Log.d("File Size", "Size " + file.length());
            inputStream.close();
            outputStream.close();
            Log.d("File Path", "Path " + file.getPath());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        return file.getPath();
    }

}