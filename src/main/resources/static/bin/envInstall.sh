#!/usr/bin/env bash

set -e

DOCKER_REGISTRY=$1
YUM_SOURCE=$2
HOST_TYPE=$3
MASTER_NAME=$4
HOST_NAME=$5

function set_yumEnv(){
#        mkdir /etc/yum.repos.d/old
#        mv /etc/yum.repos.d/*.* /etc/yum.repos.d/old
        sudo chmod 777 /etc/yum.repos.d
        sudo echo "[ftp]" >> /etc/yum.repos.d/CentOS-Ftp.repo
        sudo echo "name=ftp" >> /etc/yum.repos.d/CentOS-Ftp.repo
        sudo echo "baseurl=ftp://${YUM_SOURCE}/pub" >> /etc/yum.repos.d/CentOS-Ftp.repo
        sudo echo "enabled=1" >> /etc/yum.repos.d/CentOS-Ftp.repo
        sudo echo "gpgcheck=0" >> /etc/yum.repos.d/CentOS-Ftp.repo
        sudo echo "gpgkey=ftp://127.0.0.1/RPM-GPG-KEY-CentOS-7" >> /etc/yum.repos.d/CentOS-Ftp.repo
        sudo yum clean all
        
}

function clean_env(){
	    set +e
        sudo yum -y remove docker
	sudo yum -y remove docker-selinux
        sudo yum -y remove socat
        sudo yum -y remove flannel
        sudo yum -y remove ntp
        sudo yum -y remove net-tools
        sudo yum -y remove ntpdate
        sudo yum -y remove kubernetes-node
        sudo yum -y remove kubernetes-client
        sudo yum -y remove kubernetes-master
        sudo yum -y remove kubernetes
        sudo yum -y remove etcd
	    set -e
}

function install_rpm(){
	sudo yum -y install --enablerepo=ftp docker-selinux
	sudo yum -y install --enablerepo=ftp docker
        sudo yum -y install --enablerepo=ftp socat
        sudo yum -y install --enablerepo=ftp flannel
        sudo yum -y install --enablerepo=ftp ntp
        sudo yum -y install --enablerepo=ftp net-tools
        sudo yum -y install --enablerepo=ftp ntpdate
        sudo yum -y install --enablerepo=ftp kubernetes-node
        sudo yum -y install --enablerepo=ftp kubernetes-client
        sudo yum -y install --enablerepo=ftp kubernetes-master
        sudo yum -y install --enablerepo=ftp kubernetes
        sudo yum -y install --enablerepo=ftp etcd
}


# 配置本地YUM源
set_yumEnv
# 清理环镜
clean_env
# 安装rpm包
install_rpm

#启动ntp
sudo systemctl restart ntpd
sudo systemctl enable ntpd

set +e
#关闭防火墙,不自动启动
#systemctl stop iptables.service
#systemctl disable iptables.service
sudo systemctl stop firewalld.service
sudo systemctl disable firewalld.service
set -e

#修改k8s配置文件,启动服务
sudo chmod 777 /etc/kubernetes/config
sudo chmod 777 /etc/kubernetes/apiserver
sudo chmod 777 /etc/kubernetes/kubelet
sudo chmod 777 /etc/etcd/etcd.conf
sudo chmod 777 /etc/sysconfig/flanneld
sudo chmod 777 /etc/sysconfig/docker
sudo chmod 777 /etc/sysconfig/docker-storage

sudo echo "KUBE_ETCD_SERVERS=\"--etcd_servers=http://${MASTER_NAME}:2379\"" >> /etc/kubernetes/config
sudo sed -i "/KUBE_ALLOW_PRIV/c KUBE_ALLOW_PRIV=\"--allow_privileged=false\"" /etc/kubernetes/config
sudo sed -i "/KUBE_MASTER/c KUBE_MASTER=\"--master=http://${MASTER_NAME}:8080\"" /etc/kubernetes/config

