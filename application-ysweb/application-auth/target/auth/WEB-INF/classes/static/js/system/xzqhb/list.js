var pageii = null;
var grid = null;
$(function() {
    grid = lyGrid({
        id : 'paging',
        height : '100%', // 表格宽度
        theadHeight : '28px', // 表格的thead高度
        tbodyHeight : '27px',// 表格body的每一行高度
        checkValue : "dzbm",
        l_column : [ {
            colkey : "dm",
            name : "行政区划代码",
            isSort : true
        }, {
            colkey : "mc",
            name : "行政区划名称"
        }, {
            colkey : "zwpy",
            name : "中文拼音"
        },{
            colkey : "bz",
            name : "备注说明"
        },{
            colkey : "dzbm",
            name : "地址编码",
            hide:true
        }],
        jsonUrl : rootPath + '/xzqhb/findByPage',
        checkbox : true
    });
    $("#search").click("click", function() {// 绑定查询按扭
        var searchParams = $("#searchForm").serializeJson();// 初始化传参数
        grid.setOptions({
            data : searchParams
        });
    });
    $("#addXZQHB").click("click", function() {
        addXZQHB();
    });
    $("#editXZQHB").click("click", function() {
        editXZQHB();
    });
    $("#delXZQHB").click("click", function() {
        delXZQHB();
    });
    $("#resource").click("click", function() {
        //resourceAllocation();
        permissions();
    });

});

function editXZQHB() {
    var cbox = grid.getSelectedCheckbox();
    if (cbox.length > 1 ) {
        layer.msg("请选择一条数据");
        return;
    }else if(cbox == ""){
        layer.msg("请先选中一个");
        return;
    }
    pageii = layer.open({
        title : ["行政区划编辑","text-align:center;font-weight:bold;"],
        type : 2,
        area : [ "600px", "450px" ],
        content : rootPath + '/xzqhb/editUI?id=' + cbox
    });
}

function addXZQHB() {
    pageii = layer.open({
        title : ["行政区划添加","text-align:center;font-weight:bold;"],
        type : 2,
        area : [ "600px", "450px" ],
        content : rootPath + '/xzqhb/addUI'
    });
}

function delXZQHB() {
    var cbox = grid.getSelectedCheckbox();
    if (cbox == "") {
        layer.msg("请选择删除项！！");
        return;
    }
    layer.confirm('是否删除？', function(index) {
        var url = rootPath + '/xzqhb/deleteEntity';
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
