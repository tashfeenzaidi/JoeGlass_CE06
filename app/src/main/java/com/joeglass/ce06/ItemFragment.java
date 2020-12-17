package com.joeglass.ce06;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joeglass.ce06.dummy.DummyContent;

import java.io.File;
import java.net.FileNameMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * A fragment representing a list of Items.
 */
public class ItemFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 2;

    View view;
    File[] files;
    MyItemRecyclerViewAdapter adapter;
    RecyclerView recyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {
    }

    // TODO: Customize parameter initialization
    public static ItemFragment newInstance(int columnCount) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_item_list, container, false);
        return view;
    }

    public File[] getImageFiles(){
        File filePath = Objects.requireNonNull(getContext()).getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        if (filePath.exists()){
            files = filePath.listFiles();
            assert files != null;
            if (files.length>0){
                for (File file : files) {
                    Log.d("Files", "FileName:" + file.getName());
                }
               return files;
            }

        }
        return null;
    }



    public void setAdapter( File[] files){
            Context context = view.getContext();
            recyclerView = view.findViewById(R.id.list);
            recyclerView.setVisibility(View.VISIBLE);

            adapter = new MyItemRecyclerViewAdapter(files,context);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(adapter);

    }

    public void updateList(){
        if (files == null){
            if (getImageFiles() != null){
                setAdapter(getImageFiles());
            }
        }else {
            getImageFiles();
            adapter.notifyDataSetChanged();
        }
    }
}