
 function addNode(obj){
     var nodename = $(obj).attr('nodename');
     layer.open({
			title : '节点加入',
			content : '确定将节点加入集群吗？',
			btn : [ '确定', '取消' ],
			yes : function(index, layero) {
				layer.close(index);
				$.ajax({
					url : "" + ctx+ "/cluster/addnode?nodename="+ nodename,
					success : function(data) {
						data = eval("(" + data + ")");
						if (data.status == "200") {
							layer.msg("节点已加入集群",{icon: 6});
							setTimeout('window.location.reload()',1500);
						} else {
							layer.alert("节点加入失败，请检查服务器连接");
						}
					}
				});
			}
		});
 }

 function deleteNode(obj){
	 var nodename = $(obj).attr('nodename');
     layer.open({
			title : '节点强制隔离',
			content : '强制隔离将删除节点上的所有Pod，确定将节点从集群中强制隔离吗？',
			btn : [ '确定', '取消' ],
			yes : function(index, layero) {
				layer.close(index);
				$.ajax({
					url : "" + ctx+ "/cluster/deletenode?nodename="+ nodename,
					success : function(data) {
						data = eval("(" + data + ")");
						if (data.status == "200") {
							layer.msg("节点已被隔离",{icon: 6});
							setTimeout('window.location.reload()',1500);
						} else {
							layer.alert("节点隔离失败，请检查服务器连接");
						}
					}
				});
			}
		});
 }

 function partdeleteNode(obj){
	 var nodename = $(obj).attr('nodename');
     layer.open({
			title : '节点软隔离',
			content : '软隔离，将保留节点上已有的Pod,确定将节点从集群中软隔离吗？',
			btn : [ '确定', '取消' ],
			yes : function(index, layero) {
				layer.close(index);
				$.ajax({
					url : "" + ctx+ "/cluster/partdeletenode?nodename="+ nodename,
					success : function(data) {
						data = eval("(" + data + ")");
						if (data.status == "200") {
							layer.msg("节点已被隔离",{icon: 6});
							setTimeout('window.location.reload()',1500);
						} else {
							layer.alert("节点隔离失败，请检查服务器连接");
						}
					}
				});
			}
		});
 }

 function nodedetail(obj){
   var nodename = $(obj).attr('nodename');
   $.ajax({
		url : ctx + "/cluster/nodedetail?nodename="+nodename,
		success: function(data) {
			var data = eval("("+data+")");

            $("#nodename").val(data.node.metadata.name);
            $("#ip").val(data.node.status.addresses[0].address);
            $("#status").val(data.status!=null?'隔离':'正常');
            $("#nodestatus").val(data.node.status.conditions[1].status=='True'?'Ready':'NotReady');
            $("#cpu").val(data.node.status.capacity.cpu);
            $("#memory").val(data.node.status.capacity.memory);
            $("#pod").val(data.node.status.capacity.pods);

            $('#podTable').empty();
            	for(var i=0;i<data.pods.length;i++){
                    var tr = '<tr><td>'+data.pods[i].metadata.name+'</td>'
                                +'<td>'+data.pods[i].metadata.namespace+'</td>'
                                +'<td>'+data.pods[i].status.phase+'</td></tr>';
                    $('#podTable').append(tr);
            	};
            layer.open({
			 	type: 1,
		        title: '节点信息',
		        content: $("#nodedetail"),
		        area: ['600px'],
		        btn: false,
		    })

		}
	});

 }

 function conditonchanged(){
	 $('#nodelisttable').empty();
	 var clusterstatus = $('#cluster_status').val();
	 var nodestatus =$('#node_status').val();
	 var ip=$("#ip_content").val();
	 $.ajax({
			url : "" + ctx+ "/cluster/nodeinfo?clusterstatus="+ clusterstatus+"&nodestatus="+nodestatus+"&ip="+ip,
			success : function(data) {
				data = eval("(" + data + ")");
				if (data.status == "200") {
					var tr='';
					data = data.nodelist;
                    for(var i=0;i<data.length;i++){
                        //alert(data[i].spec.unschedulable);
                    	tr+='<tr><td style="width: 20%; text-align: center;"><a href="javascript:void(0)" onclick="nodedetail(this)" nodename="'+data[i].metadata.name+'" title="查看详细信息" '
						   + ' onmousemove="style.textDecoration=\'underline\'"'
						   + ' onmouseout="style.textDecoration=\'none\'">'+data[i].metadata.name+'</a></td>'
						   + '<td style="width: 20%; text-align: center;">'+data[i].status.addresses[0].address+'</td>';

                    	if(data[i].spec.unschedulable !=true){
                    		tr+='<td style="width: 20%; text-align: center;">正常</td>';
                    	}else{
                    		tr+='<td style="width: 20%; text-align: center;">隔离</td>';
                    	}

                    	if(data[i].status.conditions[1].status=='True'){
                    		tr+='<td style="width: 20%; text-align: center;">Ready</td>';
                    	}else{
                    		tr+='<td style="width: 20%; text-align: center;">NotReady</td>';
                    	}

                    	if(data[i].spec.unschedulable !=true){
                    		tr+='<td style="width: 20%; text-align: center;"><a class="no-drop a-oper" href="javascript:void(0)" onclick="partdeleteNode(this)" title="软隔离" nodename="'+data[i].metadata.name+'"> <i class="fa fa-chain-broken"></i></a>'
							  +'<a class="no-drop a-oper" href="javascript:void(0)" onclick="deleteNode(this)" title="强制隔离" nodename="'+data[i].metadata.name+'"> <i class="fa fa-trash"></i></a></td>';
                    	}else{
                    		tr+='<td style="width: 20%; text-align: center;"><a class="no-drop a-oper" href="javascript:void(0)" onclick="addNode(this)" title="加入" nodename="'+data[i].metadata.name+'"> <i class="fa fa-reply"></i></a></td>';
                    	}
                    	tr+='</tr>';

                    }
                    $('#nodelisttable').append(tr);
				} else {
					layer.alert("读取失败，请检查服务器连接!");
				}
			}
		});
 }
