package com.example.alex.freemusic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class Main3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

                if(MainActivity.result.data.getNet_type() == 1) {
                    StartAppSDK.init(this, "205127939", true);
                    StartAppAd.showAd(this);
                }

    }

    public void onClick(View view) {
        if(MainActivity.result.data.getIs_burst() == 1)
        {
            Intent intent = new Intent(Main3Activity.this,ButtonActivity.class);
            startActivity(intent);
        }else {

            Intent intent = new Intent(Main3Activity.this, Main4Activity.class);
            startActivity(intent);
        }

    }


}