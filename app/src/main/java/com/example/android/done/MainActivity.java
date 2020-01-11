package com.example.android.done;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.view.View;
import android.app.DialogFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    NavController navController;
    GoalViewModel goalViewModel;
    private ArrayList<Goal> goalArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        goalViewModel = ViewModelProviders.of(this).get(GoalViewModel.class);

        goalViewModel.getAllGoals().observe(this, new Observer<List<Goal>>() {
            @Override
            public void onChanged(List<Goal> goals) {

                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_WEEK);
                String today = "SUNDAY";
                if (day == 2) {
                    today = "MONDAY";
                } else if (day == 3) {
                    today = "TUESDAY";
                } else if (day == 4) {
                    today = "WEDNESDAY";
                } else if (day == 5) {
                    today = "THURSDAY";
                } else if (day == 6) {
                    today = "FRIDAY";
                } else if (day == 7) {
                    today = "SATURDAY";
                }

                for (int i =0; i< goals.size() ; i++ )
                {

                    goals.get(i).setStatus1();
                    if (goals.get(i).getCustomizeConverter().contains(today)&&goals.get(i).getStatus()==0) {
                        goalArrayList.add(goals.get(i));
                        setAlarm(goalArrayList);
                    }


                }
            }
        });



    }

    private void setAlarm(ArrayList<Goal> goals)
    {
        for (int i=0; i< goals.size();i++){

        int hour = goals.get(i).getHour();
        int minute = goals.get(i).getMinute();
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
            int day = c.get(Calendar.DAY_OF_WEEK);
            String today = "SUNDAY";
            if (day == 2) {
                today = "MONDAY";
            } else if (day == 3) {
                today = "TUESDAY";
            } else if (day == 4) {
                today = "WEDNESDAY";
            } else if (day == 5) {
                today = "THURSDAY";
            } else if (day == 6) {
                today = "FRIDAY";
            } else if (day == 7) {
                today = "SATURDAY";
            }
        startAlarm(c, i, goals.get(i) , today);
    }

    }

    private void startAlarm(Calendar c , int i , Goal goal , String today)
    {
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this , AlertReceiver.class);
        intent.putExtra("Title" , goal.getGoalName());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this , i , intent ,0 );
        alarmManager.set(AlarmManager.RTC_WAKEUP , c.getTimeInMillis() , pendingIntent);


    }







}
