package com.example.Wifi;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.R;

/**
 * Created by Jérémy on 13/12/2016.
 */
public class WifiFragment extends Fragment implements View.OnClickListener{
    Button btnConnect;
    EditText password;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wifi, container, false);
        btnConnect= (Button) view.findViewById(R.id.Connectbutton);
        password= (EditText) view.findViewById(R.id.password);
        return view;

    }

    @Override
    public void onClick(View v) {

    }



}
