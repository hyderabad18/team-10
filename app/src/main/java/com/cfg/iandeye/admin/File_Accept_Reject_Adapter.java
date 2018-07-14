package com.cfg.iandeye.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.cfg.iandeye.R;

import java.util.ArrayList;


/**
 * Created by baswarajmamidgi on 29/10/16.
 */

public class File_Accept_Reject_Adapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> filenames = new ArrayList<String>();
    private ArrayList<String> vulunteer_namers = new ArrayList<String>();

    private Context context;



    public File_Accept_Reject_Adapter(ArrayList<String> names, ArrayList<String> url, Context context) {
        this.filenames = names;
        this.vulunteer_namers =url;
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
        Button accept_button = view.findViewById(R.id.file_accept_icon);
        Button reject_button = view.findViewById(R.id.file_reject_icon);

        accept_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

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
