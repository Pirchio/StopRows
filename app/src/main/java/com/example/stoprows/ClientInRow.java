package com.example.stoprows;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import static android.widget.Toast.LENGTH_SHORT;

public class ClientInRow extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button leftRow;
    private DatabaseReference mDatabase, reference;
    private String uid;
    private String sult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientinrow);

        leftRow = (Button)findViewById(R.id.leftRow);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        uid = mAuth.getCurrentUser().getUid();
        Intent intent = getIntent();
        sult = intent.getStringExtra("company");
        Toast.makeText(ClientInRow.this, sult, LENGTH_SHORT).show();
        leftRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child(sult).child(uid).setValue(null);
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
