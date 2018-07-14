package com.cfg.iandeye.student;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cfg.iandeye.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestBook extends Fragment {
    private View holder;
    DatabaseReference databaseReference;
    static int request_count = 0;

    public RequestBook() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        holder = inflater.inflate(R.layout.fragment_request_book, container, false);
        final EditText book_name = holder.findViewById(R.id.book_name);
        final EditText book_standard = holder.findViewById(R.id.book_standard);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference().child("requested_books");


        Button submit = holder.findViewById(R.id.request_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child(String.valueOf(request_count)).child("book_name").setValue(book_name.getText().toString());
                databaseReference.child(String.valueOf(request_count)).child("standard").setValue(book_standard.getText().toString());
                request_count++;
                Toast.makeText(getContext(), "Request Submitted", Toast.LENGTH_SHORT).show();


            }
        });
        return holder;
    }
}





