package com.example.auenc.car_controller;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class Controller extends AppCompatActivity implements ControllerValues.ControllerValueHandler{

    private ControllerValues mControllerValues;

    private Connection mConnection;

    private boolean mBreaking;

    private boolean mThrottling;


    private String mColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        mColor = extras.getString("color");

        mConnection = Connection.getConnection();
        mControllerValues = new ControllerValues(this, this);

        Button breakButton = (Button) findViewById(R.id.btnBreak);
        breakButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    mBreaking = true;
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    mBreaking = false;
                }


                return false;
            }
        });
        Button breakThrottle = (Button) findViewById(R.id.btnThrottle);
        breakThrottle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    mThrottling = true;
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    mThrottling = false;
                }
                return false;
            }
        });
    }

    @Override
    public void acceptPitch(Double pitch) {
        //System.out.println("Breaking: " + mBreaking + " Throt: " + mThrottling);
        mConnection.updateState(pitch, mBreaking, mThrottling);
    }
}
