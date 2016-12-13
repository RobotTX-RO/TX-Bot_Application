package com.example.Joystick;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.R;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Jérémy on 09/12/2016.
 */
public class JoystickActivity extends Activity {
    private Socket socket;
    private DataOutputStream dataOutputStream = null;
    private OutputStream outputStream;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joystick_activity);
        JoystickView joystick = (JoystickView) findViewById(R.id.robot_joystick_view);
        joystick.setOnJoystickMoveListener(new JoystickView.OnJoystickMoveListener() {

            @Override
            public void onValueChanged(int angle, int power, int direction) {
                if (angle > 90) {
                    angle = 180 - angle;
                    power *= -1;
                } else if (angle < -90) {
                    angle = -180 - angle;
                    power *= -1;
                }
                String directionStr = String.valueOf((byte) direction);
                String powerStr = String.valueOf((byte) power);
                String angleStr = String.valueOf((byte) angle);
                MyClientTask myClientTask = new MyClientTask(
                        "192.168.4.1", 998, directionStr,powerStr,angleStr );
                myClientTask.execute();
            }


        });
    }
    public class MyClientTask extends AsyncTask<Void, Void, Void> {

        String dstAddress;
        int dstPort;
        String direction;
        String power;
        String angle;
        String response="";

        MyClientTask(String addr, int port, String directionStr,String powerStr, String angleStr){
            dstAddress = addr;
            dstPort = port;
            direction = directionStr;
            power = powerStr;
            angle = angleStr;

        }

        @Override
        protected Void doInBackground(Void... params) {

            Socket socket = null;

            try {
                socket = new Socket(dstAddress, dstPort);
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeUTF("pw="+power+" angle="+angle + " drt="+direction);
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "IOException: " + e.toString();
            }finally{
                if(socket != null){
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    finally {
                        if (socket != null) {
                            try {
                                socket.close();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }

                        if (dataOutputStream != null) {

                            try {
                                dataOutputStream.flush();
                                dataOutputStream.close();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }
                }
                return null;
            }
        }
    }
}
