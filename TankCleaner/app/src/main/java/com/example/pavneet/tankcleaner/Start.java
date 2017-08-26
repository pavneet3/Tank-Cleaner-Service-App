package com.example.pavneet.tankcleaner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

public class Start extends AppCompatActivity implements Runnable
{

    Thread thread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_start);
        thread =new Thread(this);
        thread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();

    }

    @Override
    public void run() {
        try {
            thread.sleep(5000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally{Intent intent=new Intent(this,MidActivity.class);
            startActivity(intent);

        }
    }
}
