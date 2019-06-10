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
			colkey : "description",
			name : "主旨",
			isSort : true,
		}],
		jsonUrl : rootPath + '/apiequip/findmntenceByPage?equipid=' + equipid,
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