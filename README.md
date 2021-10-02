<p align="center">
	<a href="https://github.com/Ln-guolin/project-scaffold"><img src="https://soilove.oss-cn-hangzhou.aliyuncs.com/32e/pro-mall/potat.png" height="150px"></a>
</p>

## 简介
potat脚手架平台

目的：为快速拆分服务提供平台支持，实现一键自动构建可运行的基础单服务应用。

演示地址：[http://potat.32e.co/](http://potat.32e.co/)

## 环境

|环境|要求|
|---|---|
|jre|8+|
|spring-boot|2.2.4.RELEASE|



## 输入参数

|参数|格式|描述|案例|
|---|---|---|---|
|group|英文逗号分隔的域名倒序|pom文件中的groupId标签值|cn.ex.pro|
|artifact|制品名称，多词使用中横线链接|制品或业务领域 |biz-user|
|packageName|group + 转换为英文逗号分隔的artifact|包地址 |cn.ex.pro.biz.user|
|port|整型数字，4位|端口号|8080|
|version|xx.xx|版本号|1.0.0-SNAPSHOT|
|description|字符串|工程描述|用户服务|


## 变量列表

|变量|来源值|案例|描述|
|---|---|---|---|
|group|group|cn.ex.pro|/|
|artifact|artifact|biz-user|/|
|packageName|packageName|cn.ex.pro.biz.user|/|
|port|port|8080|/|
|vers|version|1.0.0-SNAPSHOT|/|
|desc|description|用户服务|/|
|createTime|now()|2021-12-12 11:22:33|系统当前时间|
|createDate|now()|2021-12-12|系统当前日期|
|port4incr|port|8081|port值+1|
|artifact4comma|artifact|biz.user|将artifact转换为"."号分隔|
|artifact4underline|artifact|biz_user|将artifact转换为"_"号分隔|
|artifact4path|artifact|biz/user|将artifact转换为"/"号分隔|
|artifact4hump|artifact|bizUser|将artifact转换为驼峰形式|
|artifact4humpCase|artifact|BizUser|将artifact转换为首字母大写的驼峰形式|
|group4path|group|cn/ex/pro|将group转换为"/"号分隔|
|package4path|packageName|cn/ex/pro/biz/user|将package转换为"/"号分隔|


### 模版变量占位符语法

|语法|适用范围|案例|
|---|---|---|
|{xx}|支持在文件夹、文件、以及文件内容中使用|{artifact4hump}.java 或 {artifact4humpCase}Controller|

模版Demo示例: [project-template/demo](https://github.com/Ln-guolin/potat-project-scaffold/tree/main/src/main/resources/project-template/demo)

### 模版配置

#### 方式一：git 远程仓库
- 1，根据文档介绍的变量，修改自己的项目，作为脚手架的模版
- 2，新建一个git工程，并将将修改好的模版工程放进去，如：
```
http://u.32e.co:8122/git/root/demo1.git
    - {artifact}
        - src
            - main
                - java
                    - {package4path}
                - resources
            - test
    - README.md
```
- 3，在 application.properties 文件中修改配置： `template.mode.config=git`
- 4，在 resources/db/data.sql 文件中配置模版的远程仓库地址信息，其中用户名和密码可以为空，表示无需认证即可下载
- 5，启动工程，开始梦幻之旅

#### 方式二：resources 工程静态文件
- 1，根据文档介绍的变量，修改自己的项目，作为脚手架的模版
- 2，在 resources/project-template 目录下创建一个新模版目录，如：demo，然后将修改好的模版工程放进去
- 3，在 application.properties 文件中修改配置：
```
template.mode.config=resources
template.folder=demo
```
- 4，启动工程，开始梦幻之旅

#### 方式三：local 本地磁盘文件
- 1，根据文档介绍的变量，修改自己的项目，作为脚手架的模版
- 2，在本地 /xxx/project-template 目录下创建一个新模版目录，如：demo，然后将修改好的模版工程放进去
- 3，在 application.properties 文件中修改配置：
```
template.mode.config=local
template.local.path=/xxx/project-template
template.folder=demo
```
- 4，启动工程，开始梦幻之旅
