var pageii = null;
var grid = null;
$(function() {
    grid = lyGrid({
        id : 'paging',
        height : '100%', // 表格宽度
        theadHeight : '28px', // 表格的thead高度
        tbodyHeight : '27px',// 表格body的每一行高度
        isFixed : true,// 是
        checkValue : "id",
        l_column : [ {
            colkey : "menuId",
            name : "menuId",
            hide : true
        }, {
            colkey : "name",
            name : "菜单名称",
            align : 'left'
        }, {
            colkey : "type",
            name : "菜单类型",
            width : "70px",
            renderData : function(rowindex, data, rowdata, column) {
            	debugger;
                if (data == "0") {
                    return "一级菜单";
                } else if (data == "1") {
                    return "二级菜单";
                } else if (data == "2") {
                    return "三级菜单";
                }
            }
        }, {
            colkey : "menuKey",
            name : "菜单标识"
        }, {
            colkey : "menuUrl",
            name : "URL地址"
        }/*, {
            colkey : "isHide",
            name : "是否隐藏",
            renderData : function(rowindex, data, rowdata, column) {
                if (data == "0") {
                    return "<font color=green>否</font>";
                } else if (data == "1") {
                    return "<font color=red>是</font>";
                }
            }
        }*/, {
            colkey : "description",
            width : "100px",
            name : "描述"
        } ],
        jsonUrl : rootPath + '/menu/treelists',
        checkbox : true,
        usePage : false,
        records : "treelists",
        treeGrid : {
            type : 1,
            tree : true,// 是否显示树
            hide : false,// 默认展开
            name : 'name',
            id : "menuId",
            pid : "parentId"
        }
    });
    $("#search").click("click", function() {// 绑定查询按扭
        var searchParams = $("#searchForm").serializeJson();
        grid.setOptions({
            data : searchParams
        });
    });
    $("#addFun").click("click", function() {
        addFun();
    });
    $("#editFun").click("click", function() {
        editFun();
    });
    $("#delFun").click("click", function() {
        delFun();
    });
    $("#lyGridUp").click("click", function() {// 上移
        var cbox = grid.getSelectedCheckbox();
        if (cbox == "") {
            layer.alert("请选择上移项！！");
            return;
        }
        var jsonUrl = rootPath + '/menu/sortUpdate';
        grid.lyGridUp(jsonUrl);
    });
    $("#lyGridDown").click("click", function() {// 下移
        var cbox = grid.getSelectedCheckbox();
        if (cbox == "") {
            layer.alert("请选择下移项！！");
            return;
        }
        var jsonUrl = rootPath + '/menu/sortUpdate';
        grid.lyGridDown(jsonUrl);
    });
});
function editFun() {
    var cbox = grid.getSelectedCheckbox();
    if (cbox.length > 1 || cbox == "") {
        layer.alert("只能选中一个");
        return;
    }
    pageii = layer.open({
        title : "编辑",
        type : 2,
        area : [ "600px", "80%" ],
        content : rootPath + '/menu/editUI?id=' + cbox
    });
}
function addFun() {
    pageii = layer.open({
        title : "新增",
        type : 2,
        area : [ "600px", "80%" ],
        content : rootPath + '/menu/addUI'
    });
}
function delFun() {
    var cbox = grid.getSelectedCheckbox();
    if (cbox == "") {
        layer.alert("请选择删除项！！");
        return;
    }
    layer.confirm('是否删除？', function(index) {
        var url = rootPath + '/menu/deleteEntity';
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
