package com.example.stoprows;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;

public class Client extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private TextView greet;
    private Button join,left;
    private IntentResult result;
    private boolean storestatus;
    private String fresult, name, email,uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        greet = (TextView) findViewById(R.id.greeting2);
        join = (Button) findViewById(R.id.registerButton);
        left = (Button) findViewById(R.id.leftRow2);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();

        final Activity activity = this;
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });
        getUserInfo();
    }

    private void getUserInfo(){
        mDatabase.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    name = dataSnapshot.child("name").getValue().toString();
                    email = dataSnapshot.child("email").getValue().toString();
                    greet.setText("Hi, "+ name +"!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void logout(View v){
        mAuth.signOut();
        startActivity(new Intent(Client.this,MainActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (result!=null){
            if (result.getContents()==null){
                Toast.makeText(this,"Scanning canceled", LENGTH_SHORT).show();
            }
            else{
                fresult = result.getContents();

            }
        }
        else
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (fresult!=null) {
            getStoreStatus();

        }
    }

    private void getStoreStatus(){
        mDatabase.child("Users").child(fresult).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            storestatus = (boolean) dataSnapshot.child("its open?").getValue();
            if (storestatus) {
                Map<String,Object> row = new HashMap<>();
                row.put("name",name);
                row.put("email",email);
                mDatabase.child(fresult).child(uid).setValue(row);
                mDatabase.child("Users").child(uid).child("inrow").setValue(fresult);

                Intent cir = new Intent(Client.this,ClientInRow.class);
                startActivity(cir);
            } else {
                Toast.makeText(Client.this, "Closed", LENGTH_SHORT).show();
                result=null;fresult=null;
            }}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}