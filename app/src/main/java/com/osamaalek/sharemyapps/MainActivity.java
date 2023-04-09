package com.osamaalek.sharemyapps;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView textView;
    private WebServer webServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
        Switch aSwitch = findViewById(R.id.switch1);

        aSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (!b && webServer != null) {
                webServer.stop();
            } else if (b && WifiAPUtil.isWifiAPEnable(this)) {
                displayIpAddress();
                webServer = new WebServer(Constants.PORT, this);
            } else if (b) {
                Toast.makeText(this, getString(R.string.msg_enable_wifi_ap), Toast.LENGTH_SHORT).show();
                compoundButton.setChecked(false);
            }
        });
    }

    private void displayIpAddress() {
        String ip = NetworkUtil.getDeviceIpAddress();
        textView.setText(ip);
        imageView.setImageBitmap(generateQR(ip));
    }

    private Bitmap generateQR(String s) {
        QRGEncoder qrgEncoder = new QRGEncoder(s, null, QRGContents.Type.TEXT, 200);
        try {
            return qrgEncoder.encodeAsBitmap();
        } catch (WriterException e) {
            return null;
        }
    }

}