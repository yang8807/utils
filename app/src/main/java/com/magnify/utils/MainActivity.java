package com.magnify.utils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.magnify.yutils.data.SaveTool;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    public static String FRISTCOMING = "frist_coming";
    public static String INTCOMING = "intcoming";
    public static String LONGCOMING = "longcoming";
    public static String USER = "user";
    public static String USERLIST = "user_list";
    private ArrayList<People> userList = new ArrayList<>();
    private SaveTool saveTool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        saveTool = new SaveTool(this);
        for (int i = 0; i < 10; i++) {
            userList.add(new People("校长" + i, 17 + i, "小章" + i, "男"));
        }

        saveTool.save(FRISTCOMING, "")
                .save(INTCOMING, 478874)
                .save(LONGCOMING, 393)
                .save(USER, new People("校长", 17, "小章", "男"))
                .save(USERLIST, userList);


        boolean value = saveTool.getBoolean(FRISTCOMING);
        int invalue = saveTool.getInt(INTCOMING);
        long longvalue = saveTool.getLong(LONGCOMING);

        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<People> peoples = saveTool.getValue(USERLIST, new TypeToken<ArrayList<People>>() {
        });
        for (int i = 0; i < peoples.size(); i++) {
            stringBuilder.append(peoples.get(i).toString());
        }
        ((TextView) findViewById(R.id.text)).setText(value + ":" + invalue + ":" + longvalue + "\n" + saveTool.getValue(USER, new TypeToken<People>() {
        }).toString() + stringBuilder.toString());
    }
}
