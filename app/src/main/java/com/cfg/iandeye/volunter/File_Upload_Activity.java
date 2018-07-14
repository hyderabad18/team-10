package com.cfg.iandeye.volunter;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.cfg.iandeye.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class File_Upload_Activity extends AppCompatActivity {
    private StorageReference mStorageRef;
    private static final int gallery=12;
    private Uri uploadfileuri;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private static int count=1;
    Spinner spinner;
    private EditText file_name;
    private EditText edition;
    private EditText subject;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file__upload_);
        Toast.makeText(this, "File in uplaod activity", Toast.LENGTH_SHORT).show();
        final String[] standard=new String[]{"Select","10","11","12"};
        final ArrayAdapter<String> standardadapter=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,standard);
         spinner = findViewById(R.id.standard_spinner);
        spinner.setAdapter(standardadapter);
        file_name = findViewById(R.id.file_name);
        edition = findViewById(R.id.edition);
        subject = findViewById(R.id.subject);




        mStorageRef = FirebaseStorage.getInstance(com.google.firebase.FirebaseApp.initializeApp(File_Upload_Activity.this)).getReference();
        firebaseDatabase= FirebaseDatabase.getInstance();
        Button select_file = findViewById(R.id.select);

        select_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("*/*");
                startActivityForResult(Intent.createChooser(i,"select file") ,gallery);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == gallery && resultCode == RESULT_OK && data != null) {
            uploadfileuri = data.getData();
            File file = new File(uploadfileuri.getPath());
            String type=getfileext(uploadfileuri);

            Uri returnUri = data.getData();
            Cursor returnCursor =
                    getContentResolver().query(returnUri, null, null, null, null);

            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
            returnCursor.moveToFirst();


        }
    }
    public String getfileext(Uri uri)
    {
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }
    public void buttonupload(View v)
    {


        ConnectivityManager manager= (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        final boolean isconnected = info != null && info.isConnectedOrConnecting();
        if(!isconnected)
        {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(file_name.getText()))
        {
            Toast.makeText(this, "Enter File Name", Toast.LENGTH_SHORT).show();
            return;
        }


        if(uploadfileuri!=null)
        {
            final ProgressDialog progressDialog=new ProgressDialog(File_Upload_Activity.this);
            progressDialog.setTitle("uploading file");
            progressDialog.show();
            StorageReference storageReference=mStorageRef.child("documents/"+"firstfile");
            storageReference.putFile(uploadfileuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(File_Upload_Activity.this, "doc uploaded", Toast.LENGTH_SHORT).show();

                        databaseReference = firebaseDatabase.getReference().child("files_temp").child(String.valueOf(count));

                        databaseReference.child("bookname").setValue(file_name.getText().toString());
                        databaseReference.child("edition").setValue(edition.getText().toString());
                        databaseReference.child("subject").setValue(subject.getText().toString());
                        databaseReference.child("standard").setValue(spinner.getSelectedItem().toString());
                        databaseReference.child("volunteer").setValue("volunteer1");
                        databaseReference.child("keyid").setValue(String.valueOf(count++));









                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(File_Upload_Activity.this, "doc uploading failed", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Log.i("log",e.getLocalizedMessage());

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @SuppressWarnings("VisibleForTests")
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress=(100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage("uploaded   "+(int)progress+"");
                }
            });
        }
    }
}
