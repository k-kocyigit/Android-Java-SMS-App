package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    EditText etEmail, etPassword;
    Button register, redirectLogin;
    FirebaseAuth fbAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etEmail = findViewById(R.id.et_registerMail);
        etPassword = findViewById(R.id.et_registerPassword);
        register = findViewById(R.id.register_btn_register);
        redirectLogin = findViewById(R.id.btn_redirectLogin);
        fbAuth = FirebaseAuth.getInstance();

        redirectLogin.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });

        register.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(RegisterActivity.this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show();
                return;
            }
            fbAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Başarıyla kayıt olundu", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, DrawerActivity.class));
                }
                else{
                    Toast.makeText(this, "Kayıt başarısız oldu", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}