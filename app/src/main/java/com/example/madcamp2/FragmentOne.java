package com.example.madcamp2;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentOne extends Fragment {
    private RecyclerView mRecyclerView;
    private View view;
    static RecyclerAdapter recyclerAdapter;
    static Context tab_context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.frag_layout_1,container,false);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        Button sync = view.findViewById(R.id.s_button);

        recyclerAdapter = new RecyclerAdapter(getActivity(), Main2Activity.dataList);
        mRecyclerView.setAdapter(recyclerAdapter);
        Main2Activity.tab1_act = true;
        tab_context = getActivity();

        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject json = new JSONObject();
                try {
                    json.put("id", Main2Activity.user_id);
                    new JSONTask(null, json).execute("contact_delete");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }
}