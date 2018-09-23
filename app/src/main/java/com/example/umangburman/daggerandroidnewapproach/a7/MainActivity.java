package com.example.umangburman.daggerandroidnewapproach.a7;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.umangburman.daggerandroidnewapproach.R;
import com.example.umangburman.daggerandroidnewapproach.a1.LoginViewModel;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class MainActivity extends AppCompatActivity {

    @Inject
    LoginViewModel loginViewModel;

    private EditText txtEmail, txtPassword;
    private TextView lblEmail, lblPassword;
    private Button btnLogin;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidInjection.inject(this);

        setContentView(R.layout.activity_main);

        txtEmail = findViewById(R.id.txtEmailAddress);
        txtPassword = findViewById(R.id.txtPassword);

        lblEmail = findViewById(R.id.lblEmailAnswer);
        lblPassword = findViewById(R.id.lblPasswordAnswer);

        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Email = txtEmail.getText().toString().trim();
                String Password = txtPassword.getText().toString().trim();

                loginViewModel.setStrUsername(Email);
                loginViewModel.setStrrPassword(Password);

                lblEmail.setText(loginViewModel.getStrUsername());
                lblPassword.setText(loginViewModel.getStrrPassword());

            }
        });

    }
}
