package com.example.android.done;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class HomePage extends Fragment implements TaskAdapter.OnTaskListener {

    NavController navController;
    private TaskAdapter adapter;
    private GoalViewModel mViewModel;
    private RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_page, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(GoalViewModel.class);
        recyclerView = view.findViewById(R.id.home_page_recycler_view);
        navController = Navigation.findNavController(view);

        observerSetup();
        recyclerSetup();


    }

    private void observerSetup() {
        mViewModel.getAllGoals().observe(this, new Observer<List<Goal>>() {
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



                List<Goal> todaysTasks = new ArrayList<>();
                for (int i = 0; i < goals.size(); i++) {
                    goals.get(i).setStatus1();
                    if (goals.get(i).getCustomizeConverter().contains(today)&&goals.get(i).getStatus()==0) {
                        todaysTasks.add(goals.get(i));
                    }
                }

                adapter.setTasks(todaysTasks);
            }
        });
    }

    private void recyclerSetup() {

        adapter = new TaskAdapter(R.layout.task_item , this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onTaskClick(int position) {

        Goal goalTaskSelected = adapter.getTaskAt(position);
        AskOption(position , goalTaskSelected);


    }

    private void AskOption( int position , Goal goal)
    {
        CharSequence options[] = new CharSequence[] {"Mark task as done" , "Goal Details"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setTitle("Select your option:");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 1)
                {
                    navController.navigate(R.id.action_HomePage_to_MyGoalsHome);
                }

                if(which == 0)
                {

                    goal.setTaskStatus(0);  //resetting previous day taskStatus
                    goal.setDoneTask(goal.getDoneTask()+1);  //increasing no. of days the task done by 1
                    goal.setTaskStatus(1); //setting taskStatus to 1
                    mViewModel.update(goal);
                    Toast.makeText(getContext(), "Task marked as completed", Toast.LENGTH_SHORT).show();

                }
            }
        });
        builder.setNegativeButton("Cancel" , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();


    }
}
