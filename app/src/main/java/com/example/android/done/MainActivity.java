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
import android.view.View;
import android.widget.TextView;
import android.view.View;
import android.app.DialogFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    NavController navController;
    GoalViewModel goalViewModel;
    private List<Goal> goalList;

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

                for (int i =0; i< goals.size() ; i++ )
                {
                    String title = goals.get(i).getGoalName();
                    int hour = goals.get(i).getHour();
                    int minute = goals.get(i).getMinute();
                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.HOUR_OF_DAY , hour);
                    c.set(Calendar.MINUTE , minute);
                    c.set(Calendar.SECOND , 0);
                    startAlarm(c, i , title);


                }
            }
        });



    }

    private void startAlarm(Calendar c , int i , String  title)
    {
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this , AlertReceiver.class);
        intent.putExtra("Title" , title);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this , i , intent ,0 );
        alarmManager.set(AlarmManager.RTC_WAKEUP , c.getTimeInMillis() , pendingIntent);

    }







}
