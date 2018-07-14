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
public class AcceptBooksFragment extends Fragment {
    DatabaseReference databaseReference;
    ChildEventListener valueEventListener;
    View holder;

    public AcceptBooksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        holder =  inflater.inflate(R.layout.fragment_accept_books, container, false);

        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        ListView docslist= (ListView) holder.findViewById(R.id.onlinedocs);
        final ArrayList<String> filenames=new ArrayList<>();
        final ArrayList<String> volunteernames=new ArrayList<>();
        final ArrayList<String> editon_list=new ArrayList<>();
        final ArrayList<String> subject_list=new ArrayList<>();
        final ArrayList<String> standard_list=new ArrayList<>();
        final ArrayList<String> file_urls = new ArrayList<>();

        final ArrayList<String> key_ids = new ArrayList<>();




        final File_Accept_Reject_Adapter fileDownloadadapter=new
                File_Accept_Reject_Adapter(filenames,volunteernames,editon_list,subject_list,standard_list,file_urls,key_ids,getActivity());



        try {
            databaseReference = firebaseDatabase.getReference().child("files_temp");

            valueEventListener = databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Map<String, String> map = (Map) dataSnapshot.getValue();
                    String filename = map.get("bookname");
                    String volunteername = map.get("volunteer");

                    Log.i("log", filename);
                    String url = map.get("url");

                    filenames.add(filename);
                    volunteernames.add(volunteername);
                    editon_list.add(map.get("edition"));
                    subject_list.add(map.get("subject"));
                    standard_list.add(map.get("standard"));
                    file_urls.add(map.get("url"));
                    key_ids.add(map.get("keyid"));



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
