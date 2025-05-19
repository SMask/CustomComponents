package com.mask.customcomponents.view;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * RecyclerView网格间距
 * <p>
 * Created by lishilin on 2016/11/15
 */
public class DividerGridItemDecoration extends RecyclerView.ItemDecoration {

    private static final Drawable dividerDefault = new ColorDrawable(0x00000000);
    private static final int dividerWidthDefault = 0;
    private static final int dividerHeightDefault = 0;

    private Drawable divider;
    private int dividerWidth;
    private int dividerHeight;

    private boolean includeEdge;// 是否包含边缘(垂直布局 - 是否有上下间距，水平布局 - 是否有左右间距)

    public static DividerGridItemDecoration getInstance() {
        return getInstance(dividerWidthDefault, dividerHeightDefault);
    }

    public static DividerGridItemDecoration getInstance(int width, int height) {
        return getInstance(width, height, dividerDefault);
    }

    public static DividerGridItemDecoration getInstance(int width, int height, @ColorInt int color) {
        return getInstance(width, height, new ColorDrawable(color));
    }

    public static DividerGridItemDecoration getInstance(int width, int height, Drawable divider) {
        final DividerGridItemDecoration decoration = new DividerGridItemDecoration();
        decoration.setDivider(divider);
        decoration.setDividerHeight(width);
        decoration.setDividerWidth(height);
        return decoration;
    }

    private DividerGridItemDecoration() {
        super();
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (divider == null) {
            return;
        }

        final int orientation = getOrientation(parent);

        if (orientation == OrientationHelper.VERTICAL) {
            drawVerticalDividerHorizontal(c, parent);
            drawVerticalDividerVertical(c, parent);
        } else if (orientation == OrientationHelper.HORIZONTAL) {
            drawHorizontalDividerHorizontal(c, parent);
            drawHorizontalDividerVertical(c, parent);
        }
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        final int orientation = getOrientation(parent);

        if (orientation == OrientationHelper.VERTICAL) {
            if (isFirstLine(parent, view) && includeEdge) {// 绘制上间距(当 是第一排 且 包含边缘)
                outRect.top = dividerHeight;
            }

            if (!isLastLine(parent, view) || includeEdge) {// 绘制下间距(当 不是最后一排 或 包含边缘)
                outRect.bottom = dividerHeight;
            }

            outRect.left = getVerticalLeftSpace(parent, view);// 绘制左间距
            outRect.right = getVerticalRightSpace(parent, view);// 绘制右间距
        } else if (orientation == OrientationHelper.HORIZONTAL) {
            if (isFirstLine(parent, view) && includeEdge) {// 绘制左间距(当 是第一排 且 包含边缘)
                outRect.left = dividerWidth;
            }

            if (!isLastLine(parent, view) || includeEdge) {// 绘制右间距(当 不是最后一排 或 包含边缘)
                outRect.right = dividerWidth;
            }

            outRect.top = getHorizontalTopSpace(parent, view);// 绘制上间距
            outRect.bottom = getHorizontalBottomSpace(parent, view);// 绘制下间距
        }
    }

    /**
     * 绘制垂直滚动横向线条
     *
     * @param c      Canvas
     * @param parent RecyclerView
     */
    private void drawVerticalDividerHorizontal(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();

        for (int childViewIndex = 0; childViewIndex < childCount; childViewIndex++) {
            final View child = parent.getChildAt(childViewIndex);

            if (isFirstLine(parent, child) && includeEdge) {// 绘制上间距(当 是第一排 且 包含边缘)
                int left = child.getLeft();
                int top = child.getTop() - dividerHeight;
                int right = left + child.getWidth() + dividerWidth;
                int bottom = top + dividerHeight;

                divider.setBounds(left, top, right, bottom);
                divider.draw(c);
            }

            if (!isLastLine(parent, child) || includeEdge) {// 绘制下间距(当 不是最后一排 或 包含边缘)
                int left = child.getLeft();
                int top = child.getBottom();
                int right = left + child.getWidth() + dividerWidth;
                int bottom = top + dividerHeight;

                divider.setBounds(left, top, right, bottom);
                divider.draw(c);
            }
        }
    }

