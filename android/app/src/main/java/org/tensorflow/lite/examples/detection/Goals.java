package org.tensorflow.lite.examples.detection;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import java.util.Calendar;
import java.util.HashMap;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.ColorDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.preference.PowerPreference;
import com.preference.Preference;

public class Goals extends AppCompatActivity {
    private Spinner recyclable;
    private EditText amount;
    private static EditText date;
    private FloatingActionButton floatingButton;
    private Button button;
    private DatePickerDialog picker;
    private static final String TAG = "Goals";

    private View.OnClickListener buttonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getApplicationContext(), DetectorActivity.class));
            overridePendingTransition(0,0);
        }
    };
    private String selectedRecyclable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);

        floatingButton = findViewById(R.id.floatingActionButton);
        floatingButton.setOnClickListener(buttonOnClickListener);
        recyclable = findViewById(R.id.spinner3);
        amount = findViewById(R.id.editText10);
        date = findViewById(R.id.editText11);
        button = findViewById(R.id.button8);
        PowerPreference.init(this);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.navigation_goal);
        String[] items = new String[]{"Aluminium foil", "Blister pack", "Bottle", "Bottle cap", "Can", "Carton", "Cigarette", "Cup", "Food waste",
                "Glass jar", "Lid", "Other plastic", "Paper", "Paper bag", "Plastic bag & wrapper", "Plastic Container", "Plastic glooves",
                "Plastic utensils", "Pop tab", "Rope & strings", "Scrap metal", "Shoe", "Squeezable tube", "Straw", "Styrofoam piece",
                "Unlabeled litter"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        recyclable.setAdapter(adapter);
        recyclable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedRecyclable = items[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(amount.getText().toString().trim().isEmpty() || date.getText().toString().trim().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please leave no field empty", Toast.LENGTH_LONG).show();
                } else if(!isNumeric(amount.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Please enter a correct number for amount", Toast.LENGTH_LONG).show();
                }else{
                    SharedPreferences sharedpreferences  = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor;
                    if(!sharedpreferences .contains("initialized")){
                        editor = sharedpreferences .edit();
                        editor.putBoolean("initialized", true);
                        HashMap<String, GoalItem> hashMap = new HashMap<String, GoalItem>();
                        hashMap.put(selectedRecyclable, new GoalItem(Integer.parseInt(amount.getText().toString()), 0, date.getText().toString()));
                        PowerPreference.getDefaultFile().putMap("goals", hashMap);

                        editor.commit();
                        scheduleJob();

                    }else{

                        HashMap<String, GoalItem> value = PowerPreference.getDefaultFile().getMap("goals", HashMap.class, String.class, GoalItem.class);
                        if(value.containsKey(selectedRecyclable)){
                            value.put(selectedRecyclable, new GoalItem(Integer.parseInt(amount.getText().toString()), 0, date.getText().toString()));
                            PowerPreference.getDefaultFile().putMap("goals", value);
                            Toast.makeText(getApplicationContext(), "here 1",Toast.LENGTH_LONG).show();
                        }else{
                            value.put(selectedRecyclable, new GoalItem(Integer.parseInt(amount.getText().toString()), 0, date.getText().toString()));
                            PowerPreference.getDefaultFile().putMap("goals", value);
                            Toast.makeText(getApplicationContext(), "here 2",Toast.LENGTH_LONG).show();
                        }
                    }
                    Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                    Notification notify=new Notification.Builder
                            (getApplicationContext()).setContentTitle("goal status").setContentText("New Goal set for "+selectedRecyclable + " "+ amount.getText().toString() + " item Goal, let's do it!").
                            setContentTitle("Goal Set for "+date.getText().toString()).setSmallIcon(R.drawable.glassjar).setSound(soundUri).build();

                    notify.flags |= Notification.FLAG_AUTO_CANCEL;
                    notif.notify(0, notify);

                }

            }
        });


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

    public boolean isNumeric(String str){
        // null or empty
        if (str == null || str.length() == 0) {
            return false;
        }

        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }

        return true;

    }

    public void scheduleJob() {
        ComponentName componentName = new ComponentName(this, SetGoalJobService.class);
        JobInfo info = new JobInfo.Builder(123, componentName)
                .setRequiresCharging(true)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true)
                .setPeriodic(15 * 60 * 1000)
                .build();
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "Job scheduled");
        } else {
            Log.d(TAG, "Job scheduling failed");
        }
    }

    public void cancelJob() {
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(123);
        Log.d(TAG, "Job cancelled");
    }

    public void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.rgb(77, 77, 255)));
            return dialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            date.setText(year + "-" + (month+1) + "-" + day);
        }
    }
}
