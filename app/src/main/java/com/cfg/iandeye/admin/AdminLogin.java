package com.cfg.iandeye.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

import com.cfg.iandeye.R;

public class AdminLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        android.support.v7.widget.Toolbar toolbar= (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Admin Login");
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        Button admin_login = findViewById(R.id.admin);
        EditText loginid = findViewById(R.id.admin_id);
        EditText passwd = findViewById(R.id.admin_password);



        admin_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminLogin.this,Admin_Dashboard.class));
            }
        });
    }
}
