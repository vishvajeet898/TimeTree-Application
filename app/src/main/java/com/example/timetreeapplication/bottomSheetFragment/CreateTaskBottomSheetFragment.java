package com.example.timetreeapplication.bottomSheetFragment;

import static android.content.Context.ALARM_SERVICE;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.codegama.timetreeapplication.R;
import com.codegama.timetreeapplication.databinding.FragmentCreateTaskBinding;
import com.example.timetreeapplication.activity.ToDoHome;
import com.example.timetreeapplication.broadcastReceiver.AlarmBroadcastReceiver;
import com.example.timetreeapplication.database.DatabaseClient;
import com.example.timetreeapplication.model.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;
import java.util.GregorianCalendar;


public class CreateTaskBottomSheetFragment extends BottomSheetDialogFragment {

    //Unbinder unbinder;
    //@BindView(R.id.addTaskTitle)
    EditText addTaskTitle;
    //@BindView(R.id.addTaskDescription)
    EditText addTaskDescription;
    //@BindView(R.id.taskDate)
    EditText taskDate;
    //@BindView(R.id.taskTime)
    EditText taskTime;
    //@BindView(R.id.taskEvent)
    EditText taskEvent;
    //@BindView(R.id.addTask)
    Button addTask;
    int taskId;
    boolean isEdit;
    Task task;
    int mYear, mMonth, mDay;
    int mHour, mMinute;
    setRefreshListener setRefreshListener;
    AlarmManager alarmManager;
    TimePickerDialog timePickerDialog;
    DatePickerDialog datePickerDialog;
    ToDoHome activity;
    public static int count = 0;

    private FragmentCreateTaskBinding binding;


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

    public void setTaskId(int taskId, boolean isEdit, setRefreshListener setRefreshListener, ToDoHome activity) {
        this.taskId = taskId;
        this.isEdit = isEdit;
        this.activity = activity;
        this.setRefreshListener = setRefreshListener;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"RestrictedApi", "ClickableViewAccessibility"})
    @Override
    public void setupDialog(Dialog dialog, int style) {



        super.setupDialog(dialog, style);

        binding = FragmentCreateTaskBinding.inflate(LayoutInflater.from(requireContext()));

        View contentView = View.inflate(getContext(), R.layout.fragment_create_task, null);


        //unbinder = ButterKnife.bind(this, contentView);

        dialog.setContentView(binding.getRoot());

        alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
        binding.addTask.setOnClickListener(view -> {
            if(validateFields())
                createTask();
        });
        if (isEdit) {
            showTaskFromId();
        }

        binding.taskDate.setOnTouchListener((view, motionEvent) -> {
            if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(getActivity(),
                        (view1, year, monthOfYear, dayOfMonth) -> {
                            binding.taskDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            datePickerDialog.dismiss();
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
            return true;
        });

        binding.taskTime.setOnTouchListener((view, motionEvent) -> {
            if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                timePickerDialog = new TimePickerDialog(getActivity(),
                        (view12, hourOfDay, minute) -> {
                            binding.taskTime.setText(hourOfDay + ":" + minute);
                            timePickerDialog.dismiss();
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
            return true;
        });
    }

    public boolean validateFields() {
        if(binding.addTaskTitle.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(activity, "Please enter a valid title", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(binding.addTaskDescription.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(activity, "Please enter a valid description", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(binding.taskDate.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(activity, "Please enter date", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(binding.taskTime.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(activity, "Please enter time", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(binding.taskEvent.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(activity, "Please enter an event", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void createTask() {
        class saveTaskInBackend extends AsyncTask<Void, Void, Void> {
            @SuppressLint("WrongThread")
            @Override
            protected Void doInBackground(Void... voids) {
                Task createTask = new Task();
                createTask.setTaskTitle(binding.addTaskTitle.getText().toString());
                createTask.setTaskDescrption(binding.addTaskDescription.getText().toString());
                createTask.setDate(binding.taskDate.getText().toString());
                createTask.setLastAlarm(binding.taskTime.getText().toString());
                createTask.setEvent(binding.taskEvent.getText().toString());

                if (!isEdit)
                    DatabaseClient.getInstance(getActivity()).getAppDatabase()
                            .dataBaseAction()
                            .insertDataIntoTaskList(createTask);
                else
                    DatabaseClient.getInstance(getActivity()).getAppDatabase()
                            .dataBaseAction()
                            .updateAnExistingRow(taskId, binding.addTaskTitle.getText().toString(),
                                    binding.addTaskDescription.getText().toString(),
                                    "0",
                                    binding.taskDate.getText().toString(),
                                    binding.taskTime.getText().toString(),
                                    binding.taskEvent.getText().toString());

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                Toast.makeText(activity, "INSIDE POSTEXECUTE", Toast.LENGTH_SHORT).show();
                super.onPostExecute(aVoid);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    createAnAlarm();
                }
                setRefreshListener.refresh();
                Toast.makeText(getActivity(), "Your event is been added", Toast.LENGTH_SHORT).show();
                dismiss();

            }
        }
        saveTaskInBackend st = new saveTaskInBackend();
        st.execute();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void createAnAlarm() {
        try {
            String[] items1 = binding.taskDate.getText().toString().split("-");
            String dd = items1[0];
            String month = items1[1];
            String year = items1[2];

            String[] itemTime = binding.taskTime.getText().toString().split(":");
            String hour = itemTime[0];
            String min = itemTime[1];

            Calendar cur_cal = new GregorianCalendar();
            cur_cal.setTimeInMillis(System.currentTimeMillis());

            Calendar cal = new GregorianCalendar();
            cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
            cal.set(Calendar.MINUTE, Integer.parseInt(min));
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            cal.set(Calendar.DATE, Integer.parseInt(dd));

            Intent alarmIntent = new Intent(activity, AlarmBroadcastReceiver.class);
            alarmIntent.putExtra("TITLE", binding.addTaskTitle.getText().toString());
            alarmIntent.putExtra("DESC", binding.addTaskDescription.getText().toString());
            alarmIntent.putExtra("DATE", binding.taskDate.getText().toString());
            alarmIntent.putExtra("TIME", binding.taskTime.getText().toString());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(activity,count, alarmIntent, PendingIntent.FLAG_MUTABLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                Toast.makeText(activity, "HEREEEEEEE", Toast.LENGTH_SHORT).show();

                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                }
                count ++;

             //  PendingIntent intent = PendingIntent.getBroadcast(activity, count, alarmIntent, 0);

                PendingIntent intent =  PendingIntent.getBroadcast(activity, count, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() - 600000, intent);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        //PendingIntent intent =  PendingIntent.getBroadcast(activity, count, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);


                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() - 600000, intent);
                    } else {


                        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() - 600000, intent);
                    }
                }
                count ++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showTaskFromId() {
        class showTaskFromId extends AsyncTask<Void, Void, Void> {
            @SuppressLint("WrongThread")
            @Override
            protected Void doInBackground(Void... voids) {
                task = DatabaseClient.getInstance(getActivity()).getAppDatabase()
                        .dataBaseAction().selectDataFromAnId(taskId);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                setDataInUI();
            }
        }
        showTaskFromId st = new showTaskFromId();
        st.execute();
    }

    private void setDataInUI() {
        binding.addTaskTitle.setText(task.getTaskTitle());
        binding.addTaskDescription.setText(task.getTaskDescrption());
        binding.taskDate.setText(task.getDate());
        binding.taskTime.setText(task.getLastAlarm());
        binding.taskEvent.setText(task.getEvent());
    }

    public interface setRefreshListener {
        void refresh();
    }
}