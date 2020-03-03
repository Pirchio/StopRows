    package com.example.stoprows;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;

    import android.content.Intent;
    import android.os.Bundle;
    import android.view.View;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.Toast;

    import com.google.android.gms.tasks.OnCompleteListener;
    import com.google.android.gms.tasks.Task;
    import com.google.firebase.auth.AuthResult;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;

    import static android.widget.Toast.LENGTH_SHORT;


    public class MainActivity extends AppCompatActivity {
        private EditText mMail,mPass;
        private Button login;
        private boolean usertype;

        private String email = "";
        private String password = "";

        private FirebaseAuth mAuth;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            mAuth = FirebaseAuth.getInstance();

            mMail = (EditText) findViewById(R.id.IdTextBox);
            mPass = (EditText) findViewById(R.id.passwordTextBox);
            login = (Button) findViewById(R.id.loginButton);

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                email = mMail.getText().toString();
                password = mPass.getText().toString();

                if (!email.isEmpty()&&!password.isEmpty()){
                    login();
                    }
                else
                    Toast.makeText(MainActivity.this,"Complete all the fields", LENGTH_SHORT).show();
                }
            });
        }

        private void login(){
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        startActivity(new Intent(MainActivity.this,Intermediator.class));
                        finish();
                    }else
                        Toast.makeText(MainActivity.this,"Login failed", LENGTH_SHORT).show();
                }
            });
        }

        public void Register(View view){
            Intent register = new Intent(this,Register.class);
            startActivity(register);
        }

       @Override
        protected void onStart() {
            super.onStart();

            if(mAuth.getCurrentUser() != null){
                startActivity(new Intent(MainActivity.this,Intermediator.class));
                finish();
            }
        }
    }
