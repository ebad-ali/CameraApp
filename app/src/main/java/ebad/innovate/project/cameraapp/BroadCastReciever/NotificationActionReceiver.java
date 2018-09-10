package ebad.innovate.project.cameraapp.BroadCastReciever;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import ebad.innovate.project.cameraapp.Services.TestService;

public class NotificationActionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("Service is in Broadcast", " value: " + intent.getAction());

        if (intent.getAction().equalsIgnoreCase("Stop")) {

            if (isMyServiceRunning(context, TestService.class)) {
                context.stopService(new Intent(context, TestService.class));
                Toast.makeText(context, "Service is stopped", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "No Service is running", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
