package com.example.madcamp2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTwo extends Fragment {
    private static final int PICK_FROM_ALBUM = 1;
    private static final int SHOW_FULL_IMAGE = 2;
    private boolean is_delete;
    static ArrayList<Uri> im_array;
    static ImageAdapter imageAdapter;

    public FragmentTwo() {
        im_array = Main2Activity.im_array;
        is_delete = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab2, container, false);
        GridView gv = v.findViewById(R.id.grid_view);

        final Button delete = v.findViewById(R.id.d_button);
        Button gallery = v.findViewById(R.id.g_button);

        imageAdapter = new ImageAdapter(getActivity(), im_array);
        gv.setAdapter(imageAdapter);

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                final int index = position;
                if (is_delete) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setTitle("IMAGE DELETE")
                            .setMessage("Do you really want to delete the image?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    JSONObject jsonObject = new JSONObject();
                                    try {
                                        jsonObject.put("id", Main2Activity.user_id+"a");
                                        jsonObject.put("image", im_array.get(index));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    new JSONTask(null, jsonObject).execute("image_delete");

                                    im_array.remove(index);

                                    imageAdapter.notifyDataSetChanged();

                                    delete.setClickable(true);
                                    delete.setVisibility(View.VISIBLE);
                                    Toast.makeText(getContext(), "Delete image successly", Toast.LENGTH_LONG).show();
                                    is_delete = false;
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    delete.setClickable(true);
                                    delete.setVisibility(View.VISIBLE);
                                    is_delete = false;
                                }
                            }).show();
                }else {
                    Intent i = new Intent(getContext(), make_full.class);
                    i.putExtra("id", position);
                    startActivityForResult(i, SHOW_FULL_IMAGE);
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Click the image what you want to delete", Toast.LENGTH_SHORT).show();
                is_delete = true;
                delete.setClickable(false);
                delete.setVisibility(View.INVISIBLE);
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                go_album();
            }
        });
        // Inflate the layout for this fragment
        return v;
    }

    private void go_album() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_FROM_ALBUM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(getContext(), "ALBUM CANCEL" ,Toast.LENGTH_SHORT).show();
            return;
        }

        if (requestCode == PICK_FROM_ALBUM) {
            Uri photoUri = data.getData();
            if (photoUri != null) {
                String image = photoUri.toString();

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("id", Main2Activity.user_id + "a");
                    jsonObject.put("image", image);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new JSONTask(null, jsonObject).execute("image_add");
            }
        }
    }
}