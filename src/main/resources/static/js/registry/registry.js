$(document).ready(function () {
    loadImageList();

    $(".images-panel").mouseover(function () {
        $(this).children(".create-item").css("opacity", "1");
    });

    $(".images-panel").mouseout(function () {
        $(this).children(".create-item").css("opacity", "0");
    });


    $(".list_info").click(function () {
        $(".table_list>.list_info").removeClass("active");
        $(".table_list").siblings("section").addClass("hide");
        $(this).addClass("active").parent().siblings("section").eq($(this).index()).removeClass("hide");
    });

});

function initImages() {
    $.ajax({
        url: "/registry/create",
        success: function (data) {
            loadImageList();
        },
        error: function (data) {
        }
    });
}

function loadImageList() {
    $.ajax({
        url: "/registry/images",
        success: function (data) {
            data = eval("(" + data + ")");

            var html = "";
            if (data != null) {
                if (data['data'].length > 0) {
                    for (var i in data.data) {
                        var image = data.data[i];
                        html += '<li class="images-panel">' +
                            '<div class="select-img">' +
                            '<i class="fa fa-star-o star-style" style="color:#efa421"></i>' +
                            '<div class="mir-img ">' +
                            '<img src="images/image-1.png">' +
                            '</div>' +
                            '</div>' +
                            '<div class="select-info">' +
                            '<div class="pull-right-text">' + image.name + '</div>' +
                            '<div>' +
                            '<div class="pull-right">' +
                            '<a href="" class="btn-pull-deploy btn">部署</a>' +
                            '</div>' +
                            '</div>' +
                            '</div>' +
                            '<div class="create-item">' +
                            '<a href="docker-registry-detail.html">' +
                            '<span class="note-text" title="' + image.remark + '">' + image.remark + '</span>' +
                            '</a>' +
                            '</div>' +
                            '</li>';
                    }
                    $("#imageList").html(html);
                } else {
                    initImages();
                }
            } else {
            }

        }
    });
}