# WallMaker
网络框架WallMaker
使用java动态代理实现的类似retrofit的网络框架
底层使用OkHttp3，接口处理使用Rxjava2分发

# 项目配置

```
  allprojects {
      repositories {
          ...
          maven { url 'https://jitpack.io' }  //添加jitpack仓库
      }
  }
  
  dependencies {
	  implementation 'com.github.bigdongdong:WallMaker:1.1' //添加依赖
  }
```
