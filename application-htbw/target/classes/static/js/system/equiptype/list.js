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
        l_column : [  {
            colkey : "equipmentType",
            name : "名称",
            isSort : true,
        }, {
            colkey : "description",
            name : "描述"
        }, {
            colkey : "state",
            name : "状态",
            width : '90px',
            isSort : true,
            renderData : function(rowindex, data, rowdata, column) {
                if (data == "1") {
                    return "<font color=green>启用</font>";
                } else {
                    return "<font color=red>未启用</font>";
                }
            }
        }, {
            colkey : "port",
            name : "端口"
        }],
        jsonUrl : rootPath + '/equiptype/findByPage',
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
        content : rootPath + '/equiptype/editUI?id=' + cbox
    });
}
function addAccount() {
    pageii = layer.open({
        title : "新增",
        type : 2,
        area : [ "800px", "80%" ],
        content : rootPath + '/equiptype/addUI'
    });
}
function delAccount() {
    var cbox = grid.getSelectedCheckbox();
    if (cbox == "") {
        layer.msg("请选择删除项！！");
        return;
    }
    layer.confirm('是否删除？', function(index) {
        var url = rootPath + '/equiptype/deleteEntity';
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

