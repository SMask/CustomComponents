/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mask.customcomponents.decoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * RecyclerView列表间距
 * <p>
 * Created by lishilin on 2016/11/15
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private static final Drawable dividerDefault = new ColorDrawable(0x00000000);
    private static final int dividerSizeDefault = 0;

    private Drawable divider;
    private int dividerWidth;
    private int dividerHeight;

    private boolean includeEdge;// 是否包含边缘(垂直布局 - 是否有上下间距，水平布局 - 是否有左右间距)

    public static DividerItemDecoration newInstance() {
        return newInstance(dividerSizeDefault);
    }

    public static DividerItemDecoration newInstance(int size) {
        return newInstance(size, dividerDefault);
    }

    public static DividerItemDecoration newInstance(int size, @ColorInt int color) {
        return newInstance(size, new ColorDrawable(color));
    }

    public static DividerItemDecoration newInstance(int size, Drawable divider) {
        final DividerItemDecoration decoration = new DividerItemDecoration();
        decoration.setDivider(divider);
        decoration.setDividerWidth(size);
        decoration.setDividerHeight(size);
        return decoration;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (divider == null) {
            return;
        }
        final int orientation = getOrientation(parent);

        if (orientation == OrientationHelper.VERTICAL) {
            drawHorizontalDivider(c, parent);
        } else if (orientation == OrientationHelper.HORIZONTAL) {
            drawVerticalDivider(c, parent);
        }
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        final int orientation = getOrientation(parent);

        if (orientation == OrientationHelper.VERTICAL) {
            if (isFirstView(parent, view) && includeEdge) {// 绘制上间距(当 是第一排 且 包含边缘)
                outRect.top = dividerHeight;
            }

            if (!isLastView(parent, view) || includeEdge) {// 绘制下间距(当 不是最后一排 或 包含边缘)
                outRect.bottom = dividerHeight;
            }
        } else if (orientation == OrientationHelper.HORIZONTAL) {
            if (isFirstView(parent, view) && includeEdge) {// 绘制左间距(当 是第一排 且 包含边缘)
                outRect.left = dividerWidth;
            }

            if (!isLastView(parent, view) || includeEdge) {// 绘制右间距(当 不是最后一排 或 包含边缘)
                outRect.right = dividerWidth;
            }
        }
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
        if (layoutManager instanceof LinearLayoutManager) {
            orientation = ((LinearLayoutManager) layoutManager).getOrientation();
        }
        return orientation;
    }

    /**
     * 绘制纵向线条
     *
     * @param c      Canvas
     * @param parent RecyclerView
     */
    private void drawVerticalDivider(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();
        final int top = 0;
        final int bottom = parent.getHeight();

        for (int childViewIndex = 0; childViewIndex < childCount; childViewIndex++) {
            final View child = parent.getChildAt(childViewIndex);

            if (isFirstView(parent, child) && includeEdge) {// 绘制左间距(当 是第一排 且 包含边缘)
                int left = child.getLeft() - dividerWidth;
                int right = left + dividerWidth;

                divider.setBounds(left, top, right, bottom);
                divider.draw(c);
            }

            if (!isLastView(parent, child) || includeEdge) {// 绘制右间距(当 不是最后一排 或 包含边缘)
                int left = child.getRight();
                int right = left + dividerWidth;

                divider.setBounds(left, top, right, bottom);
                divider.draw(c);
            }
        }
    }

    /**
     * 绘制横向线条
     *
     * @param c      Canvas
     * @param parent RecyclerView
     */
    private void drawHorizontalDivider(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();

        for (int childViewIndex = 0; childViewIndex < childCount; childViewIndex++) {
            final View child = parent.getChildAt(childViewIndex);
            final int left = 0;
            final int right = parent.getWidth();

            if (isFirstView(parent, child) && includeEdge) {// 绘制上间距(当 是第一排 且 包含边缘)
                int top = 0;
                int bottom = top + dividerHeight;

                divider.setBounds(left, top, right, bottom);
                divider.draw(c);
            }

            if (!isLastView(parent, child) || includeEdge) {// 绘制下间距(当 不是最后一排 或 包含边缘)
                int top = child.getBottom();
                int bottom = top + dividerHeight;

                divider.setBounds(left, top, right, bottom);
                divider.draw(c);
            }
        }
    }

    /**
     * 是否是第一个
     *
     * @param parent RecyclerView
     * @param view   view
     * @return 是否是第一个
     */
    private boolean isFirstView(RecyclerView parent, View view) {
        final int position = parent.getChildAdapterPosition(view);// item position

        return position == 0;
    }

    /**
     * 是否是最后一个
     *
     * @param parent RecyclerView
     * @param view   view
     * @return 是否是最后一个
     */
    private boolean isLastView(RecyclerView parent, View view) {
        RecyclerView.Adapter adapter = parent.getAdapter();
        final int position = parent.getChildAdapterPosition(view);// item position
        int childCount = adapter == null ? 0 : adapter.getItemCount();// item总数

        return position == childCount - 1;
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
