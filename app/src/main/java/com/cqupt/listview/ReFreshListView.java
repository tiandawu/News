package com.cqupt.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cqupt.adapter.NewsAdapter;
import com.cqupt.news.R;
import com.cqupt.utils.ImageLoader;

/**
 * ===================================================================
 * <p/>
 * 版权：重庆邮电大学教育集团 版权所有 （c）  2015
 * <p/>
 * 作者：小甜甜
 * <p/>
 * 版本：1.0
 * <p/>
 * 创建日期：2015/7/4 23:06
 * <p/>
 * 描述：
 * 自定义刷新ListView
 * <p/>
 * <p/>
 * 修订历史：
 * <p/>
 * ===================================================================
 */
public class ReFreshListView extends ListView implements AbsListView.OnScrollListener {
    private LinearLayout header;//顶部布局文件
    private int headerHeight;//顶部布局文件的高度
//    private int firstVisibleItem;//当前第一个可见item的位置
    private boolean isRemark;//标记，当前是在ListView最顶端摁下的
    private int startY;//摁下时的Y值
    private int state;//当前的状态
    private int scrollState;//ListView当前滚动状态
    private final int NONE = 0;//正常状态
    private final int PULL = 1;//提示下拉刷新状态
    private final int RELESE = 2;//提示释放状态
    private final int REFRESH = 3;//正在刷新状态

    private int mStart, mEnd;//可见的开始和结束item
    private boolean mFirstIn = false;//是否为刚进入界面

    private IRefreshListener iRefreshListener;//刷新数据的接口


    public ReFreshListView(Context context) {
        super(context);
        initView(context);
    }

    public ReFreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ReFreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    /**
     * 初始化界面，添加顶部布局文件到ListView
     *
     * @param context 上下文
     */
    private void initView(Context context) {
        //刚进入界面
        mFirstIn = true;
        //获取顶部布局文件
        LayoutInflater inflater = LayoutInflater.from(context);
        header = (LinearLayout) inflater.inflate(R.layout.header_layout, null);
        //计算header的宽，高
        measureView(header);
        //获取顶部布局文件的高度
        headerHeight = header.getMeasuredHeight();
        //设置距离顶部的距离,传入高度值的负值，将顶部布局文件隐藏
        topPadding(-headerHeight);

        //将顶部布局文件添加到ListView
        this.addHeaderView(header);

        //设置滚动监听
        this.setOnScrollListener(this);
    }

    /**
     * 设置header布局的上边距
     *
     * @param mTopPadding 距离上边距的高度
     */
    private void topPadding(int mTopPadding) {
        header.setPadding(header.getPaddingLeft(), mTopPadding, header.getPaddingRight(), header.getPaddingBottom());
        header.invalidate();
    }


    /**
     * 通知父布局占用的宽，高
     *
     * @param view 被测量的控件
     */
    private void measureView(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int width = ViewGroup.getChildMeasureSpec(0, 0, params.width);
        int height;
        int tempHeight = params.height;
        if (tempHeight > 0) {
            height = MeasureSpec.makeMeasureSpec(tempHeight, MeasureSpec.EXACTLY);
        } else {
            height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        view.measure(width, height);
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        ImageLoader mImageLoader = NewsAdapter.mImageLoader;
        if (scrollState == SCROLL_STATE_IDLE) {
            //加载可见项


            mImageLoader.loadImages(mStart, mEnd);

        } else {
            //停止所有任务
            mImageLoader.cancelAllTasks();
        }

    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.mStart = firstVisibleItem;
        this.mEnd = firstVisibleItem + visibleItemCount;
        //第一次显示时调用
        if (mFirstIn && visibleItemCount > 0) {
            mFirstIn = false;
            NewsAdapter.mImageLoader.loadImages(mStart, mEnd);
        }
//        this.firstVisibleItem = firstVisibleItem;

    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mStart == 0) {
                    isRemark = true;
                    startY = (int) ev.getY();
                }
                break;

            case MotionEvent.ACTION_MOVE:
                onMove(ev);
                break;
            case MotionEvent.ACTION_UP:
                if (state == RELESE) {
                    state = REFRESH;
                    // 加载最新数据；
                    refreshViewByState();
                    iRefreshListener.onRefresh();
                } else if (state == PULL) {
                    state = NONE;
                    isRemark = false;
                    refreshViewByState();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }


    /**
     * 判断移动过程中的操作
     *
     * @param ev
     */
    private void onMove(MotionEvent ev) {
        if (!isRemark) {
            return;
        }
        int tempY = (int) ev.getY();
        int space = tempY - startY;
        int topPadding = space - headerHeight;
        switch (state) {
            case NONE:
                if (space > 0) {
                    state = PULL;
                    refreshViewByState();
                }
                break;
            case PULL:
                topPadding(topPadding);
                if (space > headerHeight + 25) {
                    state = RELESE;
                    refreshViewByState();
                }
                break;
            case RELESE:
                topPadding(topPadding);
                if (space < headerHeight + 25) {
                    state = PULL;
                    refreshViewByState();
                } else if (space <= 0) {
                    state = NONE;
                    isRemark = false;
                    refreshViewByState();
                }
                break;
        }
    }

    /**
     * 根据当前状态改变界面显示
     */
    private void refreshViewByState() {
        TextView tip = (TextView) header.findViewById(R.id.tip);
        ImageView arrow = (ImageView) header.findViewById(R.id.arrow);
        ProgressBar progress = (ProgressBar) header.findViewById(R.id.progress);
        RotateAnimation animUp = new RotateAnimation(0, 180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animUp.setDuration(500);
        animUp.setFillAfter(true);
        RotateAnimation animDown = new RotateAnimation(180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animDown.setDuration(500);
        animDown.setFillAfter(true);
        switch (state) {
            case NONE:
                arrow.clearAnimation();
                topPadding(-headerHeight);
                break;

            case PULL:
                arrow.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                tip.setText("下拉可以刷新！");
                arrow.clearAnimation();
                arrow.setAnimation(animDown);
                break;
            case RELESE:
                arrow.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                tip.setText("松开可以刷新！");
                arrow.clearAnimation();
                arrow.setAnimation(animUp);
                break;
            case REFRESH:
                topPadding(35);
                arrow.setVisibility(View.GONE);
                progress.setVisibility(View.VISIBLE);
                tip.setText("正在刷新...");
                arrow.clearAnimation();
                break;
        }

    }

    /**
     * 获取完数据
     */
    public void reFreshComplete() {
        state = NONE;
        isRemark = false;
        refreshViewByState();
    }


    /**
     * 刷新数据接口
     */
    public interface IRefreshListener {
        public void onRefresh();
    }

    public void setInterface(IRefreshListener iRefreshListener) {
        this.iRefreshListener = iRefreshListener;
    }
}
