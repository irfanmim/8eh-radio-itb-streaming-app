package id.ac.itb.a8eh.radiostreaming.service;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;

import id.ac.itb.a8eh.radiostreaming.R;
import id.ac.itb.a8eh.radiostreaming.StreamingScreen;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class StreamingPlayerService extends Service implements ExoPlayer.EventListener {

    private MediaPlayer mediaPlayer;
    private SimpleExoPlayer player;

    NotificationCompat.Builder notification;
    PendingIntent pIntent;
    NotificationManager manager;
    Intent resultIntent;
    TaskStackBuilder stackBuilder;

//    public StreamingPlayerService() {
//    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if(player == null) {
            String url = "http://gr-relay-lb.gaduradio.pl/119"; // your URL here
//                  String url = "http://streaming.sim-indonesia.com:8000/genfm"; // your URL here

            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

            TrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);

            TrackSelector trackSelector = new DefaultTrackSelector(trackSelectionFactory);


            DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(StreamingPlayerService.this,
                    Util.getUserAgent(StreamingPlayerService.this, "mediaPlayerSample"), defaultBandwidthMeter);


//                  MediaSource mediaSource = new ExtractorMediaSource(Uri.parse("http://gr-relay-lb.gaduradio.pl/119"), dataSourceFactory, extractorsFactory, null, null);
//                  MediaSource mediaSource = new ExtractorMediaSource(Uri.parse("http://streaming.sim-indonesia.com:8000/genfm"), dataSourceFactory, extractorsFactory, null, null);
            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse("http://8eh.itb.ac.id:8000/streaming.live"), dataSourceFactory, extractorsFactory, null, null);


            player = ExoPlayerFactory.newSimpleInstance(getApplicationContext(), trackSelector);

            player.addListener(StreamingPlayerService.this);
            player.prepare(mediaSource);

            player.setPlayWhenReady(true);
            startNotification();
        }
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
        stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(StreamingScreen.class);
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
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.setPlayWhenReady(false);
        player.stop();
        player.release();
        player = null;

        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancelAll();
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

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
