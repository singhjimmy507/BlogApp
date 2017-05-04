package com.example.jimmy.lestdomay;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity {

    private RecyclerView mBlogList;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBlogList = (RecyclerView)findViewById(R.id.blog_list);
        //Set Layout
        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");// get everything in blog child - posts
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Takes 4 parameters for FirebaseRecycler adapter - class returning title,desc,image
        //-the layout (blog_cardview)
        //Class returning title,image,desc from blog_cardview
        //Firebase Database referece
        FirebaseRecyclerAdapter<Blog,BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(
                Blog.class,
                R.layout.blog_cardview,
                BlogViewHolder.class,
                mDatabase) {
            @Override
            //Using getters and setters, this method fills in information for title, desc, image
            protected void populateViewHolder(BlogViewHolder viewHolder, Blog model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDescription(model.getDescription());
                viewHolder.setImage(getApplicationContext(),model.getImage());
            }
        };
                mBlogList.setAdapter(firebaseRecyclerAdapter);
    }

    //Creating a class using Recycler view to get details from blog_cardview.xml
    //Object consisting of title, desc, image will be passing into Firebase Recycler adapter
    public static class BlogViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public BlogViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        //for title in blog_cardview.xml
        public void setTitle(String title){
            TextView post_title = (TextView) mView.findViewById(R.id.post_title);
            post_title.setText(title);
        }
        //for title in blog_cardview.xml
        public void setDescription(String description){
            TextView post_desc = (TextView) mView.findViewById(R.id.post_desc);
            post_desc.setText(description);
        }
        //for image in blog_cardview.xml
        public void setImage(Context ctx, String image){
            ImageView postImage= (ImageView) mView.findViewById(R.id.post_image);
            Picasso.with(ctx).load(image).into(postImage);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_add){
            startActivity(new Intent(MainActivity.this, PostActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
