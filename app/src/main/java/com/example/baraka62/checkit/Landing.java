package com.example.baraka62.checkit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Landing extends AppCompatActivity {
    Button take_to_login,take_to_sign_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        take_to_login = (Button) findViewById(R.id.take_to_login);
        take_to_sign_up=(Button) findViewById(R.id.take_to_sign_up);

        take_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeToLogin();
            }
        });
        take_to_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeToSignUp();
            }
        });
    }

    private void takeToSignUp() {
        Intent signup_intent = new Intent(this, SignUp.class);
        startActivity(signup_intent);
    }

    private void takeToLogin() {
        Intent login_intent = new Intent(this, Login.class);
        startActivity(login_intent);
    }

}
