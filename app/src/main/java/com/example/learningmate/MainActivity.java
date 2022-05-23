package com.example.learningmate;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {
    public static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        new Thread(new Runnable() {
            @Override
            public void run() {
                //Log.d("request result", postUserInfo() + "");
            }
        }).start();
    }

    public boolean postUserInfo(String id, String pw, String name, String iden) {
        try {
            String url = "http://windry.dothome.co.kr/se_learning_mate/controller/account_controller.php";
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("signin_uid", id)
                    .add("signin_password", pw)
                    .add("userName", name)
                    .add("identity", iden)
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
                    Log.d("response", body.string());
                }
            } else {
                Log.d("response", "error");
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
