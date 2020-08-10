package org.tensorflow.lite.examples.detection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Requests extends AppCompatActivity implements AlertConfirmationAdapter.OnItemClickListener{

    private FloatingActionButton floatingButton;
    private RecyclerView alertList;
    private RecycleItApi recycleItApi;
    SharedPreferences sharedpreferences;
    private ProgressBar loadProgress;
    ArrayList<AlertConfirmation> list;
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
        setContentView(R.layout.activity_requests);

        floatingButton = findViewById(R.id.floatingActionButton);
        floatingButton.setOnClickListener(buttonOnClickListener);
        recycleItApi = RecycleItApi.retrofit.create(RecycleItApi.class);
        alertList = findViewById(R.id.alertlist);
        alertList.setLayoutManager(new LinearLayoutManager(this));
        sharedpreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        loadProgress = findViewById(R.id.progressBar);
        list = new ArrayList<>();
        getAlertConfirmations();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.navigation_requests);

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

    public void getAlertConfirmations(){
        Call<ArrayList<AlertConfirmation>> call = recycleItApi.getAlertConfirmations(sharedpreferences.getString("email",""));
        call.enqueue(new Callback<ArrayList<AlertConfirmation>>() {
            @Override
            public void onResponse(Call<ArrayList<AlertConfirmation>> call, Response<ArrayList<AlertConfirmation>> response) {
                loadProgress.setVisibility(View.GONE);
                list = response.body();
                if(list == null){
                    list = new ArrayList<>();
                }
                alertList.setAdapter(new AlertConfirmationAdapter(list, Requests.this));

            }

            @Override
            public void onFailure(Call<ArrayList<AlertConfirmation>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed to load",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void sendResponse(int id, String response){
        Call<ResponseBody> call = recycleItApi.response(id, response);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(getApplicationContext(), "Response sent",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed to load",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        LayoutInflater inflater = LayoutInflater.from(Requests.this);
        View view1 = inflater.inflate(R.layout.query_dialog, null);
        TextView explanation = view1.findViewById(R.id.textView3);
        explanation.setText("An organization claimed to handle the following alert issued by you on "+list.get(position).getDate()+" which included the following");
        TextView item1 = view1.findViewById(R.id.textview_name);
        TextView item2 = view1.findViewById(R.id.textview_name1);
        TextView item3 = view1.findViewById(R.id.textview_name2);

        TextView[] textviews = new TextView[3];
        textviews[0] = item1;
        textviews[1] = item2;
        textviews[2] = item3;

        String[] items = list.get(position).getItemList().split(",");
        HashMap<String, Integer> map = count(items);
        int size = Math.min(3, map.size());

        Iterator hmIterator = map.entrySet().iterator();
        int i = 0;
        while (i < size) {
            Map.Entry mapElement = (Map.Entry)hmIterator.next();
            int count = ((int)mapElement.getValue());
            textviews[i].setText(count + " " + mapElement.getKey());
            i++;
        }
        if(i < 3){
            textviews[i].setVisibility(View.INVISIBLE);
            i++;
        }

        if(i < 3){
            textviews[i].setVisibility(View.INVISIBLE);
            i++;
        }
        Button okButton = view1.findViewById(R.id.button5);
        Button noButton = view1.findViewById(R.id.button6);
        Button unsureButton = view1.findViewById(R.id.button7);

        AlertDialog alertDialog = new AlertDialog.Builder(Requests.this)
                .setView(view1)
                .create();

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendResponse(list.get(position).getId(), "H");
                alertDialog.dismiss();
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendResponse(list.get(position).getId(), "UH");
                alertDialog.dismiss();
            }
        });

        unsureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendResponse(list.get(position).getId(), "IN");
                alertDialog.dismiss();
            }
        });

        alertDialog.show();

    }

    public HashMap<String, Integer> count(String[] items){
        HashMap<String, Integer> map = new HashMap<>();
        for(int i = 0; i < items.length; i++){
            String key = items[i].split("\\(")[1].substring(0,items[i].split("\\(")[1].length()-1);
            if(map.containsKey(key)){
                map.put(key, map.get(key) + 1);
            }else{
                map.put(key, 1);
            }
        }

        return map;
    }
}