    /**
     * 绘制垂直滚动纵向线条
     *
     * @param c      Canvas
     * @param parent RecyclerView
     */
    private void drawVerticalDividerVertical(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();

        for (int childViewIndex = 0; childViewIndex < childCount; childViewIndex++) {
            final View child = parent.getChildAt(childViewIndex);

            final int dividerWidthLeft = getVerticalLeftSpace(parent, child);
            final int dividerWidthRight = getVerticalRightSpace(parent, child);

            if (dividerWidthLeft > 0) {// 绘制左间距
                int left = child.getLeft() - dividerWidthLeft;
                int top = child.getTop();
                int right = left + dividerWidthLeft;
                int bottom = top + child.getHeight();

                divider.setBounds(left, top, right, bottom);
                divider.draw(c);
            }

            if (dividerWidthRight > 0) {// 绘制右间距
                int left = child.getRight();
                int top = child.getTop();
                int right;
                // 如果是最后一个且未占满一排(后面有空白区域)
                if (childViewIndex == childCount - 1) {
                    right = left + dividerWidth;
                } else {
                    right = left + dividerWidthRight;
                }
                int bottom = top + child.getHeight();

                divider.setBounds(left, top, right, bottom);
                divider.draw(c);
            }
        }
    }

    /**
     * 绘制水平滚动横向线条
     *
     * @param c      Canvas
     * @param parent RecyclerView
     */
    private void drawHorizontalDividerHorizontal(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();

        for (int childViewIndex = 0; childViewIndex < childCount; childViewIndex++) {
            final View child = parent.getChildAt(childViewIndex);

            final int dividerWidthTop = getVerticalLeftSpace(parent, child);
            final int dividerWidthBottom = getVerticalRightSpace(parent, child);

            if (dividerWidthTop > 0) {// 绘制上间距
                int left = child.getLeft();
                int top = child.getTop() - dividerWidthTop;
                int right = left + child.getWidth();
                int bottom = top + dividerWidthTop;

                divider.setBounds(left, top, right, bottom);
                divider.draw(c);
            }

            if (dividerWidthBottom > 0) {// 绘制下间距
                int left = child.getLeft();
                int top = child.getBottom();
                int right = left + child.getWidth();
                // 如果是最后一个且未占满一排(后面有空白区域)
                int bottom;
                if (childViewIndex == childCount - 1) {
                    bottom = top + dividerHeight;
                } else {
                    bottom = top + dividerWidthBottom;
                }

                divider.setBounds(left, top, right, bottom);
                divider.draw(c);
            }
        }
    }

    /**
     * 绘制水平滚动纵向线条
     *
     * @param c      Canvas
     * @param parent RecyclerView
     */
    private void drawHorizontalDividerVertical(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();

        for (int childViewIndex = 0; childViewIndex < childCount; childViewIndex++) {
            final View child = parent.getChildAt(childViewIndex);

            if (isFirstLine(parent, child) && includeEdge) {// 绘制左间距(当 是第一排 且 包含边缘)
                int left = child.getLeft() - dividerWidth;
                int top = child.getTop();
                int right = left + dividerWidth;
                int bottom = top + child.getHeight() + dividerHeight;

                divider.setBounds(left, top, right, bottom);
                divider.draw(c);
            }

            if (!isLastLine(parent, child) || includeEdge) {// 绘制右间距(当 不是最后一排 或 包含边缘)
                int left = child.getRight();
                int top = child.getTop();
                int right = left + dividerWidth;
                int bottom = top + child.getHeight() + dividerHeight;

                divider.setBounds(left, top, right, bottom);
                divider.draw(c);
            }
        }
    }

