package com.example.auenc.car_controller;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

public class Lobby extends AppCompatActivity {

    private String mPlayerName;
    private String mPlayerColor;
    private Connection mConnection;
    private boolean mReady;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mConnection = Connection.getConnection();
        mConnection.setInLobby(this);
        Switch readySwitch = (Switch) findViewById(R.id.btnReady);
        readySwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mReady){
                    mReady = true;
                    mConnection.ready();
                    Snackbar.make(view, "You are now ready", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.rev);
                    mp.start();
                }else{
                    mReady = false;
                    mConnection.unready();
                    Snackbar.make(view, "You are now not ready", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.stop);
                    mp.start();
                }

            }
        });

        Bundle extra = getIntent().getExtras();
        mPlayerName = extra.getString("player-name");
        mPlayerColor = extra.getString("player-color");
        mReady = false;

        TextView playerName = (TextView) findViewById(R.id.lobbyPlayerName);
        playerName.setText(mPlayerName);
        System.out.println("APP: player name is " + mPlayerName);
        TextView playerColor = (TextView) findViewById(R.id.lobbyPlayerColor);
        playerColor.setText(mPlayerColor);
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.vroom);
        mp.start();
    }


    public void startGame(){
        Intent intent = new Intent(Lobby.this, Controller.class);
        intent.putExtra("color", mPlayerColor);
        startActivity(intent);
    }
}
