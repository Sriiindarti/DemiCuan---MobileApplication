package com.example.demicuanapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    // Membuat splash screen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        Thread thread = new Thread() {
            public void run() {
                try {
                    sleep(3000); // Dengan waktu splash 3 Detik
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(new Intent(SplashScreenActivity.this, WelcomeActivity.class)); // Akan berpindah ke halaman main
                    finish();
                }
            }
        };
        thread.start();
    }
}
