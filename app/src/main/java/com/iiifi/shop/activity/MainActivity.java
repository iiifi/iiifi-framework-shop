package com.iiifi.shop.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.iiifi.shop.view.MainView;

public class MainActivity extends AppCompatActivity {


    private Button loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new MainView(this);
    }
}
