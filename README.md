# AndroidPermissionDemo
权限检测库，兼容不同类型厂商手机，使用注解实现权限申请，使用起来很方便
<br>说明：此权限框架可以在任意类内进行权限申请，```例如：Activity，Fragment，Service 或者是其它类内都可以使用```
## 使用说明
### @RequestPermission注解
指定要申请的权限（可以声明多个，但方法名不可以相同，请求码requestCode值不能相同，请求码requestCode值相同代表同一组权限）<br>注解内可以指定requestCode值，不指定即为默认值，当权限全部授予时，就会回调此注解修饰的方法<br>例如：<br>
```
    @RequestPermission(value = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode = 6)
    public void request() {
        Log.e(TAG, "权限已经全部授予");
    }
```
### @RequestPermissionNoPassed注解
注解内可以指定requestCode值，不指定即为默认值
只要存在没有被授予的权限时（包括勾选拒绝后不再询问的权限）就会回调此注解修饰的方法
可以给方法指定参数为```List<String>```集合内存储的值是未授予的权限
<br>例如：<br>
```
    @RequestPermissionNoPassed(requestCode = 6)
    public void noPass(List<String> noPassPermission){//参数可以不写
        Log.e(TAG, "存在没有授予的权限");
    }
```
### @RequestPermissionDenied注解
注解内可以指定requestCode值，不指定即为默认值
只要有勾选拒绝后不再询问的权限，就会回调此注解修饰的方法
可以给方法指定参数为```List<String>```集合内存储的值是被拒绝申请的权限
<br>例如：<br>
```
    @RequestPermissionDenied(requestCode = 6)
    public void denied(List<String> deniedPermission){//参数可以不写
        Log.e(TAG, "存在拒绝申请的权限");
    }
```
### @RequestPermissionAutoOpenSetting注解
注解内可以指定requestCode值，不指定即为默认值
当指定的权限被勾选拒绝时，设置是否开启系统设置页面
方法一定要指定一个参数```Chain```用于控制是否开启系统设置页面，让用户手动开启权限
当不使用此注解时，默认为不开启系统设置页面，建议在这个注解方法内，弹对话框，提示用户手动开启页面
<br>例如：<br>
```
    @RequestPermissionAutoOpenSetting(requestCode = 6)
    public void autoOpenSetting(Chain chain){//一定要指定参数
        chain.open();//开启系统设置页面
        Log.e(TAG, "可以弹出对话框，提示用户手动开启设置页面，当点击确认按钮可以调用chain.open()方法，打开系统设置页面");
    }
```
### 上面四个注解为一组请求，requestCode控制是否是同一组请求，可以定义多组请求权限，一组请求内可以请求多个权限，也可以请求一个权限
### 通过如下方法执行某一组的权限申请
```
    //方法一：调用两个参数的方法，前提是已经初始化 init() 方法
    Permission.requestPermission(this, "request");//第二个参数：方法名必须是被@RequestPermission注解声明的方法
    
    //方法二：调用三个参数的方法
    Permission.requestPermission(this, this, "request");//第二个参数：方法名必须是被@RequestPermission注解声明的方法
```

<br>
<hr>
<br>

## API说明：Permission类，主要是用于权限申请，内部都是静态方法
#### ```public static void init(Application application)```
作用：初始化Context<br>此方法可以不使用，如果使用了，可以和requestPermission两个参数的方法一起使用<br>建议放到Application内初始化
#### ```public static void requestPermission(Object currentObj, String  requestMethodName)```
作用：申请权限<br>参数一：当前类对象，参数二：要调用的方法名（必须是被@RequestPermission注解修饰的方法）<br>使用此方法前必须要先初始化 init() 方法
#### ```public static void requestPermission(Context context, Object currentObj, String requestMethodName)```
作用：申请权限<br>参数一：context，参数二：当前类对象，参数三：要调用的方法名（必须是被@RequestPermission注解修饰的方法）<br>
#### ```public static void destroyPermission(Object currentObj)```
作用：释放指定类申请的权限，防止内存泄漏<br>参数为：当前类对象
#### ```public static void destroyAllPermission()```
作用：释放申请的权限，防止内存泄漏<br>建议在程序退出时调用，或最后一个页面关闭时调用

<br>
<hr>
<br>

## 从github clone 代码到本地放到AS后，发现并不能点“Run”键运行app，当强制点击运行app，会弹出窗口，在最下方提示Error:Please select Android SDK。解决办法如下：
解决办法：在Android Studio内找到File --> Project Structure 选中app，再点击右侧上方 Properties 修改Build Tools Version版本即可
