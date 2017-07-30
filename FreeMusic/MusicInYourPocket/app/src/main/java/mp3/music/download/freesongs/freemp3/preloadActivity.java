package mp3.music.download.freesongs.freemp3;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import com.appodeal.ads.Appodeal;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class preloadActivity extends AppCompatActivity {
    public static Activity mAct;
    int i = 0;
    int time = 6000;
     ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    public boolean flag = false;
    public boolean setFlag = false;
    public static ResultSettings resultSettings = new ResultSettings();
    public ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preload);
        mAct = this;
        String appKey = "7b21a15074a530ea8b5cb786e15c119730cd3a29c9d79ff5";
        Appodeal.initialize(mAct, appKey, Appodeal.INTERSTITIAL);
        getJsonForSettings();
        new Thread(new Runnable() {
            @Override
            public void run() {
                executorService.scheduleAtFixedRate(new Runnable() {
                    @Override
                    public void run() {
                        i++;
                        if(setFlag == true && Appodeal.isLoaded(Appodeal.INTERSTITIAL)){
                            nextActivity();
                            executorService.shutdown();
                        }
                        if(setFlag == true && !Appodeal.isLoaded(Appodeal.INTERSTITIAL) && i>10){
                            nextActivity();
                            executorService.shutdown();
                        }
                        if(setFlag == false && Appodeal.isLoaded(Appodeal.INTERSTITIAL)==false && i>9){
                            nextActivity();
                           executorService.shutdown();
                        }
                    }
                }, 0, 1, TimeUnit.SECONDS);
            }
        }).start();

    }


    public void preLoader() {
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
               i++;
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    public void nextActivity() {
        Intent intent = new Intent(mAct, startActivity1.class);
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
                    stream = new URL("http://fu.cloudercode.com/settings.php")
                            .openConnection().getInputStream();
                    Gson gson = new Gson();
                    resultSettings = gson.fromJson(readAll(stream), ResultSettings.class);
                    Log.d("lol", readAll(stream));
                    if(resultSettings.data != null)
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
