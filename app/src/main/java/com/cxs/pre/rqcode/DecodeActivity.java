package com.cxs.pre.rqcode;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cxs.pre.rqcode.zxingmanage.DecodeManage;
import com.google.zxing.Result;

public class DecodeActivity extends AppCompatActivity {

    private final static int RESULT_LOAD_IMAGE = 0x00;
    private final String TAG = getClass().getName();
    private DecodeManage decodeManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decode);
        init();
    }

    private void init() {
        decodeManage = new DecodeManage();
    }

    public void selectPic(View v) {
        Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Intent intent1 = new Intent(Intent.ACTION_PICK, uri);
        startActivityForResult(intent1, RESULT_LOAD_IMAGE);
    }

    public void openCamera(View v){
        startActivity(new Intent(this,CameraActivity.class));
        Toast.makeText(DecodeActivity.this, "openCamera", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn,
                    null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            Log.d(TAG, "---path--->" + filePath);
            cursor.close();

            Result result = decodeManage.scanningImage(filePath);
            if (result != null) {
                String contentStr = result.getText();
                Toast.makeText(DecodeActivity.this, contentStr, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "---content--->" + contentStr);
            } else {
                Toast.makeText(DecodeActivity.this, getString(R.string.encode_failure),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
