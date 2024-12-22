package com.example.myapplication5;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView counter1TextView;
    private TextView counter2TextView;
    private Button startButton;
    private Button stopButton;
    private Handler handler = new Handler();
    private int counter1 = 0;
    private int counter2 = 0;
    private boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        counter1TextView = findViewById(R.id.counter1TextView);
        counter2TextView = findViewById(R.id.counter2TextView);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCounters();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopCounters();
            }
        });
    }

    private void startCounters() {
        isRunning = true;
        counter1 = 0;
        counter2 = 0;
        updateCounters();
    }

    private void stopCounters() {
        isRunning = false;
        handler.removeCallbacks(updateRunnable);
    }

    private void updateCounters() {
        if (isRunning) {
            counter1++;
            counter2++;
            counter1TextView.setText("Counter 1: " + counter1);
            counter2TextView.setText("Counter 2: " + counter2);

            if (counter1 <= 30 && counter2 <= 30) {
                handler.postDelayed(updateRunnable, 1000);
            } else {
                isRunning = false;
            }
        }
    }

    private Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            updateCounters();
        }
    };
}
