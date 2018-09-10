package ebad.innovate.project.cameraapp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class CameraRecorder extends Activity implements SurfaceHolder.Callback {


    public static final int RequestPermissionCode = 7;


    private static final String TAG = "Recorder";
    public static SurfaceView mSurfaceView;
    public static SurfaceHolder mSurfaceHolder;
    public static Camera mCamera;
    public static boolean mPreviewRunning;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fabric.with(this, new Crashlytics());

        if (CheckingPermissionIsEnabledOrNot()) {
            initializeMainActivity();
        }
        else {
            RequestMultiplePermission();
        }
    }

    private void initializeMainActivity() {


        setContentView(R.layout.activity_main);

        mSurfaceView = findViewById(R.id.surfaceView1);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        Button btnStart = findViewById(R.id.StartService);
        btnStart.setOnClickListener(new View.OnClickListener()

        {
            public void onClick(View v) {

                Intent intent = new Intent(CameraRecorder.this, RecorderService.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startService(intent);

                finish();
            }
        });

        Button btnStop = findViewById(R.id.StopService);
        btnStop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stopService(new Intent(CameraRecorder.this, RecorderService.class));
            }
        });


    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }


    public boolean CheckingPermissionIsEnabledOrNot() {

        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);


        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {

                    boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean RecordAudioPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean ExternalStoragePermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                    if (CameraPermission && RecordAudioPermission && ExternalStoragePermission) {

                        Toast.makeText(CameraRecorder.this, "Permission Granted", Toast.LENGTH_LONG).show();
                        // TODO show activity here..
                        initializeMainActivity();
                    } else {
                        Toast.makeText(CameraRecorder.this, "Permission Denied", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }

                break;
        }
    }

    private void RequestMultiplePermission() {

        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(CameraRecorder.this, new String[]
                {
                        CAMERA,
                        RECORD_AUDIO,
                        WRITE_EXTERNAL_STORAGE,
                }, RequestPermissionCode);

    }

}
