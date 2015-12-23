<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>镜像中心</title>
    <%@include file="../frame/header.jsp"%>
    <link rel="stylesheet" type="text/css" href="/css/mod/docker-registry.css"/>
    <script type="text/javascript" src="/js/registry/registry.js"></script>
</head>
<body>
    <jsp:include page="../frame/menu.jsp" flush="true" >
        <jsp:param name="registry" value="0" />
    </jsp:include>

    <div class="page-container">
        <article>
            <div class="page-main">
                <div class="contentTitle">
                    <ol class="breadcrumb">
                        <li><a href="javascript:void(0)"><i class="fa fa-home"></i>&nbsp;&nbsp;<span id="nav1">控制台</span></a></li>
                        <li><i class="fa fa-angle-right"></i></li>
                        <li class="active" id="nav2">镜像中心</li>
                    </ol>
                </div>
                <div class="contentMain">

                    <div class="content">
                        <div class="search">
                            <form class="search-group-inner" style="width:60%;margin: 0 auto;position: relative;" action="/registry/${index }" method = "post">
                                <input name="imageName" class="search-img" placeholder="搜索镜像" type="text"><button type="submit" class="btn btn-primary btn-send">搜索</button>
                            </form>
                        </div>
                        <div class="images-layout">
                            <ul id="imageList">

                                <c:forEach items="${images}" var="image" >

                                    <li class="images-panel">
                                        <div class="select-img">
                                            <i class="fa fa-star-o star-style" style="color:#efa421"></i>
                                            <div class="mir-img ">
                                                <img src="/images/image-1.png">
                                            </div>
                                        </div>
                                        <div class="select-info">
                                            <div class="pull-right-text">${image.name}</div>
                                            <div>
                                                <div class="pull-right">
                                                    <a href="javascript:void(0);" class="btn-pull-deploy btn" imageversion="${image.version}" imagename="${image.name}" >部署</a>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="create-item">
                                            <a href="/registry/detail/${image.id }?type=${index }">
                                                <span class="note-text" title="${image.remark}" > ${image.remark} </span>
                                            </a>
                                        </div>
                                    </li>

                                </c:forEach>

                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </article>
    </div>

</body>
</html>