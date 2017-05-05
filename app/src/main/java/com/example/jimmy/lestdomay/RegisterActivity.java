package com.example.jimmy.lestdomay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText name;
    private EditText email;
    private EditText password;
    private Button signup, login;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgress;
    private DatabaseReference mDatabase;
    private static final String TAG = "RegisterActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        mProgress = new ProgressDialog(this);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        name = (EditText) findViewById(R.id.editName);
        email =(EditText)findViewById(R.id.editEmail);
        password = (EditText) findViewById(R.id.editPassword);
        signup = (Button) findViewById(R.id.btnRegister);
        login = (Button) findViewById(R.id.btnLogin);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegister();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

    }

    private void startRegister() {

       final String nameValue = name.getText().toString().trim();
        String emailValue = email.getText().toString().trim();
        String passwordValue = password.getText().toString().trim();

        if(!TextUtils.isEmpty(nameValue) && !TextUtils.isEmpty(emailValue) && !TextUtils.isEmpty(passwordValue)) {
            mProgress.setMessage("Signing up...");
            mProgress.show();
            //In built function only accepts email and password so need to create another databaser reference
            //to store name and image
//            Log.d(TAG,emailValue);
//            Log.d(TAG, passwordValue);
            mAuth.createUserWithEmailAndPassword(emailValue, passwordValue).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        String user_id = mAuth.getCurrentUser().getUid();
                        //Since firebase only stores email and password in the method above. We
                        //are extracting and storing name and image in a separate child.
                        DatabaseReference current_user = mDatabase.child(user_id);
                        current_user.child("Name").setValue(nameValue);
                        current_user.child("Image").setValue("default");
                        mProgress.dismiss();

                        Intent mainActivity = new Intent(RegisterActivity.this, MainActivity.class);
                        mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainActivity);

                    }
                }
            });
        }
    }
}
