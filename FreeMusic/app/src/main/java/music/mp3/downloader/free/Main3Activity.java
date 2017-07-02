package music.mp3.downloader.free;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.alex.freemusic.R;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;

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
