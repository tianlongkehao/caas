$(document).ready(function(){
	
	$("#createButton").click(function(){
            $("#createService").ajaxSubmit({
                type: "post",
                success: function (data) {
                    data = eval("(" + data + ")");
                    if (data.status == "200") {
                        $(".contentMain").load("/service");
                    } else {
                        layer.alert(data.msg);
                    }
                }
            });
    });
	
	var go_back_step = 0;

    $(".pull-deploy").click(function(){
    	
    	go_back_step = 1;
        $(".step-inner").css("left","-100%");
        $(".createPadding").removeClass("hide");
        $(".radius_step").removeClass("action").eq(1).addClass("action");
        $(".two_step").removeClass("hide");

    });

    $(".two_step").click(function(){
    	
    	go_back_step = 2;
        $(this).addClass("hide");
        $(".createPadding").removeClass("hide");
        $(".radius_step").removeClass("action").eq(2).addClass("action");
        $(".step-inner").css("left","-200%");

    });

    $(".go_backs").click(function(){

        if($(".radius_step").eq(1).hasClass("action")){
            $(".createPadding").addClass("hide");
            $(".step-inner").css("left","0");
            $(".radius_step").removeClass("action").eq(0).addClass("action");
        }else if($(".radius_step").eq(2).hasClass("action")){
            $(".createPadding").removeClass("hide");
            $(".step-inner").css("left","-100%");
            $(".radius_step").removeClass("action").eq(1).addClass("action");
        }

    });
    
    /**************************************************************************
     ***************************  Create new containers  ***********************
     ***************************************************************************/
     $('#createButton').click(function () {
       // console.log(mountPath);
       var master = $('#hosting-cluster').val();
       var name = $('#containerName').val().trim();
       var image = $('#getImageName').val();
       // var image = $('#imageName').text() + ':' + $('.imageName .version-text').text();
       var config = $('.set_container.active').attr('config');
       // Define the pull policy for new container
       var pullPolicy = $('input[name="pullPolicy"]:checked').length;
       var storageInfo;
       var env = [];
       var ports = [];
       var number = 1;
       name = name.toLowerCase();

       // if user click create in step 2 ,check the name、number ...
       if (go_back_step == 1) {
         // check the name of container
         if(!name || name.length < 1){
           layer.tips('容器名称不能为空','#containerName',{tips: [1, '#3595CC']});
           $('#containerName').focus();
           return;
         }
         name = name.toLowerCase();
         if(name.search(/^[a-z][a-z0-9-]*$/) === -1){
           layer.tips('容器名称只能由字母、数字及横线组成，且首字母不能为数字及横线。','#containerName',{tips: [1, '#3595CC'],time: 3000});
           $('#containerName').focus();
           return;
         }
         if(name.length > 24 || name.length < 3){
           layer.tips('容器名称为3~24个字符','#containerName',{tips: [1, '#3595CC'],time: 3000});
           $('#containerName').focus();
           return;
         }
         // check number of containers
         if(!$('#instsize').is(':hidden')){
           var numberStr = $('#instsize .number').val();
           if(!numberStr){
             layer.tips('请填写实例数量','#instsize .number',{tips: [1, '#3595CC'],time: 3000});
             return;
           }
           number = parseInt(numberStr);
           /*if (number > 3) {
             layer.tips('实例数量不允许超过3个','#instsize .number',{tips: [1, '#3595CC'],time: 3000});
             return;
           }
           if (number < 1) {
             layer.tips('实例数量填写错误','#instsize .number',{tips: [1, '#3595CC'],time: 3000});
             return;
           }*/
         }
         // check volume
         if($('#state_service').is(':checked') && !$('#state_service').attr('disabled')){
           var isVolumeCreate = $('#save_roll_dev').attr('create');
           if (isVolumeCreate && isVolumeCreate == 1) {
             layer.tips('请先创建一个存储卷，或取消有状态服务', '#fastCreateVolume', {tips: [1, '#3595CC'],time: 3000});
             return;
           }
           var isSelectVolume = true;
           $('#mountPathList li').each(function (index, el) {
             if ($(el).find('.selectVolume').val() == 0) {
               isSelectVolume = false;
               layer.tips('请选择一个存储卷，或取消有状态服务', $(el).find('.selectVolume'), {tips: [1, '#3595CC'],time: 3000});
               return false;
             }
           });
           if (!isSelectVolume) {
             return;
           }
         }
       }
       // 1.Get mountpath
       if($('#state_service').attr('stateless') != 1 && $('#state_service').is(':checked')){
         storageInfo = {type:'', data: []};
         if (master != 'tenx_district2') {
           storageInfo.type = 'public';
         } else {
           storageInfo.type = 'private';
         }
         $('#mountPathList .mount').each(function(index, el) {
           var mountPathVal = $(el).find('.ve_top').text().trim();
           var diskName = $(el).find('.selectVolume').val();
           var isReadonly = $(el).find('.isVolumeReadonly').prop("checked");
           if (diskName != 0) {
             var mountData = {};
             mountData.mountPath = mountPathVal;
             mountData.diskName = diskName;
             mountData.readyOnly = isReadonly;
             mountData.uid = $('option[value='+ diskName +']').attr('uid');
             storageInfo.data.push(mountData);
           }
         });
       }
       // console.log(storageInfo);
       // 2.Get env
       var checkEnv = true;
       $('#Path-oper tr').each(function(index, el) {
         // Skip undefined values
         if ($(el).find('.keys input').val() === undefined) {
           return;
         }
         var key = $(el).find('.keys input').val().trim();
         var value = $(el).find('.vals input').val().trim();
         var oldValue = $(el).find('.oldValue').val();

         key = key.toUpperCase();
         if (!oldValue && (!key || key == '')) {
           if (go_back_step == 1) {
             $('.two_step').click();
           }
           layer.tips('请填写环境变量名', $(el).find('.keys input'), {tips: [1, '#3595CC'], time: 3000});
           $(el).find('.keys input').focus();
           checkEnv = false;
           return false;
         }
         if (key.search(/^[A-Za-z_][A-Za-z0-9_]*$/) === -1) {
           if (go_back_step == 1) {
             $('.two_step').click();
           }
           layer.tips('环境变量名由字母、数字、下划线组成，且不能以数字开头', $(el).find('.keys input'), {tips: [1, '#3595CC'], time: 3000});
           $(el).find('.keys input').focus();
           checkEnv = false;
           return false;
         }
         if (oldValue === undefined && (!value || value == '')) {
           if (go_back_step == 1) {
             $('.two_step').click();
           }
           layer.tips('请填写环境变量值', $(el).find('.vals'), {tips: [1, '#3595CC'], time: 3000});
           $(el).find('.vals input').focus();
           checkEnv = false;
           return false;
         }
         if (value && value != '' && value != oldValue) {
           var envItem = {};
           envItem.name = key;
           envItem.value = value;
           env.push(envItem)
         }
       });
       if (!checkEnv) {
         return;
       }
       // console.log(env);
       // 3.Get ports
       var checkPort = true;
       if ($('#pushPrptpcol tr.plus-row').length > portNumber) {
         layer.msg('容器端口不能超过' + portNumber + '个', {icon: 0});
         return;
       }
       var portObj ={};
       $('#pushPrptpcol tr.plus-row').each(function(index, el) {
         var port = $(el).find('.port').val();
         var protocol = $(el).find('.T-http').val();
         if (!port || port.trim() == '') {
           if (go_back_step == 1) {
             $('.two_step').click();
           }
           layer.tips('请填写端口', $(el).find('.port'), {tips: [1, '#3595CC'], time: 3000});
           $(el).find('.port').focus();
           checkPort = false;
           return false;
         }
         port = parseInt(port);
         if (isNaN(port) || port < 1 || port > 65535) {
           if (go_back_step == 1) {
             $('.two_step').click();
           }
           layer.tips('端口格式填写错误', $(el).find('.port'), {tips: [1, '#3595CC'], time: 3000});
           $(el).find('.port').focus();
           checkPort = false;
           return false;
         }
         if (!portObj[port]) {
           var portItem = {};
           portItem.port = port;
           portItem.protocol = protocol;
           ports.push(portItem);
           portObj[port] = 1;
         }
       });
       if (!checkPort) {
         return;
       }

       var json = {
         name: name,
         image: image,
         imageType: $('#imageType').val(),
         pullPolicy: pullPolicy,
         ports: ports,
         env: env.length > 0 ? env: null,
         config: config,
         size: number,
         mountPath: mountPath,
         labels: {name: name},
         storageInfo: storageInfo
       }
       if(!(master.indexOf('tenx_') == 0 || master == "default")) {
         var hostingHost = $('#hosting-host-1').text().match(/[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}$/);
         if (hostingHost && hostingHost.length>0){
           json.hostingHost = hostingHost;  // a list
         }
         var hostingOut = $('#hosting-service-out').text().match(/^[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}/);
         if (hostingOut && hostingOut.length > 0) {
           if (hostingOut.length==1){
             json.hostingServiceOut = hostingOut[0]; // public ip
           } else {
             json.hostingServiceOut = hostingOut[1]; // public ip
           }
         }
       }
       var info = JSON.stringify(json);
       // show loading~
       index = layer.load(0, {
         shade: [0.5,'#fff'] //0.1透明度的白色背景
       });
       $.ajax({
         url: '/containers/'+master+'/create',
         type: 'POST',
         data: info ,
         contentType: 'application/json',
         dataType: 'json'
       }).done(function (cluster) {
         window.location = '/containers?0';
       }).fail(function (err) {
         if(err.responseJSON.message){
           layer.alert(JSON.stringify(err.responseJSON.message),{icon: 0, title: '创建失败'});
         } else if(err.responseJSON){
           layer.alert(err.responseJSON,{icon: 0, title: '创建失败'});
         } else {
           layer.msg('创建容器失败',{icon: 2});
         }
       }).complete(function () {
         layer.close(index);
       });
     });
    

});