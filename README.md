# android-statusbar-library
状态栏库，兼容5.0版本以下，不需要设置主题样式即可修改状态栏颜色，使用起来很方便
## API说明：CoreStatusBar类，主要是用与修改状态栏颜色，获取状态栏和导航栏高度
#### ```public static void setStatusBarColor(Activity activity, int statusColor)```
作用：设置状态栏颜色<br>参数一：当前activity，参数二：状态栏颜色
#### ```public static void setStatusBarColor(Activity activity, int statusColor, int alpha)```
作用：设置状态栏颜色<br>参数一：当前activity，参数二：状态栏颜色（rgb），参数三：指定透明度（0-255）
#### ```public static int getStateHeight(Context context)```
作用：获取状态栏高度
#### ```public static int getNavigationHeight(Context context)```
作用：获取导航栏高度
#### ```public static void translucentStatusBarNoFixedPosition(Activity activity)```
作用：设置状态栏为半透明（灰色）并且视图延伸到屏幕顶部
#### ```public static void translucentStatusBarNoFixedPosition(Activity activity, boolean transparentBarBackground)```
作用：设置状态栏为半透明，并且视图延伸到屏幕顶部<br>参数一：当前activity，参数二：布尔值，控制是否显示透明，true为透明，false为半透明（灰色）

<br>
<hr>
<br>

## 从github clone 代码到本地放到AS后，发现并不能点“Run”键运行app，当强制点击运行app，会弹出窗口，在最下方提示Error:Please select Android SDK。解决办法如下：
解决办法：在Android Studio内找到File --> Project Structure 选中app，再点击右侧上方 Properties 修改Build Tools Version版本即可
