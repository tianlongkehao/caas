<%@ page import="java.util.List" %>
<%@ page import="com.bonc.epm.paas.entity.Ci" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%
    List<Ci> ciList = (List<Ci>)request.getAttribute("ciList");
%>
<!DOCTYPE html>
<html>
<head lang="en">
    <title></title>
    <%@include file="../frame/header.jsp" %>
    <link rel="stylesheet" type="text/css" href="/css/mod/ci.css"/>
    <script type="text/javascript" src="/js/ci/ci.js"></script>
</head>
<body>
<jsp:include page="../frame/menu.jsp" flush="true">
    <jsp:param name="ci" value=""/>
</jsp:include>

<div class="page-container">
    <article>
        <div class="page-main">
            <div class="contentTitle">
                <ol class="breadcrumb">
                    <li><a href="javascript:void(0)"><i class="fa fa-home"></i>&nbsp;&nbsp;<span
                            id="nav1">控制台</span></a></li>
                    <li><i class="fa fa-angle-right"></i></li>
                    <li class="active" id="nav2">构建</li>
                </ol>
            </div>
            <div class="contentMain">

                <div class="caption clearfix">
                    <ul class="toolbox clearfix">
                        <li><a href="javascript:void(0);" id="ciReloadBtn"><i class="fa fa-repeat"></i></a></li>
                        <!-- <li><a href="javascript:void(0);">添加源代码</a></li> -->
                        <li><a href="/ci/add" id="ciAddBtn">快速构建</a></li>
                    </ul>
                </div>
                <div class="itemTable">
                    <table class="table ci-table">
                        <thead>
                        <tr>
                            <th>
                                <div class="table-head">
                                    <table class="table" style="margin:0px;">
                                        <thead>
                                        <tr style="height:40px;">
                                            <th style="width: 15%;text-indent:30px;">项目名称</th>
                                            <th style="width: 12%;text-indent: 15px;">构建状态</th>
                                            <th style="width: 15%;text-indent: 20px;">代码源</th>
                                            <th style="width: 12%;">上次构建时间</th>
                                            <th style="width: 10%;text-indent: 8px;">持续时间</th>
                                            <th style="width: 15%;text-indent: 10px;">镜像</th>
                                            <th style="width: 18%;text-indent: 10px;">功能</th>
                                        </tr>
                                        </thead>
                                    </table>
                                </div>
                            </th>
                        </tr>
                        </thead>
                        <tbody id="projectsBody">
                        <tr>
                            <td>
                                <div class="content-table">
                                    <table class="table tables">
                                        <tbody id="ciList">

                                        <%
                                        for(int i=0, len=ciList.size(); i<len; i++){
                                            Ci ci = ciList.get(i);
                                            String statusName = "";
                                            String statusClassName = "";
                                            String loadingImgShowClass = "hide";
                                            int codeType = ci.getCodeType();
                                            String codeTypeName = "";
                                            Long imgId = ci.getImgId();
                                            String cursorClass = "";
                                            String btnCursorClass = "";
                                            int constructionStatus = ci.getConstructionStatus();

                                            if(1 == constructionStatus) {
                                                statusName = "未构建";
                                                statusClassName = "fa_stop";
                                            }else if(2 == constructionStatus) {
                                                statusName = "构建中";
                                                statusClassName = "fa_success";
                                                loadingImgShowClass = "";
                                                btnCursorClass = "cursor-no-drop";
                                            }else if(3 == constructionStatus) {
                                                statusName = "完成";
                                                statusClassName = "fa_success";
                                            }else if(4 == constructionStatus) {
                                                statusName = "失败";
                                                statusClassName = "fa_stop";
                                            }

                                            if(1 == codeType) {
                                                codeTypeName = "svn";
                                            }else if(2 == codeType) {
                                                codeTypeName = "git";
                                            }

                                            if(imgId == null || imgId == 0){
                                                cursorClass = "cursor-no-drop";
                                            }

                                        %>

                                        <tr class="ci-listTr" style="cursor:auto">
                                            <td style="width: 15%; text-indent:22px;">
                                                <a href="/ci/detail/<%=ci.getId()%>" title="查看详细信息"><%=ci.getProjectName()%> </a>
                                            </td>
                                            <td style="width: 12%;" class="cStatusColumn">
                                                <i class="<%=statusClassName%>"></i>
                                                <%=statusName%>
                                                <img src="/images/loading4.gif" alt="" class="<%=loadingImgShowClass%>"/>
                                            </td>
                                            <td style="width: 15%;">
                                                <a data-toggle="tooltip" data-placement="left" title="" target="_blank" href="<%=ci.getCodeUrl()%>" data-original-title="查看源代码">
                                                    <span class="bj-code-source"><i class="fa fa-git-square fa-lg"></i> <%=codeTypeName%></span>
                                                </a>
                                            </td>
                                            <td style="width: 12%;"><%=ci.getConstructionDate()%></td>
                                            <td style="width: 10%;"><%=ci.getConstructionTime()%></td>
                                            <td style="width: 15%;">
                                                <a target="_blank" title="" class="<%=cursorClass%>"><%=ci.getImgNameLast()%></a>
                                            </td>
                                            <td style="width:18%">
                                                <span class="bj-green <%=btnCursorClass%>" data-toggle="tooltip" data-placement="right" title="" data-original-title="重新构建" constructionStatus="<%=constructionStatus%>"  ciId="<%=ci.getId()%>">构建&nbsp;&nbsp;<i class="fa fa-arrow-circle-right"></i></span>
                                            </td>
                                        </tr>

                                        <%
                                            }
                                        %>

                                        </tbody>
                                    </table>
                                </div>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>

            </div>
        </div>
    </article>
</div>

</body>
</html>