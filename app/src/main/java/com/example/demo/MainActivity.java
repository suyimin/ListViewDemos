package com.example.demo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.widget.xlistview.XListView;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends Activity implements XAdapter.OnViewClickListener,
        XListView.IXListViewListener, AdapterView.OnItemClickListener {

    private XListView listView;
    private TextView tvEmpty;
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
        tvEmpty = findViewById(R.id.tvEmpty);
        getData(false);
    }

    private void dealDate() {
        tvEmpty.setVisibility(View.GONE);
        adapter.update(mLists);
        listView.stopLoadMore();
        listView.stopRefresh();
    }

    private void getData(final boolean isRefresh) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isRefresh) {
                    mLists.clear();
                }
                for (int i = 0; i < NUM; i++) {
                    ListBean bean = new ListBean();
                    String s1 = "S" + i;
                    String s2 = "X" + i;
                    bean.setStr1(s1);
                    bean.setStr2(s2);
                    bean.setType(i % 5);
                    mLists.add(bean);
                    dealDate();
                }
            }
        }, 2000);
    }

    @Override
    public void onItem1Click(View view, int position, long id) {
        Toast.makeText(this, "onItem1Click,position-->" + position + ",id-->" + id + ",str1-->" + mLists.get(position).getStr1(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItem2Click(View view, int position, long id) {
        Toast.makeText(this, "onItem2Click,position-->" + position + ",id-->" + id + ",str2-->" + mLists.get(position).getStr1(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRemind1Click(View view, int position, long id) {
        Toast.makeText(this, "onRemind1Click,position-->" + position + ",id-->" + id + ",str1-->" + mLists.get(position).getStr1(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRemind2Click(View view, int position, long id) {
        Toast.makeText(this, "onRemind2Click,position-->" + position + ",id-->" + id + ",str1-->" + mLists.get(position).getStr2(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMoreClick(View view, int position, long id) {
        Toast.makeText(this, "onMoreClick,position-->" + position + ",id-->" + id + ",str1-->" + mLists.get(position).getStr1(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
        getData(true);
    }

    @Override
    public void onLoadMore() {
        getData(false);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
