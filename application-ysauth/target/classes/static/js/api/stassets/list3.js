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
			colkey : "assetsNum",
			name : "资产编号",
			isSort : true
		}, {
			colkey : "assetsName",
			name : "资产名称",
            isSort : true
		}, {
			colkey : "assetsType",
			name : "资产分类",
			isSort : true
		},{
            colkey : "assetstatus",
            name : "资产状态",
            isSort : true,
            renderData : function(rowindex, data, rowdata, column) {
                if (data == "0") {
                    return "<font color=green>闲置</font>";
                } else if (data == "1"){
                    return "<font color=red>已领用</font>";
                } else if (data == "-1"){
                    return "<font color=red>报废</font>";
                }else if(data == ""){
                	return "<font color=red></font>";
                }
            }
        }, {
			colkey : "locationName",
			name : "位置",
			isSort : true
		}, {
			colkey : "keeper",
			name : "保管人",
            isSort : true
		}, {
			colkey : "xh",
			name : "型号",
            isSort : true
		},{
            colkey : "xlh",
            name : "序列号",
            isSort : true
        }, {
			colkey : "keepdepart",
			name : "保管部门",
            isSort : true
		}, {
			colkey : "sqer",
			name : "录入人",
            isSort : true
		}, {
			colkey : "sper",
			name : "审批人",
            isSort : true
		}, {
			colkey : "organ",
			name : "所属组织",
            isSort : true
		}, {
			colkey : "status",
			name : "状态",
			renderData : function(rowindex, data, rowdata, column) {
				if (data == "0") {
					return "<font color=green>未审核</font>";
				} else if (data == "1") {
					return "<font color=red>审核</font>";
				} else if (data == "2") {
					return "<font color=red>返审核</font>";
				}
			}
		}, {
			colkey : "gmdate",
			name : "购买时间",
			isSort : true,
			renderData : function(rowindex, data, rowdata, column) {
				return new Date(data).format("yyyy-MM-dd");
			}
		} ],
		jsonUrl : rootPath + '/stassets/findByPage?status=1',
		checkbox : true,
		serNumber : true
	});
	$("#search").click("click", function() {// 绑定查询按扭
		debugger
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
	$("#upload").click("click", function() {
		upload();
	});
	$("#print").click("click", function() {
		print();
	});
	$("#createAccount").click("click", function() {
		createAccount();
	});
	$("#addPdAccount").click("click", function() {
		addPdAccount();
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
		area : [ "800px", "70%" ],
		content : rootPath + '/stassets/editUI?id=' + cbox
	});
}
function addAccount() {
	pageii = layer.open({
		title : "新增",
		type : 2,
		area : [ "800px", "70%" ],
		content : rootPath + '/stassets/addUI'
	});
}
function delAccount() {
	var cbox = grid.getSelectedCheckbox();
	if (cbox == "") {
		layer.msg("请选择删除项！！");
		return;
	}
	layer.confirm('是否删除？', function(index) {
		var url = rootPath + '/stassets/deleteEntity';
		var s = CommnUtil.ajax(url, {
			ids : cbox.join(",")
		}, "json");
		if (s == "success") {
			layer.msg('删除成功');
			grid.loadData();
		} else if (s == "fail1") {
			layer.msg('审核 状态的资产信息 不允许删除');
		} else {
			layer.msg('删除失败');
		}
	});
}

function createAccount() {
	var cbox = grid.getSelectedCheckbox();
	if (cbox == "") {
		layer.msg("至少选中一个");
		return;
	}
	pageii = layer.open({
		title : "新建盘点单",
		type : 2,
		area : [ "800px", "60%" ],
		content : rootPath + '/stassets/createPdUI?id=' + cbox
	});
}
function addPdAccount() {
	var cbox = grid.getSelectedCheckbox();
	if (cbox == "") {
		layer.msg("至少选中一个");
		return;
	}
	pageii = layer.open({
		title : "添加到盘点单",
		type : 2,
		area : [ "800px", "30%" ],
		content : rootPath + '/stassets/addPdUI?id=' + cbox
	});
}
function upload() {
	pageii = layer.open({
		title : "载入excel",
		type : 2,
		area : [ "800px", "35%" ],
		content : rootPath + '/stassets/excelUI'
	});
}
function print() {
	debugger;
	var cbox = grid.getSelectedCheckbox();
	if (cbox == "") {
		layer.msg("请选择打印项！！");
		return;
	}
	layer.confirm('是否打印？', function(index) {
		var url = rootPath + '/stassets/print';
		var s = CommnUtil.ajax(url, {
			ids : cbox.join(",")
		}, "json");
		if (s == "success") {
			layer.msg('打印成功');
			grid.loadData();
		} else {
			layer.msg(s);
		}
	});
}