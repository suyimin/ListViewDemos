package com.widget.xlistview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.widget.R;

import java.util.Date;

public class XListViewHeader extends LinearLayout {
    private LinearLayout mContainer;
    private XLoadingView mlodingView;
    private TextView mHintTextView;
    private int mState = STATE_NORMAL;

    public final static int STATE_NORMAL = 0;
    public final static int STATE_READY = 1;
    public final static int STATE_REFRESHING = 2;

    private Context mContext;

    // 进度条动画
    private AnimationDrawable animationDrawable;

    public XListViewHeader(Context context) {
        super(context);
        mContext = context;
        initView(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public XListViewHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView(context);
    }

    private void initView(Context context) {
        // 初始情况，设置下拉刷新view高度为0
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
        mContainer = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.w3_widget_xlistview_header, null);
        addView(mContainer, lp);
        setGravity(Gravity.BOTTOM);

        mlodingView = (XLoadingView) findViewById(R.id.xlv_loading_img);
        mHintTextView = (TextView) findViewById(R.id.tv_init_loading_text);
    }

    public void setState(int state) {
        if (state == mState)
            return;

        if (state == STATE_REFRESHING) { // 显示进度
            mlodingView.startLoadingAnim();
        } else { // 显示箭头图片
            mlodingView.cancelLodingAnim();
        }

        switch (state) {
            case STATE_NORMAL:
//			mHintTextView.setText(getHintText());
                if (mState == STATE_READY) {

                }
                if (mState == STATE_REFRESHING) {

                }

                break;
            case STATE_READY:
                if (mState != STATE_READY) {
//				mHintTextView.setText(getHintText());
                }
                break;
            case STATE_REFRESHING:

                break;
            default:
        }

        mState = state;
    }

    public void setVisiableHeight(int height) {
        if (height < 0)
            height = 0;
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }

    public int getVisiableHeight() {
        return mContainer.getHeight();
    }

    public LinearLayout getContentView() {
        return mContainer;
    }

    public TextView getHintTextView() {
        return mHintTextView;
    }

    public void updateHintText(long lastUpdateTime) {
        String timeStr = getUpdateStr(lastUpdateTime);
        if (TextUtils.isEmpty(timeStr)) {
            if (mHintTextView != null) {
                mHintTextView.setVisibility(GONE);
            }
        } else {
            if (mHintTextView != null) {
                mHintTextView.setVisibility(VISIBLE);
                mHintTextView.setText(timeStr);
            }
        }
    }


    public String getUpdateStr(long lastUpdateTime) {
        long timeDifference = System.currentTimeMillis() - lastUpdateTime;
        String updateStr = "";
        if (lastUpdateTime == 0) {
            return updateStr;
        } else if (timeDifference < 60 * 1000) {
            updateStr = getResources().getString(R.string.xlistview_update_time_just_now);
        } else if (timeDifference >= 60 * 1000 && timeDifference < 60 * 60 * 1000) {
            int minute = (int) (timeDifference / (60 * 1000));
            updateStr = String.format(getResources().getString(R.string.xlistview_update_time_minute_ago), String.valueOf(minute));
        } else if (timeDifference >= 60 * 60 * 1000 && timeDifference < 60 * 60 * 24 * 1000) {
            int hour = (int) (timeDifference / (60 * 60 * 1000));
            if (hour > 1) {
                updateStr = String.format(getResources().getString(R.string.xlistview_update_time_hours_ago), String.valueOf(hour));
            } else {
                updateStr = String.format(getResources().getString(R.string.xlistview_update_time_hour_ago), String.valueOf(hour));
            }
        } else if (timeDifference > 60 * 60 * 24 * 1000) {
            Date date = new Date();
            date.setTime(lastUpdateTime);
            updateStr = String.format(getResources().getString(R.string.xlistview_update_time_date), date.getMonth() + 1, date.getDate());
        }
        return updateStr;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void setBackground(Drawable background) {
        mContainer.setBackground(background);
    }

    @Override
    public void setBackgroundResource(int resid) {
        mContainer.setBackgroundResource(resid);
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
        mContainer.setBackgroundDrawable(background);
    }
}
