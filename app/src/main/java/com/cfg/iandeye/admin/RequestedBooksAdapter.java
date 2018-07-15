package com.cfg.iandeye.admin;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.Spinner;
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

        final CharSequence[] vols = new String[]{"Volunteer1","Volunteer2","Volunteer3","Volunteer4","Volunteer5"};


        TextView listItemText_filename = (TextView)view.findViewById(R.id.filename);
        TextView listItemText_standard_name = (TextView)view.findViewById(R.id.volunteer_name);


        listItemText_filename.setText(filenames.get(position));
        listItemText_standard_name.setText(standard_list.get(position));

        Button select_vol = view.findViewById(R.id.select_volunteer);
        select_vol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertdialog = new AlertDialog.Builder(context);
                alertdialog.setItems(vols, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String vol_name = vols[i].toString();
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/html");
                        intent.putExtra(Intent.EXTRA_EMAIL, "cfg2k18@gmail.com");
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Record Request");
                        intent.putExtra(Intent.EXTRA_TEXT, "Request to record "+filenames.get(position)+" For "+standard_list.get(position) +" Standard");

                        context.startActivity(Intent.createChooser(intent, "Send Email"));
                    }
                });

                AlertDialog alertDialog = alertdialog.create();
                alertDialog.show();
            }
        });




        return view;
    }



}
