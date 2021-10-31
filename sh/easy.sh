#!/bin/bash

jar_path='/home/project-scaffold/project-scaffold.jar'

# JVM参数 debug模式
JVM_OPTS_DEBUG="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=10000,suspend=n"

function start(){
    PID=`ps -ef |grep java|grep $jar_path|grep -v grep|awk '{print $2}'`

        if [ x"$PID" != x"" ]; then
            echo  -e "\033[0;31m 程序 $jar_path 正在运行中，请先停止！ \033[0m"
        else
            if [[ ! -f "$jar_path" ]]; then
                echo  -e "\033[0;31m $jar_path 不存在，无法操作命令！ \033[0m"
            else
                nohup java -jar $jar_path  > /dev/null 2>&1 &
                        echo  -e "\033[0;34m 启动 $jar_path 成功！\033[0m"
            fi
        fi
}

function debug(){
    PID=`ps -ef |grep java|grep $jar_path|grep -v grep|awk '{print $2}'`

        if [ x"$PID" != x"" ]; then
            echo  -e "\033[0;31m 程序 $jar_path 正在运行中，请先停止！ \033[0m"
        else
            if [[ ! -f "$jar_path" ]]; then
                echo  -e "\033[0;31m $jar_path 不存在，无法操作命令！ \033[0m"
            else
                nohup java -jar $JVM_OPTS_DEBUG $jar_path  > /dev/null 2>&1 &
                        echo -e "\033[0;34m Debug模式 启动 $jar_path 成功！端口：10000 \033[0m"
            fi
        fi
}

function stop(){
        PID=`ps -ef |grep java|grep $jar_path|grep -v grep|awk '{print $2}'`

        if [ x"$PID" != x"" ]; then
                kill -9 $PID
                echo -e "\033[0;34m $jar_path 已停止 \033[0m"
        else
                echo -e "\033[0;31m 程序 $jar_path 未启动，不能操作！ \033[0m"
        fi
}

function restart(){
    stop
    sleep 2
    start
}

function status()
{
    PID=`ps -ef |grep java|grep $jar_path|grep -v grep|wc -l`
    if [ $PID != 0 ];then
        echo -e "\033[0;34m 程序 $jar_path 正在运行中 \033[0m"
        PID_NUM=`ps -ef |grep java|grep $jar_path|grep -v grep|awk '{print $2}'`
        echo -e "\033[0;34m 程序对应的PID： $PID_NUM \033[0m"
    else
        echo -e "\033[0;31m 程序 $jar_path 没有运行！ \033[0m"
    fi
}

function find_help(){
    echo -e "\033[0;31m 请输入正确的操作命令！ \033[0m"
    echo -e "\033[0;34m 命令参数：{start|stop|restart|status|debug} \033[0m"
    echo -e "\033[0;34m 用法： \033[0m"
    echo -e "\033[0;34m     start    启动应用程序 \033[0m"
    echo -e "\033[0;34m     stop     停止应用程序 \033[0m"
    echo -e "\033[0;34m     restart  重启应用程序 \033[0m"
    echo -e "\033[0;34m     status   查看应用程序运行状态和PID \033[0m"
    echo -e "\033[0;34m     debug    Debug模式启动应用程序 \033[0m"
    exit 1
}

if [ "$1" = "" ];then
    find_help;
    exit 1
fi

case $1 in
    start)
    start;;
    stop)
    stop;;
    restart)
    restart;;
    status)
    status;;
    debug)
    debug;;
    *)
    find_help;;
esac
