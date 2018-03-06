package com.example.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.widget.xlistview.XListView;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends Activity implements XAdapter.OnViewClickListener,
        XListView.IXListViewListener, AdapterView.OnItemClickListener {

    private XListView listView;
    private XAdapter adapter;
    private List<ListBean> mLists = new LinkedList<>();
    private final int NUM = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);
        adapter = new XAdapter(this, this);
        listView.setAdapter(adapter);
        listView.setXListViewListener(this);
        listView.setOnItemClickListener(this);
        listView.setPullLoadEnable(true);
        getData();
        dealDate();
    }

    private void dealDate() {
        adapter.update(mLists);
    }

    private void getData() {
        for (int i = 0; i < NUM; i++) {
            ListBean bean = new ListBean();
            String s1 = "S" + i;
            String s2 = "X" + i;
            bean.setStr1(s1);
            bean.setStr2(s2);
            bean.setType(i % 5);
            mLists.add(bean);
        }
    }

    @Override
    public void onItem1Click(View view, int position, long id) {

    }

    @Override
    public void onItem2Click(View view, int position, long id) {

    }

    @Override
    public void onRemind1Click(View view, int position, long id) {

    }

    @Override
    public void onRemind2Click(View view, int position, long id) {

    }

    @Override
    public void onMoreClick(View view, int position, long id) {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
