package com.widget.xlistview;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.widget.R;

public class XListViewFooter extends LinearLayout {
    public final static int STATE_NORMAL = 0;
    public final static int STATE_READY = 1;
    public final static int STATE_LOADING = 2;

    private Context mContext;

    private View mContentView;
    private TextView mHintView;

    private int state = STATE_NORMAL;

    public XListViewFooter(Context context) {
        super(context);
        mContext = context;
        initView(context);
    }

    public XListViewFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView(context);
    }

    private String footerLoadingStr;

    private String footerReadingStr;

    private String footerNormalStr;

    public void setState(int state) {
        this.state = state;
        mHintView.setVisibility(View.INVISIBLE);
        if (state == STATE_READY) {
            if (TextUtils.isEmpty(footerReadingStr)) {
                mHintView.setText(R.string.xlistview_footer_hint_ready);
            } else {
                mHintView.setText(footerReadingStr);
            }
        } else if (state == STATE_LOADING) {
            if (TextUtils.isEmpty(footerLoadingStr)) {
                mHintView.setText(R.string.xlistview_footer_hint_loading);
            } else {
                mHintView.setText(footerLoadingStr);
            }
        } else {
            if (TextUtils.isEmpty(footerNormalStr)) {
                mHintView.setText(R.string.xlistview_footer_hint_normal);
            } else {
                mHintView.setText(footerNormalStr);
            }
        }
        mHintView.invalidate();
        mHintView.setVisibility(View.VISIBLE);
    }

    public int getState() {
        return state;
    }

    public void setBottomMargin(int height) {
        if (height < 0) {
            return;
        }
        LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
        lp.bottomMargin = height;
        mContentView.setLayoutParams(lp);
    }

    public int getBottomMargin() {
        LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
        return lp.bottomMargin;
    }

    /**
     * normal status
     */
    public void normal() {
        mHintView.setVisibility(View.VISIBLE);
    }

    /**
     * loading status
     */
    public void loading() {
        mHintView.setVisibility(View.GONE);
    }

    /**
     * hide footer when disable pull load more
     */
    public void hide() {
        LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
        lp.height = 0;
        mContentView.setLayoutParams(lp);
    }

    /**
     * show footer
     */
    public void show() {
        LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
        lp.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, getResources().getDisplayMetrics());
        mContentView.setLayoutParams(lp);
    }

    private void initView(Context context) {
        mContext = context;
        initText();
        LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.w3_widget_xlistview_footer, null);
        addView(moreView);
        moreView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        mContentView = moreView.findViewById(R.id.xlistview_footer_content);
        mHintView = (TextView) moreView.findViewById(R.id.xlistview_footer_hint_textview);
    }

    private void initText() {
        footerLoadingStr = getResources().getString(R.string.xlistview_footer_hint_loading);
        footerReadingStr = getResources().getString(R.string.xlistview_footer_hint_ready);
        footerNormalStr = getResources().getString(R.string.xlistview_footer_hint_normal);
    }


    public String getFooterLoadingStr() {
        return footerLoadingStr;
    }

    public void setFooterLoadingStr(String footerLoadingStr) {
        this.footerLoadingStr = footerLoadingStr;
    }

    public String getFooterReadingStr() {
        return footerReadingStr;
    }

    public void setFooterReadingStr(String footerReadingStr) {
        this.footerReadingStr = footerReadingStr;
    }

    public String getFooterNormalStr() {
        return footerNormalStr;
    }

    public void setFooterNormalStr(String footerNormalStr) {
        this.footerNormalStr = footerNormalStr;
    }
}