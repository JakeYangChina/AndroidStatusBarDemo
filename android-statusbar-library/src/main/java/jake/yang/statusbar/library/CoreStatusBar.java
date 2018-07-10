package jake.yang.statusbar.library;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;

@SuppressWarnings({"unused", "WeakerAccess", "ConstantConditions"})
public class CoreStatusBar {
    public static int getStateHeight(Context context) {
        int stateHeight = 0;
        Resources resources = context.getApplicationContext().getResources();
        int identifierState = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (identifierState > 0) {
            stateHeight = resources.getDimensionPixelSize(identifierState);
        }
        return stateHeight;
    }

    public static int getNavigationHeight(Context context) {
        int stateHeight = 0;
        Resources resources = context.getApplicationContext().getResources();
        int identifierNavigation = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (identifierNavigation > 0) {
            stateHeight = resources.getDimensionPixelSize(identifierNavigation);
        }
        return stateHeight;
    }
    //Get alpha color

    @SuppressWarnings("NumericOverflow")
    public static int calculateStatusBarColor(int color, int alpha) {
        float a = 1 - alpha / 255f;
        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;
        red = (int) (red * a + 0.5);
        green = (int) (green * a + 0.5);
        blue = (int) (blue * a + 0.5);
        return 0xff << 24 | red << 16 | green << 8 | blue;

    }

    /**
     * set statusBarColor
     *
     * @param statusColor color
     * @param alpha       0 - 255
     */
    public static void setStatusBarColor(@NonNull Activity activity, @ColorInt int statusColor, int alpha) {
        setStatusBarColor(activity, calculateStatusBarColor(statusColor, alpha));
    }


    public static void setStatusBarColor(@NonNull Activity activity, @ColorInt int statusColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CoreStatusBarLollipop.setStatusBarColor(activity, statusColor);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            CoreStatusBarKitKat.setStatusBarColor(activity, statusColor);
        }
    }


    public static void translucentStatusBarNoFixedPosition(@NonNull Activity activity) {
        translucentStatusBarNoFixedPosition(activity, false);
    }


    /**
     * change to full screen mode
     *
     * @param transparentBarBackground true is transparent status bar alpha Background,false is translucent status bar alpha Background
     */

    public static void translucentStatusBarNoFixedPosition(@NonNull Activity activity, boolean transparentBarBackground) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CoreStatusBarLollipop.translucentStatusBar(activity, transparentBarBackground);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            CoreStatusBarKitKat.translucentStatusBar(activity, transparentBarBackground);
        }
    }


    public static void setStatusBarColorForCollapsingToolbar(@NonNull Activity activity, AppBarLayout appBarLayout, CollapsingToolbarLayout collapsingToolbarLayout,
                                                             Toolbar toolbar, @ColorInt int statusColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CoreStatusBarLollipop.setStatusBarColorForCollapsingToolbar(activity, appBarLayout, collapsingToolbarLayout, toolbar, statusColor);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            CoreStatusBarKitKat.setStatusBarColorForCollapsingToolbar(activity, appBarLayout, collapsingToolbarLayout, toolbar, statusColor);
        }
    }
}
