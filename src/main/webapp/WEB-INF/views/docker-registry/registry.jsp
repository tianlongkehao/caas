<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title></title>
    <link rel="stylesheet" type="text/css" href="plugins/bootstrap-3.3.5/dist/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="plugins/Font-Awesome-master/css/font-awesome.css"/>
    <link rel="stylesheet" type="text/css" href="css/core/base.css"/>
    <link rel="stylesheet" type="text/css" href="css/core/layout.css"/>
    <link rel="stylesheet" type="text/css" href="css/mod/docker-registry.css"/>
    <script type="text/javascript" src="/js/registry/registry.js"></script>
</head>
<body>
    <div class="page-container">
        <article>
            <div class="page-main">
                <div class="contentMain">
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
                </div>
            </div>
        </article>
    </div>



    <script type="text/javascript" src="js/plugins/jquery-1.11.3.js"></script>
    <script type="text/javascript" src="plugins/bootstrap-3.3.5/dist/js/bootstrap.js" ></script>
    <script type="text/javascript" src="js/custom.js"></script>
    <script type="text/javascript" src="js/mod/docker-registry.js"></script>
</body>
</html>