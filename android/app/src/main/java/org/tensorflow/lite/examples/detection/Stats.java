package org.tensorflow.lite.examples.detection;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.preference.PowerPreference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import im.dacer.androidcharts.BarView;
import im.dacer.androidcharts.LineView;
import im.dacer.androidcharts.PieHelper;
import im.dacer.androidcharts.PieView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Stats extends AppCompatActivity {

    private FloatingActionButton floatingButton;
    private RecycleItApi recycleItApi;
    ArrayList<Alert> list;
    private ProgressBar loadProgress;
    SharedPreferences sharedpreferences;
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
        setContentView(R.layout.activity_stats);

        floatingButton = findViewById(R.id.floatingActionButton);
        floatingButton.setOnClickListener(buttonOnClickListener);
        loadProgress = findViewById(R.id.progressBar);
        recycleItApi = RecycleItApi.retrofit.create(RecycleItApi.class);
        LineView lineView = (LineView)findViewById(R.id.line_view);
        lineView.setDrawDotLine(true); //optional
        lineView.setShowPopup(LineView.SHOW_POPUPS_MAXMIN_ONLY); //optional
        lineView.setVisibility(View.INVISIBLE);

        BarView barView = (BarView)findViewById(R.id.bar_view);
        barView.setVisibility(View.INVISIBLE);

        PieView pieView = (PieView)findViewById(R.id.pie_view);
        pieView.setVisibility(View.INVISIBLE);

        sharedpreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        list = new ArrayList<>();

        getDisposal(lineView, barView, pieView);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.navigation_stats);

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

    public void getDisposal(LineView lineView, BarView barView, PieView pieView){
        Call<ArrayList<Alert>> call = recycleItApi.getPersonalDisposal(sharedpreferences.getString("email",""));
        call.enqueue(new Callback<ArrayList<Alert>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ArrayList<Alert>> call, Response<ArrayList<Alert>> response) {
                list = response.body();
                lineChartPreprocessing(lineView);
                barChartPreprocessing(barView);
                pieChartPreprocessing(pieView);

                loadProgress.setVisibility(View.GONE);

                lineView.setVisibility(View.VISIBLE);
                barView.setVisibility(View.VISIBLE);
                pieView.setVisibility(View.VISIBLE);

                Toast.makeText(getApplicationContext(), "loaded",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<ArrayList<Alert>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed to load",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void lineChartPreprocessing(LineView lineView){

        int year = Calendar.getInstance().get(Calendar.YEAR);

        ArrayList<String> test = new ArrayList<String>();
        for (int i = 0; i < 12; i++) {
            test.add(String.valueOf(i + 1) + "-" + year);
        }

        lineView.setBottomTextList(test);
        lineView.setColorArray(new int[]{Color.rgb(67, 160, 7),Color.rgb(255, 193, 7),Color.rgb(33, 150, 243), Color.CYAN});

        ArrayList<Integer> pub = new ArrayList<>(Collections.nCopies(12, 0));
        ArrayList<Integer> per = new ArrayList<>(Collections.nCopies(12, 0));
        ArrayList<Integer> sav = new ArrayList<>(Collections.nCopies(12, 0));

        for (int i = 0; i < 12; i++) {
            for(Alert alert: list){
                int month = Integer.parseInt(alert.getDate().split("-")[1]);
                if(alert.getDate().contains(Integer.toString(year)) && month == i){

                    switch (alert.getType()){
                        case "PUB":
                            pub.set(i, pub.get(i) + alert.getItemList().split(",").length);
                            break;
                        case "PER":
                            per.set(i, per.get(i) + alert.getItemList().split(",").length);
                            break;
                        case "SAV":
                            sav.set(i, per.get(i) + alert.getItemList().split(",").length);

                    }

                }
            }
        }

        ArrayList<ArrayList<Integer>> dataLists = new ArrayList<>();
        dataLists.add(pub);
        dataLists.add(per);
        dataLists.add(sav);
        lineView.setDataList(dataLists);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void barChartPreprocessing(BarView barView){
        HashMap<String, Integer> frequency = new HashMap<>();
        int max = 1;
        String[] categories = new String[]{"Aluminium foil", "Blister pack", "Bottle", "Bottle cap", "Can", "Carton", "Cigarette", "Cup", "Food waste",
                "Glass jar", "Lid", "Other plastic", "Paper", "Paper bag", "Plastic bag & wrapper", "Plastic Container", "Plastic glooves",
                "Plastic utensils", "Pop tab", "Rope & strings", "Scrap metal", "Shoe", "Squeezable tube", "Straw", "Styrofoam piece",
                "Unlabeled litter"};

        for(Alert alert: list){
            String[] items = alert.getItemList().split(",");
            for(int i = 0; i < items.length; i++){
                String category = items[i].split("\\(")[0];
                 if(frequency.get(category) == null){
                     frequency.put(category, 1);
                 }else {
                     frequency.put(category, frequency.get(category) + 1);
                     max = Math.max(frequency.get(category), max);
                 }


            }

        }
        HashMap<String, Integer> frequency2 = (HashMap<String, Integer>) frequency.clone();

        for(int i = 0; i < categories.length; i++){
            if(!frequency2.containsKey(categories[i])){
                frequency2.put(categories[i], 1);
            }else{
                frequency2.put(categories[i], frequency2.get(categories[i]) + 1);
            }
        }

        ArrayList<String> category = new ArrayList<String>();
        for (int i = 0; i < categories.length; i++) {
            String chosenCategory = maxCategory(frequency2);
            category.add(chosenCategory);
            frequency2.remove(chosenCategory);

        }


        barView.setBottomTextList( category);

        ArrayList<Integer> barDataList = new ArrayList<Integer>();
        for (int i = 0; i < category.size(); i++) {
            if(frequency.get(category.get(i)) == null){
                barDataList.add(1);
            }else {
                barDataList.add(frequency.get(category.get(i)) + 1);
            }

        }

        barView.setDataList(barDataList, max+10);
    }

    private void pieChartPreprocessing(PieView pieView) {

        HashMap<String, GoalItem> value = PowerPreference.getDefaultFile().getMap("goals", HashMap.class, String.class, GoalItem.class);
        Iterator<Map.Entry<String, GoalItem>> iterator =value.entrySet().iterator();

        int complete = 0;
        int incomplete = 0;
        Date c = Calendar.getInstance().getTime();

        while (iterator.hasNext()) {
            Map.Entry<String, GoalItem> entry = iterator.next();
            GoalItem recyclable = (GoalItem) entry.getValue();


            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            Date recdate = null;
            try {
                recdate = format.parse(recyclable.getDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }



            if(recdate.before(c)  && recyclable.getCurrentCount() >= recyclable.getGoalCount()){
                complete++;

            }else if (recdate.compareTo(c) == 0  && recyclable.getCurrentCount() < recyclable.getGoalCount()){
              incomplete++;
            }
        }
        ArrayList<PieHelper> pieHelperArrayList = new ArrayList<PieHelper>();

        if(complete == 0 && incomplete == 0){
            pieHelperArrayList = new ArrayList<PieHelper>();
            pieHelperArrayList.add(new PieHelper(80, Color.rgb(67, 160, 7)));
            pieHelperArrayList.add(new PieHelper(20, Color.rgb(220,20,60)));
        }else{
            pieHelperArrayList = new ArrayList<PieHelper>();
            pieHelperArrayList.add(new PieHelper((complete/(complete + incomplete) * 100), Color.rgb(67, 160, 7)));
            pieHelperArrayList.add(new PieHelper((incomplete/(complete + incomplete) * 100), Color.rgb(220,20,60)));
        }

        pieView.setDate(pieHelperArrayList);
        pieView.selectedPie(PieView.NO_SELECTED_INDEX);
        pieView.showPercentLabel(true);

        pieView.setOnPieClickListener(new PieView.OnPieClickListener() {
            String[] selectedIndex = {"Completed Goals", "Failed goals"};
            @Override public void onPieClick(int index) {
                if (index != PieView.NO_SELECTED_INDEX) {
                    Toast.makeText(getApplicationContext(), selectedIndex[index],Toast.LENGTH_LONG).show();
                } else {

                }
            }
        });

    }

    public String maxCategory(HashMap<String, Integer> map){
        String category = "";
        int max = 0;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if(entry.getValue() > max){
                max = entry.getValue();
                category = entry.getKey();
            }
        }

        return category;
    }
}
