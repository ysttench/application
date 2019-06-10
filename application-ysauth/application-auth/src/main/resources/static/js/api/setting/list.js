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
            colkey : "sysKey",
            name : "系统参数key",
            isSort : true,
        }, {
            colkey : "sysValue",
            name : "系统参数value"
        },{
            colkey : "deletedFlag",
            name : "系统参数状态",
            width : '90px',
            isSort : true,
            renderData : function(rowindex, data, rowdata, column) {
                if (data == "0") {
                    return "<font color=green>可用</font>";
                } else {
                    return "<font color=red>禁用</font>";
                }
            }
        }, {
            colkey : "createTime",
            name : "创建时间",
            isSort : true,
            renderData : function(rowindex, data, rowdata, column) {
                return new Date(data).format("yyyy-MM-dd hh:mm:ss");
            }
        } ],
        jsonUrl : rootPath + '/apiSetting/findByPage',
        checkbox : true
    });
    $("#search").click("click", function() {// 绑定查询按扭
        var searchParams = $("#searchForm").serializeJson();// 初始化传参数
        grid.setOptions({
            data : searchParams
        });
    });
    $("#addSetting").click("click", function() {
        addSetting();
    });
    $("#editSetting").click("click", function() {
        editSetting();
    });
    $("#delSetting").click("click", function() {
        delSetting();
    });
    $("#recoverAccount").click("click", function() {
        recoverAccount();
    });
});
function editSetting() {
    var cbox = grid.getSelectedCheckbox();
    if (cbox.length > 1 || cbox == "") {
        layer.msg("只能选中一个");
        return;
    }
    pageii = layer.open({
        title : "编辑",
        type : 2,
        area : [ "600px", "60%" ],
        content : rootPath + '/apiSetting/editUI?id=' + cbox
    });
}
function addSetting() {
    pageii = layer.open({
        title : "新增",
        type : 2,
        area : [ "600px", "60%" ],
        content : rootPath + '/apiSetting/addUI'
    });
}
function delSetting() {
    var cbox = grid.getSelectedCheckbox();
    if (cbox == "") {
        layer.msg("请选择删除项！！");
        return;
    }
    layer.confirm('是否删除？', function(index) {
        var url = rootPath + '/apiSetting/deleteEntity';
        var s = CommnUtil.ajax(url, {
            ids : cbox.join(",")
        }, "json");
        if (s == "success") {
            layer.msg('删除成功');
            grid.loadData();
        } else if (s == "fail") {
            layer.msg('删除异常');
        } else {
            layer.msg('删除失败');
        }
    });
}
function recoverAccount() {
    pageii = layer.open({
        title : "恢复",
        type : 2,
        area : [ "600px", "80%" ],
        content : rootPath + '/apiSetting/recoverUI'
    });

}