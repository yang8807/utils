package com.magnify.utils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.magnify.yutils.PreferencesUtil;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private TextView text;
    public static String FRISTCOMING = "frist_coming";
    public static String INTCOMING = "intcoming";
    public static String LONGCOMING = "longcoming";
    public static String USER = "user";
    public static String USERLIST = "user_list";
    private ArrayList<People> userList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = (TextView) findViewById(R.id.text);
        for (int i=0;i<10;i++){
            userList.add(new People("校长"+i, 17+i, "小章"+i, "男"));
        }

        PreferencesUtil.save(this, FRISTCOMING, true);
        PreferencesUtil.save(this, INTCOMING, 1);
        PreferencesUtil.save(this, LONGCOMING, 1.5f);
        PreferencesUtil.save(this, USER, new People("校长", 17, "小章", "男"));
        PreferencesUtil.save(this, USERLIST, userList);


        boolean value = PreferencesUtil.getBoolean(this, FRISTCOMING);
        int invalue = PreferencesUtil.getInt(this, INTCOMING);
        Float longvalue = PreferencesUtil.getFloat(this, LONGCOMING);

        StringBuilder stringBuilder=new StringBuilder();
        ArrayList<People> peoples=PreferencesUtil.getValue(this,USERLIST,new TypeToken<ArrayList<People>>(){});
        for (int i = 0; i < peoples.size(); i++) {
            stringBuilder.append(peoples.get(i).toString());
        }
        text.setText(value + ":" + invalue + ":" + longvalue+"\n"+PreferencesUtil.getValue(this,USER,new TypeToken<People>(){}).toString()+stringBuilder.toString());
    }
}
