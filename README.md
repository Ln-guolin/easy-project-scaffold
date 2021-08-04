## 简介
应用脚手架

为快速拆分服务提供平台支持，自动构建可运行的基础单服务应用、编排文件、以及CI干预配置。

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
|@xx@|支持在文件、以及文件内容中使用|@artifact4hump@.java 或 @artifact4humpCase@Controller|
|{xx}|支持文件内容中使用，推荐使用，能防止springboot变量冲突|{artifact4humpCase}Controller|
