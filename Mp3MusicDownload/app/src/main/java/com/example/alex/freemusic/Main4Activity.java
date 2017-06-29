package com.example.alex.freemusic;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;




import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import java.net.MalformedURLException;

import java.net.URL;
import java.net.URLEncoder;




public class Main4Activity extends AppCompatActivity {

    Toolbar toolbar ;
    public static Activity mAct;
    public Page page = new Page();
    private BoxAdapter boxAdapter;
    private EditText editText;
    public boolean flag = false;
    private ImageButton buttonSearch;
    public String query = "";
    public static ListView lMain;
    public static MediaPlayer mediaPlayer;
    public static LinearLayout linearLayout;
    public static ImageButton seekPlayButton;
    public static DownloadManager downloadManager;
    public static SeekBar seekBar;
    public static ImageView imageViewSeek;
    public View view;
    public  EditText name;
    public  EditText message;
    public static String videoId;
    public static String title;
    public static String resultreportString;
    public static Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            // This is where you do your work in the UI thread.
            // Your worker tells you in the message what to do.
        }
    };
    public static RatingBar ratingBar;
    public RelativeLayout relativeLayoutPop;
    public static AlertDialog alert;
    public static AlertDialog alertReport;
    public static Button laterButton;
    public LinearLayout linearLayoutButton;

    public static final String APP_PREFERENCES = "mysettings";
    private static final String MY_SETTINGS = "my_settings";
    public static final String APP_PREFERENCES_NUM = "yet";
    public SharedPreferences mSettings;
    public int counter;
    public SharedPreferences mySharedPreferences;
    public SharedPreferences sp;
    public Button BigButton;
    public LayoutInflater layoutInflater;
    public static TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        toolbar = (Toolbar)findViewById(R.id.toolbarP);
       // setSupportActionBar(toolbar);
        layoutInflater = getLayoutInflater();
        sp = getSharedPreferences(MY_SETTINGS,
                Context.MODE_PRIVATE);
        // проверяем, первый ли раз открывается программа
        boolean hasVisited = sp.getBoolean("hasVisited", false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        mediaPlayer = new MediaPlayer();
        mAct = this;
        buttonSearch = (ImageButton) findViewById(R.id.search_button);
        seekBar = (SeekBar) findViewById(R.id.seekBar);


        new Thread(new Runnable() {
            @Override
            public void run() {
                //StartPage
                try {
                    InputStream stream = null;
                    stream = new URL("https://mp3download.tube/search.php?q=" + query)
                            .openConnection().getInputStream();
                    Gson gson = new Gson();
                    page = gson.fromJson(readAll(stream), Page.class);
                    flag = true;
                    Log.d("lol", readAll(stream));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
        int a = 0;

        while (a != 1) {
            if (flag == true) {
                a = 1;
                flag = false;
            }

        }
                if(MainActivity.result.data.getIs_burst() == 0){
                    boxAdapter = new BoxAdapter(this, page.getVideos());
                    ListView lvMain = (ListView) findViewById(R.id.lvMain);
                    lvMain.setAdapter(boxAdapter);
                }
                if(MainActivity.result.data.getIs_burst() == 2){
                    BigButton  = (Button)  layoutInflater.inflate(R.layout.big_bamm_button,linearLayoutButton,false);
                    linearLayoutButton = (LinearLayout)findViewById(R.id.layoutForButton);
                    linearLayoutButton.addView(BigButton);
                    boxAdapter = new BoxAdapter(this, page.getVideos());
                    ListView lvMain = (ListView) findViewById(R.id.lvMain);
                    lvMain.setAdapter(boxAdapter);
                }

        linearLayout = (LinearLayout) findViewById(R.id.seekbarLayout);
        seekPlayButton = (ImageButton) findViewById(R.id.playButtonSeek);
        textView = (TextView) findViewById(R.id.song_title);
        if (!hasVisited) {
            if (MainActivity.result.data.getPopup() == 1) {
                popUp();
            }
        }
        if(MainActivity.result.data.getNet_type() == 1) {
            StartAppSDK.init(this, "205127939", true);
            StartAppAd.showAd(this);
        }
        flag = false;


        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

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

    public void getPageFromJSon() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream stream = null;
                    stream = new URL("https://mp3download.tube/search.php?q=" + query)
                            .openConnection().getInputStream();
                    Gson gson = new Gson();
                    page = gson.fromJson(readAll(stream), Page.class);
                    flag = true;
                    Log.d("lol", readAll(stream));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        int i = 0;
        while (i != 1) {
            if (flag == true) {
                if(MainActivity.result.data.getIs_burst() == 0){
                    boxAdapter.setObjects(page.getVideos());
                    boxAdapter.notifyDataSetChanged();
                }
                if(MainActivity.result.data.getIs_burst() == 2){
                    if(BigButton == null) {
                        BigButton = (Button) layoutInflater.inflate(R.layout.big_bamm_button, linearLayoutButton, false);
                        linearLayoutButton = (LinearLayout) findViewById(R.id.layoutForButton);
                        linearLayoutButton.addView(BigButton);
                    }
                    boxAdapter.setObjects(page.getVideos());
                    boxAdapter.notifyDataSetChanged();
                }

                i = 1;
                flag = false;
            }
        }

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
                    MainActivity.result = gson.fromJson(readAll(stream), Result.class);
                    flag = true;
                    Log.d("lol", readAll(stream));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void onClick(View view) {

        if(MainActivity.result.data.getNet_type() == 1) {
            StartAppAd.showAd(this);
        }
        editText = (EditText) findViewById(R.id.searchLine);
        try {
            query = URLEncoder.encode(editText.getText().toString(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        getPageFromJSon();
        if(boxAdapter != null) {
            boxAdapter.lastImageButton.setBackgroundResource(R.drawable.play);
            if(mediaPlayer.isPlaying()) {
                Main4Activity.seekPlayButton.setBackgroundResource(R.drawable.play);
                boxAdapter.lastImageButton.setBackgroundResource(R.drawable.play);
                mediaPlayer.pause();
                textView.setText("");
            }
        }
        InputMethodManager inputManager =
                (InputMethodManager) this.
                        getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(
                this.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void onClickSeekPlay(View view) {
        if (mediaPlayer.isPlaying()) {
            Main4Activity.seekPlayButton.setBackgroundResource(R.drawable.play);
            boxAdapter.lastImageButton.setBackgroundResource(R.drawable.play);
            mediaPlayer.pause();
        } else {
            Main4Activity.seekPlayButton.setBackgroundResource(R.drawable.pause);
            boxAdapter.lastImageButton.setBackgroundResource(R.drawable.pause);
            mediaPlayer.start();
        }
    }

    public void OnClickMaybeLater(View view) {
        alert.cancel();
    }

    public void OnClickBigButton(View view) {
        String str = MainActivity.result.data.getBurst_url();
        String apk = ".apk";
         if(str.contains(apk)){
             downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
             Uri uri = Uri.parse(str);
             DownloadManager.Request request = new DownloadManager.Request(uri);
             request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
             Long refrence = downloadManager.enqueue(request);
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

    public void popUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Main4Activity.this);
        builder.setView(R.layout.pop).setCancelable(false);
        alert = builder.create();
        alert.show();

        ratingBar = (RatingBar) alert.findViewById(R.id.ratingBar1);
        laterButton = (Button) alert.findViewById(R.id.buttonLater);
        alert.findViewById(R.id.ratingBar1).setOnTouchListener(new View.OnTouchListener() {
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
                            // startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(result.getData().getPopup_url())));
                            //https://play.google.com/store/apps/details?id=music.mp3.apps.top.download
                            String urlStr = MainActivity.result.getData().getPopup_url();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main,menu);
        return true;
    }
//    public void OnClickPolicy(View view){
//        AlertDialog.Builder builder = new AlertDialog.Builder(Main4Activity.this);
//        builder.setView(R.layout.popreport).setCancelable(false);
//        alertReport = builder.create();
//        alertReport.show();
//        System.out.println("Main activity");
//
//    }

    public void OnClickPolicyReport(View view){
        name = (EditText)alertReport.findViewById(R.id.nameText);
        message = (EditText) alertReport.findViewById(R.id.messageText);
        String url="";
        try {
            String nameString = URLEncoder.encode(name.getText().toString(), "utf-8");
            String messageString = URLEncoder.encode(message.getText().toString(), "utf-8");
            //  https://mp3-downloaders.com/report.php?message=hui&fullname=artur
            if(!nameString.isEmpty() && !messageString.isEmpty()) {
               messageString=  messageString.concat(" VideoID "+videoId+" title "+title);
                messageString = URLEncoder.encode(messageString,"utf-8");
                url  = ("https://mp3-downloaders.com/report.php?message="+messageString+"&fullname="+nameString);

                alertReport.cancel();
            }
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        final RequestQueue queue = Volley.newRequestQueue(this);

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("_____________________________");
                        System.out.println("success");
                        System.out.println(response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("error");
            }
        });

        queue.add(stringRequest);
        System.out.println("FFFFFFFFFFFFF");
        System.out.println(queue.getSequenceNumber());
    }
    public void OnCliclPolicyCancel(View view){
        alertReport.cancel();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(Main4Activity.this,ActivityWeb.class);
        startActivity(intent);
        return true;
    }

    public void OnClickCC(View view ){
        Intent intent = new Intent(Main4Activity.this,ActivityCC.class);
        startActivity(intent);
    }


}
