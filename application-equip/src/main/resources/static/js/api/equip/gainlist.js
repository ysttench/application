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
			name : "设备编号",
			isSort : true,
		}, {
			colkey : "equipmentName",
			name : "设备名称",
			isSort : true,
		}, {
			colkey : "status",
			name : "状态名称",
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
			colkey : "duration",
			name : "持续时(s)",
			isSort : true,
		}  ],
		jsonUrl : rootPath + '/apiequip/findgainByPage?equipid=' + equipid,
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
	 $("#spareAccount").click("click", function() {
		 spareAccount();
	    });
	    $("#editAccount").click("click", function() {
	        editAccount();
	    });
	    $("#delAccount").click("click", function() {
	        delAccount();
	    });
	    $("#mntenceAccount").click("click", function() {
	    	mntenceAccount();
		    });
	    $("#exceptAccount").click("click", function() {
	    	exceptAccount();
		    });
	    $("#grainAccount").click("click", function() {
	    	grainAccount();
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
        content : rootPath + '/apiequip/editUI?id=' + cbox
    });
}
function spareAccount() {
    var cbox = grid.getSelectedCheckbox();
    if (cbox.length > 1 || cbox == "") {
        layer.msg("只能选中一个");
        return;
    }
    pageii = layer.open({
        title : "配件信息",
        type : 2,
        area : [ "80%", "80%" ],
        content : rootPath + '/apiequip/spareUI?id=' + cbox
    });
}
function mntenceAccount() {
    var cbox = grid.getSelectedCheckbox();
    if (cbox.length > 1 || cbox == "") {
        layer.msg("只能选中一个");
        return;
    }
    pageii = layer.open({
        title : "维护信息",
        type : 2,
        area : [ "80%", "80%" ],
        content : rootPath + '/apiequip/mntenceUI?id=' + cbox
    });
}
function exceptAccount() {
    var cbox = grid.getSelectedCheckbox();
    if (cbox.length > 1 || cbox == "") {
        layer.msg("只能选中一个");
        return;
    }
    pageii = layer.open({
        title : "异常信息",
        type : 2,
        area : [ "80%", "80%" ],
        content : rootPath + '/apiequip/exceptUI?id=' + cbox
    });
}
function grainAccount() {
    var cbox = grid.getSelectedCheckbox();
    if (cbox.length > 1 || cbox == "") {
        layer.msg("只能选中一个");
        return;
    }
    pageii = layer.open({
        title : "稼动信息",
        type : 2,
        area : [ "80%", "80%" ],
        content : rootPath + '/apiequip/grainUI?id=' + cbox
    });
}
function addAccount() {
    pageii = layer.open({
        title : "新增",
        type : 2,
        area : [ "800px", "80%" ],
        content : rootPath + '/apiequip/addUI'
    });
}
function delAccount() {
    var cbox = grid.getSelectedCheckbox();
    if (cbox == "") {
        layer.msg("请选择删除项！！");
        return;
    }
    layer.confirm('是否删除？', function(index) {
        var url = rootPath + '/apiequip/deleteEntity';
        var s = CommnUtil.ajax(url, {
            ids : cbox.join(",")
        }, "json");
        if (s == "success") {
            layer.msg('删除成功');
            grid.loadData();
        } else {
            layer.msg('删除失败');
        }
    });
}