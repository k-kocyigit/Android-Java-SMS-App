package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText etEmail, etPassword;
    Button login, redirectRegister;
    FirebaseAuth fbAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etEmail = findViewById(R.id.et_loginMail);
        etPassword = findViewById(R.id.et_loginPassword);
        login = findViewById(R.id.login_btn_login);
        redirectRegister = findViewById(R.id.btn_redirectRegister);
        fbAuth = FirebaseAuth.getInstance();

        redirectRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        login.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show();

                return;}
            fbAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Başarıyla giriş yapıldı", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, DrawerActivity.class));
                }
                else{
                    Toast.makeText(this, "Giriş başarısız oldu", Toast.LENGTH_SHORT).show();
                }
            });
        });

    }
}