package com.example.myhomework;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.myhomework.adapter.ItemListener;
import com.example.myhomework.adapter.UserAdapter;
import com.example.myhomework.api.ApiClient;
import com.example.myhomework.api.ApiInterface;
import com.example.myhomework.model.UserData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ItemListener {

    private UserAdapter userAdapter;
    private RecyclerView recyclerView;
    private ArrayList<UserData> mUserDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<UserData>> call = apiInterface.getAllUser();
        call.enqueue(new Callback<List<UserData>>() {
            @Override
            public void onResponse(Call<List<UserData>> call, Response<List<UserData>> response) {
                int status = response.code();
                mUserDataList = (ArrayList<UserData>) response.body();
                setRecyclerView(mUserDataList);
            }

            @Override
            public void onFailure(Call<List<UserData>> call, Throwable t) {
                Log.d("Yo", "Errror!");
            }
        });
    }

    private void setRecyclerView(ArrayList<UserData> list) {
        userAdapter = new UserAdapter(this,list,this);
        recyclerView.setAdapter(userAdapter);
    }

    @Override
    public void ItemListener(int pos) {
        Intent intent = new Intent();
        intent.setClass(this,DetailActivity.class);
        intent.putExtra("username",mUserDataList.get(pos).getLogin());
        startActivity(intent);
    }
}