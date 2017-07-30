package mp3.music.download.freesongs.freemp3;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.appodeal.ads.Appodeal;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class startActivity3 extends AppCompatActivity {
    int i = 0;
    final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    public boolean flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start3);
        if (preloadActivity.resultSettings.data.getNet_type() != 0) {
            if (Appodeal.isLoaded(Appodeal.INTERSTITIAL)) {
                Appodeal.show(this, Appodeal.INTERSTITIAL);

            }
        }
    }
    public void onClick3(View view){
        if(preloadActivity.resultSettings.data.is_burst != 0){
            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(preloadActivity.resultSettings.data.getBurst_url())));
                // https://play.google.com/store/apps/details?id=music.mp3.apps.top.download
                String urlStr = preloadActivity.resultSettings.data.getPopup_url();
                urlStr = urlStr.substring(urlStr.indexOf("="));
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id" + urlStr)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }else {
            nextActivity();
        }
    }

    public void preLoader(){

        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if(Appodeal.isLoaded(Appodeal.INTERSTITIAL)) {
                    nextActivity();
                    flag = true;
                    executorService.shutdown();
                }else{
                    i++;
                    System.out.println(i);
                }
                if(i>9){
                    System.out.println("i>9");
                    nextActivity();
                    flag = true;
                    executorService.shutdown();
                }

            }
        }, 0, 1, TimeUnit.SECONDS);
    }
    public void nextActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
}
