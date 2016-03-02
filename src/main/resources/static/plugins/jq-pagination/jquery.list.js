var jqList = {
	size : 5,// 每页条数
	page : 1,// 初始页数
	xml : {},// 查询条件
	url : "",// 查询地址
	setSize : function(size) {// 初始化设置每页条数
		this.size = size;
	},
	txtPageSize : function(txt) {
		txt.value = txt.value.replace(/[^\d]/g, '');
	},
	control : function() {// 控制按钮
		var paginationHtml = " <TABLE class='pagelist' cellPadding=0 cellSpacing=0 width=\"100%\"> "
				+ "       <tr> "
				+ "           <td width=\"30%\"   id=\"butcolumn\"  align=\"left\"> "
				+ "               每页 "
				+ "               <input type=\"text\" maxlength=\"3\"   style=\"width:30px\"  "
				+ "                   id=\"pageSize\" onblur=\"jqList.queryPage(1)\" value=\""
				+ this.size
				+ "\" "
				+ "                   onkeyup=\"jqList.txtPageSize(this)\"/> "//
				+ "               条 共 "
				+ "               <span id=\"intCount\">0</span>条记录 共 "
				+ "               <span id=\"intPage\">0</span>页 当前 "
				+ "               <span id=\"intPageCont\">0</span> 页 "
				+ "           </td> "
				+ "           <td width=\"70%\" align=\"right\"> "
				+ "               &nbsp; "
				+ "               <span style=\"vertical-align: middle\"> <input id=\"Butfirst\" "
				+ "                       disabled=\"true\" onclick=\"jqList.butQuery(1)\" class=\"btn btn-primary\" "
				+ "                       type=\"button\" value=\"首页\" /> <input id=\"Butup\" disabled=\"true\" "
				+ "                       onclick=\"jqList.butQuery(2)\" class=\"btn btn-primary\" type=\"button\" "
				+ "                       value=\"上一页\" /> <input id=\"Butdown\" disabled=\"true\" "
				+ "                       onclick=\"jqList.butQuery(3)\" class=\"btn btn-primary\" type=\"button\" "
				+ "                       value=\"下一页\" /> <input id=\"Butend\" disabled=\"true\" "
				+ "                       onclick=\"jqList.butQuery(4)\" class=\"btn btn-primary\" type=\"button\" "
				+ "                       value=\"末页\" /> </span> 第 "
				+ "               <input type=\"text\" id=\"pageNo\" maxlength=\"10\" "
				+ "                   style=\"width:30px\" value=\"0\" onblur=\"jqList.setPage()\" "
				+ "                   onkeyup=\"jqList.txtPageSize(this)\"> "
				+ "               页 "
				+ "               <input type=\"button\" class=\"btn btn-primary\" value=\"go\" onclick=\"jqList.setPage()\" /> "
				+ "               &nbsp;&nbsp; "
				+ "               <input id=\"HidPage\" type=\"hidden\" /> "
				+ "               <input id=\"HidPageSQL\" type=\"hidden\" /> "
				+ "           </td> " + "       </tr> " + "   </TABLE> ";

		$("#pagination").empty().append(paginationHtml);
	},
	query : function(url, strxml, callback) {// 查询方法
		this.control();
		this.setHtml = callback;
		this.page = 1;
		this.url = url;

		if (document.getElementById("pageSize").value == "") {
			
			return false;
		}
		var size = parseInt(document.getElementById("pageSize").value, 10);
		if (size == 0) {
			size = this.size;
			document.getElementById("pageSize").value = size;
		}

		document.getElementById("intPageCont").innerHTML = "1";
		document.getElementById("pageNo").value = "1";

		// 记录总数
		var item = 0;

		this.xml = strxml;// 保存查询记录

		strxml.page = 0;
		strxml.size = size;

		this.htmlList(strxml, 1);
	},
	butQuery : function(sel) {
		var int1 = jqList.page;

		var int2 = parseInt(document.getElementById("intPage").innerHTML, 10);

		if (sel == 1) {// 首页
			this.queryPage(1);
		}
		if (sel == 2) { // 上一页

			this.queryPage(int1 - 1);
		}
		if (sel == 3) {// 下一页

			this.queryPage(int1 + 1);
		}
		if (sel == 4) {// 末页
			this.queryPage(int2);

		}
	},
	queryPage : function(QueryPage) {

		if (document.getElementById("pageSize").value == "") {
			return false;
		}

		var size = parseInt(document.getElementById("pageSize").value, 10);
		if (size == 0) {
			size = jqList.size;
			document.getElementById("pageSize").value = size;
		} else {
			jqList.size = size;
		}

		// 保存当前页数
		jqList.page = QueryPage;
		document.getElementById("intPageCont").innerHTML = QueryPage;
		document.getElementById("pageNo").value = QueryPage;
		var item = 0;

		var strXml = this.xml;
		strXml.page = QueryPage - 1;
		strXml.size = size;

		this.htmlList(strXml, QueryPage);
	},
	setPage : function() {
		var sel = document.getElementById("pageNo").value;
		if (sel == "") {
			return false;
		}

		var i = parseInt(sel, 10);
		var int2 = parseInt(document.getElementById("intPage").innerHTML, 10);
		var j = 1;
		if (i == 0) {
			j = 1;
		} else {
			j = i;
		}
		if (i >= int2) {
			j = int2;
		}
		this.queryPage(j);
	},
	command : function(intYe2, intYe3) {
		if (intYe2 == 1) {// 只有一页
			document.getElementById("Butfirst").disabled = true;
			document.getElementById("Butup").disabled = true;
			document.getElementById("Butdown").disabled = true;
			document.getElementById("Butend").disabled = true;
		}
		if (intYe3 == intYe2 && intYe2 > 1) {// 末页
			document.getElementById("Butfirst").disabled = false;
			document.getElementById("Butup").disabled = false;
			document.getElementById("Butdown").disabled = true;
			document.getElementById("Butend").disabled = true;
		}
		if (intYe3 == 1 && intYe2 > 1) {// 首页
			document.getElementById("Butfirst").disabled = true;
			document.getElementById("Butup").disabled = true;
			document.getElementById("Butdown").disabled = false;
			document.getElementById("Butend").disabled = false;
		}
		if (intYe3 > 1 && intYe3 < intYe2) {// 中间页
			document.getElementById("Butfirst").disabled = false;
			document.getElementById("Butup").disabled = false;
			document.getElementById("Butdown").disabled = false;
			document.getElementById("Butend").disabled = false;
		}
	},
	htmlList : function(jsonStr, QueryPage) {
		var rValue = this.textSendHttp(jqList.url, jsonStr);
		var item = 0;
		if (rValue != null) {
//			 item = parseInt( $(rValue).find( "result").attr( "value") ,10);
			item = rValue.count;
			this.setHtml(rValue);
		}

		// 控制按钮
		if (item > 0) {
			document.getElementById("intCount").innerHTML = item;// 显示总记录数

			var intYe = item / jqList.size;
			intYe = parseInt(intYe, 10);

			if (item % jqList.size > 0) {
				intYe += 1;
			}

			this.command(intYe, QueryPage);
			document.getElementById("intPage").innerHTML = intYe;// 显示总页数
		} else {
			document.getElementById("intCount").innerHTML = "0";

			document.getElementById("intPage").innerHTML = "0";
		}
	},
	textSendHttp : function(sJspFile, strdata) {
		var data;

		$.ajax({
			url : sJspFile,
			type : "POST",
			cache : false,
			async : false,
			data : strdata,
			dataType : "json",
			success : function(moneydata) {
				data = moneydata;
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {

				// alert(textStatus);
				// alert(errorThrown);
				// 通常 textStatus 和 errorThrown 之中
				// 只有一个会包含信息
				this; // 调用本次AJAX请求时传递的options参数
			}

		});
		return data;
	},
	setHtml : function(data) {

	}
};