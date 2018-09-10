package ebad.innovate.project.cameraapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.io.IOException;
import java.util.List;

import ebad.innovate.project.cameraapp.BroadCastReciever.NotificationActionReceiver;
import io.fabric.sdk.android.Fabric;


public class RecorderService extends Service {

    private static final String TAG = "RecorderService";
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private static Camera mServiceCamera;
    private boolean mRecordingStatus;
    private MediaRecorder mMediaRecorder;

    private final String CHANNEL_ID = "my_notification_channel";
    private final int NOTIFICATION_ID = 101;

    Context context;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.e("onStartCommand", "Service Started");
        this.context = this;

        Fabric.with(this, new Crashlytics());

        mRecordingStatus = false;
        mServiceCamera = Camera.open(0);
        mSurfaceView = CameraRecorder.mSurfaceView;
        /*   mSurfaceHolder = CameraRecorder.mSurfaceHolder;
         */

        mSurfaceHolder = mSurfaceView.getHolder();

        if (mRecordingStatus == false)
            notificationCreator();
        startRecording();

        return START_STICKY;
    }


    @Override
    public void onDestroy() {

        Log.e("onDestroy", "Service Destroyed");

        stopRecording();
        mRecordingStatus = false;

        super.onDestroy();
    }

    public boolean startRecording() {

        try {

            Toast.makeText(getBaseContext(), "Recording Started", Toast.LENGTH_SHORT).show();

            Log.e("startRecording()", "Service Recording Started");

            //mServiceCamera = Camera.open();
            Camera.Parameters params = mServiceCamera.getParameters();
            mServiceCamera.setParameters(params);
            Camera.Parameters p = mServiceCamera.getParameters();

            final List<Size> listSize = p.getSupportedPreviewSizes();

            for (Size size : listSize) {
                Log.i(TAG, String.format("Supported Preview Size (%d, %d)", size.width, size.height));
            }

            Size mPreviewSize = listSize.get(2);
            Log.v(TAG, "use: width = " + mPreviewSize.width
                    + " height = " + mPreviewSize.height);
            p.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
            p.setPreviewFormat(PixelFormat.YCbCr_420_SP);
            mServiceCamera.setParameters(p);

            try {
                mServiceCamera.setPreviewDisplay(mSurfaceHolder);
                mServiceCamera.startPreview();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }

            mServiceCamera.unlock();

            mMediaRecorder = new MediaRecorder();
            mMediaRecorder.setCamera(mServiceCamera);
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
            mMediaRecorder.setOutputFile("/sdcard/video" + System.currentTimeMillis() + ".mp4");
            mMediaRecorder.setVideoFrameRate(30);
            mMediaRecorder.setVideoSize(mPreviewSize.width, mPreviewSize.height);
            mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());

            mMediaRecorder.prepare();
            mMediaRecorder.start();

            mRecordingStatus = true;

            return true;
        } catch (IllegalStateException e) {
            Log.d(TAG, e.getMessage());
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public void stopRecording() {

        Toast.makeText(getBaseContext(), "Recording Stopped", Toast.LENGTH_SHORT).show();

        Log.e("stopRecording()", "Service Recording Stopped");

        try {
            mServiceCamera.reconnect();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mMediaRecorder.stop();
        mMediaRecorder.reset();

        mServiceCamera.stopPreview();
        mMediaRecorder.release();

        mServiceCamera.release();
        mServiceCamera = null;
    }

   /* void notificationCreator() {

       /* Intent landingIntent = new Intent(this, CameraRecorder.class);
        landingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent landingPendingIntent = PendingIntent.getActivity(this, 0, landingIntent, PendingIntent.FLAG_ONE_SHOT);


        Intent stopIntent = new Intent(this, NotificationActionReceiver.class);
        stopIntent.setAction("Stop");
        stopIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent stopIntentPending = PendingIntent.getBroadcast(this, 0, stopIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Action actions = new NotificationCompat.Action.Builder( R.drawable.ic_launcher,"Stop",stopIntentPending)
                                        .build();


        Notification notification =   new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Video Recording...")
                .setContentText("Press stop button to stop the video recording process")
                .setAutoCancel(false)
                .setContentIntent(landingPendingIntent)
                .addAction(actions)
                .build();

       /* builder.addAction(R.mipmap.ic_launcher, "Stop", stopIntentPending);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
*/


    // startForeground(NOTIFICATION_ID,notification);*/

//    }

    void notificationCreator() {

        createNotificationChannel();

        Intent landingIntent = new Intent(this, CameraRecorder.class);
        landingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent landingPendingIntent = PendingIntent.getActivity(this, 0, landingIntent, PendingIntent.FLAG_ONE_SHOT);


        Intent stopIntent = new Intent(this, NotificationActionReceiver.class);
        stopIntent.setAction("Stop");
        stopIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent stopIntentPending = PendingIntent.getBroadcast(this, 0, stopIntent, PendingIntent.FLAG_CANCEL_CURRENT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("Video Recording...");
        builder.setContentText("Press stop button to stop the video recording process");
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setAutoCancel(false);
        builder.setContentIntent(landingPendingIntent);

        // builder.addAction(R.mipmap.ic_launcher, "Stop", stopIntentPending);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());

        //  startForeground(NOTIFICATION_ID,no);

    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence name = "Personal Notifications";
            String description = "Include all the personal notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationChannel.setDescription(description);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }

    }


}