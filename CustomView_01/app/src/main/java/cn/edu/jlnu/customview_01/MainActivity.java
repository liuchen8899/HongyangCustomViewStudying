package cn.edu.jlnu.customview_01;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import cn.edu.jlnu.customview_01.view.CustomTitleView1;

public class MainActivity extends AppCompatActivity {

    private CustomTitleView1 ctv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ctv1= (CustomTitleView1) findViewById(R.id.ctv1);
        ctv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String randomText=getRandomText();
                ctv1.setTitleText(randomText);
                ctv1.invalidate();
            }
        });
    }
    private String getRandomText(){
        Random random=new Random();
        Set<Integer> set=new HashSet<Integer>();
        while(set.size()<4){
            int randomInt=random.nextInt(10);
            set.add(randomInt);
        }
        StringBuilder sb=new StringBuilder();
        for(Integer i:set){
            sb.append(""+i);
        }
        return sb.toString();
    }
}
