package com.example.timetreeapplication.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.timetreeapplication.model.Task;

import java.util.List;

@Dao
public interface OnDataBaseAction {
    String completed = "1";

    @Query("SELECT * FROM Task")
    List<Task> getAllTasksList();

    @Query("DELETE FROM Task")
    void truncateTheList();

    @Insert
    void insertDataIntoTaskList(Task task);

    @Query("DELETE FROM Task WHERE taskId = :taskId")
    void deleteTaskFromId(int taskId);

    @Query("SELECT * FROM Task WHERE taskId = :taskId")
    Task selectDataFromAnId(int taskId);

    @Query("UPDATE Task SET taskTitle = :taskTitle, taskDescription = :taskDescription, isComplete = :isComplete, date = :taskDate, " +
            "lastAlarm = :taskTime, event = :taskEvent WHERE taskId = :taskId")
    void updateAnExistingRow(int taskId, String taskTitle, String taskDescription ,String isComplete,  String taskDate, String taskTime,
                             String taskEvent);

    @Query("select * from task where isComplete == :completed")
    List<Task> getCompleted(String completed);


    @Query("SELECT * FROM task WHERE date >= DATE('now','localtime','weekday 6','-7 days')")
    List<Task> getWeekData();
}