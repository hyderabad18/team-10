package com.cfg.iandeye.volunter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cfg.iandeye.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VolunteerProfile extends AppCompatActivity {
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_profile);
        android.support.v7.widget.Toolbar toolbar= (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Volunteer Profile");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        final EditText name = findViewById(R.id.volunteer_profile_name);
        final EditText standard = findViewById(R.id.volunteer_standard);
        final EditText location = findViewById(R.id.volunteer_location);

        Button button = findViewById(R.id.voluneer_submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference().child("volunteer_profile");

                databaseReference.child(name.getText().toString()).child("name").setValue(name.getText().toString());
                databaseReference.child(name.getText().toString()).child("standard").setValue(standard.getText().toString());
                databaseReference.child(name.getText().toString()).child("location").setValue(location.getText().toString());
                startActivity(new Intent(VolunteerProfile.this,Volunteer_Dashboard.class));



            }
        });




    }
}
