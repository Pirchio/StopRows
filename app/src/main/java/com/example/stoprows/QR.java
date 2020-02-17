package com.example.stoprows;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import static android.widget.Toast.LENGTH_SHORT;

public class QR extends AppCompatActivity {
    private String uid;
    private ImageView qrCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        qrCode = findViewById(R.id.qrimage);
        uid = getIntent().getStringExtra("uid");

        try {
            qrCode();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        Toast.makeText(QR.this,"Press volume down + on to make a screenshot", LENGTH_SHORT).show();
    }

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        public void qrCode() throws WriterException {
            BitMatrix bitMatrix = multiFormatWriter.encode(uid, BarcodeFormat.QR_CODE,350,300);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrCode.setImageBitmap(bitmap);
        }
}
