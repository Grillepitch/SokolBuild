package music.mp3.downloader.free;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.media.AudioManager;

import android.media.MediaPlayer;
import android.net.Uri;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;


import com.example.alex.freemusic.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import java.net.URL;
import java.util.ArrayList;




public class BoxAdapter extends BaseAdapter {
    public Context ctx;
    public LayoutInflater lInflater;
    ImageButton lastImageButton;
    public ArrayList<Song> objects;
    public Song p;
    public  static Bitmap[] bitmap;
    public DownloadManager downloadManager;
    public boolean flag = false;
    public String currnetSongPlay = "";
    public Runnable runnable;
    public EditText name;
    public EditText message;
    public Song currentSong;
    public boolean start = false;
    public int a = 0;
    BoxAdapter(Context context, ArrayList<Song> Songs) {
        ctx = context;

        objects = Songs;

        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        lastImageButton = null;
        lastImageButton = new ImageButton(ctx);
        Main4Activity.imageViewSeek = new ImageView(ctx);
    }
    // кол-во элементов
    @Override
    public int getCount() {
        return objects.size();
    }

    public void setObjects(ArrayList<Song> objects) {

        this.objects = objects;
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    // пункт списка
    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view

        View view = convertView;
        if (view == null) view = lInflater.inflate(R.layout.item, parent, false);
        p = getSong(position);
        int i = 0;
        if(bitmap==null) {
            bitmap = new Bitmap[objects.size()];
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for(int i = 0;i<objects.size();i++) {
                        bitmap[i] = (downloadBitmap(getSong(i).getImg_small()));
                    }
                    flag = true;
                }
            }).start();
        }
        try {
            Thread.sleep(200);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        ((ImageView) view.findViewById(R.id.ivImage)).setImageBitmap(bitmap[position]);
        ((TextView) view.findViewById(R.id.tvDescr)).setText(p.getTitle());
        ((ImageButton)view.findViewById(R.id.reportButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Main4Activity.mAct);
                builder.setView(R.layout.popreport).setCancelable(false);
                Main4Activity.alertReport = builder.create();
                Main4Activity.alertReport.show();
                Main4Activity.videoId = objects.get(position).getVideoId();
                Main4Activity.title = objects.get(position).getTitle();
            }
        });
        ((ImageButton) view.findViewById(R.id.imageButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int PERMISSION_REQUEST_CODE= 0;
                if (ContextCompat.checkSelfPermission(Main4Activity.mAct, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(Main4Activity.mAct, Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Main4Activity.mAct,
                            new String[]{
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                            },
                            PERMISSION_REQUEST_CODE);
                }else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            downloadManager = (DownloadManager) Main4Activity.mAct.getSystemService(Context.DOWNLOAD_SERVICE);
                            Uri uri = Uri.parse(objects.get(position).getMp3Url());
                            DownloadManager.Request request = new DownloadManager.Request(uri);
                            request.setDescription("Download success").setTitle(objects.get(position).getTitle());
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            Long refrence = downloadManager.enqueue(request);
                        }
                    }).start();
                }
            }
        });

        ((ImageButton) view.findViewById(R.id.playButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Main4Activity.linearLayout.setVisibility(View.VISIBLE);
                if (currnetSongPlay.equals(objects.get(position).getMp3Url()))
                    if ( Main4Activity.mediaPlayer.isPlaying()) {
                        Main4Activity.mediaPlayer.pause();
                        lastImageButton.setBackgroundResource(R.drawable.play);
                        v.findViewById(R.id.playButton).setBackgroundResource(R.drawable.play);
                        Main4Activity.seekPlayButton.setBackgroundResource(R.drawable.play);
                        Main4Activity.textView.setText(objects.get(position).getTitle());
                    } else {
                        v.findViewById(R.id.playButton).setBackgroundResource(R.drawable.pause);
                        Main4Activity.seekPlayButton.setBackgroundResource(R.drawable.pause);
                        Main4Activity.mediaPlayer.start();
                        Main4Activity.textView.setText(objects.get(position).getTitle());
                    }
                else {
                    if (lastImageButton == null) {
                        lastImageButton = ((ImageButton) v.findViewById(R.id.playButton));
                        lastImageButton.setBackgroundResource(R.drawable.play);
                        Main4Activity.seekPlayButton.setBackgroundResource(R.drawable.play);
                        Main4Activity.textView.setText(objects.get(position).getTitle());
                    } else {
                        lastImageButton.setBackgroundResource(R.drawable.play);
                        lastImageButton = ((ImageButton) v.findViewById(R.id.playButton));
                        Main4Activity.seekPlayButton.setBackgroundResource(R.drawable.play);
                        Main4Activity.textView.setText(objects.get(position).getTitle());
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                 Main4Activity.mediaPlayer.reset();
                                 Main4Activity.mediaPlayer.setDataSource(objects.get(position).getMp3Url());
                                 Main4Activity.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                 Main4Activity.mediaPlayer.prepare();
                                currentSong = objects.get(position);
                                currnetSongPlay = objects.get(position).getMp3Url();
                                 Main4Activity.mediaPlayer.setOnPreparedListener(new  MediaPlayer.OnPreparedListener() {
                                    @Override
                                    public void onPrepared( MediaPlayer mp) {
                                        Main4Activity.mediaPlayer.start();
                                        Main4Activity.seekBar.setMax( Main4Activity.mediaPlayer.getDuration());
                                        playCicle();
                                    }
                                });
                             //   Main4Activity.seekBar.setMax( Main4Activity.mediaPlayer.getDuration());
                                Main4Activity.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                    @Override
                                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                        if (fromUser) {
                                            Main4Activity.mediaPlayer.seekTo(progress);
                                        }
                                    }

                                    @Override
                                    public void onStartTrackingTouch(SeekBar seekBar) {

                                    }

                                    @Override
                                    public void onStopTrackingTouch(SeekBar seekBar) {

                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }

                    }).start();
                    v.findViewById(R.id.playButton).setBackgroundResource(R.drawable.pause);
                    Main4Activity.seekPlayButton.setBackgroundResource(R.drawable.pause);
                    Main4Activity.textView.setText(objects.get(position).getTitle());


                }

            }
        });
        return view;



    }



    public void playCicle() {
        if(Main4Activity.playerAlive == true) {
            Main4Activity.seekBar.setProgress(Main4Activity.mediaPlayer.getCurrentPosition());
            if (Main4Activity.mediaPlayer.isPlaying()) {
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        playCicle();
                    }
                };
                Main4Activity.handler.postDelayed(runnable, 1000);
            }
        }
    }

    Song getSong(int position) {
        return ((Song) getItem(position));
    }

    private Bitmap downloadBitmap(String url) {
        HttpURLConnection urlConnection = null;
        try {
            URL uri = new URL(url);
            urlConnection = (HttpURLConnection) uri.openConnection();
            int statusCode = urlConnection.getResponseCode();

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream != null) {
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        } catch (Exception e) {
            urlConnection.disconnect();
            Log.w("ImageDownloader", "Error downloading image from " + url);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }





}
