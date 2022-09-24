package com.example.assignment2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public static final int PERMISSION_REQUEST_CODE = 1;

    private static final int MY_REQUEST_OPEN_CAMERA = 101;

    VideoView mVideoView ;
    ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVideoView = (VideoView) findViewById(R.id.videoView);
        mImageView = (ImageView) findViewById(R.id.photoView);

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_REQUEST_OPEN_CAMERA) {
            if (resultCode == RESULT_OK)
                Toast.makeText(this, "Successful", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean getPermission() {
        String[] permissions = new String[] {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        };

        for (String permission: permissions) {
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
        File f = new File(getExternalFilesDir(null).toString(), timeStamp + type);

        Uri fileUri;
        if (Build.VERSION.SDK_INT >= 24) {
            fileUri = FileProvider.getUriForFile(this.getApplicationContext(),
                    "com.example.assignment2.fileProvider", f);
        }
        else {
            fileUri = Uri.fromFile(f);
        }

        Log.i("info", fileUri.toString());
        return fileUri;
    }

}