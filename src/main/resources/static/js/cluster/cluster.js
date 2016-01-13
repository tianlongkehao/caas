$(function () {


    $(".Record").click(function () {

        $(".Record").removeClass("active");
        $(this).addClass("active");

        $(".tab_wrap").addClass("hide");
        $("#" + $(this).attr("id") + "_wrap").removeClass("hide");

    });

    var clusterCpuPer = $("#detailCpu")[0].textContent/$("#totalCpu")[0].textContent*100+"%";
    $("#clusterCpu")[0].style.width = clusterCpuPer;
    var clusterMemPer = $("#detailMemory")[0].textContent/$("#totalMemory")[0].textContent*100+"%";
    $("#clusterMem")[0].style.width = clusterMemPer;

    for(var i = 0; i < document.getElementsByName("clusterUseInfo").length; i++){
        var clustersCpuPer = $(".clusterDetCpuUse")[i].textContent/$(".clusterTotCpuLimit")[i].textContent*100+"%";
        $(".clusterCpuUse")[i].style.width = clustersCpuPer;
        var clustersMemPer = $(".clusterDetMemUse")[i].textContent/$(".clusterTotMemLimit")[i].textContent*100+"%";
        $(".clusterMemUse")[i].style.width = clustersMemPer;
    }

});

/**
 * 删除选中集群
 */
function delCluster() {
    var id = "";
    $(":checked[name='hosts']").each(function () {
        id = id + jQuery(this).val() + ",";
    });
    if ("" == id) {
        alert("请选择至少一个节点");
        return;
    }
    else {
        id = id.substring(0, id.length - 1);
        layer.open({
            type: 1,
            title: '删除集群节点',
            content: '确定删除多个节点吗？',
            btn: ['确定', '取消'],
            yes: function(index, layero){ //或者使用btn1
                debugger
                layer.close(index);
                $.ajax({
                    url:"/cluster/delMul.do?hosts="+id,
                    success:function(data){
                        debugger
                        data = eval("(" + data + ")");
                        if(data.status=="200"){
                            alert("节点信息删除成功");
                        }else{
                            alert("节点信息删除失败，请检查服务器连接");
                        }
                        location.href = "redirect:/cluster/management";
                    }
                })

            },
            cancel: function(index){ //或者使用btn2
                //按钮【按钮二】的回调
            }
        });
    }
}

