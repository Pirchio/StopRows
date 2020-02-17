package com.example.stoprows;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.widget.Toast.LENGTH_SHORT;

public class ClientInRow extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button leftRow;
    private DatabaseReference mDatabase;
    private String uid;
    private String fresult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientinrow);

        leftRow = (Button)findViewById(R.id.leftRow);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        uid = mAuth.getCurrentUser().getUid();
        leftRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("Users").child(uid).child("inrow").setValue(false);
                startActivity(new Intent(ClientInRow.this,Client.class));
            }
        });
    }

    public void logout(View v){
        mAuth.signOut();
        startActivity(new Intent(ClientInRow.this,MainActivity.class));
        finish();
    }
}
