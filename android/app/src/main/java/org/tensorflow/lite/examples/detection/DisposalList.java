package org.tensorflow.lite.examples.detection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.preference.PowerPreference;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DisposalList extends AppCompatActivity {


    private FloatingActionButton floatingButton;
    private ListView listView;
    private HashMap<String, Integer> imagesList;
    private HashMap<Integer, Recyclable> selectedRecyclables;
    private FloatingActionButton publicButton;
    private FloatingActionButton privateButton;
    private FloatingActionButton saveButton;
    private CheckBox checkBox;
    RecycleItApi recycleItApi;
    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;
    double latitude;
    double longitude;
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
        setContentView(R.layout.activity_disposal_list);

        floatingButton = findViewById(R.id.floatingActionButton);
        floatingButton.setOnClickListener(buttonOnClickListener);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        listView = findViewById(R.id.listView);
        checkBox = findViewById(R.id.checkBox2);
        publicButton = findViewById(R.id.button);
        privateButton = findViewById(R.id.button3);
        saveButton = findViewById(R.id.button4);
        recycleItApi = RecycleItApi.retrofit.create(RecycleItApi.class);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        latitude = 0.0;
        longitude = 0.0;
        PowerPreference.init(this);
        sharedpreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        Intent intent = getIntent();
        ArrayList<Recyclable> recyclablesList = (ArrayList<Recyclable>) intent.getSerializableExtra("recyclables");
        Bitmap bmp = null;
        String filename = getIntent().getStringExtra("image");
        try {
            FileInputStream is = this.openFileInput(filename);
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageBitmap(bmp);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final boolean isChecked = checkBox.isChecked();
                if(isChecked){
                    int i = 0;
                    for(Recyclable recyclable: recyclablesList){
                        selectedRecyclables.put(i, recyclable);
                        i++;
                    }
                    Toast.makeText(getApplicationContext(), "Selected All",Toast.LENGTH_LONG).show();

                }else{
                    selectedRecyclables.clear();
                    Toast.makeText(getApplicationContext(), "Deselected All",Toast.LENGTH_LONG).show();
                }
            }
        });

        selectedRecyclables = new HashMap<>();

        if(!recyclablesList.isEmpty()){
            Toast.makeText(getApplicationContext(), recyclablesList.get(0).getShow() + recyclablesList.get(0).getEnergySaving(),Toast.LENGTH_LONG).show();
        }
        imagesList = new HashMap<>();

        imagesList.put("Aluminium foil", R.drawable.alumunium);
        imagesList.put("Blister pack", R.drawable.blister_pack);
        imagesList.put("Bottle", R.drawable.bottle);
        imagesList.put("Bottle cap", R.drawable.cap);
        imagesList.put("Can", R.drawable.can);
        imagesList.put("Carton", R.drawable.carton);
        imagesList.put("Cigarette", R.drawable.cigarette);
        imagesList.put("Cup", R.drawable.cup);
        imagesList.put("Food waste", R.drawable.food);
        imagesList.put("Glass jar", R.drawable.glassjar);
        imagesList.put("Lid", R.drawable.lid);
        imagesList.put("Other plastic", R.drawable.plasticbag);
        imagesList.put("Paper", R.drawable.paper);
        imagesList.put("Paper bag", R.drawable.ic_paperbag);
        imagesList.put("Plastic bag & wrapper", R.drawable.plasticbag);
        imagesList.put("Plastic Container", R.drawable.plasticbag);
        imagesList.put("Plastic glooves", R.drawable.plasticglooves);
        imagesList.put("Plastic utensils", R.drawable.plasticutensils);
        imagesList.put("Pop tab", R.drawable.poptab);
        imagesList.put("Rope & strings", R.drawable.ropestring);
        imagesList.put("Scrap metal", R.drawable.scrapmetal);
        imagesList.put("Shoe", R.drawable.shoe);
        imagesList.put("Squeezable tube", R.drawable.squeezabletubes);
        imagesList.put("Straw", R.drawable.straw);
        imagesList.put("Styrofoam piece", R.drawable.styrafoam);
        imagesList.put("Unlabeled litter", R.drawable.litter);

        MyAdapter adapter = new MyAdapter(this, recyclablesList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    LayoutInflater inflater = LayoutInflater.from(DisposalList.this);
                    View view1 = inflater.inflate(R.layout.info_dialog, null);

                    TextView title = view1.findViewById(R.id.title);
                    TextView explanation = view1.findViewById(R.id.textView3);
                    Button button = view1.findViewById(R.id.button5);

                    title.setText(recyclablesList.get(position).getEnergySaving()+ " Energy Saved by Recycling "+
                            recyclablesList.get(position).getShow()+"!");
                    explanation.setText(recyclablesList.get(position).getHowTo());
                    explanation.setMovementMethod(new ScrollingMovementMethod());
                    AlertDialog alertDialog = new AlertDialog.Builder(DisposalList.this)
                            .setView(view1)
                            .create();


                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });

                    alertDialog.show();




            }
        });

        publicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedRecyclables.isEmpty()){
                    Toast.makeText(getApplicationContext(), "No items to report",Toast.LENGTH_LONG).show();
                }else{
                    String result = "";
                    updateGoals(selectedRecyclables);
                    result = getCombinedTitles(selectedRecyclables);
                    Toast.makeText(getApplicationContext(), result,Toast.LENGTH_LONG).show();
                    getLastLocation();
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("long", String.valueOf(longitude));
                    editor.putString("lat", String.valueOf(latitude));
                    editor.commit();
                    Intent privateDisposal = new Intent(getApplicationContext(), PublicDisposal.class);
                    privateDisposal.putExtra("items", result);
                    startActivity(privateDisposal);
                    overridePendingTransition(0,0);
                }

            }
        } );

        privateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedRecyclables.isEmpty()){
                    Toast.makeText(getApplicationContext(), "No items to report",Toast.LENGTH_LONG).show();
                }else{
                    String result = "";
                    updateGoals(selectedRecyclables);
                    result = getCombinedTitles(selectedRecyclables);
                    Toast.makeText(getApplicationContext(), result,Toast.LENGTH_LONG).show();
                    getLastLocation();
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("long", String.valueOf(longitude));
                    editor.putString("lat", String.valueOf(latitude));
                    editor.commit();
                    Intent privateDisposal = new Intent(getApplicationContext(), PrivateDisposal.class);
                    privateDisposal.putExtra("items", result);
                    startActivity(privateDisposal);
                    overridePendingTransition(0,0);
                }

            }
        } );

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedRecyclables.isEmpty()){
                    Toast.makeText(getApplicationContext(), "No items to save",Toast.LENGTH_LONG).show();
                }else{
                    String result = "";
                    result = getCombinedTitles(selectedRecyclables);
                    updateGoals(selectedRecyclables);
                    Toast.makeText(getApplicationContext(), result,Toast.LENGTH_LONG).show();
                    sendAlert(result);
                }
            }
        } );



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

    public void sendAlert(String items){
        Call<ResponseBody> call = recycleItApi.addPersonalDisposal(sharedpreferences.getString("email",""), items, "SAV");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(getApplicationContext(), "Alert Sent Successfully",Toast.LENGTH_LONG).show();
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Alert Sent Successfully",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed to Send Alert",Toast.LENGTH_LONG).show();
            }
        });
    }

    public String[] getTrashTitle(ArrayList<Recyclable> recyclables){
        String[] titles = new String[recyclables.size()];

        for(int i = 0; i < recyclables.size(); i++){
            titles[i] = recyclables.get(i).getShow();
        }

        return titles;
    }

    public String getCombinedTitles(HashMap<Integer, Recyclable> selectedRecyclables){

        String result = "";
        Iterator<Map.Entry<Integer, Recyclable>> iterator = selectedRecyclables.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Integer, Recyclable> entry = iterator.next();
            System.out.println(entry.getKey() + ":" + entry.getValue());
            Recyclable recyclable = (Recyclable) entry.getValue();
            for(int i = 1; i <= recyclable.getCount(); i++){
                result += "," +recyclable.getCategory()+"("+recyclable.getShow()+")";
            }
        }

        return result.substring(1);
    }

    public void updateGoals(HashMap<Integer, Recyclable> selectedRecyclables){

        SharedPreferences sharedpreferences  = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        HashMap<String, GoalItem> value = PowerPreference.getDefaultFile().getMap("goals", HashMap.class, String.class, GoalItem.class);
        if(sharedpreferences .contains("initialized")){


            Iterator<Map.Entry<Integer, Recyclable>> iterator = selectedRecyclables.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<Integer, Recyclable> entry = iterator.next();
                System.out.println(entry.getKey() + ":" + entry.getValue());
                Recyclable recyclable = (Recyclable) entry.getValue();
                if(value.containsKey(recyclable.getCategory())){
                    GoalItem item = (GoalItem) value.get(recyclable.getCategory());
                    item.setCurrentCount(item.getCurrentCount() + recyclable.getCount());
                    value.put(recyclable.getCategory(), item);
                }
            }
            PowerPreference.getDefaultFile().putMap("goals", value);
        }

    }


    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        ArrayList<Recyclable> recyclables;
        boolean[] mCheckedState;


        MyAdapter (Context c, ArrayList<Recyclable> recyclables) {

            super(c, R.layout.disposal_row, R.id.textView1, getTrashTitle(recyclables));
            mCheckedState = new boolean[recyclables.size()];
            this.context = c;
            this.recyclables = recyclables;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.disposal_row, parent, false);
            ImageView images = row.findViewById(R.id.image);
            TextView myTitle = row.findViewById(R.id.textView1);
            TextView count = row.findViewById(R.id.textView2);
            CheckBox checkBox = row.findViewById(R.id.checkBox);
            checkBox.setChecked(mCheckedState[position]);
            // now set our resources on views
            images.setImageResource(imagesList.get(recyclables.get(position).getCategory()));
            myTitle.setText(recyclables.get(position).getShow());
            count.setText(Integer.toString(recyclables.get(position).getCount()) + " Objects");

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    final boolean isChecked = checkBox.isChecked();
                    mCheckedState[position] = isChecked;
                    if(isChecked){
                        Toast.makeText(getApplicationContext(), "selected",Toast.LENGTH_LONG).show();
                        selectedRecyclables.put(position, recyclables.get(position));
                    }else{
                        Toast.makeText(getApplicationContext(), "unselected",Toast.LENGTH_LONG).show();
                        selectedRecyclables.remove(position);
                    }
                }
            });





            return row;
        }
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }


    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
        }
    };

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();

        }

    }

}
