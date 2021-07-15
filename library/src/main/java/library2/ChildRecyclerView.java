package library2;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewConfigurationCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.stone.persistent.recyclerview.library.R;

public class ChildRecyclerView extends BaseRecyclerView {
    private static final int DRAG_IDLE = 0;
    private static final int DRAG_VERTICAL = 1;
    private static final int DRAG_HORIZONTAL = 2;

    private ParentRecyclerView parentRecyclerView;
    private int mTouchSlop;
    private float downX;
    private float downY;
    private int dragState = DRAG_IDLE;

    public ChildRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public ChildRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ChildRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public String getTag() {
        return super.getTag() + "_Child";
    }

    @Override
    protected void initView(Context context) {
        super.initView(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        connectToParent();
    }

    private void connectToParent() {
        View lastTraverseView = null;
        ViewPager viewPager = null;
        ViewPager2 viewPager2 = null;
        ViewGroup parentView = (ViewGroup) getParent();
        while (parentView != null) {
            String parentClassName = parentView.getClass().getCanonicalName();
            if ("androidx.viewpager2.widget.ViewPager2.RecyclerViewImpl".equals(parentClassName)) {
                // 使用ViewPager2，parentView的顺序如下:
                // ChildRecyclerView -> 若干View -> FrameLayout -> RecyclerViewImpl -> ViewPager2 -> 若干View -> ParentRecyclerView

                // 此时lastTraverseView是上方注释中的FrameLayout，算是"ViewPager2.child"，我们此处将ChildRecyclerView设置到FrameLayout的tag中
                // 这个tag会在ParentRecyclerView中用到

                if (lastTraverseView != null)
                    lastTraverseView.setTag(R.id.tag_saved_child_recycler_view, this);
            } else if (parentView instanceof ViewPager) {
                // 使用ViewPager，parentView顺序如下：
                // ChildRecyclerView -> 若干View -> ViewPager -> 若干View -> ParentRecyclerView
                // 此处将ChildRecyclerView保存到ViewPager最直接的子View中
                if (lastTraverseView != null)
                    lastTraverseView.setTag(R.id.tag_saved_child_recycler_view, this);
                viewPager = (ViewPager) parentView;
            } else if (parentView instanceof ViewPager2) {
                viewPager2 = (ViewPager2) parentView;
            } else if (parentView instanceof ParentRecyclerView) {
                parentRecyclerView = (ParentRecyclerView) parentView;
                parentRecyclerView.setInnerViewPager(viewPager);
                parentRecyclerView.setInnerViewPager2(viewPager2);
                parentRecyclerView.setChildParentContainer(lastTraverseView);
                return;
            }
            lastTraverseView = parentView;
            parentView = (ViewGroup) parentView.getParent();
        }
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (parentRecyclerView != null) {
            if (state == SCROLL_STATE_IDLE) {
                int velocityY = (int) getVelocityY();
                if (velocityY < 0 && computeVerticalScrollOffset() == 0) {
                    parentRecyclerView.fling(0, velocityY);
                }
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            dragState = DRAG_IDLE;
            downX = e.getRawX();
            downY = e.getRawY();
            stopFling();

            getParent().requestDisallowInterceptTouchEvent(true);
        } else if (e.getAction() == MotionEvent.ACTION_MOVE) {
            formDragState(e);
            if (dragState == DRAG_VERTICAL)
                return true;
        }
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (MotionEvent.ACTION_MOVE == e.getAction()) {
            formDragState(e);
        }
        return super.onTouchEvent(e);
    }

    private void formDragState(MotionEvent e) {
        if (dragState == DRAG_IDLE) {
            float xDistance = Math.abs(e.getRawX() - downX);
            float yDistance = Math.abs(e.getRawY() - downY);
            if (xDistance > yDistance && xDistance > mTouchSlop) {
                dragState = DRAG_HORIZONTAL;
                getParent().requestDisallowInterceptTouchEvent(false);
            } else if (yDistance < xDistance && yDistance > mTouchSlop) {
                dragState = DRAG_VERTICAL;
            }
        }
    }
}
