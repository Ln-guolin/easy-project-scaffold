-- 模版和分支配置，以及git仓库的账号密码配置，可匿名访问的仓库账号密码可以为空
INSERT INTO `config_info`(name,url,branch,username,password,enable_def_variable) VALUES ('默认工程变量', 'http://u.32e.co:8122/git/root/demo1.git','master', 'root', 'root',1);
INSERT INTO `config_info`(name,url,branch,username,password,enable_def_variable,custom_variable) VALUES ('默认工程变量+自定义变量', 'http://u.32e.co:8122/git/root/demo2.git','master', 'root', 'root',1,'prefix,suffix');
INSERT INTO `config_info`(name,url,branch,username,password,enable_def_variable,custom_variable) VALUES ('自定义变量', 'http://u.32e.co:8122/git/root/demo3.git','master', 'root', 'root',0,'app-name,k8s-namespace,nacos-registry-addr,nacos-namespace,port,k8s-image-pull-secrets');
