# RuiWatchView
自定义时钟

具体的分析请前往<a href="https://hurui1990.github.io/hurui.github.io/2017/08/22/Android自定义View之自己画一个时钟/">项目博客</a>

调用步骤：
1、在项目gradle.build中添加下列代码
```java
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

2、在app的gradle.build中添加依赖
```java
dependencies {
        compile 'com.github.hurui1990:RuiWatchView:v1.0.2'
}
```
