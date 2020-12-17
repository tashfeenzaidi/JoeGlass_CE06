package com.joeglass.ce06;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.joeglass.ce06.dummy.DummyContent.DummyItem;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final File[] mValues;
    Context context;

    public MyItemRecyclerViewAdapter(File[] items,Context context) {
        mValues = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
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
        return mValues.length;
    }

    public Bitmap loadImageBitmap( File imageName) throws IOException {
        Bitmap bitmap = null;
        byte[] bytes = read(imageName);
        bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        return bitmap;
    }

    public byte[] read(File file) throws IOException {

        ByteArrayOutputStream ous = null;
        InputStream ios = null;
        try {
            byte[] buffer = new byte[4096];
            ous = new ByteArrayOutputStream();
            ios = new FileInputStream(file);
            int read = 0;
            while ((read = ios.read(buffer)) != -1) {
                ous.write(buffer, 0, read);
            }
        }finally {
            try {
                if (ous != null)
                    ous.close();
            } catch (IOException e) {
            }

            try {
                if (ios != null)
                    ios.close();
            } catch (IOException e) {
            }
        }
        return ous.toByteArray();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public final ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            imageView =  view.findViewById(R.id.image);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), loadImageBitmap(mValues[getAdapterPosition()]), null, null)), "image/*");
                intent.setFlags(FLAG_GRANT_READ_URI_PERMISSION);
                intent.setFlags(FLAG_GRANT_WRITE_URI_PERMISSION);
                context.startActivity(intent);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}