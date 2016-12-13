package com.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.Joystick.JoystickActivity;
import com.example.Wifi.WiFiDemo;

/**
 * Created by Jérémy on 09/12/2016.
 */
public class HomeActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void getWifiActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), WiFiDemo.class);
        startActivity(intent);
    }
    public void getJoystickActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), JoystickActivity.class);
        startActivity(intent);
    }
}
