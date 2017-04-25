# READ ME #
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

#### 第二部. 在module目录下的gradle中添加依赖 ####

	dependencies {
	        compile 'com.github.jianyuyouhun:jmvp:0.1.2'
	}

### 版本变化 ###

#### v 0.0.1 ####

　　第一次发布，实现基本的mvp思路

#### v 0.0.5 ####

　　增加注解绑定，使用方法：

	ViewInjectUtil.inject(activity);

#### v 0.0.6 ####

　　增加异常捕获功能，使用方法：

　　在manifest中添加

        <activity
            android:name="com.jianyuyouhun.jmvplib.app.ExceptionActivity"
            android:screenOrientation="portrait" />

　　同时确保japp处于调试模式BuildConfig.IS\_DEBUG = true;

#### v 0.0.7 ####

　　增加全局的superHandler的使用控制

　　在presenter中实现handleSuperMsg(Message msg)方法。在oncreate中调用openHandleMsg();

#### v 0.1.0 ####

1. presenter不再是单例，改为model单例。
2. 全局消息监听使用方法修改，presenter的原用法移到model，新用法则见[Android 设计模式浅入 - MVP（三）](https://jianyuyouhun.com/index.php/archives/79/)
3. model可以不是单例，具体使用见上链接。

#### v 0.1.1 ####

　　增加两个封装好的baseAdapter。其中simpleBaseAdapter模仿了recyclerview并且集成了注解绑定ViewInjectUtil

　　增加两个layoutManager，用于滑动控件嵌套recyclerview。

　　增加ScrollGridView，用于滑动控件嵌套gridView。

#### v 0.1.2 ####

　　修复了Logger的日志打印bug，之前只会打印为error类型。

　　提供非mvp模式的BaseActivity和BaseFragment。简单逻辑何必增加代码量。

# INTRO #

   [Android设计模式浅入-MVP（二）](https://jianyuyouhun.com/index.php/archives/75/)