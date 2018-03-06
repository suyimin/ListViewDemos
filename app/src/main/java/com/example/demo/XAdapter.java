package com.example.demo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_two, null, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ListBean bean = mLists.get(position);
        int type = bean.getType();
        String title = bean.getStr1();
        String title2 = bean.getStr2();
        String startTime = "X";
        String startTime2 = "Y";
        if (type == X.ONE) {
            holder.ll1.setVisibility(View.VISIBLE);
            holder.llSub.setVisibility(View.GONE);
            holder.llComing.setVisibility(View.GONE);
            holder.ll2.setVisibility(View.GONE);
            holder.tvReminder1.setVisibility(View.GONE);
            holder.tvLiving.setVisibility(View.VISIBLE);
            holder.tvTitle.setText(title);
            holder.tvTime.setText(startTime);
            holder.ll11.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItem1Click(holder.ll1, position, position);
                }
            });
        } else if (type == X.TWO) {
            holder.ll1.setVisibility(View.VISIBLE);
            holder.llSub.setVisibility(View.GONE);
            holder.llComing.setVisibility(View.GONE);
            holder.ll2.setVisibility(View.VISIBLE);
            holder.tvReminder1.setVisibility(View.GONE);
            holder.tvReminder2.setVisibility(View.GONE);
            holder.tvLiving.setVisibility(View.VISIBLE);
            holder.tvTitle.setText(title);
            holder.tvTime.setText(startTime);
            holder.ll11.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItem1Click(holder.ll11, position, position);
                }
            });
            if (title2 != null) {
                holder.tvLiving2.setVisibility(View.VISIBLE);
                holder.tvTitle2.setText(title2);
                holder.tvTime2.setText(startTime2);
                holder.ll2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onItem2Click(holder.ll2, position, position);
                    }
                });
            } else {
                holder.ll2.setVisibility(View.INVISIBLE);
                holder.tvLiving2.setVisibility(View.GONE);
            }
        } else if (type == X.COMING) {
            holder.llSub.setVisibility(View.GONE);
            holder.llComing.setVisibility(View.VISIBLE);
            holder.ll1.setVisibility(View.GONE);
            holder.tvTitleComing.setText(title);
            holder.tvTimeComing.setText(startTime);
            holder.llComing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItem1Click(holder.llComing, position, position);
                }
            });
            holder.tvReminder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onRemind1Click(holder.tvReminder, position, position);
                }
            });
        } else if (type == X.BACK) {
            holder.ll1.setVisibility(View.VISIBLE);
            holder.llSub.setVisibility(View.GONE);
            holder.llComing.setVisibility(View.GONE);
            holder.ll2.setVisibility(View.VISIBLE);
            holder.tvReminder1.setVisibility(View.GONE);
            holder.tvReminder2.setVisibility(View.GONE);
            holder.tvLiving.setVisibility(View.GONE);
            holder.tvLiving2.setVisibility(View.GONE);
            holder.tvTitle.setText(title);
            holder.tvTime.setText(startTime);
            holder.ll11.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItem1Click(holder.ll11, position, position);
                }
            });
            if (title2 != null) {
                holder.tvTitle2.setText(title2);
                holder.tvTime2.setText(startTime2);
                holder.ll2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onItem2Click(holder.ll2, position, position);
                    }
                });
            } else {
                holder.ll2.setVisibility(View.INVISIBLE);
            }
        } else if (type == X.TITLE) {
            holder.llSub.setVisibility(View.VISIBLE);
            holder.ll1.setVisibility(View.GONE);
            holder.llComing.setVisibility(View.GONE);
            holder.tvSub.setText(title);
            holder.tvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onMoreClick(holder.tvMore, position, position);
                }
            });
        }
        return convertView;
    }

    public void update(List<ListBean> lists) {
        mLists = lists;
        notifyDataSetChanged();
    }

    public interface OnViewClickListener {
        /**
         * 点击左边
         */
        void onItem1Click(View view, int position, long id);

        /**
         * 点击右边
         */
        void onItem2Click(View view, int position, long id);

        /**
         * 点击预约按钮响应
         */
        void onRemind1Click(View view, int position, long id);

        /**
         * 点击预约按钮响应
         */
        void onRemind2Click(View view, int position, long id);

        /**
         * 点击更多
         */
        void onMoreClick(View view, int position, long id);
    }

    static class ViewHolder {

        LinearLayout llSub;
        TextView tvSub;
        TextView tvMore;

        LinearLayout ll1;
        LinearLayout ll11;
        XImageView imgView;
        TextView tvLiving;
        TextView tvTitle;
        TextView tvTime;
        TextView tvReminder1;
        LinearLayout ll2;
        XImageView imgView2;
        TextView tvLiving2;
        TextView tvTitle2;
        TextView tvTime2;
        TextView tvReminder2;

        LinearLayout llComing;
        XImageView imgViewComing;
        TextView tvTitleComing;
        TextView tvTimeComing;
        TextView tvReminder;

        ViewHolder(View view) {
            llSub = view.findViewById(R.id.llSub);
            tvSub = view.findViewById(R.id.tvSub);
            tvMore = view.findViewById(R.id.tvMore);

            ll1 = view.findViewById(R.id.ll1);
            ll11 = view.findViewById(R.id.ll11);
            imgView = view.findViewById(R.id.imgView);
            tvLiving = view.findViewById(R.id.tvLiving);
            tvTitle = view.findViewById(R.id.tvTitle);
            tvTime = view.findViewById(R.id.tvTime);
            tvReminder1 = view.findViewById(R.id.tvReminder1);
            ll2 = view.findViewById(R.id.ll2);
            imgView2 = view.findViewById(R.id.imgView2);
            tvLiving2 = view.findViewById(R.id.tvLiving2);
            tvTitle2 = view.findViewById(R.id.tvTitle2);
            tvTime2 = view.findViewById(R.id.tvTime2);
            tvReminder2 = view.findViewById(R.id.tvReminder2);

            llComing = view.findViewById(R.id.llComing);
            imgViewComing = view.findViewById(R.id.imgViewComing);
            tvTitleComing = view.findViewById(R.id.tvTitleComing);
            tvTimeComing = view.findViewById(R.id.tvTimeComing);
            tvReminder = view.findViewById(R.id.tvReminder);
        }
    }
}
