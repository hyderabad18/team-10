package com.cfg.iandeye.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.cfg.iandeye.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


/**
 * Created by baswarajmamidgi on 29/10/16.
 */

public class RequestedBooksAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> filenames = new ArrayList<String>();
    private ArrayList<String> standard_list = new ArrayList<String>();


    private Context context;



    public RequestedBooksAdapter(ArrayList<String> names, ArrayList<String> standard_list, Context context) {
        this.filenames = names;
      this.standard_list = standard_list;
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
            view = inflater.inflate(R.layout.requested_books_layout, null);
        }
        TextView listItemText_filename = (TextView)view.findViewById(R.id.filename);
        TextView listItemText_standard_name = (TextView)view.findViewById(R.id.volunteer_name);

        listItemText_filename.setText(filenames.get(position));
        listItemText_standard_name.setText(standard_list.get(position));



        return view;
    }



}
