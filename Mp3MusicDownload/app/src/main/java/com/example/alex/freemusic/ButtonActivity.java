package com.example.alex.freemusic;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ButtonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button);
    }

    public void OnClickBigButton(View view) {
        String str = MainActivity.result.data.getBurst_url();
        String apk = ".apk";
        if(str.contains(apk)){
            Main4Activity.downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(str);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            Long refrence =  Main4Activity.downloadManager.enqueue(request);
        }else{
            final String appPackageName = getPackageName();
            try {

                String urlStr =MainActivity.result.getData().getPopup_url();
                urlStr = urlStr.substring(urlStr.indexOf("="));
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id" + urlStr)));
            }catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }
    }
}
