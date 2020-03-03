package com.example.stoprows;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;

public class Register extends AppCompatActivity {

    private EditText name, mail, pass, cpass;
    private RadioButton user;
    private Button register;
     //en string
    private String sid = "",smail = "",spass = "",scpass = "",inrow = "0";
    private boolean usertype;
    private boolean open = false;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    private String fresult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        name = (EditText) findViewById(R.id.IdTextBox);
        mail = (EditText) findViewById(R.id.emailTextBox);
        pass = (EditText) findViewById(R.id.passwordTextBox);
        cpass = (EditText) findViewById(R.id.cpasswordTextBox);
        register = (Button) findViewById(R.id.registerButton);
        user = (RadioButton) findViewById(R.id.userRadioButton);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            sid = name.getText().toString();
            smail = mail.getText().toString();
            spass = pass.getText().toString();
            scpass = cpass.getText().toString();

            if (user.isChecked())
                usertype = true;
            else
                usertype = false;

            if (!sid.isEmpty() && !smail.isEmpty() && !spass.isEmpty() && !scpass.isEmpty()){
                if (spass.equals(scpass)){
                    if (pass.length()<=6)
                        Toast.makeText(Register.this,"The password has to have 6 characters at least", LENGTH_SHORT).show();
                    else
                        registerUser();
                        Toast.makeText(Register.this,"Iniciating...", LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(Register.this,"No coincidence between passwords", LENGTH_SHORT).show();
            }else {
                Toast.makeText(Register.this,"Complete all the fields", LENGTH_SHORT).show();
            }

            }
        });
    }
            private void registerUser(){
                mAuth.createUserWithEmailAndPassword(smail,spass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Map<String,Object> map = new HashMap<>();
                            map.put("name", sid);
                            map.put("email",smail);
                            map.put("password",spass);
                            map.put("usertype",usertype);
                            map.put("its open?",open);
                            map.put("inrow",inrow);

                            String  id = mAuth.getCurrentUser().getUid();

                            mDatabase.child("Users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task2) {
                                    if (task2.isSuccessful()){
                                        if (usertype)
                                            startActivity(new Intent(Register.this,Client.class));
                                        else
                                            startActivity(new Intent(Register.this,Company.class));
                                        finish();
                                    }else
                                        Toast.makeText(Register.this,"Register failed2", LENGTH_SHORT).show();
                                }
                            });
                        }
                        else
                            Toast.makeText(Register.this,"The register failed", LENGTH_SHORT).show();

                    }
                });
            }

            public void AlreadyAccount(View view){
                Intent main = new Intent(this,MainActivity.class);
                startActivity(main);
            }
}
