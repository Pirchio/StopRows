package com.example.stoprows;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.widget.Toast.LENGTH_SHORT;

public class ClientInRow extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button leftRow;
    private DatabaseReference mDatabase, reference;
    private String uid;
    private String open;
    private String sult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientinrow);

        leftRow = (Button)findViewById(R.id.leftRow);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        uid = mAuth.getCurrentUser().getUid();
        mDatabase.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sult = dataSnapshot.child(uid).child("inrow").getValue().toString();
                if (sult.equals("0")) {
                    exit();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        leftRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });


    }
    public void exit (){
        mDatabase.child(sult).child(uid).child("name").setValue(null);
        mDatabase.child(sult).child(uid).child("email").setValue(null);
        mDatabase.child("Users").child(uid).child("inrow").setValue("0");
        Intent i = new Intent(ClientInRow.this,Client.class);
        i.putExtra("f", (String) null);
        startActivity(i);
    }

    public void logout(View v){
        mAuth.signOut();
        startActivity(new Intent(ClientInRow.this,MainActivity.class));
        finish();
    }
}
