<p align="center">
	<a href="https://github.com/Ln-guolin/easy-project-scaffold"><img src="https://soilove.oss-cn-hangzhou.aliyuncs.com/32e/pro-mall/easy-p-s.png" height="150px"></a>
</p>

## 简介

easy-project-scaffold(简易工程脚手架)，为快速构建项目以及自定义模版替换提供平台和工具支持。

演示地址：[http://easyps.32e.co/](http://easyps.32e.co/)


## 默认输入参数

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

### 自定义变量

- 系统支持变量自定义，可以在git模式下使用，详情可参考下文：" 方式一：git 远程仓库（推荐）"

### 模版变量占位符语法

|语法|适用范围|案例|
|---|---|---|
|{xx}|【推荐】支持在文件夹、文件、以及文件内容中使用|{artifact4hump}.java 或 {artifact4humpCase}Controller|
|@xx@|支持在文件夹、文件、以及文件内容中使用|@artifact4hump@.java 或 @artifact4humpCase@Controller|

模版Demo示例: [project-template/demo](https://github.com/Ln-guolin/potat-project-scaffold/tree/main/src/main/resources/project-template/demo)

### 模版配置

#### 方式一：git 远程仓库（推荐）
- 1，根据文档介绍的变量，修改自己的项目，作为脚手架的模版
- 2，新建一个git工程，并将将修改好的模版工程放进去，git工程示例：[http://u.32e.co:8122/](http://u.32e.co:8122/)
- 3，在 application.properties 文件中修改配置： `template.mode.config=git`
- 4，在 resources/db/config.sql 文件中配置模版的远程仓库地址信息，其中用户名和密码可以为空，表示无需认证即可下载，如：
```sql
-- 模版和分支配置，以及git仓库的账号密码配置，可匿名访问的仓库账号密码可以为空
INSERT INTO `config_info`(name,url,branch,username,password,enable_def_variable) VALUES ('默认工程变量', 'http://u.32e.co:8122/git/root/demo1.git','master', 'root', 'root',1);
INSERT INTO `config_info`(name,url,branch,username,password,enable_def_variable,custom_variable) VALUES ('默认工程变量+自定义变量', 'http://u.32e.co:8122/git/root/demo2.git','master', 'root', 'root',1,'prefix,suffix');
INSERT INTO `config_info`(name,url,branch,username,password,enable_def_variable,custom_variable) VALUES ('自定义变量', 'http://u.32e.co:8122/git/root/demo3.git','master', 'root', 'root',0,'app-name,k8s-namespace,nacos-registry-addr,nacos-namespace,port,k8s-image-pull-secrets');
```
##### config_info表字段解释：

|字段名|是否必填|字段描述|
|---|---|---|
|name|是|模版名称，自定义|
|url|是|git仓库地址|
|branch|是|git仓库分支|
|username|否|登录git仓库的用户名，为空表示无需认证|
|password|否|登录git仓库的密码，为空表示无需认证|
|enable_def_variable|否|是否需要开启默认变量，标准工程创建一般需要开启，可选值：1或0|
|custom_variable|否|自定义变量，多个自定义变量使用英文逗号分隔，如：v1,v2|

- 5，启动工程使用

#### 方式二：resources 工程静态文件
- 1，根据文档介绍的变量，修改自己的项目，作为脚手架的模版
- 2，在 resources/project-template 目录下创建一个新模版目录，如：demo，然后将修改好的模版工程放进去
- 3，在 application.properties 文件中修改配置：
```
template.mode.config=resources
template.folder=demo
```
- 4，启动工程使用

#### 方式三：local 本地磁盘文件
- 1，根据文档介绍的变量，修改自己的项目，作为脚手架的模版
- 2，在本地 /xxx/project-template 目录下创建一个新模版目录，如：demo，然后将修改好的模版工程放进去
- 3，在 application.properties 文件中修改配置：
```
template.mode.config=local
template.local.path=/xxx/
template.folder=demo
```
- 4，启动工程使用

## 如何部署脚手架平台
- 1，运行mvn clean package完成打包
- 2，拷贝project-scaffold.jar到服务器
- 3，运行`sh`目录下的`easy.sh`脚本即可(需要自己修改对应的jar路径)，如：
```
sh easy.sh start
```
