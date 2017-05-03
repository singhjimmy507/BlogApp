package com.example.jimmy.lestdomay;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private Button mFirebasebtn, mUpload;
    private EditText editName, editEmail;

    private DatabaseReference mDatabase;
    private StorageReference mStorageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");//points to root directory of database
        mStorageRef = FirebaseStorage.getInstance().getReference();
        editName = (EditText) findViewById(R.id.name);
        editEmail = (EditText) findViewById(R.id.email);
        mFirebasebtn = (Button)findViewById(R.id.submit);
        mUpload = (Button)findViewById(R.id.upload);
        mFirebasebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //Create child in root object. Assign some value to database
                String name = editName.getText().toString().trim();
                String email = editEmail.getText().toString().trim();

                HashMap<String, String> credentials = new HashMap <String, String>();
                credentials.put("Name",name);
                credentials.put("Email",email);
                mDatabase.push().setValue(credentials);

            }
        });
        //Testing some code, will work on it later
//        mUpload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
//                StorageReference riversRef = mStorageRef.child("images/rivers.jpg");
//
//                riversRef.putFile(file)
//                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                // Get a URL to the uploaded content
////                                Uri downloadUrl = taskSnapshot.getDownloadUrl();
//
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception exception) {
//                                // Handle unsuccessful uploads
//                                // ...
//                            }
//                        });
//            }
//        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
