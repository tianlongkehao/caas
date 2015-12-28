/**
 * Created by cuidong on 15-12-24.
 */

$(function(){

    $(".Record").click(function(){

        $(".Record").removeClass("active");
        $(this).addClass("active");

        $(".tab_wrap").addClass("hide");
        $("#"+$(this).attr("id")+"_wrap").removeClass("hide");

    });


    var data = [
        {
            value: 30,
            color:"#F7464A"
        },
        {
            value : 50,
            color : "#E2EAE9"
        },
        {
            value : 100,
            color : "#D4CCC5"
        },
        {
            value : 40,
            color : "#949FB1"
        },
        {
            value : 120,
            color : "#4D5360"
        }

    ];
    var piectx = $(".pieChart").getContext("2d");
    new Chart(piectx).Doughnut(data);

    //容器运行状态的环状图下图注
    var stoppedctx = $("#redPie").getContext("2d");
    stoppedctx.fillStyle = "#F7464A";
    stoppedctx.fillRect(0,0,15,15);
    var runningctx = $("#greenPie").getContext("2d");
    runningctx.fillStyle = "green";
    runningctx.fillRect(0,0,15,15);
    var grostctx = document.getElementById("grayPie").getContext("2d");
    grostctx.fillStyle = "#D4CCC5";
    grostctx.fillRect(0,0,15,15);



});




/*
var runningPie = 0;
var stoppedPie = 0;
var ghostPie = 0;
data.forEach(function(value){
    if (value.status == 1){
        $scope.containers.push(value);
        $scope.changePage("init");
        runningPie += 1
    }else if (value.status == 2){
        stoppedPie += 1
    }else{
        ghostPie += 1
    }
});
var piedata  = [
    {
        value: runningPie,
        color: "green"
    },
    {
        value : stoppedPie,
        color : "#F7464A"
    },
    {
        value : ghostPie,
        color : "#D4CCC5"
    }
];
var piectx = document.getElementById("pieChart").getContext("2d");
new Chart(piectx).Doughnut(piedata);*/
