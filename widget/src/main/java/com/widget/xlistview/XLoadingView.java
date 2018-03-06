package com.widget.xlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.widget.R;

public class XLoadingView extends RelativeLayout {

    private ImageView mImgRedPoint;
    private ImageView mImgYellowPoint;
    private ImageView mImgBluePoint;
    private Animation animationLeft;
    private Animation animationMiddle;
    private Animation animationRight;

    /**
     * 循环标记，标识动画结束了，是否重新播放(一般默认会重新播放，但通过cancel方法取消掉后，不会重新播放)
     */
    private boolean loopFlag;

    private Context mContext;

    public XLoadingView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public XLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView() {
        View view = View.inflate(mContext, R.layout.we_loading_anim_view_layout, this);
        mImgRedPoint = (ImageView) view.findViewById(R.id.iv_red);
        mImgYellowPoint = (ImageView) view.findViewById(R.id.iv_yellow);
        mImgBluePoint = (ImageView) view.findViewById(R.id.iv_blue);

        animationLeft = AnimationUtils.loadAnimation(mContext, R.anim.loading_anim_left_point);
        animationMiddle = AnimationUtils.loadAnimation(mContext, R.anim.loading_anim_middle_point);
        animationRight = AnimationUtils.loadAnimation(mContext, R.anim.loading_anim_right_point);

        animationLeft.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (loopFlag) {
                    mImgRedPoint.startAnimation(animationLeft);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animationMiddle.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (loopFlag) {
                    mImgYellowPoint.startAnimation(animationMiddle);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animationRight.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (loopFlag) {
                    mImgBluePoint.startAnimation(animationRight);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    public void startLoadingAnim() {
        loopFlag = true;
        mImgRedPoint.startAnimation(animationLeft);
        mImgYellowPoint.startAnimation(animationMiddle);
        mImgBluePoint.startAnimation(animationRight);
    }

    public void cancelLodingAnim() {
        loopFlag = false;
        mImgRedPoint.clearAnimation();
        mImgYellowPoint.clearAnimation();
        mImgBluePoint.clearAnimation();
    }

    @Override
    protected void onDetachedFromWindow() {
        cancelLodingAnim();
        super.onDetachedFromWindow();
    }
}
