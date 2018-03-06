package com.example.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.widget.xlistview.XListView;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

    private XListView listView;
    private String[] demoNames;
    private String[] demoDirects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);
        demoNames = getResources().getStringArray(R.array.demoNames);
        demoDirects = getResources().getStringArray(R.array.demoDirects);
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, demoNames));
        listView.setOnItemClickListener(this);
        listView.setPullLoadEnable(false);
        listView.setPullRefreshEnable(false);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int p = position - 1;
        if (demoDirects != null && demoDirects.length > p && !TextUtils.isEmpty(demoDirects[p].trim())) {
            Intent intent = new Intent();
            intent.setAction(demoDirects[p]);
            startActivity(intent);
        }
    }
}
