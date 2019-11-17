package id.ac.itb.a8eh.radiostreaming;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import id.ac.itb.a8eh.radiostreaming.service.StreamingPlayerService;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;

import java.util.Objects;

public class StreamingScreen extends AppCompatActivity implements ExoPlayer.EventListener {

    private boolean isPlaying;
    private boolean everPlaying;
    private boolean isNotify;
//    private MediaPlayer mediaPlayer;
//    private static SimpleExoPlayer player;
//    private NotificationCompat.Builder mBuilder;

    NotificationCompat.Builder notification;
    PendingIntent pIntent;
    NotificationManager manager;
    Intent resultIntent;
    TaskStackBuilder stackBuilder;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streaming_screen);

        Intent intent = getIntent();

        String isFromNotification = intent.getStringExtra("isFromNotification");
        if (TextUtils.isEmpty(isFromNotification)) {
//            everPlaying = false;
//            isNotify = false;

            String statusPlaying = intent.getStringExtra("isPlaying");
            if (TextUtils.equals(statusPlaying, "false") || TextUtils.isEmpty(statusPlaying)) {
                isPlaying = false;
//                Toast toast = Toast.makeText(getApplicationContext(), "false", Toast.LENGTH_SHORT);
//                toast.show();
            }
            else {
                isPlaying = true;
                everPlaying = true;
                ImageButton playButton = (ImageButton) findViewById(R.id.playButton);
                playButton.setBackgroundResource(R.drawable.btn_pause);

                TextView text = (TextView) findViewById(R.id.statusPlayer);
                text.setText("Now Playing");

//                Toast toast = Toast.makeText(getApplicationContext(), "true", Toast.LENGTH_SHORT);
//                toast.show();
            }
        }
        else {

            isPlaying = true;
            everPlaying = true;
            ImageButton playButton = (ImageButton) findViewById(R.id.playButton);
            playButton.setBackgroundResource(R.drawable.btn_pause);

            TextView text = (TextView) findViewById(R.id.statusPlayer);
            text.setText("Now Playing");
        }





        ImageButton down = (ImageButton) findViewById(R.id.downButton);
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                if(isPlaying) {
                    intent.putExtra("isPlaying", "true");
                }
                else {
                    intent.putExtra("isPlaying", "false");
                }
                startActivity(intent);
                finish();
            }
        });

        ImageButton playButton = (ImageButton) findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPlaying) {
//                    if(player == null) {
//                        String url = "http://gr-relay-lb.gaduradio.pl/119"; // your URL here
////                  String url = "http://streaming.sim-indonesia.com:8000/genfm"; // your URL here
//
//                        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
//                        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
//
//                        TrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
//
//                        TrackSelector trackSelector = new DefaultTrackSelector(trackSelectionFactory);
//
//
//                        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
//                        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(StreamingScreen.this,
//                                Util.getUserAgent(StreamingScreen.this, "mediaPlayerSample"), defaultBandwidthMeter);
//
//
////                  MediaSource mediaSource = new ExtractorMediaSource(Uri.parse("http://gr-relay-lb.gaduradio.pl/119"), dataSourceFactory, extractorsFactory, null, null);
////                  MediaSource mediaSource = new ExtractorMediaSource(Uri.parse("http://streaming.sim-indonesia.com:8000/genfm"), dataSourceFactory, extractorsFactory, null, null);
//                        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse("http://8eh.itb.ac.id:8000/streaming.live"), dataSourceFactory, extractorsFactory, null, null);
//
//
//                        player = ExoPlayerFactory.newSimpleInstance(getApplicationContext(), trackSelector);
//
//                        player.addListener(StreamingScreen.this);
//                        player.prepare(mediaSource);

                    startService(new Intent(getApplicationContext(), StreamingPlayerService.class));
                        TextView text = (TextView) findViewById(R.id.statusPlayer);
                        text.setText("Loading...");
//                        player.setPlayWhenReady(true);
//                    }

                    TextView text123 = (TextView) findViewById(R.id.statusPlayer);
                    ImageButton playButton = (ImageButton) findViewById(R.id.playButton);
                    playButton.setBackgroundResource(R.drawable.btn_pause);
                    isPlaying = true;
                    everPlaying = true;
                    text123.setText("Now Playing");
//                    startNotification();

                }
                else {
                    stopService(new Intent(getApplicationContext(), StreamingPlayerService.class));
//                    player.setPlayWhenReady(false);
//                    player.stop();
//                    player.release();
//                    player = null;

                    if(everPlaying) {
                        ImageButton playButton = (ImageButton) findViewById(R.id.playButton);
                        playButton.setBackgroundResource(R.drawable.btn_play);
                        isPlaying = false;
                        TextView text = (TextView) findViewById(R.id.statusPlayer);
                        text.setText("");
//                        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                        manager.cancelAll();

//                        Toast toast = Toast.makeText(getApplicationContext(), "stop", Toast.LENGTH_SHORT);
//                        toast.show();
                    }
                }
            }
        });


        ImageButton share = (ImageButton) findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody = "I'm streaming 8EH Radio ITB. http://8eh.itb.ac.id";
                String shareSub = "subject";
                myIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(myIntent, "Share using"));
            }
        });

        ImageButton browse = (ImageButton) findViewById(R.id.browse);
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://8eh.itb.ac.id"));
                startActivity(browserIntent);
            }
        });

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    protected void startNotification() {
        // TODO Auto-generated method stub
        notification = new NotificationCompat.Builder(this);
        notification.setContentTitle("8EH Radio ITB");
        notification.setContentText("Now playing...");
        notification.setSmallIcon(R.mipmap.ic_launcher);
        notification.setOngoing(true);
//                //Alert shown when Notification is received
//                notification.setTicker("New Message Alert!");
        //Creating new Stack Builder
        stackBuilder = TaskStackBuilder.create(StreamingScreen.this);
        stackBuilder.addParentStack(StreamingScreen.this);
        //Intent which is opened when notification is clicked
        resultIntent = new Intent(this, StreamingScreen.class);
        resultIntent.putExtra("isFromNotification", "true");
        stackBuilder.addNextIntent(resultIntent);
        pIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pIntent);
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, notification.build());
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if(isPlaying) {
            intent.putExtra("isPlaying", "true");
        }
        else {
            intent.putExtra("isPlaying", "false");
        }
        startActivity(intent);
        finish();
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        TextView text = (TextView) findViewById(R.id.statusPlayer);
        if(playWhenReady) {
            if (playbackState == ExoPlayer.STATE_READY) {
                ImageButton playButton = (ImageButton) findViewById(R.id.playButton);
                playButton.setBackgroundResource(R.drawable.btn_pause);
                isPlaying = true;
                everPlaying = true;
                text.setText("Now Playing");

                startNotification();
            }
        }
        else {

        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }
}
