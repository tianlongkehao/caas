<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title></title>
    <link rel="stylesheet" type="text/css" href="css/mod/docker-registry.css"/>

</head>
<body>
    <div class="content">

        <div class="search">
            <form class="search-group-inner" style="width:60%;margin: 0 auto;position: relative;" action="/registry/0">
                <input name="imageName" class="search-img" placeholder="搜索镜像" type="text"><button type="submit" class="btn btn-primary btn-send">搜索</button>
            </form>
        </div>

        <div class="images-layout">
            <ul id="imageList">
            </ul>
        </div>

    </div>

    <script type="text/javascript" src="js/registry/registry.js"></script>
</body>
</html>