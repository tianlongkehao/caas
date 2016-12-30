<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
<title>dockerfile</title>
<%@include file="../frame/header.jsp"%>
<link rel="stylesheet" type="text/css"
	href="<%=path%>/css/core/codemirror.css" />
	<link rel="stylesheet" type="text/css"
	href="<%=path%>/css/core/ambiance.css" />
<link rel="stylesheet" type="text/css"
	href="<%=path%>/css/mod/template.css" />
<script type="text/javascript" src="<%=path%>/js/plugins/codemirror.js"></script>
<script type="text/javascript" src="<%=path%>/js/template/dockerfile-add.js"></script>
</head>
<body>

	<jsp:include page="../frame/bcm-menu.jsp" flush="true">
		<jsp:param name="ci" value="" />
	</jsp:include>

	<div class="page-container">
		<article>
			<div class="page-main">
				<div class="contentTitle">
					<ol class="breadcrumb">
						<li><a href="<%=path %>/home"><i class="fa fa-home"></i>&nbsp;&nbsp;<span
								id="nav1">控制台</span></a></li>
						<li><i class="fa fa-angle-right"></i></li>
						<li class="active" id="nav2">模板</li>
						<li style="margin-left: -44px;"><i class="fa fa-angle-right"></i></li>
						<li class="active">dockerfile</li>
					</ol>
				</div>
				<div class="contentMain">
					<div class="modalCrealApp">
						<div class="steps-main">
							<div class="progre">
                                <ul style="padding: 0 4rem;">
                                    <li class="radius_step action" style="width:100%">新增dockerfile</li>
                                </ul>
                            </div>
							<form id="buildForm" name="buildForm"
								action="<%=path%>/template/dockerfile/build" method="post">
								<div class="container" style="width: 90%">
									<div class="row">
									<div class="form-group col-md-12">
                                        <span>模板名称：</span>
                                        <input type="hidden" id="dockerFileId" value = "" />
                                        <input type="text" id="dockerFileTemplateName" style="width: 40%" value = "" />
                                        <span id="docImportBtn" class=" btn-info btn-sm pull-right" style="cursor: pointer">导入模板</span>
                                    </div>
									<div class="form-group col-md-12" id = "dockerfiletext">
										<textarea id="dockerFile" name="dockerFile"></textarea>
									</div>
								</div>
									
								</div>
								<div class="container" style="width: 90%">
									<div class="list-item-description" style="padding-top: 100px;">
										<a href="<%=path%>/template/dockerfile"><span
											class="btn btn-default" style="margin-right: 30px;">返回</span></a>
										<span id="buildStorage" class="btn btn-primary pull-right">保存</span>
									</div>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</article>
	</div>
        <!--dockerfile导入模板 -->
       <div id="dockerfile-import"
           style="max-height: 170px; overflow-y: scroll; overflow-x: hidden;display: none;">
           <table class="table table-hover enabled" id="Path-table-doc"
               style="width: 326px; margin: 5px 10px 5px 10px">
               <tbody id="dockerfile-body">
                   <c:if test="${empty dockerFileList }">
                       <tr>
                           <td>没有保存的模板</td>
                       </tr>
                   </c:if>
                   <c:if test="${not empty dockerFileList }">
                       <c:forEach var="dockerFile" items="${dockerFileList }">
                           <tr>
                               <td class="vals vals-doc">${dockerFile.templateName }<span
                                   class="doc-tr hide"><i class="fa fa-check"></i></span> <input
                                   type="hidden" class="dockerFileTemplate"
                                   value='${dockerFile.id }' />
                               </td>
                           </tr>
                       </c:forEach>
                   </c:if>
               </tbody>
           </table>
       </div>
       
       <!-- dockerfile另存为模板 -->
       <!-- <div id="dockerfile-export" style = "display: none;">
           <div style="width: 345px; margin: 5px 10px 5px 10px">
               <span>模板名称：</span><input type="text" id="dockerFileTemplateName"
                   style="width: 77%" autofocus="autofocus" />
           </div>
       </div> -->
</body>
</html>