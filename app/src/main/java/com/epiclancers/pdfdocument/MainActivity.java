package com.epiclancers.pdfdocument;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import androidx.work.Constraints;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkStatus;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Constraints constraints = new Constraints.Builder().setRequiresCharging(true).build();
        OneTimeWorkRequest task = new OneTimeWorkRequest.Builder(CreatePdfWhenCharging.class).setConstraints(constraints).build();

        WorkManager workManager = WorkManager.getInstance();
        workManager.enqueue(task);


        workManager.getStatusById(task.getId()).observe(this, new Observer<WorkStatus>() {
            @Override
            public void onChanged(@Nullable WorkStatus workStatus) {
                Toast.makeText(MainActivity.this, "Changing Work Status", Toast.LENGTH_SHORT).show();
                System.out.println("Changing Work Status");
            }
        });




    }
}
