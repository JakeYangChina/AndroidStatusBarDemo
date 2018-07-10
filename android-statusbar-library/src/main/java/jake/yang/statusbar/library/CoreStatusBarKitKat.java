package jake.yang.statusbar.library;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

@SuppressWarnings("unused")
@TargetApi(Build.VERSION_CODES.KITKAT)
class CoreStatusBarKitKat {
    private static final String STATUS_BAR_TAG = "status_bar_tag";
    private static final String CONTENT_VIEW_TOP_MARGIN_TAG = "content_view_top_margin_tag";


    private static View addViewToDecorView(Window window, Activity activity, int statusBarColor, int statusBarHeight) {
        ViewGroup mDecorView = (ViewGroup) window.getDecorView();
        //添加View到状态栏位置
        View mStatusBarView = new View(activity);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
        layoutParams.gravity = Gravity.TOP;
        mStatusBarView.setLayoutParams(layoutParams);
        mStatusBarView.setBackgroundColor(statusBarColor);
        //设置Tag，方便查找到
        mStatusBarView.setTag(STATUS_BAR_TAG);
        mDecorView.addView(mStatusBarView);
        return mStatusBarView;
    }


    private static void removeViewFromDecorView(Window window, Activity activity) {
        ViewGroup mDecorView = (ViewGroup) window.getDecorView();
        View fakeView = mDecorView.findViewWithTag(STATUS_BAR_TAG);
        if (fakeView != null) {
            mDecorView.removeView(fakeView);
        }
    }


    private static void addContentViewMarginTop(View mContentChild, int statusBarHeight) {
        if (mContentChild == null) {
            return;
        }
        if (!CONTENT_VIEW_TOP_MARGIN_TAG.equals(mContentChild.getTag())) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mContentChild.getLayoutParams();
            lp.topMargin += statusBarHeight;
            mContentChild.setLayoutParams(lp);
            mContentChild.setTag(CONTENT_VIEW_TOP_MARGIN_TAG);
        }
    }


    private static void removeContentViewMarginTop(View mContentChild, int statusBarHeight) {
        if (mContentChild == null) {
            return;
        }
        if (CONTENT_VIEW_TOP_MARGIN_TAG.equals(mContentChild.getTag())) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mContentChild.getLayoutParams();
            lp.topMargin -= statusBarHeight;
            mContentChild.setLayoutParams(lp);
            mContentChild.setTag(null);
        }
    }


    static void setStatusBarColor(Activity activity, int statusColor) {
        Window window = activity.getWindow();
        //控制布局延伸到状态栏底部
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ViewGroup mContentView = window.findViewById(Window.ID_ANDROID_CONTENT);
        View mContentChild = mContentView.getChildAt(0);
        View decorView = window.getDecorView();
        int statusBarHeight = CoreStatusBar.getStateHeight(activity);
        removeViewFromDecorView(window, activity);
        addViewToDecorView(window, activity, statusColor, statusBarHeight);
        addContentViewMarginTop(mContentChild, statusBarHeight);
        if (mContentChild != null) {
            //不固定位置，通过设置content布局topMargin值即可
            mContentChild.setFitsSystemWindows(false);
        }
    }

    /**
     * 设置状态栏半透明，当调用此方法，布局文件会延伸到顶部，即状态栏位置
     *
     * @param activity               activity
     * @param statusBarIsTransparent 是否为透明，true为透明，false为半透明
     */
    static void translucentStatusBar(Activity activity, boolean statusBarIsTransparent) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ViewGroup mContentView = activity.findViewById(Window.ID_ANDROID_CONTENT);
        View mContentChild = mContentView.getChildAt(0);
        removeViewFromDecorView(window, activity);
        if (!statusBarIsTransparent) {
            addViewToDecorView(window, activity, Color.argb(200, 122, 122, 122), CoreStatusBar.getStateHeight(activity));
        }
        removeContentViewMarginTop(mContentChild, CoreStatusBar.getStateHeight(activity));
        if (mContentChild != null) {
            mContentChild.setFitsSystemWindows(false);
        }
    }


    /**
     * compat for CollapsingToolbarLayout
     * <p>
     * <p>
     * <p>
     * 1. set Window Flag : WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
     * <p>
     * 2. set FitsSystemWindows for views.
     * <p>
     * 3. add Toolbar's height, let it layout from top, then add paddingTop to layout normal.
     * <p>
     * 4. removeFakeStatusBarViewIfExist
     * <p>
     * 5. removeMarginTopOfContentChild
     * <p>
     * 6. add OnOffsetChangedListener to change statusBarView's alpha
     */
    static void setStatusBarColorForCollapsingToolbar(Activity activity, final AppBarLayout appBarLayout, final CollapsingToolbarLayout collapsingToolbarLayout,
                                                      Toolbar toolbar, int statusColor) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ViewGroup mContentView = window.findViewById(Window.ID_ANDROID_CONTENT);
        View mContentChild = mContentView.getChildAt(0);
        mContentChild.setFitsSystemWindows(false);
        ((View) appBarLayout.getParent()).setFitsSystemWindows(false);
        appBarLayout.setFitsSystemWindows(false);
        collapsingToolbarLayout.setFitsSystemWindows(false);
        collapsingToolbarLayout.getChildAt(0).setFitsSystemWindows(false);
        toolbar.setFitsSystemWindows(false);
        int statusBarHeight = CoreStatusBar.getStateHeight(activity);
        if (toolbar.getTag() == null) {
            CollapsingToolbarLayout.LayoutParams lp = (CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams();
            lp.height += statusBarHeight;
            toolbar.setLayoutParams(lp);
            toolbar.setPadding(toolbar.getPaddingLeft(), toolbar.getPaddingTop() + statusBarHeight, toolbar.getPaddingRight(), toolbar.getPaddingBottom());
            toolbar.setTag(true);
        }

        removeViewFromDecorView(window, activity);
        removeContentViewMarginTop(mContentChild, statusBarHeight);
        final View statusView = addViewToDecorView(window, activity, statusColor, statusBarHeight);
        CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams()).getBehavior();
        if (behavior != null && behavior instanceof AppBarLayout.Behavior) {
            int verticalOffset = ((AppBarLayout.Behavior) behavior).getTopAndBottomOffset();
            if (Math.abs(verticalOffset) > appBarLayout.getHeight() - collapsingToolbarLayout.getScrimVisibleHeightTrigger()) {
                statusView.setAlpha(1f);
            } else {
                statusView.setAlpha(0f);
            }
        } else {
            statusView.setAlpha(0f);
        }

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) > appBarLayout.getHeight() - collapsingToolbarLayout.getScrimVisibleHeightTrigger()) {
                    if (statusView.getAlpha() == 0) {
                        statusView.animate().cancel();
                        statusView.animate().alpha(1f).setDuration(collapsingToolbarLayout.getScrimAnimationDuration()).start();
                    }

                } else {
                    if (statusView.getAlpha() == 1) {
                        statusView.animate().cancel();
                        statusView.animate().alpha(0f).setDuration(collapsingToolbarLayout.getScrimAnimationDuration()).start();
                    }
                }
            }
        });
    }
}
