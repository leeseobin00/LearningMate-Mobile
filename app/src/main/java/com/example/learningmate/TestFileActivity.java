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
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
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
    private String fileName = "ec32d6ee67cc518f47dea67c4996b88b.pdf";
    private String fileExtend = "pdf";
    private ProgressBar progressBar;
    private Button realDownloadButton;
    private Button getFileButton;
    private Button uploadFileButton;
    private DownloadManager downloadManager;
    private long id;
    private String originalFileName = "YOLOv3.pdf";
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
                if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, originalFileName);
                }
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
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse(fileUrl + "/" + fileName));
//                startActivity(intent);
                startActivity(new Intent(TestFileActivity.this, FilePreviewWebViewActivity.class));
                /*
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        File file = new File(fileUrl+"/"+fileName);
                        Uri uri = Uri.fromFile(file);

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(uri, "application/pdf");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }).start();


                 */
                /*
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

                 */
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
                Log.d("local", localPath);
                Log.d("path?", file.getAbsolutePath());
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
//            String path = getPath(this, uri);
//            Log.d("path", path);
            // 파일의 경로 가져오기
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            String wholeID = DocumentsContract.getDocumentId(uri);
            String[] splits = wholeID.split(":");
            String id = splits[1];
            Log.d("uri", uri.getPath());
            Log.d("id", id+"");
            String sel = MediaStore.Images.Media._ID + "=?";
            String newPath = getDriveFilePath(uri, this);
//            String selection = MediaStore.Files.FileColumns._ID + " = " + id;
            Cursor cursor1 = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, filePathColumn, sel, new String[]{id}, null);

            String filePath = "";
            Log.d("length", cursor1.getColumnCount()+"");
            if (cursor1.moveToFirst()) {
                int cIndex = cursor1.getColumnIndex(filePathColumn[0]);
                filePath = cursor1.getString(cIndex);
            }
            Log.d("path", filePath);
            // 파일 이름 가져오기
            String name = "";
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                name = cursor.getString(nameIndex);
            }
            String finalName = name;

            // 파일 데이터 생성
            File sourceFile = new File(newPath);
//            if (!sourceFile.exists()) {
//                sourceFile.mkdir();
//            }


            new Thread(new Runnable() {
                @Override
                public void run() {
                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("info", "test16")
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
    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    Log.d("dwfqw",contentUri.getPath());
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Log.d("uri", uri.toString());
        Log.d("select", selection);
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                String a = cursor.getString(column_index);
                return a;
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private String getDriveFilePath(Uri uri, Context context) {
        Uri returnUri = uri;
        Cursor returnCursor = context.getContentResolver().query(returnUri, null, null, null, null);
        /*
         * Get the column indexes of the data in the Cursor,
         *     * move to the first row in the Cursor, get the data,
         *     * and display it.
         * */
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();

        String name = (returnCursor.getString(nameIndex));
        String size = (Long.toString(returnCursor.getLong(sizeIndex)));
        File file = new File(context.getCacheDir(), name);
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
            Log.e("File Size", "Size " + file.length());
            inputStream.close();
            outputStream.close();
            Log.e("File Path", "Path " + file.getPath());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        return file.getPath();
    }

}