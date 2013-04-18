package com.mdf3w2.rbarnes.rememberme;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class ReminderDetailActivity extends Activity implements LocationListener{
	
	private LocationManager locationManager;
	private String provider;
	private static final int IMAGE_CAPTURE = 0;
    private Button startBtn;
    private Uri imageUri;
    private ImageView imageView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView)findViewById(R.id.imgPreview);
        startBtn = (Button) findViewById(R.id.newButton);
        startBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startCamera();
            }
        });
        
        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        if (location != null) {
          Log.i("PROVIDER","Provider " + provider + " has been selected.");
          onLocationChanged(location);
        } else {
          
        }
    }

    
    //LAUNCH CAMERA
    public void startCamera() {
        Log.i("CAMERA", "Camera Launched");
        String fileName = "pic.jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, IMAGE_CAPTURE);
    }

    //DISPLAY PREVIEW
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK){
                Log.i("CAMERA","Picture taken!!!");
                imageView.setImageURI(imageUri);
            }
        }
    }
    
    //Resume Location updates
    @Override
    protected void onResume() {
      super.onResume();
      locationManager.requestLocationUpdates(provider, 0, 0, this);
    }

    //Pause Location updates
    @Override
    protected void onPause() {
      super.onPause();
      locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
      double lat = (double) (location.getLatitude());
      double lng = (double) (location.getLongitude());
      
      Log.i("LATITUDE",String.valueOf(lat));
      Log.i("LATITUDE",String.valueOf(lng));
      ;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
      // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
      Toast.makeText(this, "Enabled new provider " + provider,
          Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
      Toast.makeText(this, "Disabled provider " + provider,
          Toast.LENGTH_SHORT).show();
    }
}