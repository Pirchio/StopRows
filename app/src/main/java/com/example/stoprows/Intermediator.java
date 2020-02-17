package com.example.stoprows;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Intermediator extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private boolean usertype,inrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intermediator);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        getUserInfo();
    }

    private void getUserInfo(){
        String uid = mAuth.getCurrentUser().getUid();
        mDatabase.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usertype = (boolean) dataSnapshot.child("usertype").getValue();
                inrow = (boolean) dataSnapshot.child("inrow").getValue();
                if (usertype){
                    if (inrow)
                        startActivity(new Intent(Intermediator.this, ClientInRow.class));
                    else
                        startActivity(new Intent(Intermediator.this,Client.class));
                }
                else{
                    if (inrow)
                        startActivity(new Intent(Intermediator.this,CompanyInRow.class));
                    else
                        startActivity(new Intent(Intermediator.this,Company.class));
                }
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getUserInfo();

    }
}
