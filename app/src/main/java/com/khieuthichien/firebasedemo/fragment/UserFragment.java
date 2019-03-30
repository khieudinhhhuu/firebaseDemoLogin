package com.khieuthichien.firebasedemo.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.khieuthichien.firebasedemo.R;

public class UserFragment extends Fragment {

    public UserFragment() {
        // Required empty public constructor
    }

    private ImageView imgView;
    private TextView tvname;
    private TextView tvemail;
    private Button btnLogout;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        imgView = findViewById(R.id.imgView);
//        tvname = findViewById(R.id.tvname);
//        tvemail = findViewById(R.id.tvemail);
//        btnLogout = findViewById(R.id.btnLogout);
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

}
