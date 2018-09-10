package ebad.innovate.project.cameraapp.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import ebad.innovate.project.cameraapp.BroadCastReciever.NotificationActionReceiver;
import ebad.innovate.project.cameraapp.R;
import ebad.innovate.project.cameraapp.TestActivity;

public class TestService extends Service {

    Context context;
    public int counter = 0;

    private final String CHANNEL_ID = "my_notification_channel";
    private final int NOTIFICATION_ID = 101;


    public TestService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        this.context = this;

        notificationCreator();
        startTimer();
        Log.e("onStartCommand", "Service Started");

        return START_STICKY;
    }

    @Override
    public void onDestroy() {

        Log.e("onDestroy", "Service Destroyed");
        stoptimertask();
        super.onDestroy();
    }


    void notificationCreator() {

//        createNotificationChannel();

    /*    Intent landingIntent = new Intent(this, TestActivity.class);
        landingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent landingPendingIntent = PendingIntent.getActivity(this, 0, landingIntent, PendingIntent.FLAG_ONE_SHOT);


        Intent stopIntent = new Intent(this, NotificationActionReceiver.class);
        stopIntent.setAction("Stop");
        stopIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent stopIntentPending = PendingIntent.getBroadcast(this, 0, stopIntent, PendingIntent.FLAG_CANCEL_CURRENT);


        Notification notification =   new Notification.Builder(this, CHANNEL_ID)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle("Video Recording...")
        .setContentText("Press stop button to stop the video recording process")
        .setAutoCancel(false)
        .setContentIntent(landingPendingIntent)
                .build();

       /* builder.addAction(R.mipmap.ic_launcher, "Stop", stopIntentPending);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
*/
      //  startForeground(NOTIFICATION_ID,notification);*/
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


    private Timer timer;
    private TimerTask timerTask;
    long oldTime = 0;

    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, to wake up every 1 second
        timer.schedule(timerTask, 1000, 1000); //
    }

    /**
     * it sets the timer to print the counter every x seconds
     */
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                Log.e("in timer", "in timer ++++  " + (counter++));
            }
        };
    }

    /**
     * not needed
     */
    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
