 $(function(){
  getData();
  setInterval(function(){
   getData();
  }, 60000);
 });
function getData(){
	var pageii = null;
	var grid = null;
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
				return "<font color=red>"+data+"</font>"
			}
		}, {
			colkey : "humValue",
			name : "湿度",
			isSort : true,
			renderData : function(rowindex, data, rowdata, column) {
				return "<font color=blue>"+data+"</font>"
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