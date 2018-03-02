package com.example.demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

class XAdapter extends BaseAdapter {

    private Context mContext;
    private OnViewClickListener mListener;
    private List<ListBean> mLists = new ArrayList<>();

    public XAdapter(Context context, OnViewClickListener listener) {
        mContext = context;
        mListener = listener;
    }

    @Override
    public int getCount() {
        return mLists.size();
    }

    @Override
    public Object getItem(int position) {
        return mLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return LayoutInflater.from(mContext).inflate(R.layout.item_two, null, false);
    }

    public void update(List<ListBean> lists) {
        mLists = lists;
        notifyDataSetChanged();
    }

    public interface OnViewClickListener {
        void onItem1Click(View view, int position, long id);

        void onItem2Click(View view, int position, long id);

        void onMoreClick(View view, int position, long id);
    }
}
