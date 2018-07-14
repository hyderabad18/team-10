package com.cfg.iandeye.student;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.cfg.iandeye.BuildConfig;
import com.cfg.iandeye.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;


/**
 * Created by baswarajmamidgi on 29/10/16.
 */

public class FileDownloadadapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private ArrayList<String> url = new ArrayList<String>();
    private static final int MEGABYTE = 1024 * 1024;

    private Context context;


    public FileDownloadadapter(ArrayList<String> names, ArrayList<String> url, Context context) {
        this.list = names;
        this.url = url;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
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
            view = inflater.inflate(R.layout.filedownload, null);
        }
        TextView listItemText = (TextView) view.findViewById(R.id.textView5);
        final ImageView button = (ImageView) view.findViewById(R.id.imageView);
        listItemText.setText(list.get(position));
        listItemText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download(url.get(position), list.get(position));
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download(url.get(position), list.get(position));

            }
        });


        return view;
    }


    public void download(String url, String filename) {

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        boolean isconnected = info != null && info.isConnectedOrConnecting();
        if (!isconnected) {
            Toast.makeText(context, "No Internet", Toast.LENGTH_LONG).show();
            return;
        }
        new FileDownloadadapter.Downloadpdf().execute(url, filename);

    }


    private class Downloadpdf extends AsyncTask<String, Integer, String[]> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setIndeterminate(false);
            progressDialog.setMax(100);
            progressDialog.setMessage("Downloading file...");
            progressDialog.show();

        }

        @Override
        protected String[] doInBackground(String... strings) {
            String url = strings[0];
            Log.i("log",url);
            String filename = strings[1];
            Log.i("log",filename);
            String extstoragedir = Environment.getExternalStorageDirectory().toString();
            File fol = new File(extstoragedir, "iandeye");
            File folder = new File(fol, "audiobooks");

            boolean bool = folder.mkdir();

            File file = new File(folder, filename);
            try {
                boolean state = file.createNewFile();
            } catch (IOException e) {
                Log.i("log", e.getLocalizedMessage());
                e.printStackTrace();
            }
            int count;
            try {
                Log.i("log", "file downloader called");
                URL contenturl = new URL(url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) contenturl.openConnection();
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                FileOutputStream outputStream = new FileOutputStream(file);
                int totalsize = httpURLConnection.getContentLength();
                Log.i("log", "length of file" + String.valueOf(totalsize));
                byte[] buffer = new byte[MEGABYTE];
                int bufferlength = 0;
                while ((count = inputStream.read(buffer)) > 0) {
                    bufferlength += count;
                    publishProgress((int) ((bufferlength / (float) totalsize) * 100));
                    outputStream.write(buffer, 0, count);
                }
                inputStream.close();
                outputStream.flush();
                outputStream.close();


            } catch (MalformedURLException e) {
                Log.i("loge", e.getLocalizedMessage());
                e.printStackTrace();
            } catch (IOException e) {
                Log.i("loge", e.getLocalizedMessage());
                e.printStackTrace();
            }


            String[] data = new String[]{filename, strings[1]};
            return data;


        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String file[]) {
            view(file[0], file[1]);
            progressDialog.dismiss();

        }
    }


    public void view(String filename, String type) {
        Log.i("log", filename);
        File file = new File(Environment.getExternalStorageDirectory() + "/iandeye/audiobooks/" + filename);
        Uri path = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);

        Intent pdfintent = new Intent(Intent.ACTION_VIEW);
        pdfintent.setDataAndType(path, "*/*");


        pdfintent.setFlags(FLAG_GRANT_READ_URI_PERMISSION | FLAG_GRANT_WRITE_URI_PERMISSION); //must for reading data from directory
        try {
            context.startActivity(pdfintent);
        } catch (ActivityNotFoundException e) {
            Log.i("log", e.getLocalizedMessage());
        }


    }
}



