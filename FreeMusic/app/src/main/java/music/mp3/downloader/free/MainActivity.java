package music.mp3.downloader.free;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.example.alex.freemusic.R;
import com.google.android.gms.ads.InterstitialAd;

import io.fabric.sdk.android.Fabric;

import com.google.gson.Gson;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends AppCompatActivity {
    InterstitialAd mInterstitialAd;
    boolean flag = false;
    Thread s;
    public static Result result = new Result();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        getJsonForSettings();
        int a = 0;
        while (a != 1) {
            if (flag == true) {
                if(result.data.getNet_type() == 1) {
                    StartAppSDK.init(this, "205127939", true);
                    StartAppAd.showAd(this);
                }
                a = 1;
                flag = false;
            }

        }

    }

    public void onClick(View view) {

            Intent intent = new Intent(MainActivity.this,Main2Activity.class);
            startActivity(intent);

    }
    public void forceCrash(View view) {
        throw new RuntimeException("This is a crash");
    }


    public void getJsonForSettings() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream stream = null;
                    stream = new URL("http://mp3download.guru/api/v1/sokolstyle/settings")
                            .openConnection().getInputStream();
                    Gson gson = new Gson();
                    result = gson.fromJson(Main4Activity.readAll(stream), Result.class);
                    Log.d("lol", Main4Activity.readAll(stream));
                    flag = true;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
