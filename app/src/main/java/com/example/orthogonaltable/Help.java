package com.example.orthogonaltable;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Help extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_standard;
    private TextView tv_complex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        initView();


    }

    private void initView() {
        tv_standard=findViewById(R.id.standard);
        tv_complex=findViewById(R.id.complex);
        tv_standard.setOnClickListener(this);
        tv_complex.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.standard:
                CopyButtonLibrary copyButtonLibrary1 = new CopyButtonLibrary(getApplicationContext(),tv_standard);
                copyButtonLibrary1.init();
                break;

            case R.id.complex:
                CopyButtonLibrary copyButtonLibrary2 = new CopyButtonLibrary(getApplicationContext(),tv_complex);
                copyButtonLibrary2.init();
        }
    }
}
