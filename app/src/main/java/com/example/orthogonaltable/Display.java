package com.example.orthogonaltable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.orthogonaltable.Utils.ExcelUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Display extends AppCompatActivity implements View.OnClickListener {

    private TableView tableView;
    private ImageView imageView;
    private FloatingActionButton exportButton;
    private File file;
    private String fileName;
    private List<String[]>data1;
    private String[] title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        initview();

        Bundle bundle=getIntent().getExtras();
        //数据表头
        title=(String[])bundle.getSerializable("header");
        String [][]data=(String[][])bundle.getSerializable("content");

        //数据内容
        data1=new ArrayList<>();
        int k=0;
        for(int i=0;i<data.length;i++){
            data1.add(data[i]);
        }

        int row=data.length;
        tableView.clearTableContents()
                .setHeader(title)
                .addContents(data1)
                .refreshTable();




    }

    private void initview() {
        tableView = findViewById(R.id.table);
        imageView=findViewById(R.id.iv_return);
        exportButton=findViewById(R.id.btn_export);
        imageView.setOnClickListener(this);
        exportButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_return:
                Intent intent=new Intent(Display.this,Input.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_export:
                exportExcel(this);
        }
    }


    /**
     * 导出excel
     * @param view
     */
    public void exportExcel(Display view) {
        file = new File(getSDPath() + "/Record");
        makeDir(file);
        ExcelUtils.initExcel(file.toString() + "/成绩表.xls", title);
        fileName = getSDPath() + "/Record/成绩表.xls";
        ExcelUtils.writeObjListToExcel(data1, fileName, this);
        Toast.makeText(this, "导出成功", Toast.LENGTH_SHORT).show();
//        ExcelUtils.writeObjListToExcel(getRecordData(), fileName, this);
    }

//    /**
//     * 将数据集合 转化成ArrayList<ArrayList<String>>
//     * @return
//     */
//    private  ArrayList<ArrayList<String>> getRecordData() {
//        recordList = new ArrayList<>();
//        for (int i = 0; i <students.size(); i++) {
//            Student student = students.get(i);
//            ArrayList<String> beanList = new ArrayList<String>();
//            beanList.add(student.id);
//            beanList.add(student.name);
//            beanList.add(student.sex);
//            beanList.add(student.age);
//            beanList.add(student.classNo);
//            beanList.add(student.math);
//            beanList.add(student.english);
//            beanList.add(student.chinese);
//            recordList.add(beanList);
//        }
//        return recordList;
//    }



    private  String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
        }
        String dir = sdDir.toString();
        return dir;
    }

    public  void makeDir(File dir) {
        if (!dir.getParentFile().exists()) {
            makeDir(dir.getParentFile());
        }
        dir.mkdir();
    }
}
