package com.khieuthichien.firebasedemo.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.util.AsyncListUtil;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.khieuthichien.firebasedemo.R;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

public class SignupActivity extends AppCompatActivity {

    private EditText edt_signup_email, edt_signup_password;
    private Button btn_register, btn_cancel;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initViews();
        initActions();

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        }

    }

    private void initViews() {
        edt_signup_email = findViewById(R.id.edt_signup_email);
        edt_signup_password = findViewById(R.id.edt_signup_password);
        btn_register = findViewById(R.id.btn_register);
        btn_cancel = findViewById(R.id.btn_cancel);
    }

    private void initActions() {
        btn_register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edt_signup_email.getText().toString();
                String password = edt_signup_password.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    if (email.equals("")) {
                        edt_signup_email.setError(getString(R.string.notify_empty_email));
                        return;
                    }
                    if (password.equals("")) {
                        edt_signup_password.setError(getString(R.string.notify_empty_password));
                        return;
                    }
                } else {
                    if (password.length() < 6) {
                        if (password.length() < 6) {
                            edt_signup_password.setError(getString(R.string.notify_empty_password_length));
                            return;
                        }
                    } else {
                        firebaseAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                            finish();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });


                    }
                }


            }
        });

        btn_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }


}

