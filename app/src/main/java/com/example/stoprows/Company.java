package com.example.stoprows;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

import static android.widget.Toast.LENGTH_SHORT;

public class Company extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextView greet;
    private Button startRow;
    private DatabaseReference mDatabase;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        startRow = (Button) findViewById(R.id.startRow);
        greet = (TextView) findViewById(R.id.greeting);

        getUserInfo();

        startRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mDatabase.child("Users").child(uid).child("its open?").setValue(true);
               mDatabase.child("Users").child(uid).child("inrow").setValue(uid);
                startActivity(new Intent(Company.this,CompanyInRow.class));
            }
        });
    }

    private void getUserInfo(){
        uid = mAuth.getCurrentUser().getUid();
        mDatabase.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String name = dataSnapshot.child("name").getValue().toString();
                    greet.setText("Hi, "+ name +"!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getQR(View v){
        Intent qr = new Intent(this,QR.class);
        qr.putExtra("uid",uid);
        startActivity(qr);
    }

    public void logout(View v){
        mAuth.signOut();
        startActivity(new Intent(Company.this,MainActivity.class));
        finish();
    }
}
