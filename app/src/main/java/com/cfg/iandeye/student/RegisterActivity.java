package com.cfg.iandeye.student;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cfg.iandeye.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    public EditText name;
    public EditText s_class,location;

    // public Button login;
    public Button register;
    public FirebaseAuth firebaseAuth;
    public TextView login;
    public EditText person_name;
    public FirebaseDatabase database;
    public DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        name = (EditText) findViewById(R.id.name);
        s_class = (EditText) findViewById(R.id.student_class);
        location=(EditText) findViewById(R.id.s_location);
        register = (Button) findViewById(R.id.student_register);
        register.setOnClickListener(this);

        database=FirebaseDatabase.getInstance();
        databaseReference=database.getReference("student_profile");

        //person_name=(EditText) findViewById(R.id.Name);
//number=(ContactsContract.CommonDataKinds.Phone)findViewById(R.id.Number);


    }


    @Override
    public void onClick(View view) {
        if(view==register)
        {
            String n=name.getText().toString().trim();
            String s=s_class.getText().toString().trim();
            String l=location.getText().toString().trim();
            user_profile up=new user_profile(n,s,l);
            FirebaseUser user=firebaseAuth.getCurrentUser();
            databaseReference.child(user.getUid()).setValue(up);
            databaseReference.push().setValue(up);
            Toast.makeText(this," Successfully uploaded",Toast.LENGTH_LONG).show();


        }

    }
}

