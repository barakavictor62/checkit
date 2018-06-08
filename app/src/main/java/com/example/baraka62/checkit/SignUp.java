package com.example.baraka62.checkit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {
    private static final String TAG = "EmailPassword";

    FirebaseAuth firebaseAuth;

    Button trySignUp, backToLogin;

    TextView email, password, confirm_password, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        trySignUp = (Button) findViewById(R.id.sign_me_up);
        backToLogin = (Button) findViewById(R.id.go_to_login);

        username = (TextView) findViewById(R.id.username);
        email = (TextView) findViewById(R.id.email);
        password = (TextView) findViewById(R.id.password);
        confirm_password = (TextView) findViewById(R.id.confirm_password);

        firebaseAuth = FirebaseAuth.getInstance();

        goBackLogin();
        goSignUp();
    }

    public void goSignUp(){
        trySignUp.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                signUp(
                        String.valueOf(username.getText()),
                        String.valueOf(email.getText()),
                        String.valueOf(password.getText()),
                        String.valueOf(confirm_password.getText()));
            }
        });
    }
    private  void signUp(final String username, String email, String password, String confirm_password){
        firebaseAuth.createUserWithEmailAndPassword(email,password).
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            FirebaseDatabase my_profile_database = FirebaseDatabase.getInstance();
                            DatabaseReference my_profile_data = my_profile_database.getReference().child("Users").child(user.getUid()).child("Profile");
                            User me = new User();
                            me.setName(username);
                            my_profile_data.setValue(me);
                            Intent intent =new Intent(SignUp.this, Login.class);
                            startActivity(intent);
                        }
                        else{
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "signup failed",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    public void goBackLogin(){
        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });
    }
}
