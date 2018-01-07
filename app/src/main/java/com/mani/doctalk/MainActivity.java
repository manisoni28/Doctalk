package com.mani.doctalk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import models.GitResult;
import models.Item;
import rest.RestClient;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

public class MainActivity extends AppCompatActivity {

    private UserAdapter adapter ;
    List<Item> Users ;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView listView = (ListView) findViewById(R.id.listView);
        Users = new ArrayList<Item>();

        Intent i=getIntent();
        i.getExtras();
        name = i.getStringExtra("name_");
        final ProgressDialog dialog = ProgressDialog.show(this, "", "loading...");
        RestClient.GitApiInterface service = RestClient.getClient();
        Call<GitResult> call = service.getUsersNamedTom(name+"sort:followers");
        call.enqueue(new Callback<GitResult>() {
            @Override
            public void onResponse(Response<GitResult> response) {
                dialog.dismiss();
                //Toast.makeText(MainActivity.this,"Hi "+response.toString(),Toast.LENGTH_LONG).show();
                Log.d("MainActivity", "Status Code = " + response.code());
                if (response.isSuccess()) {
                    // request successful (status code 200, 201)
                    GitResult result = response.body();
                    Log.d("MainActivity", "response = " + new Gson().toJson(result));
                    Users = result.getItems();
                    Log.d("MainActivity", "Items = " + Users.size());
                    adapter = new UserAdapter(MainActivity.this, Users);
                    listView.setAdapter(adapter);
                } else {
                    // response received but request not successful (like 400,401,403 etc)
                    //Handle errors

                }
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
            }
        });
    }

}
