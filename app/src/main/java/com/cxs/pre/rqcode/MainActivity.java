package com.cxs.pre.rqcode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void scan(View v) {
        startActivity(new Intent(this,DecodeActivity.class));
    }

    public void generate(View v) {
        startActivity(new Intent(this, GenerateActivity.class));
    }
}
