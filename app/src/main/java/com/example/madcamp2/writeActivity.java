package com.example.madcamp2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class writeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        final TextView name_t = findViewById(R.id.name);
        final EditText inner_t = findViewById(R.id.inner);
        final EditText outer_t = findViewById(R.id.outer);
        final EditText feedback_t = findViewById(R.id.feedback);
        Button button = findViewById(R.id.commit);

        name_t.setText(Main2Activity.user_name);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String inner=inner_t.getText().toString();
                String outer = outer_t.getText().toString();
                String feedback = feedback_t.getText().toString();

                JSONObject json = new JSONObject();
                try {
                    json.put("region", Main2Activity.region);
                    json.put("id", Main2Activity.user_name);
                    json.put("inner", inner);
                    json.put("outer", outer);
                    json.put("feedback", feedback);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new JSONTask(null, json).execute("content_add");

                finish();
            }
        });
    }
}
