package org.tensorflow.lite.examples.detection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class History extends AppCompatActivity implements AlertAdapter.OnItemClickListener{

    private FloatingActionButton floatingButton;
    private RecyclerView alertList;
    private RecycleItApi recycleItApi;
    SharedPreferences sharedpreferences;
    private ProgressBar loadProgress;
    ArrayList<Alert> list;
    private View.OnClickListener buttonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getApplicationContext(), DetectorActivity.class));
            overridePendingTransition(0,0);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        floatingButton = findViewById(R.id.floatingActionButton);
        floatingButton.setOnClickListener(buttonOnClickListener);
        alertList = findViewById(R.id.alertlist);
        alertList.setLayoutManager(new LinearLayoutManager(this));
        recycleItApi = RecycleItApi.retrofit.create(RecycleItApi.class);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_history);
        sharedpreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        loadProgress = findViewById(R.id.progressBar);
        list = new ArrayList<>();
        getDisposal();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.navigation_cam:
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_goal:
                        startActivity(new Intent(getApplicationContext(), Goals.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_stats:
                        startActivity(new Intent(getApplicationContext(), Stats.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_history:
                        startActivity(new Intent(getApplicationContext(), History.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_requests:
                        startActivity(new Intent(getApplicationContext(), Requests.class));
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });
    }
    public void getDisposal(){
        Call<ArrayList<Alert>> call = recycleItApi.getPersonalDisposal(sharedpreferences.getString("email",""));
        call.enqueue(new Callback<ArrayList<Alert>>() {
            @Override
            public void onResponse(Call<ArrayList<Alert>> call, Response<ArrayList<Alert>> response) {
                loadProgress.setVisibility(View.GONE);
                list = response.body();
                alertList.setAdapter(new AlertAdapter(list, History.this));

            }

            @Override
            public void onFailure(Call<ArrayList<Alert>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed to load",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(History.this);
        builder1.setMessage(processItemList(list.get(position).getItemList()));
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public String processItemList(String item){
        String items[] = item.split(",");
        HashMap<String, Integer> count = new HashMap<>();
        String result = "";

        for(int i = 0; i < items.length; i++){
            if(count.containsKey(items[i])){
                count.put(items[i], count.get(items[i])+1);
            }else{
                count.put(items[i], 1);
            }

        }
        for (Map.Entry<String, Integer> entry : count.entrySet()) {
            String[] name = entry.getKey().split("\\(");
            if(entry.getValue() == 1){
                result += entry.getValue() + " " + name[1].substring(0, name[1].length()-1) + " ";
            }else{
                result += entry.getValue() + " " + name[1].substring(0, name[1].length()-1) + "s ";
            }
        }

        return result;
    }
}
