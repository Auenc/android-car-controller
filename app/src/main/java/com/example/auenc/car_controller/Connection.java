package com.example.auenc.car_controller;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by auenc on 23/04/17.
 */

public class Connection {

    private static Connection mInstance;
    private io.socket.client.Socket mSocket;

    private MainActivity mMain;

    private Connection(){

    }

    public void connect(MainActivity main){
        mMain = main;
        try {
             mSocket = IO.socket("http://192.168.0.43:3082");
            mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener(){
                @Override
                public void call(Object... args) {
                    System.out.println("APP: connected");

                }
            }).on("login", new Emitter.Listener(){
                @Override
                public void call(Object... args) {
                    System.out.println("Got login");
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("name", mMain.getPlayerName());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    mSocket.emit("login", obj);

                    System.out.println("Join sent");
                }
            }).on("set-color", new Emitter.Listener(){
                @Override
                public void call(Object... args) {
                    if(args.length < 1){
                        System.out.println("App: set-color hasn't received any arguments");
                        return;
                    }
                    String color = (String) args[0];
                    mMain.setPlayerColor(color);
                    mMain.enterLobby();
                }
            });
            mSocket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void ready(){
        mSocket.emit("ready");
    }

    public void unready(){
        mSocket.emit("unready");
    }

    private void login(){

    }

    public static Connection getConnection(){
        if(mInstance == null){
          mInstance =   new Connection();
        }
        return mInstance;
    }
}
