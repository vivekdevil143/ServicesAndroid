package com.iws.servicesandroid;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    int count = 0;
    private Button btnstartservice, btnstopservice, btnbindservice, btnunboundservice, btngetrandomnumber;
    private TextView txtviewthreadcount;
    private AsyncTask myAsyncTask;


    private MyService myService;
    private boolean isServiceBound;
    private ServiceConnection serviceConnection;

    private Intent ServiceIntent;

    private boolean mStopLoop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Log.i(getString(R.string.service_demo_tag), "MainActivity thread id:" + Thread.currentThread().getId());

        btnstartservice = (Button) findViewById(R.id.startservice);
        btnstopservice = (Button) findViewById(R.id.stopservice);
        btnbindservice = (Button) findViewById(R.id.bindService);
        btnunboundservice = (Button) findViewById(R.id.UnboundService);
        btngetrandomnumber = (Button) findViewById(R.id.getrandomnumber);

        txtviewthreadcount = (TextView) findViewById(R.id.txtviewthreadcount);

        btnstartservice.setOnClickListener(this);
        btnstopservice.setOnClickListener(this);
        btnbindservice.setOnClickListener(this);
        btnunboundservice.setOnClickListener(this);
        btngetrandomnumber.setOnClickListener(this);

        ServiceIntent = new Intent(getApplicationContext(), MyService.class);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startservice:
                mStopLoop = true;


                startService(ServiceIntent);
                break;
            case R.id.stopservice:

                stopService(ServiceIntent);

                break;

            case R.id.bindService:
                bindService();
                break;


            case R.id.UnboundService:
                unbindService();
                break;

            case R.id.getrandomnumber:
                setRandomNumber();
                break;


        }
    }


    private void bindService() {

        if (serviceConnection == null) {
            serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder iBinder) {
                    MyService.MyServiceBinder myServiceBinder = (MyService.MyServiceBinder) iBinder;
                    myService = myServiceBinder.getService();
                    isServiceBound = true;
                }

                @Override
                public void onServiceDisconnected(ComponentName componentName) {

                    isServiceBound = false;
                }
            };
        }


        bindService(ServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE);

    }


    private void unbindService() {
        if (isServiceBound) {
            unbindService(serviceConnection);
            isServiceBound = false;
        }

    }

    private void setRandomNumber() {

        if (isServiceBound) {
            txtviewthreadcount.setText("Random number:" + myService.getRandomNumber());
        } else {

            txtviewthreadcount.setText("Service is unbound");
        }

    }
}
