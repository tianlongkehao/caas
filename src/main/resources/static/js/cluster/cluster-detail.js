$(function(){

    $(".Record").click(function(){

        $(".Record").removeClass("active");
        $(this).addClass("active");
        $(".tab_wrap").addClass("hide");
        debugger
        $("#"+$(this).attr("id")+"_wrap").removeClass("hide");
        debugger
    });


    for(var i = 0; i < document.getElementsByName("clusterHost").length; i++){
        var cpuPer = $(".detailCpus")[i].textContent/$(".totalCpus")[i].textContent*100+"%";
        $(".cpuPercents")[i].style.width = cpuPer;
        var memPer = $(".detailMems")[i].textContent/$(".totalMems")[i].textContent*100+"%";
        $(".memPercents")[i].style.width = memPer;
        var memPer = $(".detailMemsSet")[i].textContent/$(".totalMemsSet")[i].textContent*100+"%";
        $(".workDetailMems")[i].style.width = memPer;
    }


    /*var cpuPer = document.getElementById("detailCpu").textContent/document.getElementById("totalCpu").textContent*100+"%";
    document.getElementById("cpuPercent").style.width = cpuPer;*/


});





