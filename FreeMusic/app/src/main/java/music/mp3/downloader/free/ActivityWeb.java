package music.mp3.downloader.free;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.crashlytics.android.Crashlytics;
import com.example.alex.freemusic.R;

import io.fabric.sdk.android.Fabric;

public class ActivityWeb extends AppCompatActivity {
    int check= 0;
    private WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)
                .build();
        Fabric.with(fabric);
        mWebView = (WebView) findViewById(R.id.webView);
        // включаем поддержку JavaScript
        mWebView.getSettings().setJavaScriptEnabled(true);
        // указываем страницу загрузки
        mWebView.loadUrl("http://audiko.net/privacy.html");



    }

    public void forceCrash(View view) {
        throw new RuntimeException("This is a crash");
    }

}
