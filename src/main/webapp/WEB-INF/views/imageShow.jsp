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

    <div class="page-imageContainer container" style="margin-top:100px">
        <article>
            <div class="page-main">
                <div class="contentMain">
                    <div class="content">
                    	<div class="searchCenter">
	                    	<div class="imageText">
	                            <span>分享镜像，交流协作</span>
	                        </div>
	                        <div class="search">
	                            <form class="search-group-inner" style="width:60%;margin: 0 auto;position: relative;" >
	                                <i class="fa fa-search searchImage-img"></i>
	                                <input name = "search" class="search-img centerSearchInput" id="centerSearchInput" placeholder="搜索镜像" type="text">
	                                <button type="button" id="centerSearchImages" class="btn btn-default btn-color btn-send">搜索</button>
	                            </form>
	                        </div>
                        </div>
                        <div class="search row searchResult hide">
                        	
                        	<div class="col-md-4 ">
                                <input name = "search" class="search-img" id="searchCon" placeholder="搜索镜像" type="text">
                                <i class="fa fa-search" id="searchImages"></i>
                                <!-- <button type="submit" id="searchImages" class="btn btn-default btn-color btn-send">搜索</button> -->
                            </div>
                            <div class="col-md-1 searchLabel"><label>排序 ：</label></div>
                            <div class="col-md-3">
                        		<select class="form-control searchCondition" id="searchCondition" onchange="searchImagesResult()">
                        			<option value="0">默认</option>
                        			<option value="1">按导出次数</option>
                        			<option value="2">按创建时间</option>
                        		</select>
                        	</div>
                        </div>
                        
                        <div class="images-layout imagesCenter">
                        	<div class="imageInfo">推荐镜像</div>
                            <ul id="imageList" style="height:460px">
								<c:forEach items="${imageList }" var = "image">
								
                                    <li class="images-panel">
                                        <div class="select-img">
                                            <div class="mir-img ">
                                                <img class="imageTypeSrc" imageType="${image.imageType }" src="<%=path %>/images/image-1.png">
                                                <div class="imageInfoText">${image.remark }</div>
                                            </div>
                                        </div>
                                        <div class="select-info">
                                            <div class="pull-right-text">
                                                <span class="imageCenter-name">${image.name}</span>
                                                <div class="pull-right">
                                                	<i class="fa fa-cloud-download" style="color:#e8504f"></i>
                                                	<c:if test="${image.exportCount == null || image.exportCount == '' || image.exportCount == 0}">
                                                		<span>0</span>
                                                	</c:if>
                                                	<c:if test="${image.exportCount != null && image.exportCount != '' && image.exportCount != 0}">
                                                		<span>${image.exportCount }</span>
                                                	</c:if> 
                                                </div>
                                            </div>
                                            <div>
                                            	<i class="fa fa-tag"></i>${image.version }
                                            	
                                                <div class="pull-right">
                                                    <i class="fa fa-star-o star-style" style="color:#e8504f"></i>
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
                                   
                            </ul>
                            <div class="imageInfo" style="position:absolute;top:633px"><span>最新镜像</span></div><br>
                            <ul id="newimageList" style="height:460px">
                                <c:forEach items="${newImage }" var = "image" varStatus="status">
                                    <c:if test="${status.index < 8 }">
                                    
	                                    <li class="images-panel">
	                                        <div class="select-img">
	                                            <div class="mir-img ">
	                                                <img class="imageTypeSrc" imageType="${image.imageType }" src="<%=path %>/images/image-1.png">
	                                                <div class="imageInfoText">${image.remark }</div>
	                                                
	                                            </div>
	                                        </div>
	                                        <div class="select-info">
	                                            <div class="pull-right-text">
	                                                <span class="imageCenter-name">${image.name}</span>
	                                                <div class="pull-right">
	                                                	<i class="fa fa-cloud-download" style="color:#e8504f"></i>
	                                                	<c:if test="${image.exportCount == null || image.exportCount == ''|| image.exportCount == 0}">
	                                                		<span>0</span>
	                                                	</c:if>
	                                                	<c:if test="${image.exportCount != null && image.exportCount != '' && image.exportCount != 0}">
	                                                		<span>${image.exportCount }</span>
	                                                	</c:if>  
	                                                </div>
	                                            </div>
	                                            
	                                            <div>
	                                                <i class="fa fa-tag"></i>${image.version }
	                                                <div class="pull-right">
	                                                    <i class="fa fa-star-o star-style" style="color:#e8504f"></i>
	                                                    <c:if test="${image.currUserFavor==0 ||image.currUserFavor==null||image.currUserFavor==''}">
	                                                        <span>0</span>
	                                                    </c:if>
	                                                    <c:if test="${image.currUserFavor!=0 &&image.currUserFavor!=null&&image.currUserFavor!=''}">
	                                                    	<span>${image.currUserFavorCount }</span>
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
                        
                        <!-- 镜像搜索结果 -->
						<div class="images-layout imagesSearchResult hide">
							<div class="imageInfo" style="color: #e66">共找到<span id="imageSearchCount" value=""></span>个镜像</div>
							<ul id="searchImagesList">
								<%-- <li class="images-panel">
									<div class="select-img">
										<div class="mir-img ">
											<img src="<%=path %>/images/image-1.png">
											<div class="imageInfoText">image.remark</div>
										</div>
									</div>
									<div class="select-info">
										<div class="pull-right-text">image.name</div>
										<div>

											<div class="pull-left">
												<i class="fa fa-cloud-download" style="color: #e8504f"></i>
												<span>image.exportCount </span>
											</div>
											<div class="pull-right">
												<a
													href="<%=path %>/service/add?imageName=${image.name}&imageVersion=${image.version}&imgID=${image.id}&resourceName=${image.resourceName}"
													class="btn-pull-deploy btn" imageversion="${image.version}"
													imagename="${image.name}">部署</a>
											</div>
										</div>
									</div>
								</li> --%>
								
							</ul>

						</div>
					</div>
                </div>
            </div>
        </article>
    </div>

</body>
</html>