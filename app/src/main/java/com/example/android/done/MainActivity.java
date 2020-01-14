package com.example.android.done;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.view.View;
import android.app.DialogFragment;
import android.widget.Toast;

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
        ArrayList<Goal> allGoals = new ArrayList<>();
        goalViewModel.getAllGoals().observe(this, new Observer<List<Goal>>() {
            @Override
            public void onChanged(List<Goal> goals) {
                allGoals.addAll(goals);
                reset(allGoals);

            }
        });

        goalViewModel.getAllGoals().observe(this, new Observer<List<Goal>>() {
            @Override
            public void onChanged(List<Goal> goals) {

                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_WEEK);
                Log.e("I got here", "Yes");
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



                for (int i = 0; i < goals.size(); i++) {

                    goals.get(i).setStatus1();
                    int hour = goals.get(i).getHour();
                    int minute = goals.get(i).getMinute();
                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.HOUR_OF_DAY, hour);
                    c.set(Calendar.MINUTE, minute);
                    c.set(Calendar.SECOND, 0);
                    if (goals.get(i).getCustomizeConverter().contains(today) && goals.get(i).getStatus() == 0 && !c.before(Calendar.getInstance())) {
                        goalArrayList.add(goals.get(i));
                        setAlarm(goalArrayList);
                    }


                }
            }
        });


    }

    private void reset(ArrayList<Goal> allGoals) {
        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        SharedPreferences settings = getSharedPreferences("PREFS", 0);
        int lastDay = settings.getInt("day", 0);
        if (lastDay != currentDay) {

            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("day", currentDay);
            editor.commit();
            for (int i = 0; i < allGoals.size(); i++) {
                Goal goal = allGoals.get(i);
                goal.setTaskStatus(0);
                goalViewModel.update(goal);
            }


        }
    }

    private void setAlarm(ArrayList<Goal> goals) {
        for (int i = 0; i < goals.size(); i++) {

            int hour = goals.get(i).getHour();
            int minute = goals.get(i).getMinute();
            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, hour);
            c.set(Calendar.MINUTE, minute);
            c.set(Calendar.SECOND, 0);
            startAlarm(c, i, goals.get(i));
        }

    }

    private void startAlarm(Calendar c, int i, Goal goal) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("Title", goal.getGoalName());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, i, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);


    }
}
