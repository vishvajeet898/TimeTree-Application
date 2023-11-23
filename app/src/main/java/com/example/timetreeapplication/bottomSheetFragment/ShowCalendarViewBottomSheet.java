package com.example.timetreeapplication.bottomSheetFragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.codegama.timetreeapplication.R;
import com.codegama.timetreeapplication.databinding.FragmentCalendarViewBinding;
import com.codegama.timetreeapplication.databinding.FragmentCreateTaskBinding;
import com.example.timetreeapplication.MainActivity;
import com.example.timetreeapplication.database.DatabaseClient;
import com.example.timetreeapplication.model.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ShowCalendarViewBottomSheet extends BottomSheetDialogFragment {

    MainActivity activity;
    ImageView back;
    CalendarView calendarView;
    List<Task> tasks = new ArrayList<>();

    private FragmentCalendarViewBinding binding;


    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"RestrictedApi", "ClickableViewAccessibility"})
    @Override
    public void setupDialog(Dialog dialog, int style) {
        binding = FragmentCalendarViewBinding.inflate(LayoutInflater.from(requireContext()));


        super.setupDialog(dialog, style);


      //  View contentView = View.inflate(getContext(), R.layout.fragment_calendar_view, null);

//        unbinder = ButterKnife.bind(this, contentView);

        dialog.setContentView(binding.getRoot());
        binding.calendarView.setHeaderColor(R.color.colorAccent);
        getSavedTasks();
        binding.back.setOnClickListener(view -> dialog.dismiss());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void getSavedTasks() {

        class GetSavedTasks extends AsyncTask<Void, Void, List<Task>> {
            @Override
            protected List<Task> doInBackground(Void... voids) {
                tasks = DatabaseClient
                        .getInstance(getActivity())
                        .getAppDatabase()
                        .dataBaseAction()
                        .getAllTasksList();
                return tasks;
            }

            @Override
            protected void onPostExecute(List<Task> tasks) {
                super.onPostExecute(tasks);
                binding.calendarView.setEvents(getHighlitedDays());
            }
        }

        GetSavedTasks savedTasks = new GetSavedTasks();
        savedTasks.execute();
    }

    public List<EventDay> getHighlitedDays() {
        List<EventDay> events = new ArrayList<>();

        for(int i = 0; i < tasks.size(); i++) {
            Calendar calendar = Calendar.getInstance();
            String[] items1 = tasks.get(i).getDate().split("-");
            String dd = items1[0];
            String month = items1[1];
            String year = items1[2];

            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dd));
            calendar.set(Calendar.MONTH, Integer.parseInt(month) - 1);
            calendar.set(Calendar.YEAR, Integer.parseInt(year));
            events.add(new EventDay(calendar, R.drawable.dot));
        }
        return events;
    }

}