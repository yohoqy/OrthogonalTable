package com.example.orthogonaltable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.example.orthogonaltable.bean.Factors;
import com.example.orthogonaltable.bean.UniFormTable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class Input extends AppCompatActivity {
    private ArrayList<UniFormTable> UniFormTableList=new ArrayList<UniFormTable>();
    private List<Factors> Myfactors = new ArrayList<Factors>();
    private UniFormTable TestCaseTable=new UniFormTable();
    private EditText et;
    private Button button;
    private LinearLayout ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        LoadTable();
        initView();



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取用户输入
                String s = et.getText().toString();
                boolean sign=false;
                if (s.length()!=0) {
                    String content[] = et.getText().toString().split("\n");
                    for (int i = 0; i < content.length; i++) {
                        int index1 = content[i].indexOf(':');
                        if(index1!=-1) {
                            Factors f = new Factors();
                            f.setmFactorName(content[i].substring(0, index1));
                            int length = content[i].length();
                            String sss = content[i].substring(index1 + 1, length).trim();

                            //用","分隔各个水平，注意均为英文符号","
                            int index2=sss.indexOf("，");
                            if(index2==-1) {
                                int temp = sss.split(",").length;
                                for (int j = 0; j < temp; j++) {
                                    f.getmLevels().add(sss.split(",")[j].toString());
                                }
                                Myfactors.add(f);
                                sign=true;
                            }
                        }
                    }

                    if(sign==true) {
                        boolean findresult = SearchTable(Myfactors);
                        if (findresult == true) {
                            int len1 = Myfactors.size();
                            String[] title = new String[len1];
                            for (int i = 0; i < len1; i++) {
                                title[i] = Myfactors.get(i).getmFactorName();
                                System.out.print(title[i]);
                            }
                            System.out.println();

                            int len2 = TestCaseTable.getmRuns();
                            int len3 = Myfactors.size();
                            String[][] data = new String[len2][len3];

                            for (int i = 0; i < len2; i++) {
                                for (int j = 0; j < len3; j++) {
                                    data[i][j] = TestCaseTable.getmTableMatrix()[i][j];
                                    System.out.print(data[i][j]);
                                }
                                System.out.println();
                            }

                            Intent intent = new Intent(Input.this, Display.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("header", title);
                            bundle.putSerializable("content", data);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                        }
                    }else{
                        Toast toast=Toast.makeText(Input.this,"请输入英文格式标点符号",Toast.LENGTH_SHORT    );
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                }else{
                    Toast toast=Toast.makeText(Input.this,"你还未输入",Toast.LENGTH_SHORT    );
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });
    }

    /**
     * 加载正交表
     */
    private void LoadTable() {
        try{
            InputStream inputStream=getResources().openRawResource(R.raw.table);
            InputStreamReader isr=null;
            isr=new InputStreamReader(inputStream,"UTF-8");
            BufferedReader br=new BufferedReader(isr);
            String line="";
            ArrayList<String> readList=new ArrayList<String>();
            while((line=br.readLine())!=null){
                readList.add(line);
            }


            for(int k=0;k<readList.size();k++){
                line=readList.get(k);
                if(line.contains("^")){
                    //创建正交矩阵表对象
                    UniFormTable TableFactors=new UniFormTable();
                    int index1=line.indexOf('=');
                    int length=line.length();

                    //行数
                    TableFactors.setmRuns(Integer.parseInt(line.substring(index1+1,length).trim()));

                    //因素
                    String s=line.substring(0,index1-1).trim();
                    TableFactors.setmFactorLevelCount(s.split(" ").length);
                    TableFactors.setmFactors(new int[TableFactors.getmFactorLevelCount()]);
                    TableFactors.setmLevels(new int[TableFactors.getmFactorLevelCount()]);


                    for(int j=0;j<TableFactors.getmFactorLevelCount();j++){
                        //因素
                        TableFactors.getmFactors()[j]=Integer.parseInt(s.split(" ")[j].split("\\^")[1].toString());
                        //水平
                        TableFactors.getmLevels()[j]=Integer.parseInt(s.split(" ")[j].split("\\^")[0].toString());
//                        System.out.println(TableFactors.getmFactors()[j]+" "+TableFactors.getmLevels()[j]);
                    }

                    //总因素数
                    int TotalFactorCount=0;
                    for(int i=0;i<TableFactors.getmFactorLevelCount();i++){
                        TotalFactorCount=TotalFactorCount+TableFactors.getmFactors()[i];
                    }


                    TableFactors.setmTableMatrixString(new String[TableFactors.getmRuns()]);
                    //构造正交矩阵表，行数=实验次数，列数=总因素数也就是变量的个数
                    TableFactors.setmTableMatrix(new String[TableFactors.getmRuns()][TotalFactorCount]);

                    //从"^"的下一行开始
                    String MatrixString="";
                    int indexs=0;//正交表矩阵行数
                    for(int ll=k+1;ll<readList.size();ll++){
                        //判断正交矩阵是否结束
                        if(readList.get(ll).length()==0){
                            break;
                        }

                        int start=0;
                        int end=0;
                        int eee=0;
                        //有几组因素
                        for(int bb=0;bb<TableFactors.getmFactorLevelCount();bb++){
                            //每组因素数的个数
                            for(int cc=0;cc<TableFactors.getmFactors()[bb];cc++){
//                                System.out.println(TableFactors.getmFactors()[bb]);
                                //该组因素水平数的占位长度
                                int levelwidth=0;
                                //这一组因素数有多少种取值
                                if(TableFactors.getmLevels()[bb]<=10){
                                    levelwidth=1;
                                }
                                else if(TableFactors.getmLevels()[bb]<=100){
                                    levelwidth=2;
                                }
                                //逐行逐个添加
                                end=end+levelwidth;
                                TableFactors.getmTableMatrix()[indexs][eee]=readList.get(ll).substring(start,end);
//                                System.out.print(TableFactors.getmTableMatrix()[indexs][eee]);
//                                System.out.print(String.valueOf(indexs)+String.valueOf(eee)+" ");
                                eee++;
                                start=start +levelwidth;
                            }
                        }
//                        System.out.println();

                        //按行存入正交表矩阵中
                        TableFactors.getmTableMatrixString()[indexs]=readList.get(ll);
                        indexs++;
                    }
                    //按每种正交表存入列表
                    UniFormTableList.add(TableFactors);
//                    System.out.println(UniFormTableList.get(0).getmFactors());
                }
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 查找正交表，分为标准正交表和混合正交表
     * @param MyFactors
     * 标准正交表：
     * 情况1：水平数相等，查找正交表中水平数等于指定水平数，因素数等于指定的因素数的正交表（支持查询）
     * 情况2：水平数相等，但是在正交表中找不到因素数等于指定因素数的标准正交表（支持查询）
     * 混合正交表：
     * 情况1：正交表文件中能够找到刚好合适的正交表（支持查询）
     * 情况2：正交表文件中无法能够找到刚好合适的正交表（暂不支持查询）
     */
    private boolean SearchTable(List<Factors> MyFactors){
        //看标准正交表是否适用
        boolean isCase1=true;
        int count=MyFactors.get(0).getmLevels().size();
        for(int i=1;i<MyFactors.size();i++){
            if(count!=MyFactors.get(i).getmLevels().size()){
                isCase1=false;
                break;
            }
        }

        //是标准在正交表
        if(isCase1==true){
            //情况1：水平数相等，查找正交表中水平数等于指定水平数，因素数等于指定的因素数的正交表，看是否存在
//            测试用例：
//            姓名:填,不填
//            身份证号:填,不填
//            手机号码:填,不填

            boolean found=false;
            for(int i=0;i<UniFormTableList.size();i++){
                if(UniFormTableList.get(i).getmFactorLevelCount()==1){
                    //判断因素个数是否相等
                    if(UniFormTableList.get(i).getmFactors()[0]==MyFactors.size()){
                        if(UniFormTableList.get(i).getmLevels()[0]==MyFactors.get(0).getmLevels().size()){
                            //找到因素数、水平数相符的正交表
                            for(int aa=0;aa<MyFactors.size();aa++){
                                System.out.print(MyFactors.get(aa).getmFactorName()+" ");
                            }
                            System.out.println();
//                            UniFormTable TestCaseTable=new UniFormTable();
                            TestCaseTable=UniFormTableList.get(i);

                            //行，实验的次数
                            for(int aa=0;aa<TestCaseTable.getmRuns();aa++){
                                //列:因素的个数，代表有多少列
                                for(int bb=0;bb<MyFactors.size();bb++){
                                    int index=Integer.parseInt(TestCaseTable.getmTableMatrix()[aa][bb]);
                                    TestCaseTable.getmTableMatrix()[aa][bb]=MyFactors.get(bb).getmLevels().get(index);
                                }
                            }
                            for(int aa=0;aa<TestCaseTable.getmRuns();aa++){
                                for(int bb=0;bb<MyFactors.size();bb++){
                                    System.out.print(TestCaseTable.getmTableMatrix()[aa][bb]+" ");
                                }
                                System.out.println();
                            }
                            found=true;
                            break;
                        }
                    }
                }
            }


            //如果未找到
            if(found==false){
                //情况2：水平数相等，但是在正交表中找不到因素数等于指定因素数的标准正交表，则查找大于等于指定因素数且最接近的标准正交表
//                测试用例：
//                操作系统:2000,XP,2003
//                浏览器:IE6.0,IE7.0,TT
//                杀毒软件:卡巴,金山,诺顿

                List<UniFormTable> FitTableList=new ArrayList<UniFormTable>();
                for(int i=0;i<UniFormTableList.size();i++){
                    if(UniFormTableList.get(i).getmFactorLevelCount()==1){
                        if(UniFormTableList.get(i).getmLevels()[0]==MyFactors.get(0).getmLevels().size()){
                            if(UniFormTableList.get(i).getmFactors()[0]>=MyFactors.size()){
                                found=true;
                                FitTableList.add(UniFormTableList.get(i));
                            }
                        }
                    }
                }

                //如果找到
                if(found==true){
                    int currfitindex=0;
                    for(int i=0;i<FitTableList.size();i++){
                        if(FitTableList.get(i).getmFactors()[0]<FitTableList.get(currfitindex).getmFactors()[0]){
                            currfitindex=i;
                        }
                    }

                    for(int aa=0;aa<MyFactors.size();aa++){
                        System.out.print(MyFactors.get(aa).getmFactorName()+" ");
                    }
                    System.out.println();
//                    UniFormTable TestCaseTable=new UniFormTable();
                    TestCaseTable=FitTableList.get(currfitindex);

                    for(int aa=0;aa<TestCaseTable.getmRuns();aa++){
                        //列:因素的个数，代表有多少列
                        for(int bb=0;bb<MyFactors.size();bb++){

                            int index=Integer.parseInt(TestCaseTable.getmTableMatrix()[aa][bb]);
                            TestCaseTable.getmTableMatrix()[aa][bb]=MyFactors.get(bb).getmLevels().get(index);
                        }
                    }
                    for(int aa=0;aa<TestCaseTable.getmRuns();aa++){
                        for(int bb=0;bb<MyFactors.size();bb++){
                            System.out.print(TestCaseTable.getmTableMatrix()[aa][bb]+" ");
                        }
                        System.out.println();
                    }
                }
            }
            return true;
        }
        else{
//            情况3：混合水平正交表
//            各因素的水平数不相同
//            测试用例：
//            A:A1,A2
//            B:B1,B2
//            C:C1,C2
//            D:D1,D2
//            E:E1,E2,E3,E4

            List<String> FactorLevelPair=new ArrayList<String>();
            int n=0;//因素数
            int m=0;//水平数
            for(int i=0;i<MyFactors.size();i++){
                m=MyFactors.get(i).getmLevels().size();
                n=0;
                for(int j=0;j<MyFactors.size();j++){
                    if(MyFactors.get(i).getmLevels().size()==MyFactors.get(j).getmLevels().size()){
                        //有n个水平数等于m的因素
                        n++;
                    }
                }

                String ss=String.valueOf(m)+"^"+String.valueOf(n);
                if(FactorLevelPair.indexOf(ss)==-1){
                    FactorLevelPair.add(ss);
                }
            }

            boolean find=false;
            //先看能否查找到刚好合适的混合水平正交表
            for(int i=0;i<UniFormTableList.size();i++){
                if(UniFormTableList.get(i).getmFactorLevelCount()==FactorLevelPair.size()){
                    int fitcount=0;
                    String restr1="";
                    String restr2="";
                    for(int j=0;j<UniFormTableList.get(i).getmFactorLevelCount();j++){
                        //重组
                        restr1=restr1+UniFormTableList.get(i).getmLevels()[j]+"^"+UniFormTableList.get(i).getmFactors()[j]+" ";
                        restr2=restr2+FactorLevelPair.get(j)+" ";
                    }

                    if(restr1.length()==restr2.length()){
                        for(int k=0;k<FactorLevelPair.size();k++){
                            if(restr1.contains(FactorLevelPair.get(k))){
                                fitcount++;
                            }
                        }
                    }

                    if(fitcount==FactorLevelPair.size()){
                        find=true;
                        for(int aa=0;aa<MyFactors.size();aa++){
                            System.out.print(MyFactors.get(aa).getmFactorName()+" ");
                        }
                        System.out.println();
//                        UniFormTable TestCaseTable=new UniFormTable();
                        TestCaseTable=UniFormTableList.get(i);

                        //行，实验的次数
                        for(int aa=0;aa<TestCaseTable.getmRuns();aa++){
                            //列:因素的个数，代表有多少列
                            for(int bb=0;bb<MyFactors.size();bb++){
                                int index=Integer.parseInt(TestCaseTable.getmTableMatrix()[aa][bb]);
                                TestCaseTable.getmTableMatrix()[aa][bb]=MyFactors.get(bb).getmLevels().get(index);
                            }
                        }
                        for(int aa=0;aa<TestCaseTable.getmRuns();aa++){
                            for(int bb=0;bb<MyFactors.size();bb++){
                                System.out.print(TestCaseTable.getmTableMatrix()[aa][bb]+" ");
                            }
                            System.out.println();
                        }
                        return true;
//                        break;
                    }
                }
            }

            //找不到刚好合适的混合水平正交表，则查找表中因素之和大于指定因素之和，且每一个不同的指定水平数都覆盖
            if(find==false){
                System.out.println("找不到");
                Toast toast=Toast.makeText(Input.this,"暂不支持该类型",Toast.LENGTH_SHORT    );
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
            return false;
        }
    }


    /**
     * 控件实例化
     */
    private void initView(){
        et=findViewById(R.id.et_input);
        button=findViewById(R.id.confirm);
        ll=findViewById(R.id.ll_main);
    }


    /**
     * 给布局加触摸监听，当点击EditText之外的地方时
     *hint自动隐藏
     */
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            ll.setFocusable(true);
            ll.setFocusableInTouchMode(true);
            ll.requestFocus();//布局获得焦点
            return false;
        }
    };
}
