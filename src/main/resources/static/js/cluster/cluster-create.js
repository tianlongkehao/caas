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
    $(".checkBtn").click(function () {
        $(".radius_step").removeClass("action").eq(2).addClass("action");
        $(".step-inner").css("left", "-200%");
        var data = $("#checkedClusters").val();
        data = JSON.parse(data);
        var str = '';
        for (var i = 0; i < data.length; i++) {
            str += '<tr>';
            var tds1 = '<td>' + data[i].host + '</td>';
            var tds2 = '<td><input type="checkbox"/></td>';
            var tds3 = '<td><input type="checkbox"/></td>';
            var tds4 = '<td><input type="checkbox"/></td>';
            str += tds1 + tds2 + tds3 + tds4;
            str += '</tr>';
        }
        $('#divId').html(str);
    });
    $(".installBtn").click(function () {
        $(".radius_step").removeClass("action").eq(3).addClass("action");
        $(".step-inner").css("left", "-300%");

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





