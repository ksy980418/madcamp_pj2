package com.example.madcamp2;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentThree extends Fragment {
    public static final int THREAD_HANDLER_SUCCESS_INFO = 1;
    TextView textView_w1;
    TextView textView_w2;
    TextView textView_w3;
    private View view;

    ForeCastManager mForeCast;
    FragmentThree mThis;
    ArrayList<ContentValues> mWeatherData;
    ArrayList<WeatherInfo> mWeatherInfomation;

    static t3_recyclerAdapter t3_adapter;
    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.activity_weather,container,false);

        textView_w1=(TextView)view.findViewById(R.id.textView_w1);
        textView_w2=(TextView)view.findViewById(R.id.textView_w2);

        mWeatherInfomation = new ArrayList<>();

        textView_w1.setText(Main2Activity.currentLocation);

        String lon = Double.toString(Main2Activity.longitude);
        String lat = Double.toString(Main2Activity.latitude);
        mThis = this;

        mForeCast = new ForeCastManager(lon,lat,mThis);
        mForeCast.run();
        //recyclerview 부분
        mRecyclerView = view.findViewById(R.id.recycler_view1);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        t3_adapter = new t3_recyclerAdapter(getActivity(), Main2Activity.cList);
        mRecyclerView.setAdapter(t3_adapter);

        Main2Activity.tab3_act = true;

        Button new_button = view.findViewById(R.id.new_button);
        Button button =view.findViewById(R.id.w_button);

        new_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("region", Main2Activity.region);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new JSONTask(null, jsonObject).execute("get_content");
            }
        });

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getContext(), writeActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private String PrintValue1() {
        String mData = mWeatherInfomation.get(0).getWeather_Name() +
                            " 최고: " + mWeatherInfomation.get(0).getTemp_Max() + "℃"
                            +  "/최저: " + mWeatherInfomation.get(0).getTemp_Min() + "℃" ;
        return mData;
    }

    private void DataChangedToHangeul() {
        for(int i = 0 ; i <mWeatherInfomation.size(); i ++) {
            WeatherToHangeul mHangeul = new WeatherToHangeul(mWeatherInfomation.get(i));
            mWeatherInfomation.set(i,mHangeul.getHangeulWeather());
        }
    }

    private void DataToInformation() {
        for(int i = 0; i < mWeatherData.size(); i++) {
            mWeatherInfomation.add(new WeatherInfo(
                    String.valueOf(mWeatherData.get(i).get("weather_Name")),  String.valueOf(mWeatherData.get(i).get("weather_Number")), String.valueOf(mWeatherData.get(i).get("weather_Much")),
                    String.valueOf(mWeatherData.get(i).get("weather_Type")),  String.valueOf(mWeatherData.get(i).get("wind_Direction")),  String.valueOf(mWeatherData.get(i).get("wind_SortNumber")),
                    String.valueOf(mWeatherData.get(i).get("wind_SortCode")),  String.valueOf(mWeatherData.get(i).get("wind_Speed")),  String.valueOf(mWeatherData.get(i).get("wind_Name")),
                    String.valueOf(mWeatherData.get(i).get("temp_Min")),  String.valueOf(mWeatherData.get(i).get("temp_Max")),  String.valueOf(mWeatherData.get(i).get("humidity")),
                    String.valueOf(mWeatherData.get(i).get("Clouds_Value")),  String.valueOf(mWeatherData.get(i).get("Clouds_Sort")), String.valueOf(mWeatherData.get(i).get("Clouds_Per")),String.valueOf(mWeatherData.get(i).get("day"))
            ));
        }
    }

    public Handler handler = new Handler(){
        @Override      public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case THREAD_HANDLER_SUCCESS_INFO :
                    mForeCast.getmWeather();
                    mWeatherData = mForeCast.getmWeather();
                    if(mWeatherData.size() ==0)
                        textView_w2.setText("데이터가 없습니다");

                    DataToInformation(); // 자료 클래스로 저장,

                    DataChangedToHangeul();
                    String data1 = "";
                    data1 = PrintValue1();

                    textView_w2.setText(data1);
                    break;
                default:
                    break;
            }
        }
    };
}