if [ ${HOST_TYPE} = 'master' ];then
	sudo echo "master"
        sudo sed -i "/KUBE_API_ADDRESS/c KUBE_API_ADDRESS=\"--insecure-bind-address=0.0.0.0\"" /etc/kubernetes/apiserver
        sudo sed -i "/# KUBE_API_PORT/c KUBE_API_PORT=\"--port=8080\"" /etc/kubernetes/apiserver
        sudo sed -i "/# KUBELET_PORT/c KUBELET_PORT=\"--kubelet_port=10250\"" /etc/kubernetes/apiserver
        sudo sed -i "/KUBE_ETCD_SERVERS/c KUBE_ETCD_SERVERS=\"--etcd_servers=http://${MASTER_NAME}:2379\"" /etc/kubernetes/apiserver
        sudo sed -i "/KUBE_ADMISSION_CONTROL/c KUBE_ADMISSION_CONTROL=\"--admission_control=NamespaceLifecycle,NamespaceExists,LimitRanger,SecurityContextDeny,ResourceQuota\"" /etc/kubernetes/apiserver

        sudo sed -i "/ETCD_LISTEN_CLIENT_URLS/c ETCD_LISTEN_CLIENT_URLS=\"http://0.0.0.0:2379\"" /etc/etcd/etcd.conf
        sudo sed -i "/ETCD_ADVERTISE_CLIENT_URLS/c ETCD_ADVERTISE_CLIENT_URLS=\"http://localhost:2379\"" /etc/etcd/etcd.conf

        #启动K8S服务
        sudo systemctl restart etcd
        sudo systemctl enable etcd
        sudo systemctl status etcd
        sudo systemctl restart kube-apiserver
        sudo systemctl enable kube-apiserver
        sudo systemctl status kube-apiserver
        sudo systemctl restart kube-controller-manager
        sudo systemctl enable kube-controller-manager
        sudo systemctl status kube-controller-manager
        sudo systemctl restart kube-scheduler
        sudo systemctl enable kube-scheduler
        sudo systemctl status kube-scheduler
        #设置flanneld的IP段
        sudo etcdctl mk /cluster.hostgw/network/config '{"Network": "172.17.0.0/16", "Backend": {"Type": "host-gw"}}'

        sudo sed -i "/FLANNEL_ETCD=/c FLANNEL_ETCD=\"http://127.0.0.1:2379\"" /etc/sysconfig/flanneld
        sudo sed -i "/FLANNEL_ETCD_KEY=/c FLANNEL_ETCD_KEY=\"/cluster.hostgw/network\"" /etc/sysconfig/flanneld
        sudo systemctl restart flanneld
        sudo systemctl enable flanneld
        sudo systemctl status flanneld
else
	sudo echo "slaver"
        sudo sed -i "/KUBELET_ADDRESS/c KUBELET_ADDRESS=\"--address=0.0.0.0\"" /etc/kubernetes/kubelet
        sudo sed -i "/# KUBELET_PORT/c KUBELET_PORT=\"--port=10250\"" /etc/kubernetes/kubelet
        sudo sed -i "/KUBELET_HOSTNAME/c KUBELET_HOSTNAME=\"--hostname_override=${HOST_NAME}\"" /etc/kubernetes/kubelet
        sudo sed -i "/KUBELET_API_SERVER/c KUBELET_API_SERVER=\"--api_servers=http://${MASTER_NAME}:8080\"" /etc/kubernetes/kubelet
        sudo sed -i "/KUBELET_POD_INFRA_CONTAINER/c KUBELET_POD_INFRA_CONTAINER=\"--pod-infra-container-image=${DOCKER_REGISTRY}/pause:2.0\"" /etc/kubernetes/kubelet
        sudo sed -i "/KUBELET_ARGS/c KUBELET_ARGS=\"--cluster-dns=10.254.100.100 --cluster-domain=cluster.local\"" /etc/kubernetes/kubelet      
        #启动k8s服务
        sudo systemctl restart kube-proxy
        sudo systemctl enable kube-proxy
        sudo systemctl status kube-proxy
        sudo systemctl restart kubelet
        sudo systemctl enable kubelet
        sudo systemctl status kubelet

        sudo sed -i "/FLANNEL_ETCD=/c FLANNEL_ETCD=\"http://${MASTER_NAME}:2379\"" /etc/sysconfig/flanneld
        sudo sed -i "/FLANNEL_ETCD_KEY=/c FLANNEL_ETCD_KEY=\"/cluster.hostgw/network\"" /etc/sysconfig/flanneld
        sudo systemctl restart flanneld
        sudo systemctl enable flanneld
        sudo systemctl status flanneld

        #修改docker配置文件，允许远程访问
        sudo sed -i "/OPTIONS/c OPTIONS='--insecure-registry ${DOCKER_REGISTRY} --selinux-enabled -H tcp://0.0.0.0:28015 -H unix://var/run/docker.sock'" /etc/sysconfig/docker
       # sudo cat /etc/sysconfig/docker-storage | grep DOCKER_STORAGE
       set +e
       if [ /etc/sysconfig/docker-storage.rpmsave ]; then
       sudo echo "/etc/sysconfig/docker-storage.rpmsave exists"
       A=$(sudo grep -A 1 "\DOCKER_STORAGE_OPTIONS" /etc/sysconfig/docker-storage)
       sudo echo "$A"
       B=$(sudo grep -A 1 "\DOCKER_STORAGE_OPTIONS" /etc/sysconfig/docker-storage.rpmsave)
       sudo echo "$B"
       sudo sed -i -e "/$A/d" /etc/sysconfig/docker-storage
       sudo echo "$B" >> /etc/sysconfig/docker-storage  
       else
       sudo echo "/etc/sysconfig/docker-storage not exists!"
       fi
       set -e
        # 启动docker
        sudo systemctl restart docker
        sudo systemctl enable docker
        sudo systemctl status docker
fi
#设置docker的IP,重启DOCKER
#flanneldIp=$(ip a | grep flannel | grep inet | awk '{print $2}' | awk -F '/' '{print $1}')
#sudo ifconfig docker0 ${flanneldIp/.0/.1} netmask 255.255.255.0
#sudo systemctl restart docker
#sudo systemctl enable docker

sudo echo "install success"

