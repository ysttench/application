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
		checkValue : "id",
		l_column : [ {
			colkey : "equipmentNum",
			name : "设备编码",
			isSort : true,
		}, {
			colkey : "equipmentName",
			name : "设备名称",
			isSort : true,
		}, {
			colkey : "exceptCode",
			name : "异常码",
			isSort : true,
		}, {
			colkey : "description",
			name : "异常名称",
			isSort : true,
		}, {
			colkey : "startTime",
			name : "开始时间",
			isSort : true,
            renderData : function(rowindex, data, rowdata, column) {
                return new Date(data).format("yyyy-MM-dd");
            }
		}, {
			colkey : "endTime",
			name : "结束时间",
			isSort : true,
            renderData : function(rowindex, data, rowdata, column) {
                return new Date(data).format("yyyy-MM-dd");
            }
		}, {
			colkey : "dracution",
			name : "持续时(s)",
			isSort : true,
		} ],
		jsonUrl : rootPath + '/apiequip/findExceptByPage?equipid=' + equipid,
		checkbox : true,
		serNumber : true
	});
	
	$("#search").click("click", function() {// 绑定查询按扭
		var searchParams = $("#searchForm").serializeJson();// 初始化传参数
		grid.setOptions({
			data : searchParams
		});
	});
});
