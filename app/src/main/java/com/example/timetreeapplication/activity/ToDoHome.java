package com.example.timetreeapplication.activity;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

   /* @BindView(R.id.taskRecycler)
    RecyclerView taskRecycler;
    @BindView(R.id.addTask)
    TextView addTask;*/
    TaskAdapter taskAdapter;
    List<Task> tasks = new ArrayList<>();
   /* @BindView(R.id.noDataImage)
    ImageView noDataImage;
    @BindView(R.id.calendar)
    ImageView calendar;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        toDoHomeBinding = ActivityToDoHomeBinding.inflate(getLayoutInflater());
        View v = toDoHomeBinding.getRoot();
        super.onCreate(savedInstanceState);
        setContentView(v);
        setUpAdapter();
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

        toDoHomeBinding.calendar.setOnClickListener(view -> {
            ShowCalendarViewBottomSheet showCalendarViewBottomSheet = new ShowCalendarViewBottomSheet();
            showCalendarViewBottomSheet.show(getSupportFragmentManager(), showCalendarViewBottomSheet.getTag());
        });
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
    }
}