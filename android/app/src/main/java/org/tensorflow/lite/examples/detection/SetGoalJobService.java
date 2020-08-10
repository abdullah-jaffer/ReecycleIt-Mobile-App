package org.tensorflow.lite.examples.detection;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import com.preference.PowerPreference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public class SetGoalJobService extends JobService {
    private static final String TAG = "ExampleJobService";
    private boolean jobCancelled = false;
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "Job started");
        PowerPreference.init(this);
        doBackgroundWork(jobParameters);
        return true;
    }

    private void doBackgroundWork(final JobParameters params) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


                for (int i = 0; i < 10; i++) {
                    Log.d(TAG, "run: " + i);
                    if (jobCancelled) {
                        return;
                    }
                    Date c = Calendar.getInstance().getTime();


                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String currentDate = df.format(c);

                    HashMap<String, GoalItem> value = PowerPreference.getDefaultFile().getMap("goals", HashMap.class, String.class, GoalItem.class);
                    Iterator<Map.Entry<String, GoalItem>> iterator =value.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<String, GoalItem> entry = iterator.next();
                        System.out.println(entry.getKey() + ":" + entry.getValue());
                        GoalItem recyclable = (GoalItem) entry.getValue();

                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                        Date recdate = null;
                        try {
                            recdate = format.parse(recyclable.getDate());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if(recdate.before(c) && recyclable.getCurrentCount() >= recyclable.getGoalCount()){
                            NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                            Notification notify=new Notification.Builder
                                    (getApplicationContext()).setContentTitle(recyclable.getDate()+" goal status").setContentText("You reached your Goal of  "+recyclable.getGoalCount() +" "+ entry.getKey()+"s, congratulations").
                                    setContentTitle(recyclable.getDate()+" goal status").setSmallIcon(R.drawable.glassjar).setSound(soundUri).build();

                            notify.flags |= Notification.FLAG_AUTO_CANCEL;
                            notif.notify(0, notify);
                        }else if (recdate.before(c) && recyclable.getCurrentCount() < recyclable.getGoalCount()){
                            NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                            Notification notify=new Notification.Builder
                                    (getApplicationContext()).setContentTitle(recyclable.getDate()+" goal status").setContentText("You did not reach your Goal of  "+recyclable.getGoalCount()).
                                    setContentTitle(recyclable.getDate()+" goal status").setSmallIcon(R.drawable.glassjar).setSound(soundUri).build();

                            notify.flags |= Notification.FLAG_AUTO_CANCEL;
                            notif.notify(0, notify);
                        }

                    }

                }
                Log.d(TAG, "Job finished");
                jobFinished(params, false);
            }
        }).start();
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d(TAG, "Job cancelled before completion");
        jobCancelled = true;
        return true;
    }
}
