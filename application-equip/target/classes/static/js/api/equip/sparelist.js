var pageii = null;
var grid = null;
$(function() {
	var equipid = $('#equipid').val();
	grid = lyGrid({
		pagId : 'paging',
		height : '100%', // 表格宽度
		theadHeight : '28px', // 表格的thead高度
		tbodyHeight : '27px',// 表格body的每一行高度
		isFixed : true,// 是
		checkValue : "uid",
		l_column : [ {
			colkey : "spareNum",
			name : "配件编号",
			isSort : true,
		}, {
			colkey : "spareName",
			name : "配件名称",
			isSort : true,
		}, {
			colkey : "spareSpec",
			name : "配件规格",
			isSort : true,
		}, {
			colkey : "num",
			name : "数量",
			isSort : true,
		}, {
			colkey : "unitName",
			name : "计量单位",
			isSort : true,
		}, {
			colkey : "life",
			name : "寿命(年)",
			isSort : true,
		}, {
			colkey : "enableTime",
			name : "启用时间",
			isSort : true,
			renderData : function(rowindex, data, rowdata, column) {
				if (data == "") {
					return " ";
				} else {
					return new Date(data).format("yyyy-MM-dd");
				}
			}
		} ],
		jsonUrl : rootPath + '/apispare/findByPage2?equipid=' + equipid,
		checkbox : true,
		serNumber : true
	});

	$("#search").click("click", function() {// 绑定查询按扭
		var searchParams = $("#searchForm").serializeJson();// 初始化传参数
		grid.setOptions({
			data : searchParams
		});
	});
	$("#spareTimeStart").click("click", function() {
		spareTimeStart();
	});
	$("#spareNumchange").click("click", function() {
		spareNumchange();
	});
});
function spareTimeStart() {
	var cbox = grid.getSelectedCheckbox();
	if (cbox == "") {
		layer.msg("请选择启动配件！！");
		return;
	}
	layer.confirm('是否启动？', function(index) {
		var url = rootPath + '/apispare/spareTimeStart';
		var s = CommnUtil.ajax(url, {
			ids : cbox.join(",")
		}, "json");
		if (s == "success") {
			layer.msg('启动成功');
			grid.loadData();
		} else {
			layer.msg('启动失败');
		}
	});
}
function spareNumchange() {
	var cbox = grid.getSelectedCheckbox();
	if (cbox.length > 1 || cbox == "") {
		layer.msg("只能选中一个");
		return;
	}
	pageii = layer.open({
		title : "变更配件数量",
		type : 2,
		area : [ "800px", "80%" ],
		content : rootPath + '/apispare/spareNumchange?id=' + cbox
	});
}
