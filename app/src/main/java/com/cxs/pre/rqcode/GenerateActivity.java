package com.cxs.pre.rqcode;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.cxs.pre.rqcode.zxingmanage.EncodeManage;


public class GenerateActivity extends AppCompatActivity {


    private ImageView ivQr;
    private Bitmap bitmap;
    private EncodeManage encodeManage;
    private View dialogView;
    private EditText etContent;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);
        init();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initDialog();
                if (etContent != null) {
                    etContent.setText("");
                }
                alertDialog.show();
            }
        });

    }

    private void initDialog() {
        if (alertDialog != null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(GenerateActivity.this);
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                encode(etContent.getText().toString());
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), null);
        builder.setView(dialogView);
        alertDialog = builder.create();
    }

    private void init() {
        encodeManage = new EncodeManage(GenerateActivity.this);
        ivQr = (ImageView) findViewById(R.id.iv_qr);
        dialogView = getLayoutInflater().inflate(R.layout.dialog_input, null);
        etContent = (EditText) dialogView.findViewById(R.id.et_content);
    }


    @Override
    protected void onResume() {
        super.onResume();
        encode("Hello Kitty!");
    }

    private void encode(String contentsStr) {
        try {
            bitmap = encodeManage.encodeAsBitmap(contentsStr);
            ivQr.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmap != null) {
            bitmap.recycle();
        }
    }
}
