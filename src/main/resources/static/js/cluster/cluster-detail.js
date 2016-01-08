$(function(){

    $(".Record").click(function(){

        $(".Record").removeClass("active");
        $(this).addClass("active");

        $(".tab_wrap").addClass("hide");
        $("#"+$(this).attr("id")+"_wrap").removeClass("hide");

    });


    $("#detailInfo").click(function () {
        var data = $("#checkedClusters").val();
        data = JSON.parse(data);
        var str = '';
        for (var i = 0; i < data.length; i++) {
            str += '<div>';
            var tds1 = '<a id="resourceinfo" class="Record action">172.16.71.146</a>';
            str += tds1;
            str += '</div>';
        }
        $('#resourceTab').html(str);

    });

});

function resourceInfo(host, type, rowNum, rowsLength, rowsHostType) {
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
}




