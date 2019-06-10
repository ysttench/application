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
			colkey : "materNum",
			name : "物料编号",
			isSort : true,
		}, {
			colkey : "msdNum",
			name : "MSD编号",
			isSort : true,
		}, {
			colkey : "flot",
			name : "批号",
			isSort : true,
		}, {
			colkey : "msdType",
			name : "MSD分类",
			isSort : true,
		}, {
			colkey : "msdLevel",
			name : "MSD等级",
			isSort : true,
		}, {
			colkey : "thick",
			name : "厚度（mm）",
			isSort : true,
		}, {
			colkey : "pmResistance",
			name : "包装物耐热性",
			isSort : true,
		}, {
			colkey : "arkName",
			name : "实时位置",
			isSort : true,
		}, {
			colkey : "resiLife",
			name : "剩余车间寿命",
			isSort : true,
			renderData : function(rowindex, data, rowdata, column) {
				if (data == "0") {
					return "<font color=red>0小时</font>";
				} else {
					return "<font color=green>" + data + "小时</font>";
				}
			}
		}, {
			colkey : "tankTime",
			name : "扫描入柜时间",
			isSort : true,
			renderData : function(rowindex, data, rowdata, column) {
				return new Date(data).format("yyyy-MM-dd hh:mm:ss");
			}
		}, {
			colkey : "requestStatus",
			name : "取用状态",
			width : '90px',
			isSort : true,
			renderData : function(rowindex, data, rowdata, column) {
				if (data == "0") {
					return "<font color=green>未取用</font>";
				} else if (data == "1") {
					return "<font color=yellow>请求取用</font>";
				} else {
					return "<font color=red>已取用</font>";
				}
			}
		}/*, {
			colkey : "status",
			name : "状态",
			width : '90px',
			isSort : true,
			renderData : function(rowindex, data, rowdata, column) {
				if (data == "0") {
					return "<font color=green>可用</font>";
				} else {
					return "<font color=red>不可用</font>";
				}
			}
		}*/, {
			colkey : "jycl",
			name : "建议处理方式"
		} ],
		jsonUrl : rootPath + '/msdqd/findByPage',
		checkbox : true,
		serNumber : true
	});
	$("#search").click("click", function() {// 绑定查询按扭
		var searchParams = $("#searchForm").serializeJson();// 初始化传参数
		grid.setOptions({
			data : searchParams
		});
	});
	$("#status").change("change", function() {// 绑定查询按扭
		var searchParams = $("#status").serializeJson();// 初始化传参数
		grid.setOptions({
			data : searchParams
		});
	});
	$("#takAccount").click("click", function() {
		takAccount();
	});
	$("#takoverAccount").click("click", function() {
		takoverAccount();
	});
	$("#returnAccount").click("click", function() {
		returnAccount();
	});
	$("#addAccount").click("click", function() {
		addAccount();
	});
});
function takAccount() {
	var cbox = grid.getSelectedCheckbox();
	if (cbox == "") {
		layer.msg("请选择取用项！！");
		return;
	}
	layer.confirm('是否取用？', function(index) {
		var url = rootPath + '/msdqd/takEntity';
		var s = CommnUtil.ajax(url, {
			ids : cbox.join(",")
		}, "json");
		if (s == "success") {
			layer.msg('取用请求成功');
			grid.loadData();
		} else if (s == "areadyrequest") {
			layer.msg('该元件已请求取用');
		} else if (s == "lifeout") {
			layer.confirm('该元件车间寿命小于0，是否强制取用？', function(index) {
				var url = rootPath + '/msdqd/qtakEntity?id='+cbox;
				var s = CommnUtil.ajax(url, {
					ids : cbox.join(",")
				}, "json");
				if (s == "success") {
					layer.msg('强制取用成功');
					grid.loadData();
				}  else {
					layer.msg('强制取用失败');
				}
			});
			grid.loadData();
		} else if (s == "areadyget") {
			layer.msg('该元件已取用');
		} else {
			layer.msg('取用请求失败');
		}
	});
}
function takoverAccount() {
	var cbox = grid.getSelectedCheckbox();
	if (cbox == "") {
		layer.msg("请选择取用完成项！！");
		return;
	}
	layer.confirm('是否确认完成取用？', function(index) {
		var url = rootPath + '/msdqd/takoverEntity';
		var s = CommnUtil.ajax(url, {
			ids : cbox.join(",")
		}, "json");
		if (s == "success") {
			layer.msg('取用完成请求成功');
			grid.loadData();
		} else if (s == "fail1") {
			layer.msg('该元件不是请求取用元件或该元件是已取用元件');
		} else {
			layer.msg('取用完成请求失败');
		}
	});
}
function addAccount() {
	var cbox = grid.getSelectedCheckbox();
	if (cbox == "") {
		layer.msg("请选择取用存放项！！");
		return;
	}
	layer.confirm('是否存放？', function(index) {
		var url = rootPath + '/msdqd/keepUI?id=' + cbox;
		var s = CommnUtil.ajax(url, {
			ids : cbox.join(",")
		}, "json");
		if (s == "success") {
			layer.confirm('存放位置灯已亮,是否确认已放入元件？', function(index) {
				var url = rootPath + '/msdqd/saveUI?id=' + cbox;
				var s = CommnUtil.ajax(url, {
					ids : cbox.join(",")
				}, "json");
				if (s == "success") {
					layer.msg('完成存放');
					grid.loadData();
				}else{
					layer.msg('存放失败');
				}
			});
			grid.loadData();
		}else if(s== "fail1"){
			layer.msg('暂无位置存放');
		}else{
			layer.msg('选中存放的项已经存放');
		}
	});
}
function returnAccount() {
	var cbox = grid.getSelectedCheckbox();
	if (cbox == "") {
		layer.msg("请选择还原项！！");
		return;
	}
	layer.confirm('是否还原？', function(index) {
		var url = rootPath + '/msdqd/returnEntity';
		var s = CommnUtil.ajax(url, {
			ids : cbox.join(",")
		}, "json");
		if (s == "success") {
			layer.msg('还原请求成功');
			grid.loadData();
		} else {
			layer.msg('还原请求失败');
		}
	});
}