package com.khieuthichien.firebasedemo.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.khieuthichien.firebasedemo.R;
import com.khieuthichien.firebasedemo.adapter.PhotoAdapter;
import com.khieuthichien.firebasedemo.model.Photo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;


public class NewsFeedFragment extends Fragment {

    private LinearLayout statusNewsFeed;
    private LinearLayout photoNewsFeed;
    private ProgressBar progressCircle;

    private RecyclerView recyclerviewNewsFeed;
    private PhotoAdapter photoAdapter;
    private List<Photo> photoList;

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;

    private FirebaseStorage storage;
    private StorageReference mStorageReference;
    private DatabaseReference mDatabaseReference;
    private ValueEventListener mValueEventListener;

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_feed, container, false);

        //storage = FirebaseStorage.getInstance();
        mStorageReference = FirebaseStorage.getInstance().getReference("photos");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("photos");

        statusNewsFeed = view.findViewById(R.id.status_news_feed);
        photoNewsFeed = view.findViewById(R.id.photo_news_feed);
        progressCircle = view.findViewById(R.id.progress_circle);
        recyclerviewNewsFeed = view.findViewById(R.id.recyclerview_news_feed);

        recyclerviewNewsFeed.setHasFixedSize(true);
        recyclerviewNewsFeed.setLayoutManager(new LinearLayoutManager(getContext()));
        photoList = new ArrayList<>();
        photoAdapter = new PhotoAdapter(getContext(), photoList);
        recyclerviewNewsFeed.setAdapter(photoAdapter);

        //hien thi anh
        mValueEventListener = mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                photoList.clear();

                for (DataSnapshot posSnapshot : dataSnapshot.getChildren()) {
                    Photo photo = posSnapshot.getValue(Photo.class);
                    photo.setmKey(posSnapshot.getKey());
                    photoList.add(photo);
                }

                photoAdapter.notifyDataSetChanged();
                progressCircle.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                progressCircle.setVisibility(View.INVISIBLE);
            }

        });

        initActions();

        return view;
    }


    public void initActions() {
        statusNewsFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.dialog_status_news_feed, null);
                builder.setView(dialogView);
                final Dialog dialog = builder.show();

                final Button btn_post_status_news_feed;
                final Button btn_cancel_status_news_feed;

                btn_post_status_news_feed = dialog.findViewById(R.id.btn_post_status_news_feed);
                btn_cancel_status_news_feed = dialog.findViewById(R.id.btn_cancel_status_news_feed);

                btn_post_status_news_feed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final EditText edt_stt_status_news_feed;
                        edt_stt_status_news_feed = dialog.findViewById(R.id.edt_stt_status_news_feed);

                        String stt_status = edt_stt_status_news_feed.getText().toString().trim();

                        if (stt_status.equals("")){
                            Toast.makeText(getContext(), "Cancel the post", Toast.LENGTH_SHORT).show();
                            return;
                        }else {
                            Toast.makeText(getContext(), "Cancel the post", Toast.LENGTH_SHORT).show();
                        }


                        dialog.dismiss();
                    }
                });

                btn_cancel_status_news_feed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "Cancel the post", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

            }
        });
        photoNewsFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialogView = inflater.inflate(R.layout.dialog_photo_news_feed, null);
            builder.setView(dialogView);
            final Dialog dialog = builder.show();

            final EditText edt_stt_photo_news_feed;
            final ImageView img_photo_news_feed;
            final Button btn_post_photo_news_feed;
            final Button btn_cancel_photo_news_feed;

            edt_stt_photo_news_feed = dialog.findViewById(R.id.edt_stt_photo_news_feed);
            img_photo_news_feed = dialog.findViewById(R.id.img_photo_news_feed);
            btn_post_photo_news_feed = dialog.findViewById(R.id.btn_post_photo_news_feed);
            btn_cancel_photo_news_feed = dialog.findViewById(R.id.btn_cancel_photo_news_feed);

            filePath = data.getData();
            Picasso.with(getContext())
                    .load(filePath)
                    .into(img_photo_news_feed);

            btn_post_photo_news_feed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (filePath != null) {

                        final ProgressDialog progressDialog = new ProgressDialog(getContext());
                        progressDialog.setTitle("Uploading...");
                        progressDialog.show();

                        StorageReference ref = mStorageReference.child("photos/" + UUID.randomUUID().toString());
                        ref.putFile(filePath)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getContext(), "Post Successful", Toast.LENGTH_SHORT).show();

                                        Photo photo = new Photo(edt_stt_photo_news_feed.getText().toString().trim(), taskSnapshot.getDownloadUrl().toString());
                                        String photoId = mDatabaseReference.push().getKey();
                                        mDatabaseReference.child(photoId).setValue(photo);

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                        progressDialog.setMessage("Uploaded " + (int) progress + "%");
                                    }
                                });
                    }
                    dialog.dismiss();
                }
            });

            btn_cancel_photo_news_feed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Cancel the post", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabaseReference.removeEventListener(mValueEventListener);
    }


}
