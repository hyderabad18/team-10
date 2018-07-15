package com.cfg.iandeye.volunter;


import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.cfg.iandeye.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.net.URISyntaxException;

import cafe.adriel.androidaudioconverter.AndroidAudioConverter;
import cafe.adriel.androidaudioconverter.callback.IConvertCallback;
import cafe.adriel.androidaudioconverter.model.AudioFormat;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class FileUpload_Fragment extends Fragment {
    private StorageReference mStorageRef;
    private static final int gallery = 12;
    private Uri uploadfileuri;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private static int count = 1;
    Spinner spinner;
    private EditText file_name;
    private EditText edition;
    private EditText subject;
    View holder;
    String uploadfilename;
    Button  button;

    FirebaseUser user;
    private FirebaseAuth mAuth;

    public FileUpload_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        holder = inflater.inflate(R.layout.fragment_file_upload, container, false);
        button = holder.findViewById(R.id.upload);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonupload();
            }
        });
        //Toast.makeText(getActivity(), "File in uplaod activity", Toast.LENGTH_SHORT).show();
        final String[] standard = new String[]{"Select", "10", "11", "12"};
        final ArrayAdapter<String> standardadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, standard);
        spinner = holder.findViewById(R.id.standard_spinner);
        spinner.setAdapter(standardadapter);
        file_name = holder.findViewById(R.id.file_name);
        edition = holder.findViewById(R.id.edition);
        subject = holder.findViewById(R.id.subject);
        FloatingActionButton floatingActionButton = holder.findViewById(R.id.share);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey, I have Contributed to Visually Impared students by preparing study material using i&Eye Mobile app");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

        mAuth = FirebaseAuth.getInstance();

        user = mAuth.getCurrentUser();


        mStorageRef = FirebaseStorage.getInstance(com.google.firebase.FirebaseApp.initializeApp(getActivity())).getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();
        Button select_file = holder.findViewById(R.id.select);

        select_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("audio/*");
                startActivityForResult(Intent.createChooser(i, "select file"), gallery);
            }
        });

        return holder;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == gallery && resultCode == RESULT_OK && data != null) {
            uploadfileuri = data.getData();
            String path = null;
            try {
                path = getFilePath(getContext(),uploadfileuri);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            Log.i("log",path);

            File file = new File(path);
            Log.i("log",file.toString());
            String type = getfileext(uploadfileuri);
            Log.i("log",type);


            if(type.equals("wav") || type.equals("WAV")) {

                final ProgressDialog progress = new ProgressDialog(getContext());
                progress.setMessage("Converting ....");
                progress.show();

                IConvertCallback callback = new IConvertCallback() {
                    @Override
                    public void onSuccess(File convertedFile) {
                        //Toast.makeText(getContext(), "SUCCESS: " + convertedFile.getPath(), Toast.LENGTH_LONG).show();
                        uploadfileuri = Uri.fromFile(new File(convertedFile.getPath()));
                        progress.dismiss();
                    }
                    @Override
                    public void onFailure(Exception error) {
                        Toast.makeText(getContext(), "ERROR: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                };

                AndroidAudioConverter.with(getContext())
                        .setFile(file)
                        .setFormat(AudioFormat.MP3)
                        .setCallback(callback)
                        .convert();
            }

            Uri returnUri = data.getData();
            Cursor returnCursor =
                    getActivity().getContentResolver().query(returnUri, null, null, null, null);

            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
            returnCursor.moveToFirst();

            uploadfilename=returnCursor.getString(nameIndex);
            //filename.setText(uploadfilename);


        }
    }

    public String getfileext(Uri uri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    public void buttonupload() {


        ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        final boolean isconnected = info != null && info.isConnectedOrConnecting();
        if (!isconnected) {
            Toast.makeText(getActivity(), "No Internet", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(file_name.getText())) {
            Toast.makeText(getActivity(), "Enter File Name", Toast.LENGTH_SHORT).show();
            return;
        }


        if (uploadfileuri != null) {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("uploading file");
            progressDialog.show();
            final StorageReference storageReference = mStorageRef.child("audiobooks/" + uploadfilename);
            storageReference.putFile(uploadfileuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Toast.makeText(getActivity(), "doc uploaded", Toast.LENGTH_SHORT).show();

                            databaseReference = firebaseDatabase.getReference().child("files_temp").child(String.valueOf(count));

                            databaseReference.child("bookname").setValue(file_name.getText().toString());
                            databaseReference.child("edition").setValue(edition.getText().toString());
                            databaseReference.child("subject").setValue(subject.getText().toString());
                            databaseReference.child("standard").setValue(spinner.getSelectedItem().toString());
                            databaseReference.child("volunteer").setValue(user.getEmail().toString());
                            databaseReference.child("url").setValue(uri.toString());
                            databaseReference.child("keyid").setValue(String.valueOf(count++));

                            Log.i("log",uri.toString());

                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "doc uploading failed", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Log.i("log", e.getLocalizedMessage());

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @SuppressWarnings("VisibleForTests")
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage("uploaded   " + (int) progress + "");
                }
            });
        }
    }

    public static String getFilePath(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }}
