package com.example.madcamp2;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class JSONTask extends AsyncTask<String, String ,String > {
    private JSONArray jsonArray;
    private JSONObject jsonObject;
    private final String server = "http://192.168.0.78:80/";
    private String process;

    public JSONTask(JSONArray _jsonArray, JSONObject _jsonobject) {
        jsonArray = _jsonArray;
        jsonObject = _jsonobject;
    }

    @Override
    protected String doInBackground (String... urls) {
        try {
            HttpURLConnection con = null;
            BufferedReader reader = null;

            try{
                process = urls[0];
                URL url = new URL(server + urls[0]);
                con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("POST");
                con.setRequestProperty("Cache-Control", "no-cache");
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "text/html");
                con.setDoOutput(true);
                con.setDoInput(true);
                con.connect();

                OutputStream outStream = con.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                if (!process.equals("contact_sync")) {
                    writer.write(jsonObject.toString());
                }else {
                    writer.write(jsonArray.toString());
                }
                writer.flush();
                writer.close();

                InputStream stream = con.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuilder buffer = new StringBuilder();

                String line;
                while((line = reader.readLine()) != null){
                    buffer.append(line);
                }

                return buffer.toString();

            } catch (IOException e){
                e.printStackTrace();
            } finally {
                if(con != null){
                    con.disconnect();
                }
                try {
                    if(reader != null){
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        switch (process) {
            case "a_get": {
                JSONArray jsonArray;
                try {
                    jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String img = jsonArray.getJSONObject(i).getString("_image");
                        Uri PhotoUri = Uri.parse(img);

                        Main2Activity.im_array.add(PhotoUri);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            }
            case "image_add":
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(result);
                    String image = jsonObject.getString("image");

                    if (!image.equals("X")) {
                        Uri photoUri = Uri.parse(image);

                        Main2Activity.im_array.add(photoUri);
                        FragmentTwo.imageAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "c_get": {
                JSONArray jsonArray;
                try {
                    jsonArray = new JSONArray(result);
                    Main2Activity.dataList.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            Phonenumber p = new Phonenumber(jsonArray.getJSONObject(i).getString("_name")
                                    , jsonArray.getJSONObject(i).getString("_number")
                                    , jsonArray.getJSONObject(i).getString("_photo_id")
                                    , jsonArray.getJSONObject(i).getString("_photo_thum_id")
                                    , jsonArray.getJSONObject(i).getString("_person_id")
                                    , jsonArray.getJSONObject(i).getString("_photo_url"));
                            Main2Activity.dataList.add(p);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Collections.sort(Main2Activity.dataList, new Comparator<Phonenumber>() {
                        @Override
                        public int compare(Phonenumber s1, Phonenumber s2) {
                            return s1.getName().toUpperCase().compareTo(s2.getName().toUpperCase());
                        }
                    });
                    if (Main2Activity.tab1_act)
                        FragmentOne.recyclerAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            }
            case "contact_sync": {
                JSONObject json = new JSONObject();
                try {
                    json.put("id", Main2Activity.user_id);
                    new JSONTask(null, json).execute("c_get");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            }
            case "get_content": {
                Main2Activity.cList.clear();
                JSONArray jsonArray;
                try {
                    jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            wContents w = new wContents(jsonArray.getJSONObject(i).getString("_id")
                                    , jsonArray.getJSONObject(i).getString("_outer")
                                    , jsonArray.getJSONObject(i).getString("_inner")
                                    , jsonArray.getJSONObject(i).getString("_feedback")
                                    , jsonArray.getJSONObject(i).getInt("_num"));
                            Main2Activity.cList.add(w);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Collections.sort(Main2Activity.cList, new Comparator<wContents>() {
                        @Override
                        public int compare(wContents s1, wContents s2) {
                            return s2.getGoodnum() - s1.getGoodnum();
                        }
                    });
                    if (Main2Activity.tab3_act)
                        FragmentThree.t3_adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            }
            case "content_add":
            case "good_add": {
                JSONObject json = new JSONObject();
                try {
                    json.put("region", Main2Activity.region);
                    new JSONTask(null, json).execute("get_content");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            }
            case "contact_delete": {
                ArrayList<Phonenumber> data = new ArrayList<>();

                Cursor c = FragmentOne.tab_context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                        null, null, null,
                        ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " asc");

                while (c.moveToNext()) {
                    // 연락처 id 값
                    String id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                    // 연락처 대표 이름
                    String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
                    String photo_id = c.getString(c.getColumnIndex(ContactsContract.Contacts.PHOTO_ID));
                    String photo_thum_id = c.getString(c.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI));
                    String person_id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                    String photo_url = c.getString(c.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
                    String number;

                    // ID로 전화 정보 조회
                    Cursor phoneCursor = FragmentOne.tab_context.getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                            null, null);

                    // 데이터가 있는 경우
                    if (phoneCursor.moveToFirst()) {
                        number = phoneCursor.getString(phoneCursor.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                    else{
                        number="None";
                    }
                    Phonenumber p = new Phonenumber(name,number, photo_id, photo_thum_id, person_id,photo_url);
                    phoneCursor.close();
                    data.add(p);
                }// end while
                c.close();

                JSONArray jsonArr = new JSONArray();
                JSONObject jsonO = new JSONObject();
                try {
                    jsonO.put("id", Main2Activity.user_id);
                    jsonArr.put(jsonO);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < data.size(); i++) {
                    JSONObject json = new JSONObject();
                    try {
                        json.put("name", data.get(i).name);
                        json.put("number", data.get(i).number);
                        json.put("person_id", data.get(i).person_id);
                        if (data.get(i).photo_id == null) {
                            json.put("photo_id", "n");
                            json.put("photo_thum_id", "n");
                            json.put("photo_url", "n");
                        } else {
                            json.put("photo_id", data.get(i).photo_id);
                            json.put("photo_thum_id", data.get(i).photo_thum_id);
                            json.put("photo_url", data.get(i).photo_url);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArr.put(json);
                }
                new JSONTask(jsonArr, jsonO).execute("contact_sync");
                break;
            }
        }
    }
}
