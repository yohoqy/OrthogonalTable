package com.example.orthogonaltable;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

//import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Login extends AppCompatActivity {
    private Button button;
    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

//        FloatingActionButton button=findViewById(R.id.btn_enter);
        button=findViewById(R.id.btn_enter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this,Input.class);
                startActivity(intent);
            }
        });

        textView=findViewById(R.id.tv_help);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this,Help.class);
                startActivity(intent);
            }
        });
    }
}
