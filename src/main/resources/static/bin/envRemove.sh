#!/usr/bin/env bash

set -e

HOST_TYPE=$1
set +e
#还原yum源
sudo mv /etc/yum.repos.d/CentOS-Ftp.repo /data/paas/CentOS-Ftp.repo
set -e
sudo yum clean all
set +e
#停止ntpd,docker,flanneld
for SERVICES in ntpd docker flanneld; do 
    sudo systemctl stop $SERVICES
    sudo systemctl disable $SERVICES
done
set +e
#停止kubernetes服务
if [ ${HOST_TYPE} = 'master' ];then
    for SERVICES in etcd kube-apiserver kube-controller-manager kube-scheduler ; do 
        sudo systemctl stop $SERVICES
        sudo systemctl disable $SERVICES
    done
else
    for SERVICES in kube-proxy kubelet; do 
        sudo systemctl stop $SERVICES
        sudo systemctl disable $SERVICES
    done
fi

#卸载安装的包
sudo yum -y remove docker
sudo yum -y remove docker-selinux
sudo yum -y remove socat
sudo yum -y remove flannel
sudo yum -y remove ntp
sudo yum -y remove ntpdate
sudo yum -y remove net-tools
sudo yum -y remove kubernetes-node
sudo yum -y remove kubernetes-client
sudo yum -y remove kubernetes-master
sudo yum -y remove kubernetes
sudo yum -y remove etcd
