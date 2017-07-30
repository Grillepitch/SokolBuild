package mp3.music.download.freesongs.freemp3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.appodeal.ads.Appodeal;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class startActivity2 extends AppCompatActivity {
    int i = 0;
    final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    public boolean flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start2);

        if (preloadActivity.resultSettings.data.getNet_type() != 0) {
            if (Appodeal.isLoaded(Appodeal.INTERSTITIAL)) {
                Appodeal.show(this, Appodeal.INTERSTITIAL);
            }
        }

    }
    public void onClick2(View view){
       nextActivity();
    }

    public void preLoader() {
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (i > 9) {
                    flag = true;
                    executorService.shutdown();
                }

            }
        }, 0, 1, TimeUnit.SECONDS);
    }
    public void nextActivity(){
        Intent intent = new Intent(this, startActivity3.class);
        startActivity(intent);
    }
}
