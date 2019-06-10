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
			colkey : "thick",
			name : "厚度（mm）"
		}, {
			colkey : "msdLevel",
			name : "MSD等级"
		}, {
			colkey : "plant",
			name : "超出车间寿命(h)"
		}, {
			colkey : "rh",
			name : "湿度（%）"
		}, {
			colkey : "temple",
			name : "温度（℃）"
		}, {
			colkey : "bakeTime",
			name : "烘烤时间(h)"
		} ],
		jsonUrl : rootPath + '/bake/findByPage',
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
});
