package com.example.timetreeapplication.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codegama.timetreeapplication.R;
import com.codegama.timetreeapplication.databinding.ItemTaskBinding;
import com.example.timetreeapplication.activity.ToDoHome;
import com.example.timetreeapplication.bottomSheetFragment.CreateTaskBottomSheetFragment;
import com.example.timetreeapplication.database.DatabaseClient;
import com.example.timetreeapplication.model.Task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private ToDoHome context;
 //   private LayoutInflater inflater;
    private List<Task> taskList;
    public SimpleDateFormat dateFormat = new SimpleDateFormat("EE dd MMM yyyy", Locale.US);
    public SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd-M-yyyy", Locale.US);
    Date date = null;
    String outputDateString = null;
    CreateTaskBottomSheetFragment.setRefreshListener setRefreshListener;

    public TaskAdapter(ToDoHome context, List<Task> taskList, CreateTaskBottomSheetFragment.setRefreshListener setRefreshListener) {
        this.context = context;
        this.taskList = taskList;
        this.setRefreshListener = setRefreshListener;
       // this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        ItemTaskBinding itemBinding = ItemTaskBinding .inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
//        View view = inflater.inflate(R.layout.item_task, viewGroup, false);
        return new TaskViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.binding.title.setText(task.getTaskTitle());
        holder.binding.description.setText(task.getTaskDescrption());
        holder.binding.time.setText(task.getLastAlarm());
        holder.binding.status.setText(task.isComplete() ? "COMPLETED" : "UPCOMING");
        holder.binding.options.setOnClickListener(view -> showPopUpMenu(view, position));

        try {
            date = inputDateFormat.parse(task.getDate());
            outputDateString = dateFormat.format(date);

            String[] items1 = outputDateString.split(" ");
            String day = items1[0];
            String dd = items1[1];
            String month = items1[2];

            holder.binding.day.setText(day);
            holder.binding.date.setText(dd);
            holder.binding.month.setText(month);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showPopUpMenu(View view, int position) {
        final Task task = taskList.get(position);
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menuDelete){
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.AppTheme_Dialog);
                alertDialogBuilder.setTitle(R.string.delete_confirmation).setMessage(R.string.sureToDelete).
                        setPositiveButton(R.string.yes, (dialog, which) -> {
                            deleteTaskFromId(task, position);
                        })
                        .setNegativeButton(R.string.no, (dialog, which) -> dialog.cancel()).show();
            }
            else if (item.getItemId() == R.id.menuUpdate){
                CreateTaskBottomSheetFragment createTaskBottomSheetFragment = new CreateTaskBottomSheetFragment();
                createTaskBottomSheetFragment.setTaskId(task.getTaskId(), true, context, context);
                createTaskBottomSheetFragment.show(context.getSupportFragmentManager(), createTaskBottomSheetFragment.getTag());

            }
            else if (item.getItemId() == R.id.menuComplete){
                AlertDialog.Builder completeAlertDialog = new AlertDialog.Builder(context, R.style.AppTheme_Dialog);
                completeAlertDialog.setTitle(R.string.confirmation).setMessage(R.string.sureToMarkAsComplete).
                        setPositiveButton(R.string.yes, (dialog, which)  -> {
                            showCompleteDialog(task, position);
                            updateTaskFromId(task, position);
                        })
                        .setNegativeButton(R.string.no, (dialog, which) -> dialog.cancel()).show();

            }

            return false;
        });
        popupMenu.show();
    }

    public void showCompleteDialog(Task task, int position) {
        Dialog dialog = new Dialog(context, R.style.AppTheme);
        dialog.setContentView(R.layout.dialog_completed_theme);
        Button close = dialog.findViewById(R.id.closeButton);
        close.setOnClickListener(view -> {
            deleteTaskFromId(task, position);
            dialog.dismiss();
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }


    private void updateTaskFromId(Task task, int position) {
        class GetSavedTasks extends AsyncTask<Void, Void, List<Task>> {
            @Override
            protected List<Task> doInBackground(Void... voids) {
                DatabaseClient.getInstance(context)
                        .getAppDatabase()
                        .dataBaseAction()
                        .updateAnExistingRow(task.getTaskId(),task.getTaskTitle(),task.getTaskDescrption(),"1",task.getDate(),
                        task.getFirstAlarmTime(), task.getEvent());

                return taskList;
            }

            @Override
            protected void onPostExecute(List<Task> tasks) {
                super.onPostExecute(tasks);
                removeAtPosition(position);
                setRefreshListener.refresh();
            }
        }
        GetSavedTasks savedTasks = new GetSavedTasks();
        savedTasks.execute();
    }
private void deleteTaskFromId(Task task, int position) {
        class GetSavedTasks extends AsyncTask<Void, Void, List<Task>> {
            @Override
            protected List<Task> doInBackground(Void... voids) {
                DatabaseClient.getInstance(context)
                        .getAppDatabase()
                        .dataBaseAction()
                        .deleteTaskFromId(task.getTaskId());


                return taskList;
            }

            @Override
            protected void onPostExecute(List<Task> tasks) {
                super.onPostExecute(tasks);
                removeAtPosition(position);
                setRefreshListener.refresh();
            }
        }
        GetSavedTasks savedTasks = new GetSavedTasks();
        savedTasks.execute();
    }




    private void removeAtPosition(int position) {
        taskList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, taskList.size());
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {


        ItemTaskBinding binding;

        TaskViewHolder(@NonNull ItemTaskBinding b) {
            super(b.getRoot());
            binding = b;
        }
    }

}