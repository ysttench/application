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
            colkey : "organ",
            name : "组织名称"
        }, {
            colkey : "wsdlUrl",
            name : "url地址"
        },  {
            colkey : "nameSpace",
            name : "命名空间"
        },{
            colkey : "xNum",
            name : "x轴距离"
        },{
            colkey : "yNum",
            name : "y轴距离"
        },{
            colkey : "module",
            name : "打印方法"
        },{
            colkey : "type",
            name : "打印分类",
            renderData : function(rowindex, data, rowdata, column) {
                if (data == "0") {
                    return "<font color=green>出入库</font>";
                } else if(data == "1") {
                    return "<font color=red>固定资产</font>";
                }else if(data == "2") {
                    return "<font color=red>列管资产</font>";
                }else if(data == "3") {
                    return "<font color=red>excel打印管理</font>";
                }else{
                    return "<font color=red>excel打印仓储</font>";
                }
            }
        },{
            colkey : "num",
            name : "一次打印上限"
        } ],
        jsonUrl : rootPath + '/print/findByPage',
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
    $("#addPrint").click("click", function() {
        addPrint();
    });
    $("#editPrint").click("click", function() {
        editPrint();
    });
    $("#delPrint").click("click", function() {
        delPrint();
    });
});
function editPrint() {
    var cbox = grid.getSelectedCheckbox();
    if (cbox.length > 1 || cbox == "") {
        layer.msg("只能选中一个");
        return;
    }
    pageii = layer.open({
        title : "编辑",
        type : 2,
        area : [ "600px", "60%" ],
        content : rootPath + '/print/editUI?id=' + cbox
    });
}
function addPrint() {
    pageii = layer.open({
        title : "新增",
        type : 2,
        area : [ "600px", "60%" ],
        content : rootPath + '/print/addUI'
    });
}
function delPrint() {
    var cbox = grid.getSelectedCheckbox();
    if (cbox == "") {
        layer.msg("请选择删除项！！");
        return;
    }
    layer.confirm('是否删除？', function(index) {
        var url = rootPath + '/print/deleteEntity';
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
