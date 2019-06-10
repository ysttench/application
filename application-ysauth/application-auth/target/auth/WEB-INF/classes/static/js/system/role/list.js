var pageii = null;
var grid = null;
$(function() {
    grid = lyGrid({
        id : 'paging',
        height : '100%', // 表格宽度
        theadHeight : '28px', // 表格的thead高度
        tbodyHeight : '27px',// 表格body的每一行高度
        isFixed : true,// 是
        checkValue : "roleId",
        l_column : [ {
            colkey : "roleId",
            name : "roleId",
            width : "50px",
            hide:true,
        }, {
            colkey : "name",
            name : "角色名"
        }, {
            colkey : "state",
            name : "状态",
            width : "100px",
            renderData : function(rowindex, data, rowdata, column) {
                if (data == "0") {
                    return "<font color=green>可用</font>";
                } else {
                    return "<font color=red>禁用</font>";
                }
            }
        }, {
            colkey : "description",
            name : "描述"
        },{
            colkey : "createTime",
            name : "时间",
            isSort : true,
            renderData : function(rowindex, data, rowdata, column) {
                return new Date(data).format("yyyy-MM-dd hh:mm:ss");
            }
        } ],
        jsonUrl : rootPath + '/role/findByPage',
        checkbox : true
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
    $("#addRole").click("click", function() {
        addRole();
    });
    $("#editRole").click("click", function() {
        editRole();
    });
    $("#delRole").click("click", function() {
        delRole();
    });
    $("#permissions").click("click", function() {
        permissions();
    });
});
function editRole() {
    var cbox = grid.getSelectedCheckbox();
    if (cbox.length > 1 || cbox == "") {
        layer.msg("只能选中一个");
        return;
    }
    pageii = layer.open({
        title : "编辑",
        type : 2,
        area : [ "600px", "60%" ],
        content : rootPath + '/role/editUI?id=' + cbox
    });
}
function permissions() {
    var cbox = grid.getSelectedCheckbox();
    if (cbox.length > 1 || cbox == "") {
        layer.msg("请选择一个对象！");
        return;
    }
    var url = rootPath + '/right/menuRight?roleId=' + cbox;
    pageii = layer.open({
        title : "分配权限",
        type : 2,
        area : [ "700px", "60%" ],
        content : url
    });
}
function addRole() {
    pageii = layer.open({
        title : "新增",
        type : 2,
        area : [ "600px", "60%" ],
        content : rootPath + '/role/addUI'
    });
}
function delRole() {
    var cbox = grid.getSelectedCheckbox();
    if (cbox == "") {
        layer.msg("请选择删除项！！");
        return;
    }
    layer.confirm('是否删除？', function(index) {
        var url = rootPath + '/role/deleteEntity';
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
