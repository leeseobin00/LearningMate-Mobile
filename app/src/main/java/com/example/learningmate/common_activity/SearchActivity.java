package com.example.learningmate.common_activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.learningmate.R;
import com.example.learningmate.adapter.SearchRVAdapter;
import com.example.learningmate.model.User;

import org.json.JSONArray;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private ArrayList<User> filteredList, userArrayList;
    private SearchRVAdapter searchRVAdapter;
    private RecyclerView userRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private EditText searchIdEt;
    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.arg1 == 2) {
                Toast.makeText(SearchActivity.this, (User.currentUser.getIdentity() == 0 ? "멘토" : "멘티") + " 등록이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            if (msg.arg1 == 1) {
                searchRVAdapter = new SearchRVAdapter(userArrayList);
                userRecyclerView.setAdapter(searchRVAdapter);
                searchRVAdapter.setOnItemClickListener(new SearchRVAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClicked(int position, User user) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
                        builder.setTitle(User.currentUser.getIdentity() == 0 ? "멘토 연결하기" : "멘티 연결하기")
                                .setMessage(user.getUserName() + "님과 정말 연결하시겠습니까?")
                                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                updatePairRequest(user.getUserId());
                                            }
                                        }).start();
                                    }
                                })
                                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                });
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        findViewById(R.id.back_ib).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        searchIdEt = findViewById(R.id.search_id_et);
        filteredList = new ArrayList<>();
        userArrayList = new ArrayList<>();
        userRecyclerView = findViewById(R.id.search_rv);
        userRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        userRecyclerView.setLayoutManager(layoutManager);
        new Thread(new Runnable() {
            @Override
            public void run() {
                getAllUsers();
            }
        }).start();
        searchIdEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String searchId = searchIdEt.getText().toString();
                searchFilter(searchId);
            }
        });
    }

    public void searchFilter(String searchText) {
        filteredList.clear();
        for (int i = 0; i < userArrayList.size(); i++) {
            if (userArrayList.get(i).getUserId().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(userArrayList.get(i));
            }
        }
        searchRVAdapter.filterList(filteredList);
    }

    public void getAllUsers() {
        try {
            String url = "http://windry.dothome.co.kr/se_learning_mate/controller/account_controller.php";
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("senderIdt", User.currentUser.getIdentity() + "")
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
                    JSONArray jsonArray = new JSONArray(data);
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                        userArrayList.add(new User(jsonObject.get("uid").toString()
                                , jsonObject.get("userName").toString()
                                , Integer.parseInt(jsonObject.get("identity").toString())
                                , jsonObject.get("pair_uid").toString()
                                , jsonObject.get("rDate").toString()
                        ));
                    }
                    Message message = handler.obtainMessage();
                    message.arg1 = 1;
                    handler.sendMessage(message);
                }
            } else {
                Log.d("response", "error");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatePairRequest(String targetUid) {
        try {
            String url = "http://windry.dothome.co.kr/se_learning_mate/controller/account_controller.php";
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("myUid", User.currentUser.getUserId())
                    .add("targetUid", targetUid)
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
                    if (data.equals("2")) {
                        User.currentUser.setPairId(targetUid);
                        Message message = handler.obtainMessage();
                        message.arg1 = 2;
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

}
