package com.example.assignment2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public static final int PERMISSION_REQUEST_CODE = 1;

    private static final int MY_REQUEST_OPEN_CAMERA = 101;

    VideoView mVideoView;
    ImageView mImageView;
    TextView mTextView;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // The geographical location where the device is currently located. That is, the last-known location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_LOCATION = "location";

    String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Retrieve location from saved instance state.
        if (savedInstanceState != null)
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        mVideoView = (VideoView) findViewById(R.id.videoView);
        mImageView = (ImageView) findViewById(R.id.photoView);
        mTextView = (TextView) findViewById(R.id.location);

        mVideoView.setVisibility(View.GONE);
        mImageView.setVisibility(View.GONE);
    }

    public void onTakePhotoClick(View v) {
        if (!getPermission())
            return;

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getFileUri(".jpg"));
        startActivityForResult(intent, MY_REQUEST_OPEN_CAMERA);
    }

    public void onRecordVideoClick(View v) {
        if (!getPermission())
            return;

        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getFileUri(".mp4"));
        startActivityForResult(intent, MY_REQUEST_OPEN_CAMERA);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_REQUEST_OPEN_CAMERA) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Successful", Toast.LENGTH_SHORT).show();
                getDeviceLocation();
            } else
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean getPermission() {
        String[] permissions = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), permission)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
                return false;
            }
        }

        Log.i("info", "all permission granted");
        return true;
    }

    private Uri getFileUri(String type) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        fileName = timeStamp + type;
        File f = new File(getExternalFilesDir(null).toString(), fileName);

        Uri fileUri;
        if (Build.VERSION.SDK_INT >= 24) {
            fileUri = FileProvider.getUriForFile(this.getApplicationContext(),
                    "com.example.assignment2.fileProvider", f);
        } else {
            fileUri = Uri.fromFile(f);
        }

        Log.i("info", fileUri.toString());
        return fileUri;
    }

    private void getDeviceLocation() {
        if (!getPermission())
            return;

        @SuppressLint("MissingPermission")
        Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
        locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    String currentOrDefault = "Current";

                    // Obtain the current location of the device
                    mLastKnownLocation = task.getResult();
                    if (mLastKnownLocation == null) {
                        currentOrDefault = "Default";
                        // Set current location to the default location
                        mLastKnownLocation = new Location("");
                    }
                    // Show location details on the location TextView
                    String msg = currentOrDefault + " Location: " +
                            Double.toString(mLastKnownLocation.getLatitude()) + ", " +
                            Double.toString(mLastKnownLocation.getLongitude());

                    mTextView.setText(msg);
                    Log.i("info", msg);
                }
            }
        });
    }

}