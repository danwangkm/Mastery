package com.example.dawang.mastery;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.SeekBar;
import android.widget.TextView;

import com.beyondar.android.fragment.BeyondarFragmentSupport;
import com.beyondar.android.opengl.util.LowPassFilter;
import com.beyondar.android.plugin.radar.RadarView;
import com.beyondar.android.plugin.radar.RadarWorldPlugin;
import com.beyondar.android.world.GeoObject;
import com.beyondar.android.world.World;

/**
 * Created by dawang on 12/22/16.
 * This is the temp activity follow the instruction of BeyondAR Wiki
 */

public class TempActivity extends FragmentActivity implements SeekBar.OnSeekBarChangeListener {
    private BeyondarFragmentSupport mBeyondarFragment;
    private RadarView mRadarView;
    private RadarWorldPlugin mRadarPlugin;
    private SeekBar mSeekBarMaxDistance;
    private TextView mTextviewMaxDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temptest);

        mBeyondarFragment = (BeyondarFragmentSupport) getSupportFragmentManager().findFragmentById(R.id.beyondarFragment);

        World world = new World(this.getApplicationContext());

        mTextviewMaxDistance = (TextView) findViewById(R.id.textMaxDistance);
        mSeekBarMaxDistance = (SeekBar) findViewById(R.id.seekBarMaxDistance);
        mRadarView = (RadarView) findViewById(R.id.radarView);

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

        // User position (you can change it using the GPS listeners form Android API)
        world.setGeoPosition(40.729655d, -73.997164d);

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
        /*GeoObject go4 = new GeoObject(4l);
        go4.setGeoPosition(41.26518862002349d, 1.925662767707665d);
        go4.setImageUri("assets://creature_7.png");
        go4.setName("Image from assets");*/

        // We add this GeoObjects to the world
        world.addBeyondarObject(go1);
        world.addBeyondarObject(go2);
        world.addBeyondarObject(go3);
        //world.addBeyondarObject(go4);

        // Finally we add the Wold data in to the fragment
        mBeyondarFragment.setWorld(world);
        mBeyondarFragment.setMaxDistanceToRender(1000f);
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
}
