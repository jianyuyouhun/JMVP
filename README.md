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
	        compile 'com.github.jianyuyouhun:jmvp:0.0.4'
	}

# INTRO #

   [Android设计模式浅入-（二）](https://jianyuyouhun.com/index.php/archives/75/)