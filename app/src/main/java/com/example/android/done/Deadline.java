package com.example.android.done;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Deadline extends Fragment {
    NavController navController;
    CalendarView calendarView;
    SharedViewModel viewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_deadline, container, false);

        return v;

    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        navController = Navigation.findNavController(view);
        calendarView = (CalendarView) view.findViewById(R.id.calendar_view);
        if (!viewModel.getDeadline().trim().isEmpty()) {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date date = format.parse(viewModel.getDeadline());
                int day = date.getDate();
                int month = date.getMonth();
                int yr = date.getYear();
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, yr+1900);
                calendar.set(Calendar.MONTH, month+1);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                long millitime = calendar.getTimeInMillis();
                calendarView.setDate(millitime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String deadline = dayOfMonth + "/" + month + "/" + year;
                viewModel.setDeadline(deadline);
                Toast.makeText(getContext(), "Deadline: " + deadline, Toast.LENGTH_SHORT).show();
            }
        });
        Button next = (Button) view.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_deadline_to_customize);
            }
        });


    }


}
