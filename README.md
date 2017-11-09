# SortRecyclerView
The list data is processed and a new list of alphabetically sorted letters is displayed on RecyclerView<br>（用RecyclerView对数据进行字母排序）<br>
<br>
效果图：<br>
<br>
![这里写图片描述](https://github.com/xupeng92/SortRecyclerView/raw/master/img/Recyclerview.gif)<br>
<br>
<br>
解析博客地址:http://blog.csdn.net/SilenceOO/article/details/75661590


```java
/**
 * Android使用RecyclerView实现（仿微信）的联系人A-Z字母排序和过滤搜索功能:
 * 1、支持字母、汉字搜索
 * 2、全局使用一个RecyclerView，根据查询条件过滤数据源，然后更新列表并展示
 * 3、拼音解析使用了jar包，见libs目录
 * 4、本例可使用jar包(PinyinUtils.java类)、CharacterParser.java两种形式来解析汉字，详见说明
 *
 * GitHub：https://github.com/xupeng92/SortRecyclerView
 *
 * CSDN：http://blog.csdn.net/SilenceOO/article/details/75661590?locationNum=5&fps=1
 */
```

![](https://github.com/ykmeory/Android_IndexSideBar_Recyclerview/blob/master/img/img1.png "启动界面")
![](https://github.com/ykmeory/Android_IndexSideBar_Recyclerview/blob/master/img/img2.png "摁下侧边栏")
![](https://github.com/ykmeory/Android_IndexSideBar_Recyclerview/blob/master/img/img3.png "点击侧边栏，抬起，更改背景色。字母搜索")
![](https://github.com/ykmeory/Android_IndexSideBar_Recyclerview/blob/master/img/img4.png "点击侧边栏，抬起，更改背景色。汉字搜索")
