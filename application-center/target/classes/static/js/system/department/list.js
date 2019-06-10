var pageii = null;
var grid = null;
$(function() {
    grid = lyGrid({
        id : 'paging',
        height : '100%', // 表格宽度
        theadHeight : '28px', // 表格的thead高度
        tbodyHeight : '27px',// 表格body的每一行高度
        l_column : [{
            colkey : "deptCode",
            name : "部门编码",
        }, {
            colkey : "deptName",
            name : "部门名称",
            align : "left",
        },{
            colkey : "parentname",
            name : "上级部门名称", 
        },{
            colkey : "parentcode",
            name : "上级部门编码",
        }],
        jsonUrl : rootPath + '/sysdepartment/findByPage',
        checkbox : true,
    });
    $("#search").click("click", function() {// 绑定查询按扭
        var searchParams = $("#searchForm").serializeJson();// 初始化传参数
        grid.setOptions({
            data : searchParams
        });
    });
    $("#addDepartment").click("click", function() {
        addDepartment();
    });
    $("#editDepartment").click("click", function() {
        editDepartment();
    });
    $("#delDepartment").click("click", function() {
        delDepartment();
    });
});

function editDepartment() {
    var cbox = grid.getSelectedCheckbox();
    if (cbox.length > 1 ) {
        layer.msg("请选择一条数据");
        return;
    }else if(cbox == ""){
        layer.msg("请先选中一个");
        return;
    }
    pageii = layer.open({
        title : ["部门编辑","text-align:center;font-weight:bold;"],
        type : 2,
        area : [ "1000px", "470px" ],
        content : rootPath + '/sysdepartment/editUI?id=' + cbox
    });
}

function addDepartment() {
    pageii = layer.open({
        title : ["部门添加","text-align:center;font-weight:bold;"],
        type : 2,
        area : [ "1000px", "470px" ],
        content : rootPath + '/sysdepartment/addUI'
    });
}

function delDepartment() {
    var cbox = grid.getSelectedCheckbox();
    if (cbox == "") {
        layer.msg("请选择删除项！！");
        return;
    }
    layer.confirm('是否删除？', function(index) {
        var url = rootPath + '/sysdepartment/deleteEntity';
        var s = CommnUtil.ajax(url, {
            ids : cbox
        }, "json");
        if (s == "success") {
            layer.msg('删除成功');
            grid.loadData();
        } else {
            layer.msg('删除失败');
        }
    });
}
