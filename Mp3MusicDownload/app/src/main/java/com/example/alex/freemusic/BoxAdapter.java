package com.example.alex.freemusic;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;


import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;




public class BoxAdapter extends BaseAdapter {
    public Context ctx;
    public LayoutInflater lInflater;
    ImageButton lastImageButton;
    public ArrayList<Song> objects;
    public Song p;
    public Bitmap bitmap;
    public DownloadManager downloadManager;
    public boolean flag = false;
    public String currnetSongPlay = "";
    public Runnable runnable;
    public EditText name;
    public EditText message;
    public Song currentSong;
    BoxAdapter(Context context, ArrayList<Song> Songs) {
        ctx = context;

        objects = Songs;

        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        lastImageButton = new ImageButton(ctx);
        currentSong = objects.get(0);
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                bitmap = (downloadBitmap(p.getImg_small()));
                flag = true;
            }
        }).start();
        int i = 0;
        while (i != 1) {
            if (flag == true) {
                ((ImageView) view.findViewById(R.id.ivImage)).setImageBitmap(bitmap);
                i = 1;
                flag = false;
            }
        }
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
                downloadManager = (DownloadManager) Main4Activity.mAct.getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(objects.get(position).getMp3Url());
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                Long refrence = downloadManager.enqueue(request);

            }
        });
        if(!currentSong.getMp3Url().equals(currnetSongPlay))
        ((ImageButton) view.findViewById(R.id.playButton)).setBackgroundResource(R.drawable.play);
        ((ImageButton) view.findViewById(R.id.playButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Main4Activity.linearLayout.setVisibility(View.VISIBLE);
                if (currnetSongPlay.equals(objects.get(position).getMp3Url()))
                    if (Main4Activity.mediaPlayer.isPlaying()) {
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
                                Main4Activity.mediaPlayer.start();
                                currentSong = objects.get(position);
                                currnetSongPlay = objects.get(position).getMp3Url();
                                Main4Activity.seekBar.setMax(Main4Activity.mediaPlayer.getDuration());
                                Main4Activity.mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                    @Override
                                    public void onPrepared(MediaPlayer mp) {
                                        Main4Activity.seekBar.setMax(Main4Activity.mediaPlayer.getDuration());
                                        playCicle();
                                    }
                                });

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


}
