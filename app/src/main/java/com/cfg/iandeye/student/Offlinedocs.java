package com.cfg.iandeye.student;


import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.cfg.iandeye.BuildConfig;
import com.cfg.iandeye.R;

import java.io.File;
import java.util.ArrayList;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;


/**
 * A simple {@link Fragment} subclass.
 */
public class Offlinedocs extends Fragment {
   private ArrayList<String> offlinefiles=new ArrayList<>();
    private ArrayAdapter<String> adapter;

    public Offlinedocs() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View holder=inflater.inflate(R.layout.fragment_onlinebooks,container,false);
        ListView fileslist = (ListView) holder.findViewById(R.id.onlinedocs);
        fileslist.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        String path = Environment.getExternalStorageDirectory().toString()+"/iandeye/audiobooks";
        File directory=new File(path);
        if(directory.exists()) {
            File[] files = directory.listFiles();
            if (files.length > 0) {
                for (File file : files) {
                    offlinefiles.add(file.getName());
                    Log.i("Files", "FileName:" + file.getName());
                }
            }
        }
        adapter=new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,offlinefiles);


        fileslist.setAdapter(adapter);

        fileslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view(offlinefiles.get(position));

            }
        });



        return holder;

    }





    public void view(String filename)
    {
        File file=new File(Environment.getExternalStorageDirectory()+"/iandeye/audiobooks/"+filename);
        Uri path= FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider",file);

        Intent pdfintent=new Intent(Intent.ACTION_VIEW);
        String type=getFileExt(path).toLowerCase();
        switch (type) {

            case "wav": {
                pdfintent.setDataAndType(path, "audio/*");
                break;
            }

            default: {
                pdfintent.setDataAndType(path, "application/vnd.ms-powerpoint");
                break;
            }
        }
        pdfintent.setFlags(FLAG_GRANT_READ_URI_PERMISSION | FLAG_GRANT_WRITE_URI_PERMISSION); //must for reading data from directory
        try {
            startActivity(pdfintent);
        }catch (ActivityNotFoundException e){
            Log.i("log",e.getLocalizedMessage());
        }


    }




    public String getFileExt(Uri uri){
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();             //return "jpeg,png,pdf epub of files"
        String type = mime.getExtensionFromMimeType(cR.getType(uri));
        Log.i("log",type);
        return type;
    }



}

