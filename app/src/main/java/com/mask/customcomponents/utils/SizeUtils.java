package com.mask.customcomponents.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class SizeUtils {

    /**
     * 获取屏幕信息
     *
     * @return DisplayMetrics
     */
    public static DisplayMetrics getScreenDisplayMetrics() {
        return Resources.getSystem().getDisplayMetrics();
    }

    /**
     * 获取屏幕密度Dpi
     *
     * @return 屏幕密度Dpi
     */
    public static float getDensityDpi() {
        DisplayMetrics display = getScreenDisplayMetrics();
        return display.densityDpi;
    }

    /**
     * 获取屏幕密度
     *
     * @return 屏幕密度
     */
    public static float getDensity() {
        DisplayMetrics display = getScreenDisplayMetrics();
        return display.density;
    }

    /**
     * 获取字体密度
     *
     * @return 字体密度
     */
    public static float getDensityScaled() {
        DisplayMetrics display = getScreenDisplayMetrics();
        return display.scaledDensity;
    }

    /**
     * 获取屏幕宽
     *
     * @return 屏幕宽
     */
    public static int getScreenWidth() {
        DisplayMetrics display = getScreenDisplayMetrics();
        return display.widthPixels;
    }

    /**
     * 获取屏幕高
     *
     * @return 屏幕高
     */
    public static int getScreenHeight() {
        DisplayMetrics display = getScreenDisplayMetrics();
        return display.heightPixels;
    }

    /**
     * 获取实际屏幕高(包括虚拟按键)
     *
     * @param context context
     * @return 实际屏幕高(包括虚拟按键)
     */
    public static int getScreenHeightReal(Context context) {
        int height = 0;

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        try {
            Point realSize = new Point();
            Display.class.getMethod("getRealSize", Point.class).invoke(display, realSize);
            height = realSize.y;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return height;
    }

    /**
     * 获取屏幕宽高比
     *
     * @return 屏幕宽高比
     */
    public static float getScreenRate() {
        DisplayMetrics display = getScreenDisplayMetrics();
        float height = display.heightPixels;
        float width = display.widthPixels;
        return (width / height);
    }

    /**
     * dp转px
     *
     * @param dp dp
     * @return px
     */
    public static int dpToPx(int dp) {
        return (int) (dp * getDensity() + 0.5f);
    }

    /**
     * px转dp
     *
     * @param px px
     * @return dp
     */
    public static int pxToDp(int px) {
        return (int) (px / getDensity() + 0.5f);
    }

    /**
     * 取得状态栏高度
     *
     * @return 状态栏高度
     */
    public static int getStatusBarHeight() {
        int result = 0;
        try {
            int resourceId = Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = Resources.getSystem().getDimensionPixelSize(resourceId);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取ViewTop距离Screen底部距离
     *
     * @param view view
     * @return ViewTop距离Screen底部距离
     */
    public static int getViewTopToScreenBottom(View view) {
        int[] location = {0, 0};
        view.getLocationOnScreen(location);
        int topScreen = location[1];
        int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        return screenHeight - topScreen;
    }

}
