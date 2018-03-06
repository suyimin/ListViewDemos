package com.example.demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class XMAdapter extends BaseAdapter {

    private Context mContext;
    private OnViewClickListener mListener;
    private List<ListBean> mLists = new ArrayList<>();

    public XMAdapter(Context context, OnViewClickListener listener) {
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
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        ListBean bean = mLists.get(position);
        switch (bean.getType()) {
            case X.TITLE:
                return X.TITLE;
            case X.ONE:
            case X.TWO:
            case X.BACK:
                return X.TWO;
            case X.COMING:
                return X.COMING;
            default:
                return X.TWO;
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        int viewType = getItemViewType(position);
        if (convertView == null) {
            if (viewType == X.TITLE) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_title, null, false);
            } else if (viewType == X.TWO) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_two, null, false);
            }
            if (viewType == X.COMING) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_coming, null, false);
            }
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

        if (viewType == X.TITLE) {
            holder.llSub = convertView.findViewById(R.id.llSub);
            holder.tvSub = convertView.findViewById(R.id.tvSub);
            holder.tvMore = convertView.findViewById(R.id.tvMore);
            holder.tvSub.setText(title);
            holder.tvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onMoreClick(holder.tvMore, position, position);
                }
            });
        } else if (viewType == X.TWO) {
            holder.ll1 = convertView.findViewById(R.id.ll1);
            holder.ll11 = convertView.findViewById(R.id.ll11);
            holder.imgView = convertView.findViewById(R.id.imgView);
            holder.tvLiving = convertView.findViewById(R.id.tvLiving);
            holder.tvTitle = convertView.findViewById(R.id.tvTitle);
            holder.tvTime = convertView.findViewById(R.id.tvTime);
            holder.tvReminder1 = convertView.findViewById(R.id.tvReminder1);
            holder.ll2 = convertView.findViewById(R.id.ll2);
            holder.imgView2 = convertView.findViewById(R.id.imgView2);
            holder.tvLiving2 = convertView.findViewById(R.id.tvLiving2);
            holder.tvTitle2 = convertView.findViewById(R.id.tvTitle2);
            holder.tvTime2 = convertView.findViewById(R.id.tvTime2);
            holder.tvReminder2 = convertView.findViewById(R.id.tvReminder2);

            if (type == X.ONE) {
                holder.ll1.setVisibility(View.VISIBLE);
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
            } else if (type == X.BACK) {
                holder.ll1.setVisibility(View.VISIBLE);
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
            }
        } else if (viewType == X.COMING) {
            holder.llComing = convertView.findViewById(R.id.llComing);
            holder.imgViewComing = convertView.findViewById(R.id.imgViewComing);
            holder.tvTitleComing = convertView.findViewById(R.id.tvTitleComing);
            holder.tvTimeComing = convertView.findViewById(R.id.tvTimeComing);
            holder.tvReminder = convertView.findViewById(R.id.tvReminder);
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

        public ViewHolder(View view) {
        }
    }
}
