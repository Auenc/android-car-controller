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

    private int mID;

    private Lobby mLobby;

    boolean inLobby;

    private Connection(){

    }

    public void connect(MainActivity main){
        mMain = main;
        try {
             mSocket = IO.socket("http://"+mMain.getServerAddress()+":3082");
            mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener(){
                @Override
                public void call(Object... args) {
                    System.out.println("APP: connected");

                }
            }).on("login", new Emitter.Listener(){
                @Override
                public void call(Object... args) {
                    System.out.println("Got login " +  args.length);
                    if(args.length < 1)return;
                    mID = (int) args[0];
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("name", mMain.getPlayerName());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    mSocket.emit("login", mID, obj);

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
            }).on("start-game", new Emitter.Listener(){
                @Override
                public void call(Object... args) {
                    System.out.println("APP: start-game called");
                    if(inLobby){
                        mLobby.startGame();
                    }
                }
            });
            mSocket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void setInLobby(Lobby lob){
        this.inLobby = true;
        mLobby = lob;
    }

    public void ready(){
        mSocket.emit("ready", mID);
    }

    public void unready(){
        mSocket.emit("unready", mID);
    }

    public void updateState(double x, boolean breaking, boolean throttling){
        JSONObject obj = new JSONObject();
        try {
            obj.put("x", x);
            obj.put("breaking", breaking);
            obj.put("throttling", throttling);
            mSocket.emit("update-state", mID, obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection(){
        if(mInstance == null){
          mInstance =   new Connection();
        }
        return mInstance;
    }
}
