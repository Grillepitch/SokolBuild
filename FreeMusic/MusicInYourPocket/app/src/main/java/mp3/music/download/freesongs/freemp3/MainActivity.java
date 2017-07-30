package mp3.music.download.freesongs.freemp3;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import android.net.Uri;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.MotionEvent;
import android.view.View;

import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import android.widget.RatingBar;


import com.appodeal.ads.Appodeal;


public class MainActivity extends AppCompatActivity {
    private WebView mWebView;
    private DownloadManager downloadManager;
    private Activity mAct;
    public static RatingBar ratingBar;
    public static AlertDialog alert;
    public static Button laterButton;
    public SharedPreferences sp;
    private static final String MY_SETTINGS = "my_settings";
    private boolean permFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAct = this;
        int a = 0;
        sp = getSharedPreferences(MY_SETTINGS,
                Context.MODE_PRIVATE);

        boolean hasVisited = sp.getBoolean("hasVisited", false);
        mWebView = (WebView) findViewById(R.id.musicWebView);
        // включаем поддержку JavaScript
        mWebView.getSettings().setJavaScriptEnabled(true);
        // указываем страницу загрузки
        mWebView.loadUrl("http://fu.cloudercode.com/");
        if (preloadActivity.resultSettings.data.getPopup() !=0) {
            if(!hasVisited)
            popUp();
        }
        final int PERMISSION_REQUEST_CODE= 0;
        if (ContextCompat.checkSelfPermission(mAct, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(mAct, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mAct,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    PERMISSION_REQUEST_CODE);
        }
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                final int PERMISSION_REQUEST_CODE= 0;
                if (ContextCompat.checkSelfPermission(mAct, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(mAct, Manifest.permission.READ_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_GRANTED) {
                    downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri uri = Uri.parse(url);
                    System.out.println(url);
                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    Long refrence = downloadManager.enqueue(request);
                }else{
                    ActivityCompat.requestPermissions(mAct,
                            new String[]{
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                            },
                            PERMISSION_REQUEST_CODE);
                }
                if (preloadActivity.resultSettings.data.getNet_type() != 0) {
                    if (Appodeal.isLoaded(Appodeal.INTERSTITIAL))
                        Appodeal.show(mAct, Appodeal.INTERSTITIAL);
                }
                return true;
            }



        });



    }


    public void OnClickMaybeLater(View view) {
        alert.cancel();
    }

    public void popUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(R.layout.pop).setCancelable(false);
        alert = builder.create();
        alert.show();

        ratingBar = (RatingBar) alert.findViewById(R.id.ratingBar2);
        laterButton = (Button) alert.findViewById(R.id.button5);
        alert.findViewById(R.id.ratingBar2).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    float touchPositionX = event.getX();
                    float width = ratingBar.getWidth();
                    float starsf = (touchPositionX / width) * 5.0f;
                    int stars = (int) starsf + 1;
                    ratingBar.setRating(stars);

                    if (ratingBar.getRating() > 3) {
                        SharedPreferences.Editor e = sp.edit();
                        e.putBoolean("hasVisited", true);
                        e.commit(); // не забудьте подтвердить изменения
                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(preloadActivity.resultSettings.data.getPopup_url())));
                            // https://play.google.com/store/apps/details?id=music.mp3.apps.top.download
                            String urlStr = preloadActivity.resultSettings.data.getPopup_url();
                            urlStr = urlStr.substring(urlStr.indexOf("="));
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id" + urlStr)));
                            alert.cancel();
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                            alert.cancel();
                        }
                    } else {
                        alert.cancel();
                        SharedPreferences.Editor e = sp.edit();
                        e.putBoolean("hasVisited", true);
                        e.commit(); // не забудьте подтвердить изменения
                    }
                    v.setPressed(false);
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                }

                if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    v.setPressed(false);
                }

                return true;
            }


        });
    }

//    public void getJsonForSettings() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    InputStream stream = null;
//                    stream = new URL("http://asoptimize.me/settings.php")
//                            .openConnection().getInputStream();
//                    Gson gson = new Gson();
//                    resultSettings = gson.fromJson(readAll(stream), ResultSettings.class);
//                    Log.d("lol", readAll(stream));
//                    setFlag = true;
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }
//
//    public static String readAll(InputStream stream) {
//        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
//        StringBuilder response = new StringBuilder();
//        try {
//            String readedLine;
//            while ((readedLine = reader.readLine()) != null)
//                response.append(readedLine).append("\n");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return response.toString();
//    }
}
