var pageii = null;
var grid = null;
$(function() {

	grid = lyGrid({
		pagId : 'paging',
		height : '100%', // 表格宽度
		theadHeight : '28px', // 表格的thead高度
		tbodyHeight : '27px',// 表格body的每一行高度
		isFixed : true,// 是
		checkValue : "id",
		l_column : [ {
			colkey : "equipmentNum",
			name : "设备编号",
			isSort : true,
		}, {
			colkey : "equipmentName",
			name : "设备名称",
			isSort : true,
		}, {
			colkey : "userName",
			name : "设备负责人",
			isSort : true,
		}, {
			colkey : "equipmentIp",
			name : "设备Ip",
			isSort : true,
		}, {
			colkey : "wsPort",
			name : "温湿度端口",
			isSort : true,
		}, {
			colkey : "domainName",
			name : "区域",
			isSort : true,
		}, {
			colkey : "equiptypeName",
			name : "设备分类",
			isSort : true,
		}, {
			colkey : "enableStatus",
			name : "运行状态",
			width : '90px',
			isSort : true,
			renderData : function(rowindex, data, rowdata, column) {
				if (data == "1") {
					return "<font color=green>运行</font>";
				} else if (data == "0") {
					return "<font color=green>调试</font>";
				} else if (data == "-1") {
					return "<font color=red>报废</font>";
				} else if (data == "2") {
					return "<font color=red>暂停运行</font>";
				}else if (data == "3") {
					return "<font color=red>离线</font>";
				}
			}
		}, {
			colkey : "monitor",
			name : "监测状态",
			width : '90px',
			isSort : true,
			renderData : function(rowindex, data, rowdata, column) {
				if (data == "0") {
					return "<font color=green>启用</font>";
				} else if (data == "1") {
					return "<font color=red>未启用</font>";
				}
			}
		},{
			colkey : "checkDate",
			name : "设备校验时间",
	            isSort : true,
	            renderData : function(rowindex, data, rowdata, column) {
	                return new Date(data).format("yyyy-MM-dd hh:mm:ss");
	            }
		}, {
			colkey : "import",
			name : "设备重要性",
			width : '90px',
			isSort : true,
			renderData : function(rowindex, data, rowdata, column) {
				return "<font color=green>" + data + "</font>";
			}
		} ],
		jsonUrl : rootPath + '/equipment/findByPage',
		checkbox : true,
		serNumber : true
	});
	$("#search").click("click", function() {// 绑定查询按扭
		var searchParams = $("#searchForm").serializeJson();// 初始化传参数
		grid.setOptions({
			data : searchParams
		});
	});
	$("#addAccount").click("click", function() {
		addAccount();
	});
	$("#editAccount").click("click", function() {
		editAccount();
	});
	$("#delAccount").click("click", function() {
		delAccount();
	});

});
function editAccount() {
	var cbox = grid.getSelectedCheckbox();
	if (cbox.length > 1 || cbox == "") {
		layer.msg("只能选中一个");
		return;
	}
	pageii = layer.open({
		title : "编辑",
		type : 2,
		area : [ "800px", "80%" ],
		content : rootPath + '/equipment/editUI?id=' + cbox
	});
}
function addAccount() {
	pageii = layer.open({
		title : "新增",
		type : 2,
		area : [ "800px", "80%" ],
		content : rootPath + '/equipment/addUI'
	});
}
function delAccount() {
	var cbox = grid.getSelectedCheckbox();
	if (cbox == "") {
		layer.msg("请选择删除项！！");
		return;
	}
	layer.confirm('是否删除？', function(index) {
		var url = rootPath + '/equipment/deleteEntity';
		var s = CommnUtil.ajax(url, {
			ids : cbox.join(",")
		}, "json");
		if (s == "success") {
			layer.msg('删除成功');
			grid.loadData();
			keepsocket();
		} else {
			layer.msg('删除失败');
		}
	});
}
function keepsocket(){
	$.ajax({
		url : rootPath + "/equipment/keepsocket",
		data : {},
		dataType : "json",
		success : function(data) {
			
		}
	})
}
