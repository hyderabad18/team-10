package com.cfg.iandeye.student;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.cfg.iandeye.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class Onlinedocs extends Fragment {
    View holder;
    private ArrayList<String> docurl;
    DatabaseReference databaseReference;
    ChildEventListener valueEventListener;




    public Onlinedocs() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        holder=inflater.inflate(R.layout.fragment_onlinebooks, container, false);
        FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
        ListView docslist= (ListView) holder.findViewById(R.id.onlinedocs);
        final ArrayList<String> docnames=new ArrayList<>();
        docurl=new ArrayList<>();

        final FileDownloadadapter fileDownloadadapter=new FileDownloadadapter(docnames,docurl,getContext());
        SharedPreferences sharedPreferences=getContext().getSharedPreferences("info", Context.MODE_PRIVATE);




     try {
         databaseReference = firebaseDatabase.getReference().child("rootfiles");

         valueEventListener = databaseReference.addChildEventListener(new ChildEventListener() {
             @Override
             public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                 Map<String, String> map = (Map) dataSnapshot.getValue();
                 String name = map.get("bookname");
                 Log.i("log", name);
                 String url = map.get("url");

                 docnames.add(name);
                 docurl.add(url);

                 fileDownloadadapter.notifyDataSetChanged();
             }


             @Override
             public void onChildChanged(DataSnapshot dataSnapshot, String s) {

             }

             @Override
             public void onChildRemoved(DataSnapshot dataSnapshot) {

             }

             @Override
             public void onChildMoved(DataSnapshot dataSnapshot, String s) {

             }

             @Override
             public void onCancelled(DatabaseError databaseError) {

             }
         });

     }catch (Exception e){
         Log.i("error",e.getLocalizedMessage());
     }
        docslist.setAdapter(fileDownloadadapter);


        return holder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(valueEventListener!=null) {
            databaseReference.removeEventListener(valueEventListener);
        }
    }
}
