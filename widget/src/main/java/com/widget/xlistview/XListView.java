package com.widget.xlistview;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Scroller;
import android.widget.TextView;

import com.huawei.it.w3m.widget.R;

public class XListView extends ListView implements OnScrollListener {

	private Context mContext;

	private float mLastY = -1; // save event y
	private Scroller mScroller; // used for scroll back
	private OnScrollListener mScrollListener; // user's scroll listener

	// the interface to trigger refresh and load more.
	private IXListViewListener mListViewListener;

	// -- header view
	private XListViewHeader mHeaderView;
	// header view content, use it to calculate the Header's height. And hide it
	// when disable pull refresh.
	private LinearLayout mHeaderViewContent;
	private TextView mHeaderTimeView;
	private int mHeaderViewHeight; // header view's height
	private boolean mEnablePullRefresh = true;
	private boolean mPullRefreshing = false; // is refreashing.

	// -- footer view
	private XListViewFooter mFooterView;
	private boolean mEnablePullLoad;
	private boolean mPullLoading;
	private boolean mIsFooterReady = false;

	// total list items, used to detect is at the bottom of listview.
	private int mTotalItemCount;

	// for mScroller, scroll back from header or footer.
	private int mScrollBack;
	private final static int SCROLLBACK_HEADER = 0;
	private final static int SCROLLBACK_FOOTER = 1;

	private final static int SCROLL_DURATION = 400; // scroll back duration
	private final static int PULL_LOAD_MORE_DELTA = 50; // when pull up >= 50px
														// at bottom, trigger
	// qwx230848 原先的是1.8f，加大这个参数可以减少下拉过长 // load more.
	private final static float OFFSET_RADIO = 2.6f; // support iOS like pull
													// feature.

	private String mHintText;


	private IPressDownListener pressDownListener;

	/**
	 * @param context
	 */
	public XListView(Context context) {
		super(context);
		mContext = context;
		initWithContext(context);
	}

