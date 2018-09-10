package ebad.innovate.project.cameraapp;

import android.app.Service;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ebad.innovate.project.cameraapp.Services.TestService;

public class TestActivity extends AppCompatActivity {

    Button startServiceButton, stopServiceButton;

    TestService testService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        startServiceButton = findViewById(R.id.startServiceButton);
        startServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startService(new Intent(TestActivity.this, TestService.class));
                //Toast.makeText(TestActivity.this, "Service is Started!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        stopServiceButton = findViewById(R.id.stopServiceButton);

        stopServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stopService(new Intent(TestActivity.this, TestService.class));

                //Toast.makeText(TestActivity.this, "Service is Started!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
