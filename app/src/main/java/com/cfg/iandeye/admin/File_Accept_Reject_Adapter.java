package com.cfg.iandeye.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.cfg.iandeye.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


/**
 * Created by baswarajmamidgi on 29/10/16.
 */

public class File_Accept_Reject_Adapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> filenames = new ArrayList<String>();
    private ArrayList<String> vulunteer_namers = new ArrayList<String>();
    ArrayList<String> editon_list=new ArrayList<>();
    ArrayList<String> subject_list=new ArrayList<>();
    ArrayList<String> standard_list=new ArrayList<>();
    ArrayList<String> keyid_list=new ArrayList<>();


    private Context context;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;



    public File_Accept_Reject_Adapter(ArrayList<String> names,
                                      ArrayList<String> url,ArrayList<String> editon_list,ArrayList<String> subject_list,ArrayList<String> standard_list,ArrayList<String> keyid_list, Context context) {
        this.filenames = names;
        this.vulunteer_namers =url;
        this.editon_list = editon_list;
        this.subject_list = subject_list;
        this.standard_list = standard_list;
        this.keyid_list = keyid_list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return filenames.size();
    }

    @Override
    public Object getItem(int pos) {
        return pos;
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }


    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_accept_reject, null);
        }
        TextView listItemText_filename = (TextView)view.findViewById(R.id.filename);
        TextView listItemText_volunteername = (TextView)view.findViewById(R.id.volunteer_name);

        listItemText_filename.setText(filenames.get(position));
        listItemText_volunteername.setText(vulunteer_namers.get(position));
        ImageView accept_button = view.findViewById(R.id.file_accept_icon);
        ImageView reject_button = view.findViewById(R.id.file_reject_icon);
        firebaseDatabase= FirebaseDatabase.getInstance();


        accept_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                databaseReference = firebaseDatabase.getReference().child("rootfiles").child(standard_list.get(position));

                databaseReference.child("bookname").setValue(filenames.get(position));
                databaseReference.child("edition").setValue(editon_list.get(position));
                databaseReference.child("subject").setValue(subject_list.get(position));
                databaseReference.child("standard").setValue(standard_list.get(position));
                databaseReference.child("volunteer").setValue(vulunteer_namers.get(position));

                databaseReference = firebaseDatabase.getReference().child("files_temp");
                databaseReference.child(String.valueOf(keyid_list.get(position))).removeValue();


            }
        });

        reject_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "File Rejected", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }



}
