$(function() {
	var highestTimeoutId = setTimeout(";");
	for (var i = 0; i < highestTimeoutId; i++) {
		clearTimeout(i);
	}
	getData();
	timerFun();
});
 function timerFun(){
	  //要执行的操作
	  var timer3=setTimeout(function(){
	  getData();
	  timerFun();
	  clearTimeout(timer3)
	  },10000)
	}
function getData(){
	var pageii = null;
	var grid = null;
	grid = lyGrid({
		pagId : 'lpaging',
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
			colkey : "date",
			name : "采集时间",
			isSort : true,
			renderData : function(rowindex, data, rowdata, column) {
				return new Date(data).format("yyyy-MM-dd hh:mm:ss");
			}
		}, {
			colkey : "tempValue",
			name : "温度",
			isSort : true,
			renderData : function(rowindex, data, rowdata, column) {
				if (data != '') {
					return "<font color=red>"+data+"</font>"
				}else{
					return "<font color=red></font>"
				}
			}
		}, {
			colkey : "humValue",
			name : "湿度",
			isSort : true,
			renderData : function(rowindex, data, rowdata, column) {
				if (data != '') {
					return "<font color=blue>"+data+"</font>"
				}else{
					return "<font color=blue></font>"
				}
			}
		} ],
		jsonUrl : rootPath + '/equipmentjk/findByPage',
	});
	$("#search").click("click", function() {// 绑定查询按扭
		var searchParams = $("#searchForm").serializeJson();// 初始化传参数
		grid.setOptions({
			data : searchParams
		});
	});

}