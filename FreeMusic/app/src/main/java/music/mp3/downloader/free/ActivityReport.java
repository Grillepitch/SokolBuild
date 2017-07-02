package music.mp3.downloader.free;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.example.alex.freemusic.R;

public class ActivityReport extends AppCompatActivity {
    private WebView mWebView;
    public static String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_report);

        mWebView = (WebView) findViewById(R.id.webreport);
        // включаем поддержку JavaScript
        mWebView.getSettings().setJavaScriptEnabled(true);
        // указываем страницу загрузки
        mWebView.loadUrl(url);
    }
}
