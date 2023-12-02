package com.example.timetreeapplication.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.codegama.timetreeapplication.R;
import com.codegama.timetreeapplication.databinding.ActivityToDoHomeBinding;
import com.example.timetreeapplication.adapter.TaskAdapter;
import com.example.timetreeapplication.bottomSheetFragment.CreateTaskBottomSheetFragment;
import com.example.timetreeapplication.bottomSheetFragment.ShowCalendarViewBottomSheet;
import com.example.timetreeapplication.broadcastReceiver.AlarmBroadcastReceiver;
import com.example.timetreeapplication.database.DatabaseClient;
import com.example.timetreeapplication.model.Task;

import java.util.ArrayList;
import java.util.List;


public class ToDoHome extends BaseActivity implements CreateTaskBottomSheetFragment.setRefreshListener {

    private ActivityToDoHomeBinding toDoHomeBinding;


    TaskAdapter taskAdapter;
    List<Task> tasks = new ArrayList<>();
    List<Task> completedTasks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        toDoHomeBinding = ActivityToDoHomeBinding.inflate(getLayoutInflater());
        View v = toDoHomeBinding.getRoot();
        super.onCreate(savedInstanceState);
        setContentView(v);
        setUpAdapter();

        toDoHomeBinding.card.setOnClickListener(view -> {
            Intent i = new Intent(this,Statistics.class);
            startActivity(i);
        });


        toDoHomeBinding.sprint.setOnClickListener(view -> {
            Intent i = new Intent(this, AprintActivity.class);
            startActivity(i);
        });



        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        ComponentName receiver = new ComponentName(this, AlarmBroadcastReceiver.class);
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
        Glide.with(getApplicationContext()).load(R.drawable.first_note).into(toDoHomeBinding.noDataImage);

        toDoHomeBinding.addTask.setOnClickListener(view -> {
            CreateTaskBottomSheetFragment createTaskBottomSheetFragment = new CreateTaskBottomSheetFragment();
            createTaskBottomSheetFragment.setTaskId(0, false, this, ToDoHome.this);
            createTaskBottomSheetFragment.show(getSupportFragmentManager(), createTaskBottomSheetFragment.getTag());
        });

        getSavedTasks();
        getCompletedTasks(this);


        toDoHomeBinding.calendar.setOnClickListener(view -> {
            ShowCalendarViewBottomSheet showCalendarViewBottomSheet = new ShowCalendarViewBottomSheet();
            showCalendarViewBottomSheet.show(getSupportFragmentManager(), showCalendarViewBottomSheet.getTag());
        });



        toDoHomeBinding.circularProgress.setMaxProgress(8);
        toDoHomeBinding.circularProgress.setCurrentProgress(4);
    }

    public void setUpAdapter() {
        taskAdapter = new TaskAdapter(this, tasks,this);
        toDoHomeBinding.taskRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        toDoHomeBinding.taskRecycler.setAdapter(taskAdapter);
    }

    private void getSavedTasks() {

        class GetSavedTasks extends AsyncTask<Void, Void, List<Task>> {
            @Override
            protected List<Task> doInBackground(Void... voids) {
                tasks = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .dataBaseAction()
                        .getAllTasksList();
                return tasks;
            }

            @Override
            protected void onPostExecute(List<Task> tasks) {
                super.onPostExecute(tasks);
                toDoHomeBinding.noDataImage.setVisibility(tasks.isEmpty() ? View.VISIBLE : View.GONE);
                setUpAdapter();
            }
        }

        GetSavedTasks savedTasks = new GetSavedTasks();
        savedTasks.execute();
    }

    @Override
    public void refresh() {
        getSavedTasks();
        getCompletedTasks(this);
       /* toDoHomeBinding.circularProgress.setMaxProgress(tasks.size());

        toDoHomeBinding.circularProgress.setCurrentProgress(completedTasks.size());*/
    }


    private void getCompletedTasks(Context context) {
        class GetSavedTasks extends AsyncTask<Void, Void, List<Task>> {
            @Override
            protected List<Task> doInBackground(Void... voids) {
                completedTasks = DatabaseClient.getInstance(context)
                        .getAppDatabase()
                        .dataBaseAction()
                        .getCompleted("1");

                return completedTasks;
            }

            @Override
            protected void onPostExecute(List<Task> completedTask) {
                super.onPostExecute(completedTask);
                toDoHomeBinding.circularProgress.setMaxProgress(tasks.size());
                toDoHomeBinding.circularProgress.setCurrentProgress(completedTask.size());



              //  refresh();
            }
        }
        GetSavedTasks savedTasks = new GetSavedTasks();
        savedTasks.execute();
    }



}