package com.example.jimmy.lestdomay;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class PostActivity extends AppCompatActivity {

    private ImageButton mImage;
    private static final int GALLERY_REQUEST =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        mImage = (ImageButton)findViewById(R.id.imagePost);
        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);

                galleryIntent.setType("image/*"); // will open gallery to select images
                startActivityForResult(galleryIntent,GALLERY_REQUEST);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_REQUEST && resultCode==RESULT_OK){
            Uri imageUri = data.getData();
            mImage.setImageURI(imageUri);
        }



    }
}
