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

public class AllBooksAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> filenames = new ArrayList<String>();
    private ArrayList<String> volunteernames = new ArrayList<String>();


    private Context context;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;



    public AllBooksAdapter(ArrayList<String> names, ArrayList<String> volunteernames, Context context) {
        this.filenames = names;
      this.volunteernames = volunteernames;
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
        listItemText_volunteername.setText(volunteernames.get(position));



        return view;
    }



}
