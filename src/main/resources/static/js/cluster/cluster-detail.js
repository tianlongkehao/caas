$(function(){

    $(".Record").click(function(){

        $(".Record").removeClass("active");
        $(this).addClass("active");
        $(".tab_wrap").addClass("hide");


        /*document.getElementById("id").removeClass("hide");*/
        /*$("#"+document.getElementsByName('clusterHost')).removeClass("hide");*/
        debugger
        $("#"+$(this).attr("id")+"_wrap").removeClass("hide");
        debugger
    });







});



/*function resourceInfo(host, type, rowNum, rowsLength, rowsHostType) {
    rowNum = rowNum + 1;
    $.ajax({
        url: "/cluster/installCluster?user=root&pass=a1s2d3&ip=" + host + "&port=22&type=" + type,
        success: function (data) {
            var str = "";
            str += '<tr>';
            var tds1 = '<tr>'
            +'<td style="width:15%">CPU（核）</td>'
            +'<td style="width:25%">'
                +'<div class="slider_bj">'
                +'<div class="slider_block detailCpu"></div>'
                +'</div>'
                +'</td>'
                +'<td style="width:10%"><span id="detailCpu">-</span>（核）</td>'
            +'</tr>'
            var tds2 = '<tr>'
            +'<td style="width:15%">网络（M）</td>'
            +'<td style="width:25%">'
                +'<div class="slider_bj">'
                +'<div class="slider_block detailNetwork"></div>'
                +'</div>'
                +'</td>'
                +'<td style="width:10%"><span id="detailNetwork">-</span>10M</td>'
            +'</tr>'
            var tds3 = '<tr>'
            +'<td>内存（G）</td>'
            +'<td>'
            +'<div class="slider_bj">'
                +'<div class="slider_block detailMemory"></div>'
                +'</div>'
                +'</td>'
                +'<td><span id="detailMemory">-</span>/<span id="totalMemory">-</span>（G）</td>'
            +'</tr>'

            str += tds1 + tds2 + tds3;
            str += '</tr>';
            var divResultInfo = $('#divResultInfo')[0].innerHTML.trim();
            $('#divResultInfo').html(divResultInfo + str);
            if (rowNum < rowsLength) {
                var hostType = rowsHostType[rowNum];
                resourceInfo(hostType.host, hostType.type, rowNum, rowsLength);
            }
        }
    });
}*/




