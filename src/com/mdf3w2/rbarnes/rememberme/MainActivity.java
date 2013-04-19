/*
 * project	RememberMe
 * 
 * package	com.mdf3w2.rbarnes.rememberme
 * 
 * @author	Ronaldo Barnes
 * 
 * date		Apr 16, 2013
 */
package com.mdf3w2.rbarnes.rememberme;

import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements LocationListener{
	
	private LocationManager locationManager;
	private String provider;
	private static final int IMAGE_CAPTURE = 0;
    private Uri imageUri;
    private ImageView imageView;
    private String _lat;
    private String _long;
    private String _userInput;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView)findViewById(R.id.imgPreview);
        Button addButton =(Button)findViewById(R.id.newButton);
        final EditText et = (EditText)findViewById(R.id.user_input);
        //final Intent addIntent = new Intent(this, ReminderAddActivity.class);
        
        
        addButton.setOnClickListener(new OnClickListener() {

		    public void onClick(View v) {
		    	startCamera();
		    	_userInput = et.getText().toString();
		    	
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
                
                
                imageView.setImageURI(imageUri);
                
                
                showNotication();
        }}
        
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
      
      _lat = String.valueOf(lat);
      _long = String.valueOf(lng);
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
    
    private void showNotication(){
    	String uriBegin = "geo:" + _lat + "," + _long;
        String query = _lat + "," + _long + "("+_userInput+")";
        String encodedQuery = Uri.encode(query);
        String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
        Uri uri = Uri.parse(uriString);
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // Build notification
        NotificationCompat.Builder mBuilder =   new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.ic_launcher) // notification icon
        .setContentTitle("Remember Me?")
        .setContentText(_userInput)
        .setContentIntent(pIntent)
        .setAutoCancel(true); // clear notification after click
        
        
        PendingIntent pi = PendingIntent.getActivity(this,0,intent,Intent.FLAG_ACTIVITY_NEW_TASK);
        mBuilder.setContentIntent(pi);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }
}
