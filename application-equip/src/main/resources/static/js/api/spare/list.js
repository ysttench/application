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
			colkey : "unitName",
			name : "计量单位",
			isSort : true,
		}, {
			colkey : "life",
			name : "寿命(年)",
			isSort : true,
		}, {
			colkey : "status",
			name : "配件状态",
			isSort : true, 
			renderData : function(rowindex, data, rowdata, column) {
                if (data == "0") {
                    return "<font color=green>启用</font>";
                } else if(data == "1") {
                    return "<font color=red>废弃</font>";
                }
            }
		}],
		jsonUrl : rootPath + '/apispare/findByPage',
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
        content : rootPath + '/apispare/editUI?id=' + cbox
    });
}
function addAccount() {
    pageii = layer.open({
        title : "新增",
        type : 2,
        area : [ "800px", "80%" ],
        content : rootPath + '/apispare/addUI'
    });
}
function delAccount() {
    var cbox = grid.getSelectedCheckbox();
    if (cbox == "") {
        layer.msg("请选择删除项！！");
        return;
    }
    layer.confirm('是否删除？', function(index) {
        var url = rootPath + '/apispare/deleteEntity';
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