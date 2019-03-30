package com.khieuthichien.firebasedemo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.khieuthichien.firebasedemo.R;
import com.khieuthichien.firebasedemo.model.Photo;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder> {

    private Context context;
    private List<Photo> photoList;

    public PhotoAdapter(Context context, List<Photo> photoList) {
        this.context = context;
        this.photoList = photoList;
    }

    @NonNull
    @Override
    public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_news_feed, parent, false );
        return new PhotoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoHolder holder, int position) {
        final Photo photo = photoList.get(position);
        holder.textViewName.setText(photo.getmName());
        Picasso.with(context)
                .load(photo.getmImageUrl())
                .into(holder.imageViewUpload);
    }

    @Override
    public int getItemCount() {
        if (photoList == null){
            return 0;
        }
        return photoList.size();
    }

    public class PhotoHolder extends RecyclerView.ViewHolder {

        private TextView textViewName;
        private ImageView imageViewUpload;

        public PhotoHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.text_view_name);
            imageViewUpload = itemView.findViewById(R.id.image_view_upload);
        }

    }

}
