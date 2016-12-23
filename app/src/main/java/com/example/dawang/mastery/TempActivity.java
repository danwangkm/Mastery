package com.example.dawang.mastery;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.beyondar.android.fragment.BeyondarFragmentSupport;
import com.beyondar.android.plugin.radar.RadarView;
import com.beyondar.android.plugin.radar.RadarWorldPlugin;
import com.beyondar.android.util.location.BeyondarLocationManager;
import com.beyondar.android.world.GeoObject;
import com.beyondar.android.world.World;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dawang on 12/22/16.
 * This is the temp activity follow the instruction of BeyondAR Wiki
 */

public class TempActivity extends FragmentActivity implements SeekBar.OnSeekBarChangeListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private BeyondarFragmentSupport mBeyondarFragment;
    private RadarView mRadarView;
    private RadarWorldPlugin mRadarPlugin;
    private SeekBar mSeekBarMaxDistance;
    private TextView mTextviewMaxDistance;
    private Button btn;

    List<Location> list = new ArrayList<>();
    Location currentLocation;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    World world;

    public static final String TAG = TempActivity.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temptest);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5 * 1000)        // 5 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds


        mBeyondarFragment = (BeyondarFragmentSupport) getSupportFragmentManager().findFragmentById(R.id.beyondarFragment);

        world = new World(this.getApplicationContext());

        mTextviewMaxDistance = (TextView) findViewById(R.id.textMaxDistance);
        mSeekBarMaxDistance = (SeekBar) findViewById(R.id.seekBarMaxDistance);
        mRadarView = (RadarView) findViewById(R.id.radarView);
        btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (currentLocation!=null) {
                    list.add(currentLocation);
                    GeoObject go = new GeoObject((long) list.size());
                    go.setGeoPosition(currentLocation.getLatitude(), currentLocation.getLongitude());
                    go.setImageResource(R.drawable.flag);
                    go.setName("Creature " + list.size());
                    world.addBeyondarObject(go);
                }
            }
        });

        // Create the Radar plugin
        mRadarPlugin = new RadarWorldPlugin(this);
        // set the radar view in to our radar plugin
        mRadarPlugin.setRadarView(mRadarView);
        // Set how far (in meters) we want to display in the view
        mRadarPlugin.setMaxDistance(100);

        // We can customize the color of the items
        mRadarPlugin.setListColor(1, Color.RED);
        // and also the size
        mRadarPlugin.setListDotRadius(1, 3);

        // The user can set the default bitmap. This is useful if you are
        // loading images form Internet and the connection get lost
        world.setDefaultImage(R.drawable.beyondar_default_unknow_icon);

        if (currentLocation!=null)
            world.setGeoPosition(currentLocation.getLatitude(), currentLocation.getLongitude());
        GeoObject go1 = new GeoObject(90000l);
        go1.setGeoPosition(40.696265d,-73.983557d);
        go1.setImageResource(R.drawable.flag);
        go1.setName("Creature ");
        world.addBeyondarObject(go1);
        for (int i=0; i<list.size(); i++) {
            GeoObject go = new GeoObject((long) i);
            go.setGeoPosition(list.get(i).getLatitude(), list.get(i).getLongitude());
            go.setImageResource(R.drawable.flag);
            go.setName("Creature "+i);
            world.addBeyondarObject(go);
        }


        // User position (you can change it using the GPS listeners form Android API)
        /*world.setGeoPosition(40.729655d, -73.997164d);

        // Create an object with an image in the app resources.
        GeoObject go1 = new GeoObject(1l);
        go1.setGeoPosition(40.727558d, -73.998252d);
        go1.setImageResource(R.drawable.flag);
        go1.setName("Creature 1");

        // Is it also possible to load the image asynchronously form internet
        GeoObject go2 = new GeoObject(2l);
        go2.setGeoPosition(40.737148d, -73.995580d);
        go2.setImageUri("http://beyondar.com/sites/default/files/logo_reduced.png");
        go2.setName("Online image");

        // Also possible to get images from the SDcard
        GeoObject go3 = new GeoObject(3l);
        go3.setGeoPosition(41.26550959641445d, 1.925873388087619d);
        go3.setImageResource(R.drawable.flag);
        //go3.setImageUri("/sdcardUnexpected value from nativeGetEnabledTags/someImageInYourSDcard.jpeg");
        go3.setName("IronMan from sdcard");

        // And the same goes for the app assets
        GeoObject go4 = new GeoObject(4l);
        go4.setGeoPosition(41.26518862002349d, 1.925662767707665d);
        go4.setImageUri("assets://creature_7.png");
        go4.setName("Image from assets");

        // We add this GeoObjects to the world
        world.addBeyondarObject(go1);
        world.addBeyondarObject(go2);
        world.addBeyondarObject(go3);
        //world.addBeyondarObject(go4);*/

        // Finally we add the Wold data in to the fragment
        mBeyondarFragment.setWorld(world);
        mBeyondarFragment.setMaxDistanceToRender(1000f);

        //BeyondarLocationManager.addWorldLocationUpdate(world);

        // add the plugin
        world.addPlugin(mRadarPlugin);

        // We also can see the Frames per seconds
        mBeyondarFragment.showFPS(true);

        mSeekBarMaxDistance.setOnSeekBarChangeListener(this);
        mSeekBarMaxDistance.setMax(1000);
        mSeekBarMaxDistance.setProgress(23);

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (mRadarPlugin == null)
            return;
        if (seekBar == mSeekBarMaxDistance) {
            // float value = ((float) progress/(float) 10000);
            mTextviewMaxDistance.setText("Max distance Value: " + progress);
            mRadarPlugin.setMaxDistance(progress);
            //LowPassFilter.ALPHA = value;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "Location services connected.");
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        currentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        if (currentLocation == null) {
            //LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        else {
            handleNewLocation(currentLocation);
        }
    }

    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());
        //World newworld = new World(this.getApplicationContext());
        //world.setGeoPosition(location.getLatitude(), location.getLongitude());
        mBeyondarFragment.getWorld().setGeoPosition(location.getLatitude(), location.getLongitude());
        //BeyondarLocationManager.addWorldLocationUpdate(world);
        mTextviewMaxDistance.setText(location.getLatitude()+"\n"+location.getLongitude()+"\n"+mBeyondarFragment.getWorld().getLatitude()+"\n"+mBeyondarFragment.getWorld().getLongitude());
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
//        /*
//         * Google Play services can resolve some errors it detects.
//         * If the error has a resolution, try sending an Intent to
//         * start a Google Play services activity that can resolve
//         * error.
//         */
//        if (connectionResult.hasResolution()) {
//            try {
//                // Start an Activity that tries to resolve the error
//                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
//                /*
//                 * Thrown if Google Play services canceled the original
//                 * PendingIntent
//                 */
//            } catch (IntentSender.SendIntentException e) {
//                // Log the error
//                e.printStackTrace();
//            }
//        } else {
//            /*
//             * If no resolution is available, display a dialog to the
//             * user with the error.
//             */
//            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }
}