	public XListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initWithContext(context);
	}

	public XListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		initWithContext(context);
	}

	private void initWithContext(Context context) {
		mScroller = new Scroller(context, new DecelerateInterpolator());
		// XListView need the scroll event, and it will dispatch the event to
		// user's listener (as a proxy).
		super.setOnScrollListener(this);

		// init header view
		mHeaderView = new XListViewHeader(context);
		mHeaderViewContent = (LinearLayout) mHeaderView.findViewById(R.id.w3_xlistview_header_content);
		mHeaderTimeView = (TextView) mHeaderView.findViewById(R.id.tv_init_loading_text);
		addHeaderView(mHeaderView);

		// init footer view
		mFooterView = new XListViewFooter(context);

		// init header height
		mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				measureView(mHeaderViewContent);
				mHeaderViewHeight = mHeaderViewContent.getMeasuredHeight();
				getViewTreeObserver().removeGlobalOnLayoutListener(this);
			}
		});
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		// make sure XListViewFooter is the last footer view, and only add once.
		if (mIsFooterReady == false) {
			mIsFooterReady = true;
			addFooterView(mFooterView);
		}
		super.setAdapter(adapter);
	}

	/**
	 * enable or disable pull down refresh feature.
	 * 
	 * @param enable
	 */
	public void setPullRefreshEnable(boolean enable) {
		mEnablePullRefresh = enable;
		if (!mEnablePullRefresh) { // disable, hide the content
			mHeaderViewContent.setVisibility(View.INVISIBLE);
		} else {
			mHeaderViewContent.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * enable or disable pull up load more feature.
	 * 
	 * @param enable
	 */
	public void setPullLoadEnable(boolean enable) {
		mEnablePullLoad = enable;
		if (!mEnablePullLoad) {
			mFooterView.hide();
			mFooterView.setOnClickListener(null);
		} else {
			mPullLoading = false;
			mFooterView.show();
			mFooterView.setState(XListViewFooter.STATE_NORMAL);
			// both "pull up" and "click" will invoke load more.
			mFooterView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(mFooterView.getState() == XListViewFooter.STATE_NORMAL) {
						startLoadMore();
					}
				}
			});
		}
	}

	/**
	 * stop refresh, reset header view.
	 */
	public void stopRefresh() {
		if (mPullRefreshing == true) {
			mPullRefreshing = false;
			resetHeaderHeight();
		}
	}

	/**
	 * stop load more, reset footer view.
	 */
	public void stopLoadMore() {
		if (mPullLoading == true) {
			mPullLoading = false;
			mFooterView.setState(XListViewFooter.STATE_NORMAL);
		}
	}

	/**
	 * set last refresh time
	 * 
	 * @param time
	 */
	public void setRefreshTime(String time) {
		mHeaderTimeView.setText(time);
	}

	public void autoRefresh() {
		mLastY = -1; // reset
		// 判断是否在第一行，如果不是第一行，则不执行
		if (getFirstVisiblePosition() == 0) {
			// 判断是否可刷新和不处于刷新状态
			if (mEnablePullRefresh && mPullRefreshing != true) {
				mPullRefreshing = true;
				mScrollBack = SCROLLBACK_HEADER;
				if (mHeaderViewHeight == 0) {
					WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
					int width = windowManager.getDefaultDisplay().getWidth();
					mHeaderViewContent.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST),
							MeasureSpec.makeMeasureSpec((1 << 30) - 1, MeasureSpec.AT_MOST));
					int autoRefreshHeigtht = mHeaderViewContent.getMeasuredHeight();
					mHeaderView.setVisiableHeight(autoRefreshHeigtht);
					mScroller.startScroll(0, 0, 0, autoRefreshHeigtht, SCROLL_DURATION);
					invalidate();
				} else {
					mScroller.startScroll(0, 0, 0, mHeaderViewHeight, SCROLL_DURATION);
					invalidate();
				}
				stopLoadMore();
				mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
				if (mListViewListener != null) {
					mListViewListener.onRefresh();
				}
			}
			// resetHeaderHeight();
		}
	}

	private void invokeOnScrolling() {
		if (mScrollListener instanceof OnXScrollListener) {
			OnXScrollListener l = (OnXScrollListener) mScrollListener;
			l.onXScrolling(this);
		}
	}

	private void updateHeaderHeight(float delta) {
		mHeaderView.setVisiableHeight((int) delta + mHeaderView.getVisiableHeight());
		if (mEnablePullRefresh && !mPullRefreshing) { // 未处于刷新状态，更新箭头
			if (mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
				mHeaderView.setState(XListViewHeader.STATE_READY);
			} else {
				mHeaderView.setState(XListViewHeader.STATE_NORMAL);
			}
		}
		setSelection(0); // scroll to top each time
	}

	/**
	 * reset header view's height.
	 */
	private void resetHeaderHeight() {
		int height = mHeaderView.getVisiableHeight();
		if (height == 0) // not visible.
			return;

		int finalHeight = 0; // default: scroll back to dismiss header.
		// refreshing and header isn't shown fully. do nothing.
		if (mPullRefreshing) {
			if(height <= mHeaderViewHeight) {
				return;
			} else {
				// is refreshing, just scroll back to show all the header.
				finalHeight = mHeaderViewHeight;
			}
		}

		mScrollBack = SCROLLBACK_HEADER;
		mScroller.startScroll(0, height, 0, finalHeight - height, SCROLL_DURATION);
		// trigger computeScroll
		invalidate();
	}

	private void updateFooterHeight(float delta) {
		int height = mFooterView.getBottomMargin() + (int) delta;
		if (mEnablePullLoad && !mPullLoading) {
			if (height > PULL_LOAD_MORE_DELTA) { // height enough to invoke load
													// more.
				mFooterView.setState(XListViewFooter.STATE_READY);
			} else {
				mFooterView.setState(XListViewFooter.STATE_NORMAL);
			}
		}
		mFooterView.setBottomMargin(height);
		// setSelection(mTotalItemCount - 1); // scroll to bottom
	}

	private void resetFooterHeight() {
		int bottomMargin = mFooterView.getBottomMargin();
		if (bottomMargin > 0) {
			mScrollBack = SCROLLBACK_FOOTER;
			mScroller.startScroll(0, bottomMargin, 0, -bottomMargin, SCROLL_DURATION);
			invalidate();
		}
	}

	public void startLoadMore() {
		stopRefresh();
		mPullLoading = true;
		mFooterView.setState(XListViewFooter.STATE_LOADING);
		if (mListViewListener != null) {
			mListViewListener.onLoadMore();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mLastY == -1) {
			mLastY = ev.getRawY();
		}

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastY = ev.getRawY();
			if(pressDownListener != null) {
				pressDownListener.onPullDown();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			final float deltaY = ev.getRawY() - mLastY;
			mLastY = ev.getRawY();
			if (getFirstVisiblePosition() == 0 && (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)
					&& mEnablePullRefresh) {
				// the first item is showing, header has shown or pull down.
				updateHeaderHeight(deltaY / OFFSET_RADIO);
				invokeOnScrolling();
			} else if (getLastVisiblePosition() == mTotalItemCount - 1
					&& (mFooterView.getBottomMargin() > 0 || deltaY < 0) && mEnablePullLoad) {
				// last item, already pulled up or want to pull up.
				updateFooterHeight(-deltaY / OFFSET_RADIO);
			}
			break;
		default:
			if (getFirstVisiblePosition() == 0) {
				// invoke refresh
				if (mEnablePullRefresh && mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
					stopLoadMore();
					mPullRefreshing = true;
					mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
					if (mListViewListener != null) {
						mListViewListener.onRefresh();
					}
				}
				resetHeaderHeight();
			}

			if (getLastVisiblePosition() == mTotalItemCount - 1) {
				final float y = ev.getRawY() - mLastY;
				// invoke load more.
				if ((mEnablePullLoad && mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA && !mPullLoading)
						|| (mEnablePullLoad && y < 0 && getFirstVisiblePosition() == 0)) {
					startLoadMore();
				}
				resetFooterHeight();
			}
			mLastY = -1; // reset
			break;
		}
		return super.onTouchEvent(ev);
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			if (mScrollBack == SCROLLBACK_HEADER) {
				mHeaderView.setVisiableHeight(mScroller.getCurrY());
			} else {
				mFooterView.setBottomMargin(mScroller.getCurrY());
			}
			postInvalidate();
			invokeOnScrolling();
		}
		super.computeScroll();
	}

	@Override
	public void setOnScrollListener(OnScrollListener l) {
		mScrollListener = l;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (mScrollListener != null) {
			mScrollListener.onScrollStateChanged(view, scrollState);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// send to user's listener
		// if (mFooterView != null) {
		// if (totalItemCount > visibleItemCount) {
		// mFooterView.setVisibility(view.VISIBLE);
		// } else {
		// mFooterView.setVisibility(view.GONE);
		// }
		// }
		mTotalItemCount = totalItemCount;
		if (mScrollListener != null) {
			mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		}
	}

	public void setXListViewListener(IXListViewListener l) {
		mListViewListener = l;
	}

	public void setHintText(String mHintText) {
		this.mHintText = mHintText;

		if(TextUtils.isEmpty(mHintText)){
			if(mHeaderTimeView != null){
				mHeaderTimeView.setVisibility(GONE);
			}
		}else{
			if(mHeaderTimeView != null){
				mHeaderTimeView.setVisibility(VISIBLE);
				mHeaderTimeView.setText(mHintText);
			}
		}

		measureView(mHeaderViewContent);
		mHeaderViewHeight = mHeaderViewContent.getMeasuredHeight();
	}

	public void setLastUpdateTime(long lastUpdate){
		if(mHeaderView != null){
			mHeaderView.updateHintText(lastUpdate);
		}

		measureView(mHeaderViewContent);
		mHeaderViewHeight = mHeaderViewContent.getMeasuredHeight();
	}



	/**
	 * you can listen ListView.OnScrollListener or this one. it will invoke onXScrolling when header/footer scroll back.
	 */
	public interface OnXScrollListener extends OnScrollListener {
		public void onXScrolling(View view);
	}

	/**
	 * implements this interface to get refresh/load more event.
	 */
	public interface IXListViewListener {
		public void onRefresh();

		public void onLoadMore();
	}

	public interface IPressDownListener{
		void onPullDown();
	};

	/**
	 * qwx230848 该方法是在原生代码上增加的方法，返回里listview的footview
	 * 
	 * @return
	 */
	public XListViewFooter getViewFooter() {
		return mFooterView;
	}

	public XListViewHeader getViewHeader() {
		return mHeaderView;
	}

	/**
	 * 目前该方法只支持预计算宽高设置为准确值或wrap_content的情况，
	 * 不支持match_parent的情况，因为view的父view还未预计算出宽高
	 * @param v 要预计算的view
	 */

	private void measureView(View v) {

		if(v == null){
			return;
		}

		ViewGroup.LayoutParams lp = v.getLayoutParams();
		if (lp == null) {
			return;
		}
		int width;
		int height;
		if (lp.width > 0) {
			// xml文件中设置了该view的准确宽度值，例如android:layout_width="150dp"
			width = View.MeasureSpec.makeMeasureSpec(lp.width, View.MeasureSpec.EXACTLY);
		} else {
			// xml文件中使用wrap_content设定该view宽度，例如android:layout_width="wrap_content"
			width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		}

		if (lp.height > 0) {
			// xml文件中设置了该view的准确高度值，例如android:layout_height="50dp"
			height = View.MeasureSpec.makeMeasureSpec(lp.height, View.MeasureSpec.EXACTLY);
		} else {
			// xml文件中使用wrap_content设定该view高度，例如android:layout_height="wrap_content"
			height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		}

		v.measure(width, height);
	}

	public void setPressDownListener(IPressDownListener pressDownListener) {
		this.pressDownListener = pressDownListener;
	}
}
