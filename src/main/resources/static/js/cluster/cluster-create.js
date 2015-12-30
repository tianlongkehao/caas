$(document).ready(function () {
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
                alert(host + "没有设置节点类型");
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
        }
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
function chkMaster(ddd) {
    var d = ddd.parentElement.nextElementSibling.children[0].checked;
    if (d == true) {
        alert(222)
        ddd.checked = false;
    }
}
function installEnv(host, type, rowNum, rowsLength, rowsHostType) {
    rowNum = rowNum + 1;
    $.ajax({
        url: "/cluster/installCluster?user=root&pass=a1s2d3&ip=" + host + "&port=22&type=" + type,
        success: function (data) {
            var str = "";
            str += '<tr>';
            var tds1 = '<td name="rowHost">' + host + '</td>';
            var tds2 = '<td>100%</td>';
            var tds3 = '<td>' + data + '</td>';
            /*var tds4 = '<td><a href="#">查看详情</a></td>';*/
            str += tds1 + tds2 + tds3;
            str += '</tr>';
            var divResult = $('#divResult')[0].innerHTML.trim();
            $('#divResult').html(divResult + str);
            if (rowNum < rowsLength) {
                var hostType = rowsHostType[rowNum];
                installEnv(hostType.host, hostType.type, rowNum, rowsLength);
            }
        }
    });
}
