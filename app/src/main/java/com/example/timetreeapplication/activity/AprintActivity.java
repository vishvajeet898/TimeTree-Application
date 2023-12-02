package com.example.timetreeapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.codegama.timetreeapplication.R;
import com.codegama.timetreeapplication.databinding.ActivityAprintBinding;
import com.codegama.timetreeapplication.databinding.ActivityToDoHomeBinding;
import com.uzairiqbal.circulartimerview.CircularTimerListener;
import com.uzairiqbal.circulartimerview.CircularTimerView;
import com.uzairiqbal.circulartimerview.TimeFormatEnum;

public class AprintActivity extends AppCompatActivity {


    ActivityAprintBinding binding;
    Boolean started = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAprintBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();

        setContentView(v);

        binding.progressBar.setProgress(0);



// To Initialize Timer
        binding.progressBar.setCircularTimerListener(new CircularTimerListener() {
            @Override
            public String updateDataOnTick(long remainingTimeInMs) {
                return String.valueOf((int)Math.ceil((remainingTimeInMs / 1000.f)));
            }

            @Override
            public void onTimerFinished() {
                binding.progressBar.setPrefix("");
                binding.progressBar.setSuffix("");
                binding.progressBar.setText("FINISHED THANKS!");
            }
        }, 20, TimeFormatEnum.MINUTES, 20);




        binding.start.setOnClickListener(view -> {
            binding.start.setVisibility(View.GONE);
            binding.progressBar.startTimer();
        });


        binding.backTime.setOnClickListener(view -> {
            Intent i = new Intent(this, ToDoHome.class);
            startActivity(i);
        });

    }
}