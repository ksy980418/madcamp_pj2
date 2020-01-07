package com.example.madcamp2;

import android.content.Context;
import android.os.Build;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class t3_recyclerAdapter extends RecyclerView.Adapter<t3_recyclerAdapter.ItemViewHolder2>{

    private ArrayList<wContents> listData2=new ArrayList<>();
    private Context context;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    private ItemViewHolder2 holder;
    private int position;

    public t3_recyclerAdapter(Context context, ArrayList<wContents> list){
        this.context = context;
        this.listData2 = list;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public t3_recyclerAdapter.ItemViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_item, parent, false);
        return new ItemViewHolder2(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(@NonNull t3_recyclerAdapter.ItemViewHolder2 holder, int position) {
        holder.onBind(listData2.get(position),position);
    }

    @Override
    public int getItemCount() {
        return listData2.size();
    }

    void addItem(wContents data) {
        // 외부에서 item을 추가시킬 함수입니다.
         listData2.add(data);
    }
     class ItemViewHolder2 extends RecyclerView.ViewHolder {

         private TextView textView1;
         private TextView textView2;
         private TextView textView3;
         private TextView textView4;
         private TextView textView5;
         private Button like;
         private wContents data;
         private int position;

         public ItemViewHolder2(@NonNull View itemView) {
             super(itemView);

             textView1 = itemView.findViewById(R.id.name);
             textView2 = itemView.findViewById(R.id.outer);
             textView3 = itemView.findViewById(R.id.inner);
             textView4 = itemView.findViewById(R.id.feedback);
             textView5 = itemView.findViewById(R.id.goodnum);
             like = itemView.findViewById(R.id.like_button);
         }

         @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
         void onBind(wContents _data, int position) {
             this.data = _data;
             this.position = position;
             textView1.setText(_data.getId());
             textView2.setText(_data.getOuter());
             textView3.setText(_data.getInner());
             textView4.setText(_data.getFeedback());
             textView5.setText(String.valueOf(_data.getGoodnum()));
             like.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     JSONObject jsonObject = new JSONObject();
                     try {
                         jsonObject.put("region", Main2Activity.region);
                         jsonObject.put("id", data.getId());
                         jsonObject.put("outer", data.getOuter());
                         jsonObject.put("inner", data.getInner());
                         jsonObject.put("feedback", data.getFeedback());
                         jsonObject.put("user_id", Main2Activity.user_id);

                     } catch (JSONException e) {
                         e.printStackTrace();
                     }
                     new JSONTask(null, jsonObject).execute("good_add");
                 }
             });
         }
     }
}
