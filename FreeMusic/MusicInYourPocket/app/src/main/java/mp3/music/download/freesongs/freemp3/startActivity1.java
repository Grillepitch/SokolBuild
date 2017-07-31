package mp3.music.download.freesongs.freemp3;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.appodeal.ads.Appodeal;

import com.crashlytics.android.Crashlytics;

import com.google.gson.Gson;

import io.fabric.sdk.android.Fabric;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class startActivity1 extends AppCompatActivity {
    public static Activity mAct;
    int i = 0;
     ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    public boolean flag = false;
    public boolean setFlag = false;
   // public static ResultSettings resultSettings = new ResultSettings();
    public ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_start1);
        mAct = this;
//        if(preloadActivity.resultSettings.data == null){
//            preloadActivity.resultSettings.data  = new DataSettings(1,1,"",0,"");
//        }
    }

    public void onClick(View view) {
            nextActivity();
    }


    public void preLoader() {
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (Appodeal.isLoaded(Appodeal.INTERSTITIAL)) {
                    flag = true;
                    executorService.shutdown();
                }
                if (i > 9) {
                    flag = true;
                    executorService.shutdown();
                }

            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    public void nextActivity() {
        Intent intent = new Intent(this, startActivity2.class);
        startActivity(intent);
    }

    public static String readAll(InputStream stream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder response = new StringBuilder();
        try {
            String readedLine;
            while ((readedLine = reader.readLine()) != null)
                response.append(readedLine).append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    public void getJsonForSettings() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream stream = null;
                    stream = new URL("http://asoptimize.me/settings.php")
                            .openConnection().getInputStream();
                    Gson gson = new Gson();
                    preloadActivity.resultSettings = gson.fromJson(readAll(stream), ResultSettings.class);
                    Log.d("lol", readAll(stream));
                    setFlag = true;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
