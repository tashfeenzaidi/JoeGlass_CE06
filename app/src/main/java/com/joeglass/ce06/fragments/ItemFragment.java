// Joe Glass

// JAV2 - C20201201

// ItemFragment.java
package com.joeglass.ce06.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joeglass.ce06.adapters.MyItemRecyclerViewAdapter;
import com.joeglass.ce06.R;
import com.joeglass.ce06.utilities.FileUtil;

import java.io.File;

/**
 * A fragment representing a list of Items.
 */
public class ItemFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 2;

    View view;
    File[] files;
    File filePath;
    MyItemRecyclerViewAdapter adapter;
    RecyclerView recyclerView;

    public ItemFragment() {
    }

    public static ItemFragment newInstance(int columnCount,String filePath) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putString("file_path",filePath);
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            filePath = new File(getArguments().getString("file_path"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_item_list, container, false);
        return view;
    }

    public void setAdapter( File[] files){
            Context context = view.getContext();
            TextView textView = view.findViewById(R.id.label);
            textView.setVisibility(View.INVISIBLE);
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
            files = FileUtil.getFlies(filePath);

            if (files != null){
                setAdapter(files);
            }
        }else {
            if (FileUtil.getFlies(filePath) != null){
                adapter = new MyItemRecyclerViewAdapter(FileUtil.getFlies(filePath),view.getContext());
                recyclerView.setAdapter(adapter);
            }
        }
    }
}