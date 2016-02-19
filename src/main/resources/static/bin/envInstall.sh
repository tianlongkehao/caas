#!/usr/bin/env bash

set -e

DOCKER_REGISTRY=$1
YUM_SOURCE=$2
HOST_TYPE=$3
MASTER_NAME=$4
HOST_NAME=$5

function set_yumEnv(){
        mkdir /etc/yum.repos.d/old
        mv /etc/yum.repos.d/*.* /etc/yum.repos.d/old
        echo "[ftp]" >> /etc/yum.repos.d/CentOS-Ftp.repo
        echo "name=ftp" >> /etc/yum.repos.d/CentOS-Ftp.repo
        echo "baseurl=ftp://${YUM_SOURCE}/pub" >> /etc/yum.repos.d/CentOS-Ftp.repo
        echo "enabled=1" >> /etc/yum.repos.d/CentOS-Ftp.repo
        echo "gpgcheck=0" >> /etc/yum.repos.d/CentOS-Ftp.repo
        echo "gpgkey=ftp://127.0.0.1/RPM-GPG-KEY-CentOS-7" >> /etc/yum.repos.d/CentOS-Ftp.repo
        yum clean all
        
}

function clean_env(){
	    set +e
        yum -y remove docker
	    yum -y remove docker-selinux
        yum -y remove socat
        yum -y remove flannel
        yum -y remove ntp
        yum -y remove net-tools
        yum -y remove ntpdate
        yum -y remove kubernetes-node
        yum -y remove kubernetes-client
        yum -y remove kubernetes-master
        yum -y remove kubernetes
        yum -y remove etcd
	    set -e
}

function install_rpm(){
	    yum -y install --enablerepo=ftp docker-selinux
	    yum -y install --enablerepo=ftp docker
        yum -y install --enablerepo=ftp socat
        yum -y install --enablerepo=ftp flannel
        yum -y install --enablerepo=ftp ntp
        yum -y install --enablerepo=ftp net-tools
        yum -y install --enablerepo=ftp ntpdate
        yum -y install --enablerepo=ftp kubernetes-node
        yum -y install --enablerepo=ftp kubernetes-client
        yum -y install --enablerepo=ftp kubernetes-master
        yum -y install --enablerepo=ftp kubernetes
        yum -y install --enablerepo=ftp etcd
}


# 配置本地YUM源
set_yumEnv
# 清理环镜
clean_env
# 安装rpm包
install_rpm

#修改docker配置文件，允许远程访问
sed -i "/OPTIONS/c OPTIONS='--insecure-registry ${DOCKER_REGISTRY} --selinux-enabled -H tcp://0.0.0.0:28015 -H unix://var/run/docker.sock'" /etc/sysconfig/docker
# 启动docker
systemctl restart docker
systemctl enable docker

#启动ntp
systemctl restart ntpd
systemctl enable ntpd

#关闭防火墙,不自动启动
#systemctl stop iptables.service
#systemctl disable iptables.service
systemctl stop firewalld.service
systemctl disable firewalld.service

#修改k8s配置文件,启动服务
echo "KUBE_ETCD_SERVERS=\"--etcd_servers=http://${MASTER_NAME}:2379\"" >> /etc/kubernetes/config
sed -i "/KUBE_ALLOW_PRIV/c KUBE_ALLOW_PRIV=\"--allow_privileged=false\"" /etc/kubernetes/config
sed -i "/KUBE_MASTER/c KUBE_MASTER=\"--master=http://${MASTER_NAME}:8080\"" /etc/kubernetes/config

if [ ${HOST_TYPE} = 'master' ];then
	    echo "master"
        sed -i "/KUBE_API_ADDRESS/c KUBE_API_ADDRESS=\"--address=0.0.0.0\"" /etc/kubernetes/apiserver
        sed -i "/# KUBE_API_PORT/c KUBE_API_PORT=\"--port=8080\"" /etc/kubernetes/apiserver
        sed -i "/# KUBELET_PORT/c KUBELET_PORT=\"--kubelet_port=10250\"" /etc/kubernetes/apiserver
        sed -i "/KUBE_ETCD_SERVERS/c KUBE_ETCD_SERVERS=\"--etcd_servers=http://${MASTER_NAME}:2379\"" /etc/kubernetes/apiserver
        sed -i "/KUBE_ADMISSION_CONTROL/c KUBE_ADMISSION_CONTROL=\"--admission_control=NamespaceLifecycle,NamespaceExists,LimitRanger,SecurityContextDeny,ResourceQuota\"" /etc/kubernetes/apiserver

        sed -i "/ETCD_LISTEN_CLIENT_URLS/c ETCD_LISTEN_CLIENT_URLS=\"http://0.0.0.0:2379\"" /etc/etcd/etcd.conf
        sed -i "/ETCD_ADVERTISE_CLIENT_URLS/c ETCD_ADVERTISE_CLIENT_URLS=\"http://localhost:2379\"" /etc/etcd/etcd.conf

        #启动K8S服务
        systemctl restart etcd
        systemctl enable etcd
        systemctl status etcd
        systemctl restart kube-apiserver
        systemctl enable kube-apiserver
        systemctl status kube-apiserver
        systemctl restart kube-controller-manager
        systemctl enable kube-controller-manager
        systemctl status kube-controller-manager
        systemctl restart kube-scheduler
        systemctl enable kube-scheduler
        systemctl status kube-scheduler
        #设置flanneld的IP段
        etcdctl mk /coreos.com/network/config '{"Network": "172.17.0.0/16", "Backend": {"Type": "vxlan"}}'

        sed -i "/FLANNEL_ETCD=/c FLANNEL_ETCD=\"http://127.0.0.1:2379\"" /etc/sysconfig/flanneld
        sed -i "/FLANNEL_ETCD_KEY=/c FLANNEL_ETCD_KEY=\"/coreos.com/network\"" /etc/sysconfig/flanneld
        systemctl restart flanneld
        systemctl enable flanneld
        systemctl status flanneld
else
	    echo "slaver"
        sed -i "/KUBELET_ADDRESS/c KUBELET_ADDRESS=\"--address=0.0.0.0\"" /etc/kubernetes/kubelet
        sed -i "/# KUBELET_PORT/c KUBELET_PORT=\"--port=10250\"" /etc/kubernetes/kubelet
        sed -i "/KUBELET_HOSTNAME/c KUBELET_HOSTNAME=\"--hostname_override=${HOST_NAME}\"" /etc/kubernetes/kubelet
        sed -i "/KUBELET_API_SERVER/c KUBELET_API_SERVER=\"--api_servers=http://${MASTER_NAME}:8080\"" /etc/kubernetes/kubelet
        sed -i "/KUBELET_ARGS/c KUBELET_ARGS=\"--pod-infra-container-image=${DOCKER_REGISTRY}/pause:0.8.0\"" /etc/kubernetes/kubelet

        #启动k8s服务
        systemctl restart kube-proxy
        systemctl enable kube-proxy
        systemctl status kube-proxy
        systemctl restart kubelet
        systemctl enable kubelet
        systemctl status kubelet

        sed -i "/FLANNEL_ETCD=/c FLANNEL_ETCD=\"http://${MASTER_NAME}:2379\"" /etc/sysconfig/flanneld
        sed -i "/FLANNEL_ETCD_KEY=/c FLANNEL_ETCD_KEY=\"/coreos.com/network\"" /etc/sysconfig/flanneld
        systemctl restart flanneld
        systemctl enable flanneld
        systemctl status flanneld
fi
#设置docker的IP,重启DOCKER
flanneldIp=$(ip a | grep flannel | grep inet | awk '{print $2}' | awk -F '/' '{print $1}')
ifconfig docker0 ${flanneldIp/.0/.1} netmask 255.255.255.0
systemctl restart docker
systemctl enable docker

echo "install success"

