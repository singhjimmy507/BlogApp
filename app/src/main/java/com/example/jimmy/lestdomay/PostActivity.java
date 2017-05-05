package com.example.jimmy.lestdomay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import static android.app.Activity.RESULT_OK;

public class PostActivity extends AppCompatActivity {

    private ImageButton mImage;
    private EditText mTitle, mPost;
    private Button mSubmit;
    private Uri imageUri=null;
    private static final int GALLERY_REQUEST =1;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mImage = (ImageButton)findViewById(R.id.imagePost);

        mTitle = (EditText)findViewById(R.id.title);
        mPost = (EditText) findViewById(R.id.post);

        mSubmit = (Button) findViewById(R.id.Submit);

        mStorage = FirebaseStorage.getInstance().getReference(); //will upload images in the root directory
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog"); //will upload info under the blog child
        mProgress = new ProgressDialog(this); //Adding progress dialog

        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);

                galleryIntent.setType("image/*"); // will open gallery to select images
                startActivityForResult(galleryIntent,GALLERY_REQUEST);

            }
        });
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting(); // uploading it in database
            }


        });

    }
    private void startPosting() {
        mProgress.setMessage("Uploading to blog...");
        mProgress.show();
        final String title_value = mPost.getText().toString().trim(); //accessing value of title
        final String desc_value= mTitle.getText().toString().trim();
        //Checking if strings are empty
        if(!TextUtils.isEmpty(title_value)&& !TextUtils.isEmpty(desc_value)&&imageUri!=null){

            StorageReference filepath = mStorage.child("Blog_Images").child(imageUri.getLastPathSegment());
            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    @SuppressWarnings(VisibleForTests);
                    @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    DatabaseReference newPost = mDatabase.push(); //will create random ID so posts are appended and not overriden
                    newPost.child("Title").setValue(title_value);
                    newPost.child("Description").setValue(desc_value);
                    newPost.child("Image").setValue(downloadUrl.toString());
                    mProgress.dismiss();
                    startActivity(new Intent(PostActivity.this, MainActivity.class));
                }
            });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_REQUEST && resultCode==RESULT_OK){
            //declaring Uri globally as it needs to be accessed in startPosting()
           Uri image = data.getData();
            //Adding image cropping functionality
            CropImage.activity(image)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1280,720)
                    .start(this);
//            mImage.setImageURI(imageUri);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                mImage.setImageURI(imageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
