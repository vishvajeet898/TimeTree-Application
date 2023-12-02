package com.example.timetreeapplication.activity;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.codegama.timetreeapplication.R;
import com.codegama.timetreeapplication.databinding.ActivityStatisticsBinding;
import com.codegama.timetreeapplication.databinding.ActivityToDoHomeBinding;
import com.example.timetreeapplication.database.DatabaseClient;
import com.example.timetreeapplication.model.Task;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Statistics extends AppCompatActivity {


    private ActivityStatisticsBinding binding;

    List<Task> tasks = new ArrayList<>();
    List<Task> completedTasks = new ArrayList<>();
    List<Task> weekDataTask = new ArrayList<>();
    TreeMap<String, Integer> weekMap = new TreeMap<>();
    ArrayList<Integer> weekData = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityStatisticsBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);


        getSavedTasks();
        getCompletedTasks(this);

        getWeeData(this);


        binding.backBtn.setOnClickListener(view -> {
            Intent i = new Intent(this, ToDoHome.class);
            startActivity(i);
        });








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
              /*  toDoHomeBinding.noDataImage.setVisibility(tasks.isEmpty() ? View.VISIBLE : View.GONE);
                setUpAdapter();*/
            }
        }

        GetSavedTasks savedTasks = new GetSavedTasks();
        savedTasks.execute();
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

                binding.totalTask.setText(tasks.size() + " Tasks");

                if (completedTask.size() != 0){
                    Log.d("TSK", "onPostExecute: Size = " + completedTask.size() + "   " + tasks.size() + "   " + (completedTask.size() / tasks.size()));
                    double percent = ((double) completedTask.size() / (double) tasks.size()) * 100;
                    int p =  (int) Math.round(percent);
                    binding.completePercent.setText(p + "%");
                }else {

                    binding.completePercent.setText(  "0%");
                }
                //  refresh();
            }
        }
        GetSavedTasks savedTasks = new GetSavedTasks();
        savedTasks.execute();
    }


    private void getWeeData(Context context) {
        class GetSavedTasks extends AsyncTask<Void, Void, List<Task>> {
            @Override
            protected List<Task> doInBackground(Void... voids) {
                weekDataTask = DatabaseClient.getInstance(context)
                        .getAppDatabase()
                        .dataBaseAction()
                        .getWeekData();

                return weekDataTask;
            }

            @Override
            protected void onPostExecute(List<Task> completedTask) {
                super.onPostExecute(completedTask);
/*                Collections.sort(weekDataTask, new Comparator<Task>() {
                    @Override
                    public int compare(Task task, Task t1) {
                        return task.getDate().compareTo(t1.getDate());
                    }
                });*/






                for (Task task : weekDataTask) {
                    Log.d("TAG-Data", "Data = "+task.getDate());
                    if (!weekMap.containsKey(task.getDate())) {
                        weekMap.put(task.getDate(), 1);
                    } else {
                        weekMap.put(task.getDate(), weekMap.get(task.getDate()) + 1);
                    }
                }





                for (int i =0; i< 7; i++){
                    weekData.add(0);
                }

                for (int d : weekData){
                    Log.d("TAG-DATA", "data = "+d);
                }

                if (weekMap.size() < 7) {
                    while (weekMap.size() != 0){
                        String[] date = weekMap.firstKey().split("-");
                        LocalDate localDate = LocalDate.of(Integer.parseInt(date[2]), Integer.parseInt(date[1]), Integer.parseInt(date[0]));
                        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
                        int day = dayOfWeek.getValue();


                        Log.d("TAG-Data", "Date = "+ weekMap.firstKey()+"Day - " + (day));
                        weekData.remove(day-1);
                        weekData.add(day-1,weekMap.get(weekMap.firstKey()));
                        weekMap.remove(weekMap.firstKey());
                    }
                }



                int[] numArr = {1, 2, 3, 4, 5, 6, 7};
               // int[] numArr2 = {3, 6, 4, 3, 6, 7, 2};

                final HashMap<Integer, String> numMap = new HashMap<>();
                numMap.put(1, "Mon");
                numMap.put(2, "Tue");
                numMap.put(3, "Wed");
                numMap.put(4, "Thu");
                numMap.put(5, "Fri");
                numMap.put(6, "Sat");
                numMap.put(7, "Sun");

                List<Entry> values = new ArrayList<Entry>();

                int i = 0;
                for (int num : numArr) {

                    values.add(new Entry(num, weekData.get(i)));
                    i++;
                }


                XAxis xAxis = binding.chart.getXAxis();
                xAxis.setValueFormatter(new ValueFormatter() {

                    @Override
                    public String getAxisLabel(float value, AxisBase axis) {
                        return numMap.get((int) value);
                    }
                });


                LineDataSet lineDataSet = new LineDataSet(values, "Work");
                lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
                lineDataSet.setLineWidth(2);
                lineDataSet.setDrawValues(false);
                lineDataSet.setDrawFilled(true);
                Drawable drawable = ContextCompat.getDrawable(context, R.drawable.green_gradient);
                lineDataSet.setFillDrawable(drawable);

                lineDataSet.setColor(Color.CYAN);
                lineDataSet.setCircleRadius(6);
                lineDataSet.setCircleHoleRadius(3);
                lineDataSet.setDrawCircles(true);
                lineDataSet.setDrawHighlightIndicators(true);
                lineDataSet.setHighlightEnabled(true);
                lineDataSet.setHighLightColor(Color.CYAN);
                lineDataSet.setValueTextSize(12);
                lineDataSet.setValueTextColor(Color.DKGRAY);
                lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

                LineData lineData = new LineData(lineDataSet);
                binding.chart.getDescription().setTextSize(12);
                binding.chart.getDescription().setEnabled(false);
                binding.chart.getAxisLeft().setDrawAxisLine(false);
                binding.chart.getAxisLeft().setDrawGridLines(false);
                binding.chart.getAxisRight().setDrawAxisLine(false);
                binding.chart.getAxisRight().setDrawGridLines(false);
                binding.chart.getXAxis().setDrawAxisLine(false);
                binding.chart.getXAxis().setDrawGridLines(false);
                binding.chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                binding.chart.getLegend().setEnabled(false);


                binding.chart.animateY(1000);
                binding.chart.setData(lineData);


                binding.chart.getAxisRight().setEnabled(false);
                binding.chart.invalidate();

            }
        }
        GetSavedTasks savedTasks = new GetSavedTasks();
        savedTasks.execute();
    }
}