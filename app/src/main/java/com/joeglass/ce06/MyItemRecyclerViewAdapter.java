// Joe Glass

// JAV2 - C20201201

// MyItemRecyclerViewAdapter.java
package com.joeglass.ce06;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final File[] mValues;
    final Context context;

    public MyItemRecyclerViewAdapter(File[] items,Context context) {
        mValues = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Bitmap bitmap = null;
        try {
            bitmap = loadImageBitmap(mValues[position]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        holder.imageView.setImageBitmap(ThumbnailUtils.extractThumbnail(bitmap,660,660));

    }

    @Override
    public int getItemCount() {
        return mValues != null? mValues.length:0;
    }

    public Bitmap loadImageBitmap( File imageName) throws IOException {
        Bitmap bitmap;
        bitmap = BitmapFactory.decodeFile(imageName.toString());
        return bitmap;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            imageView =  view.findViewById(R.id.image);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Uri uri =  FileProvider.getUriForFile(context,BuildConfig.APPLICATION_ID+".provider",mValues[getAdapterPosition()]);
            if (uri != null){
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "image/*");
                intent.setFlags(FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(intent);
            }
        }
    }
}