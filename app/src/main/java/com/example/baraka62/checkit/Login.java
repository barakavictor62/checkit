package com.example.baraka62.checkit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private FirebaseAuth auth;
    Button login_button, signup;
    TextView email, password,username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        login_button = (Button) findViewById(R.id.loginbtn);
        signup = (Button) findViewById(R.id.got_to_sign_up);
        email = (TextView) findViewById(R.id.email);
        password = (TextView) findViewById(R.id.password);
        username = (TextView) findViewById(R.id.my_username);

        auth= FirebaseAuth.getInstance();

        signUp();
        tryLogin();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = auth.getCurrentUser();
    }

    public void tryLogin() {
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               login(String.valueOf(username.getText()),String.valueOf(email.getText()),String.valueOf(password.getText()));
            }
        });
    }

    private void login(final String my_username, String email, String password){
       auth.signInWithEmailAndPassword(email, password )
               .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                           final FirebaseUser user = auth.getCurrentUser();
                           if(user != null){
                                final String user_email = user.getEmail();
                               // bundle.putString("name",my_username);
                               FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                               DatabaseReference find_me = firebaseDatabase.getReference().child("Users");
                               find_me.addListenerForSingleValueEvent(new ValueEventListener() {
                                   @Override
                                   public void onDataChange(DataSnapshot dataSnapshot) {
                                       if(dataSnapshot.hasChild(user.getUid())){
                                           Intent intent = new Intent(Login.this, Home.class);
                                          Bundle bundle = new Bundle();
                                           bundle.putString("email", user_email);
                                           bundle.putString("name", my_username);
                                           intent.putExtras(bundle);
                                           startActivity(intent);
                                           Toast.makeText(getApplicationContext(), "Logged In", Toast.LENGTH_LONG).show();

                                       }
                                       else{
                                           Toast.makeText(getApplicationContext(), "Login failed1", Toast.LENGTH_LONG).show();

                                       }
                                   }

                                   @Override
                                   public void onCancelled(DatabaseError databaseError) {

                                   }
                               });

                            }

                        }
                        else {
                           Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void signUp(){
        signup.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SignUp.class);
                startActivity(intent);
            }
        });

    }
}
