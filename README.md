# READ ME #

### 相关链接 ###

　　[JMVP powered by kotlin](https://github.com/jianyuyouhun/KotlinJMVP)

### 用法 ###

在你的build中引入本项目

#### 第一步. 在build 中添加jitpack引用 ####

在你的根目录下的build.gradle中添加下面代码

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

#### 第二步. 在module目录下的gradle中添加依赖 ####

	dependencies {
	        compile 'com.github.jianyuyouhun:jmvp:0.2.6'
	}

### 版本变化 ###

#### v 0.2.7 ####

　　增加了长按事件的注解绑定，使用方法如下（绑定依然调用ViewInjector.inject(activity)...）

    @OnLongClick({
            R.id.dialog_test,
            R.id.adapter_test,
            R.id.image_load_test,
            R.id.permission_test,
            R.id.animator_view_test,
            R.id.login_test,
            R.id.http_get})
    protected boolean onDialogLongClick(View view) {
        showToast("别乱按");
        return true;
    }

#### v 0.2.6 ####

　　Model和Presenter去除接口，新的实现类名字改为BaseJPresenter,BaseJModel(用于替换BaseJPresenterImpl等),项目进行了一些结构上的调整。默认引用JHttp，ImageLoader和LightBroadcast，如果不想装载的话，在Application中重写initDependencies()，进行对应的库的初始化。

　　动态权限申请的调用进行了调整。避免了一些空指针的出现

　　修复了一些其他bug

#### v 0.2.5(测试版) ####

　　service中提供关闭广播

#### v 0.2.4 ####

　　删除Activity/Fragment中的getPresenter方法，取代的是使用反射来初始化presenter，

　　修复了一些已知bug

#### v 0.2.3 ####

　　全局消息机制进行封装，改到LightBroadcast中，使用方式见[LightBroadcast注释](https://github.com/jianyuyouhun/JMVP/blob/master/library/src/main/java/com/jianyuyouhun/jmvplib/app/broadcast/LightBroadcast.java)

　　增加最大高度限制的视图MaxHeightLinearLayout和可控制滑动的NestScrollViewPager

#### v 0.2.0 ####

　　修复动态权限处理bug

#### v 0.1.8 ####

　　增加源码打包

#### v 0.1.5 ####

　　增加动态权限处理。目前仅封装了单次一个权限申请，如要多个则需要手动多次调用，**并发请求将无法生效**。

　　使用方法：先设置请求回调监听器

        permissionRequester.setOnPermissionRequestListener(new PermissionRequestListener() {
            @Override
            public void onRequestSuccess(String permission, String permissionName) {
                
            }

            @Override
            public void onRequestFailed(String permission, String permissionName) {

            }
        });

　　然后开始请求权限：

         permissionRequester.requestPermission(getActivity(), "存储", Manifest.permission.WRITE_EXTERNAL_STORAGE);


#### v 0.1.4 ####

　　修复http请求bug，新用法如下：

	App的onCreate中初始化

	JHttpFactory.init();

	使用方式：

	JHttpFactory.getInstance().execute(client, new OnResultListener<String>() {
            @Override
            public void onResult(int result, String data) {
                ...
            }
        });

	JHttpFactory.getInstance().execute(client, new OnProgressChangeListener() {
            @Override
            public void onStart() {
                
            }

            @Override
            public void onProgressChanged(int current, int total) {

            }

            @Override
            public void onFinish(String result) {

            }

            @Override
            public void onError(int code, Exception e) {

            }
        });

#### v 0.1.3 ####

　　增加http网络请求处理，支持get，post和post上传文件。

　　使用方式：

　　get

        JHttpClient client = new JHttpTask.ClientBuilder()
                .setUrl("https://jianyuyouhun.com")
                .setMethod(JHttpRequest.METHOD_GET)
                .build();
        new JHttpTask(client, new OnResultListener<String>() {
            @Override
            public void onResult(int result, String data) {
                listener.onResult(result, data);
            }
        }).execute();

　　post和get相似，略。

　　upload：

        JHttpClient client = new JHttpTask.ClientBuilder()
                .setUrl("https://jianyuyouhun.com")
                .setMethod(JHttpRequest.METHOD_UPLOAD)
                .setFilePath("...")
                .build();
        new JHttpTask(client, new OnProgressChangeListener() {
            @Override
            public void onStart() {
                
            }

            @Override
            public void onProgressChanged(int current, int total) {

            }

            @Override
            public void onFinish(String result) {

            }

            @Override
            public void onError(int code, Exception e) {

            }
        }).execute();

#### v 0.1.1 ####

　　增加两个封装好的baseAdapter。其中simpleBaseAdapter模仿了recyclerview并且集成了注解绑定ViewInjectUtil

　　增加两个layoutManager，用于滑动控件嵌套recyclerview。

　　增加ScrollGridView，用于滑动控件嵌套gridView。

#### v 0.1.0 ####

1. presenter不再是单例，改为model单例。
2. 全局消息监听使用方法修改，presenter的原用法移到model，新用法则见[Android 设计模式浅入 - MVP（三）](https://jianyuyouhun.com/index.php/archives/79/)
3. model可以不是单例，具体使用见上链接。

> 5/27/2017 11:43:21 AM 最新用法见v 0.2.3

#### v 0.0.6 ####

　　增加异常捕获功能，使用方法：

　　在manifest中添加

        <activity
            android:name="com.jianyuyouhun.jmvplib.app.exception.ExceptionActivity"
            android:screenOrientation="portrait" />

　　同时确保japp处于调试模式BuildConfig.IS\_DEBUG = true;

#### v 0.0.5 ####

　　增加注解绑定，使用方法：

	ViewInjectUtil.inject(activity);

#### v 0.0.1 ####

　　第一次发布，实现基本的mvp思路

# INTRO #

   [Android设计模式浅入-MVP（二）](https://jianyuyouhun.com/index.php/archives/75/)
