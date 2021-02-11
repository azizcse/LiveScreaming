package com.test.techfort.livestreamingdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements Session.SessionListener, PublisherKit.PublisherListener{
    //Secret 0018adaa93713a11ec0261b3f583a39c5461e97a
    private static String API_KEY = "47121514";
    private static String SESSION_ID = "2_MX40NzEyMTUxNH5-MTYxMzA2NjY1OTE2NX5VZHh5ZCsrc3JnVDRlTk9WMnVxQXVacDR-fg";
    private static String TOKEN = "T1==cGFydG5lcl9pZD00NzEyMTUxNCZzaWc9MWZhYjdlMzg5NGQ1MjZmOWU0MmVhMjMwZGMyOGE3ZGI4ZGJmMTAwYTpzZXNzaW9uX2lkPTJfTVg0ME56RXlNVFV4Tkg1LU1UWXhNekEyTmpZMU9URTJOWDVWWkhoNVpDc3JjM0puVkRSbFRrOVdNblZ4UVhWYWNEUi1mZyZjcmVhdGVfdGltZT0xNjEzMDY2NzIzJm5vbmNlPTAuMDMwMjY3MjUyMDQxNzY4MTY3JnJvbGU9cHVibGlzaGVyJmV4cGlyZV90aW1lPTE2MTMwODgzMjMmaW5pdGlhbF9sYXlvdXRfY2xhc3NfbGlzdD0=";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int RC_SETTINGS_SCREEN_PERM = 123;
    private static final int RC_VIDEO_APP_PERM = 124;

    private Session mSession;
    private FrameLayout publisherContainer;
    private FrameLayout subscriberContainer;
    private Publisher mPublisher;
    private Subscriber mSubscriber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
        publisherContainer = findViewById(R.id.publisher_container);
        subscriberContainer = findViewById(R.id.subscriber_container);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    //Subscriber
    @AfterPermissionGranted(123)
    private void requestPermission() {
        String[] peram = {Manifest.permission.CAMERA, Manifest.permission.INTERNET, Manifest.permission.RECORD_AUDIO};
        if (EasyPermissions.hasPermissions(this, peram)) {
            mSession = new Session.Builder(this, API_KEY, SESSION_ID).build();
            mSession.setSessionListener(this);
            mSession.connect(TOKEN);
        } else {
            EasyPermissions.requestPermissions(this, "This app need to access your camera", 123, peram);
        }
    }

    @Override
    public void onConnected(Session session) {
        mPublisher = new Publisher.Builder(this).build();
        mPublisher.setPublisherListener(this);

        publisherContainer.addView(mPublisher.getView());
        mSession.publish(mPublisher);
    }

    @Override
    public void onDisconnected(Session session) {

    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        if (mSubscriber == null) {
            mSubscriber = new Subscriber.Builder(this, stream).build();
            mSession.subscribe(mSubscriber);
            subscriberContainer.addView(mSubscriber.getView());
        }
    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
        if(mSubscriber != null){
            mSubscriber = null;
            subscriberContainer.removeAllViews();
        }
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {

    }


    //Puvlisher listener

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {

    }
}