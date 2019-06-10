var unit = "<option value=''>---全部----</option>";
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
        l_column : [   {
            colkey : "locationCode",
            name : "位置编码",
            isSort : true,
        }, {
            colkey : "superName",
            name : "上级位置",
            isSort : true
        }, {
            colkey : "locationName",
            name : "位置名称"
        } ],
        jsonUrl : rootPath + '/apiOffice/findByPage',
        checkbox : true,
        serNumber : true
    });
    $("#search").click("click", function() {// 绑定查询按扭
        var searchParams = $("#searchForm").serializeJson();// 初始化传参数
        grid.setOptions({
            data : searchParams
        });
    });
    $("#addEquiment").click("click", function() {
        addAccount();
    });
    $("#editEquiment").click("click", function() {
        editAccount();
    });
    $("#delEquiment").click("click", function() {
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
        content : rootPath + '/apiOffice/editUI?id=' + cbox
    });
}
function addAccount() {
    pageii = layer.open({
        title : "新增",
        type : 2,
        area : [ "800px", "80%" ],
        content : rootPath + '/apiOffice/addUI'
    });
}
function delAccount() {
    var cbox = grid.getSelectedCheckbox();
    if (cbox == "") {
        layer.msg("请选择删除项！！");
        return;
    }
    layer.confirm('是否删除？', function(index) {
        var url = rootPath + '/apiOffice/deleteEntity';
        var s = CommnUtil.ajax(url, {
            ids : cbox.join(",")
        }, "json");
        if (s == "success") {
            layer.msg('删除成功');
            grid.loadData();
        } else if(s == "fail"){
            layer.msg('删除异常');
        } else {
            layer.msg('删除失败');
        }
    });
}
