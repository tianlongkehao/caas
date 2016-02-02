$(document).ready(function () {
    document.onkeydown=function(){
        var a = window.event.keyCode;
        if( a == 9){
            window.event.returnValue = false;
        }
    };

    $(".nextTwo").click(function () {
        $(".radius_step").removeClass("action").eq(1).addClass("action");
        $(".step-inner").css("left", "-100%");
        var checkedClusters = [];
        $(':hidden[name=aaa]').each(function () {
            var b = $(this)[0].previousElementSibling.checked;
            if (b == true) {
                var checkedCluster = {};
                checkedCluster.host = $(this).val();
                checkedClusters.push(checkedCluster)
            }
        });
        $("#checkedClusters").val(JSON.stringify(checkedClusters));
    });

    $("#checkAllBox").click(function () {
        var checkAll = document.getElementsByName("checkAll");
        var checkbox = document.getElementsByName("checkbox");
        if (checkAll[0].checked == true) {
            for (var i = 0; i < checkbox.length; i++) {
                checkbox[i].checked = true;
            }
        } else {
            for (var i = 0; i < checkbox.length; i++) {
                checkbox[i].checked = false;
            }
        }
    });

    $(".checkBtn").click(function () {
        $(".radius_step").removeClass("action").eq(2).addClass("action");
        $(".step-inner").css("left", "-200%");
        var data = $("#checkedClusters").val();
        data = JSON.parse(data);
        var str = '';
        for (var i = 0; i < data.length; i++) {
            str += '<tr>';
            var tds1 = '<td name="rowHost">' + data[i].host + '</td>';
            var tds2 = '<td><input type="checkbox" onclick="chkMaster(this)"/></td>';
            var tds3 = '<td><input type="checkbox" onclick="chkSlave(this)"/></td>';
            /*var tds4 = '<td><input type="checkbox"/></td>';*/
            str += tds1 + tds2 + tds3;
            str += '</tr>';
        }
        $('#divId').html(str);
    });

    $(".installBtn").click(function () {
        $(".radius_step").removeClass("action").eq(3).addClass("action");
        $(".step-inner").css("left", "-300%");
        var rowHosts = document.getElementsByName("rowHost");
        var allRowsChecked = true;
        var rowsHostType = [];
        for (var i = 0; i < rowHosts.length; i++) {
            var rowHost = rowHosts[0];
            var host = rowHost.innerHTML;
            var rowMaster = rowHost.nextElementSibling;
            var masterChecked = rowMaster.childNodes[0].checked;
            var rowSlave = rowMaster.nextElementSibling;
            var slaveChecked = rowSlave.childNodes[0].checked;
            /*var rowEtcd = rowSlave.nextElementSibling;
            var etcdChecked = rowEtcd.childNodes[0].checked;*/
            if (masterChecked == false && slaveChecked == false) {
                layer.alert(host + "没有设置节点类型");
                allRowsChecked = false;
                return;
            }
            var hostType = {};
            hostType.host = host;
            if (masterChecked == true) {
                hostType.type = "master";
            } else if (slaveChecked == true) {
                hostType.type = "slave";
            } /*else if (etcdChecked == true) {
                hostType.type = "etcd";
            }*/
            rowsHostType.push(hostType);
        }
        if (allRowsChecked == true) {
            var rowsLength = rowsHostType.length;
            var rowNum = 0;
            var hostType = rowsHostType[rowNum];
            installEnv(hostType.host, hostType.type, rowNum, rowsLength, rowsHostType);
            layer.load(0, {shade: [0.3, '#000']});
        }

        return false

    });
    $(".last_step").click(function () {
        if ($(".radius_step").eq(1).hasClass("action")) {
            $(".step-inner").css("left", "0");
            $(".radius_step").removeClass("action").eq(0).addClass("action");
        } else if ($(".radius_step").eq(2).hasClass("action")) {
            $(".step-inner").css("left", "-100%");
            $(".radius_step").removeClass("action").eq(1).addClass("action");
        }
        else if ($(".radius_step").eq(3).hasClass("action")) {
            $(".step-inner").css("left", "-200%");
            $(".radius_step").removeClass("action").eq(2).addClass("action");
        }
    });
});
function chkMaster(ddd){
    var d = ddd.parentElement.nextElementSibling.children[0];
    if(ddd.checked == true){
        d.checked = false;
    }
};
function chkSlave(ccc){

    var c = ccc.parentElement.previousElementSibling.children[0];
    if(ccc.checked == true){
        c.checked = false;
    }
};

/*function setProcess(){
    var processbar = document.getElementById("processbar");
    processbar.style.width = parseInt(processbar.style.width) + 1 + "%";
    processbar.innerHTML = processbar.style.width;
    if(processbar.style.width == "100%"){
        window.clearInterval(bartimer);
    }
}
var bartimer = window.setInterval(function(){setProcess();},300);
window.onload = function(){
    debugger
    bartimer;
};*/
function installEnv(host, type, rowNum, rowsLength, rowsHostType) {
    rowNum = rowNum + 1;
    var str = "";
    str += '<tr>';
    var tds1 = '<td name="rowHost">' + host + '</td>';
    var tds2 = '<td><div class="processcontainer" style="border:1px solid #6C9C2C; height:25px">'+
        '<div id="processbar" style="width:0%;background:#95CA0D;float:left;height:100%;text-align:center;line-height:150%;"></div>'+
        '</div></td>';
    var tds3 = '<td id="qqq">安装进行中...</td>';
    /*var tds4 = '<td><a href="#">查看详情</a></td>';*/
    str += tds1 + tds2 + tds3;
    str += '</tr>';
    var divResult = $('#divResult')[0].innerHTML.trim();
    $('#divResult').html(divResult + str);

    var bartimer = window.setInterval(function(){setProcess();},300);
    window.onload = function(){
        bartimer;
    };
    function setProcess(){
        var processbar = document.getElementById("processbar");
        if(processbar.style.width > "90%"){
            window.clearInterval(bartimer);
        }
        processbar.style.width = parseInt(processbar.style.width) + 1 + "%";
        processbar.innerHTML = processbar.style.width;
    }
    $.ajax({
        url:ctx+"/cluster/installCluster?user=root&pass=a1s2d3&ip=" + host + "&port=22&type=" + type,
        success: function (data) {
            if(data == "安装成功"){
                setTimeout(function(){
                    layer.closeAll('loading');
                },2000);
                window.clearInterval(bartimer);
                var processbar = document.getElementById("processbar");
                processbar.style.width = "100%";
                processbar.innerHTML = processbar.style.width;
            }else{
                setTimeout(function(){
                    layer.closeAll('loading');
                },2000);
                window.clearInterval(bartimer);
                var processbar = document.getElementById("processbar");
                processbar.style.width = "0%";
                processbar.innerHTML = processbar.style.width;
            }
            $("#qqq")[0].innerHTML = data;
            if (rowNum < rowsLength) {
                var hostType = rowsHostType[rowNum];
                installEnv(hostType.host, hostType.type, rowNum, rowsLength);
            }

        }
    });
}