    /**
     * 获取列数
     *
     * @param parent RecyclerView
     * @return 列数
     */
    private int getSpanCount(RecyclerView parent) {
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        }
        return spanCount;
    }

    /**
     * 获取当前View列下标
     *
     * @param parent RecyclerView
     * @param view   view
     * @return 列下标
     */
    private int getSpanIndex(RecyclerView parent, View view) {
        int spanIndex = -1;
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params instanceof GridLayoutManager.LayoutParams) {
            final int spanCount = getSpanCount(parent);
            final int position = getChildPosition(parent, view);// item position
            spanIndex = position % spanCount;// item spanIndex
        }
        return spanIndex;
    }

    /**
     * 获取方向
     *
     * @param parent RecyclerView
     * @return 方向
     */
    private int getOrientation(RecyclerView parent) {
        int orientation = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            orientation = ((GridLayoutManager) layoutManager).getOrientation();
        }
        return orientation;
    }

    /**
     * 获取位置position
     *
     * @param parent RecyclerView
     * @param view   child
     * @return position
     */
    private int getChildPosition(RecyclerView parent, View view) {
        return parent.getChildAdapterPosition(view);
    }

    /**
     * 获取垂直滑动item左间距
     *
     * @param parent RecyclerView
     * @param view   item
     * @return 垂直滑动item左间距
     */
    private int getVerticalLeftSpace(RecyclerView parent, View view) {
        final int spanCount = getSpanCount(parent);
        final int spanIndex = getSpanIndex(parent, view);// item spanIndex

        return spanIndex * dividerWidth / spanCount;
    }

    /**
     * 获取垂直滑动item右间距
     *
     * @param parent RecyclerView
     * @param view   item
     * @return 垂直滑动item右间距
     */
    private int getVerticalRightSpace(RecyclerView parent, View view) {
        final int spanCount = getSpanCount(parent);
        final int spanIndex = getSpanIndex(parent, view);// item spanIndex

        return dividerWidth - (spanIndex + 1) * dividerWidth / spanCount;
    }

    /**
     * 获取水平滑动item上间距
     *
     * @param parent RecyclerView
     * @param view   item
     * @return 水平滑动item上间距
     */
    private int getHorizontalTopSpace(RecyclerView parent, View view) {
        final int spanCount = getSpanCount(parent);
        final int spanIndex = getSpanIndex(parent, view);// item spanIndex

        return spanIndex * dividerHeight / spanCount;
    }

    /**
     * 获取水平滑动item下间距
     *
     * @param parent RecyclerView
     * @param view   item
     * @return 水平滑动item下间距
     */
    private int getHorizontalBottomSpace(RecyclerView parent, View view) {
        final int spanCount = getSpanCount(parent);
        final int spanIndex = getSpanIndex(parent, view);// item spanIndex

        return dividerHeight - (spanIndex + 1) * dividerHeight / spanCount;
    }

    /**
     * 是否是第一排(垂直布局 - 第一横排，水平布局 - 第一竖排)
     *
     * @param parent RecyclerView
     * @param view   当前view
     * @return 是否是第一排
     */
    private boolean isFirstLine(RecyclerView parent, View view) {
        final int position = getChildPosition(parent, view);// item position
        final int spanCount = getSpanCount(parent);

        return position < spanCount;
    }

    /**
     * 是否是最后一排(垂直布局 - 最后一横排，水平布局 - 最后一竖排)
     *
     * @param parent RecyclerView
     * @param view   当前view
     * @return 是否是最后一排
     */
    private boolean isLastLine(RecyclerView parent, View view) {
        RecyclerView.Adapter adapter = parent.getAdapter();
        final int position = getChildPosition(parent, view);// item position
        final int spanCount = getSpanCount(parent);
        int childCount = adapter == null ? 0 : adapter.getItemCount();// item总数
        int lastLineCount = childCount % spanCount;// 最后一排item数量

        if (childCount <= spanCount) {
            return true;
        }

        if (lastLineCount == 0) {
            lastLineCount = spanCount;
        }

        childCount = childCount - lastLineCount;
        return position >= childCount;
    }

    public boolean isIncludeEdge() {
        return includeEdge;
    }

    public void setIncludeEdge(boolean includeEdge) {
        this.includeEdge = includeEdge;
    }

    /**
     * Sets the drawable to be used as the divider.
     *
     * @param divider divider
     */
    public void setDivider(Drawable divider) {
        if (divider != null) {
            int intrinsicWidth = divider.getIntrinsicWidth();
            int intrinsicHeight = divider.getIntrinsicHeight();
            if (intrinsicWidth > 0) {
                dividerWidth = intrinsicWidth;
            }
            if (intrinsicHeight > 0) {
                dividerHeight = intrinsicHeight;
            }
        } else {
            dividerWidth = 0;
            dividerHeight = 0;
        }
        this.divider = divider;
    }

    /**
     * Gets the drawable currently used as the divider.
     */
    public Drawable getDivider() {
        return divider;
    }

    /**
     * Sets the divider width, in pixels.
     */
    public void setDividerWidth(int dividerWidth) {
        this.dividerWidth = dividerWidth;
    }

    /**
     * Gets the divider width, in pixels.
     */
    public int getDividerWidth() {
        return dividerWidth;
    }

    /**
     * Sets the divider height, in pixels.
     */
    public void setDividerHeight(int dividerHeight) {
        this.dividerHeight = dividerHeight;
    }

    /**
     * Gets the divider height, in pixels.
     */
    public int getDividerHeight() {
        return dividerHeight;
    }

}