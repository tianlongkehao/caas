<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>镜像广场</title>
    <%@include file="frame/header.jsp"%>
    <link rel="stylesheet" type="text/css" href="<%=path %>/css/mod/docker-registry.css"/>
    <script type="text/javascript" src="<%=path %>/js/registry/registry.js"></script>
</head>
<body>
  <header class="header" style="box-shadow:none">
    <div class="navbar navbar-fixed-top">
        <div class="container">
            <div class="navbar-header">
                <span><a href="<%=path %>/home"><h1>BCM</h1></a></span>
            </div>
            <div class="navbar-tab">
                <ul>
                	<li><a class="navTab first" href="<%=path %>/home">首页</a></li>
                	<li class="active"><a class="navTab" href="<%=path %>/registry/imageShow">镜像广场</a></li>
                	<li><a class="navTab" href="<%=path %>/bcm/${cur_user.id }">控制台</a></li>
                </ul>
            </div>
        </div>
    </div>
</header>  

    <div class="page-imageContainer" style="margin-left:10px;margin-top:30px">
        <article>
            <div class="page-main">
                <div class="contentMain">
                    <div class="content">
                    	<div class="imageText">
                            <span>分享镜像，交流协作</span>
                        </div>
                        <div class="search">
                            <form class="search-group-inner" style="width:60%;margin: 0 auto;position: relative;" action="<%=path %>/" method = "post">
                                <input name="imageName" class="search-img" placeholder="搜索镜像" type="text"><button type="submit" class="btn btn-default btn-color btn-send">搜索</button>
                            </form>
                        </div>
                        <div class="images-layout">
                        	<div class="imageInfo">推荐镜像</div>
                            <ul id="imageList" style="height:460px">
								<c:forEach items="${imageList }" var = "image">
								
                                    <li class="images-panel">
                                        <div class="select-img">
                                            <div class="mir-img ">
                                                <img src="<%=path %>/images/image-1.png">
                                                <div class="imageInfoText">${image.remark }</div>
                                            </div>
                                        </div>
                                        <div class="select-info">
                                            <div class="pull-right-text">
                                                ${image.name}
                                            </div>
                                            <div>
                                            	<i class="fa fa-tag"></i>${image.version }
                                                <div class="pull-right">
		                                            <c:if test="${image.currUserFavor==0 }">
                                                        <i class="fa fa-star-o star-style" style="color:#4280CB"></i>
                                                    </c:if>
                                                    <c:if test="${image.currUserFavor==1 }">
                                                        <i class="fa fa-star star-style" style="color:#efa421"></i>
                                                    </c:if>
	                                              <span>${image.currUserFavorCount }</span>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="create-item ">
                                            <a href="<%=path %>/registry/detail/${image.id }">
                                                <span class="note-text" > ${image.summary } </span>
                                            </a>
                                        </div>
                                        
                                    </li>
                               </c:forEach>     
                                    <li class="images-panel hide">
                                        <div class="select-img">
	                                        <c:if test="${image.currUserFavor==0 }">
	                                            <i class="fa fa-star-o star-style" style="color:#4280CB"></i>
	                                        </c:if>
	                                        <c:if test="${image.currUserFavor==1 }">
	                                            <i class="fa fa-star star-style" style="color:#efa421"></i>
	                                        </c:if>
                                            <div class="mir-img ">
                                                <img src="<%=path %>/images/image-1.png">
                                            </div>
                                        </div>
                                        <div class="select-info">
                                            <div class="pull-right-text">${image.name}
                                             	
	                                            	<span class="btn btn-link lock">
	                                            		<i class="fa "></i>
	                               					</span>
                               					
                                            </div>
                                            <div>
                                            	<i class="fa fa-tag"></i> ${image.version }
                                                <div class="pull-right">
                                                    <a href="<%=path %>/service/add?imageName=${image.name}&imageVersion=${image.version}&imgID=${image.id}&resourceName=${image.resourceName}" class="btn-pull-deploy btn" imageversion="${image.version}" imagename="${image.name}" >部署</a>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="create-item">
                                            <a href="<%=path %>/registry/detail/${image.id }">
                                                <span class="note-text" > ${image.summary } </span>
                                            </a>
                                        </div>
                                    </li>

                                

                            </ul><br>
                            <div class="imageInfo">最新镜像</div>
                            <ul id="newimageList" style="height:460px">
                                <c:forEach items="${newImage }" var = "image" varStatus="status">
                                    <c:if test="${status.index < 10 }">
                                    
	                                    <li class="images-panel">
	                                        <div class="select-img">
	                                            <div class="mir-img ">
	                                                <img src="<%=path %>/images/image-1.png">
	                                                <div class="imageInfoText">${image.remark }</div>
	                                            </div>
	                                        </div>
	                                        <div class="select-info">
	                                            <div class="pull-right-text">
	                                                ${image.name}
	                                            </div>
	                                            <div>
	                                                <i class="fa fa-tag"></i>${image.version }
	                                                <div class="pull-right">
	                                                    <c:if test="${image.currUserFavor==0 }">
	                                                        <i class="fa fa-star-o star-style" style="color:#4280CB"></i>
	                                                    </c:if>
	                                                    <c:if test="${image.currUserFavor==1 }">
	                                                        <i class="fa fa-star star-style" style="color:#efa421"></i>
	                                                    </c:if>
	                                                </div>
	                                            </div>
	                                        </div>
	                                        <div class="create-item ">
	                                            <a href="<%=path %>/registry/detail/${image.id }">
	                                                <span class="note-text" > ${image.summary } </span>
	                                            </a>
	                                        </div>
	                                        
	                                    </li>
	                                </c:if>
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