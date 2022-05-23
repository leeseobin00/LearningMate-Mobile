package com.example.learningmate;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    ArrayList<User> filteredList, userArrayList;
    SearchRVAdapter searchRVAdapter;
    RecyclerView userRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    EditText searchIdEt;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ImageButton searchbutton = findViewById(R.id.search_ib);
        searchIdEt = findViewById(R.id.search_id_et);

        filteredList = new ArrayList<>();
        userArrayList = new ArrayList<>();

        userRecyclerView = findViewById(R.id.search_rv);
        userRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        userRecyclerView.setLayoutManager(layoutManager);


        userArrayList.add(new User("이서현", "chaconne"));
        userArrayList.add(new User("이서빈", "choco"));
        userArrayList.add(new User("신승건", "seunggun"));
        userArrayList.add(new User("이민아", "minah"));

        searchRVAdapter = new SearchRVAdapter(userArrayList);
        userRecyclerView.setAdapter(searchRVAdapter);

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

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void searchFilter(String searchText){
        filteredList.clear();

        for(int i = 0; i<userArrayList.size(); i++){
            if(userArrayList.get(i).getUserId().toLowerCase().contains(searchText.toLowerCase())){
                filteredList.add(userArrayList.get(i));
            }
        }
        searchRVAdapter.filterList(filteredList);
    }
}
