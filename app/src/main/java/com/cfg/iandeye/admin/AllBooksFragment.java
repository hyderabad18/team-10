package com.cfg.iandeye.admin;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
public class AllBooksFragment extends Fragment {
    View holder;
    DatabaseReference databaseReference;
    ChildEventListener valueEventListener;
    private ArrayList<String> filenames = new ArrayList<String>();
    private ArrayList<String> vulunteer_namers = new ArrayList<String>();
    public AllBooksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        holder =  inflater.inflate(R.layout.fragment_all_books, container, false);
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();

        final AllBooksAdapter allBooksAdapter=new
                AllBooksAdapter(filenames,vulunteer_namers,getActivity());
        ListView docslist= (ListView) holder.findViewById(R.id.allbookslist);



        try {
            databaseReference = firebaseDatabase.getReference().child("rootfiles");

            valueEventListener = databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Map<String, String> map = (Map) dataSnapshot.getValue();
                    String filename = map.get("bookname");
                    String volunteername = map.get("volunteer");

                    Log.i("log", filename);
                    String url = map.get("url");

                    filenames.add(filename);
                    vulunteer_namers.add(map.get("edition"));




                    allBooksAdapter.notifyDataSetChanged();
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
        docslist.setAdapter(allBooksAdapter);


        return holder;
    }

}
