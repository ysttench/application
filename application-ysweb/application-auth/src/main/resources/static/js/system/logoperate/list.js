var dialog;
var grid;
$(function() {

    grid = lyGrid({
        id : 'paging',
        height : '100%', // 表格宽度
        theadHeight : '28px', // 表格的thead高度
        tbodyHeight : '27px',// 表格body的每一行高度
        isFixed : true,// 是
        l_column : [ {
            colkey : "id",
            name : "id",
            hide : true
        }, {
            colkey : "userName",
            name : "账号",
            isSort : true
        }, {
            colkey : "module",
            name : "模块名称",
            isSort : true
        }, {
            colkey : "method",
            name : "执行方法",
            isSort : true
        }, {
            colkey : "actionTime",
            name : "响应时间",
            width : "150px",
            isSort : true
        }, {
            colkey : "userIpAddress",
            name : "IP地址",
            isSort : true
        }, {
            colkey : "operateTime",
            name : "执行时间",
            isSort : true,
            renderData : function(rowindex, data, rowdata, column) {
                return new Date(data).format("yyyy-MM-dd hh:mm:ss");
            }
        }, {
            colkey : "description",
            name : "执行描述"
        } ],
         jsonUrl : rootPath + '/logoperate/findByPage',
        checkbox : false
    });
    $("#search").click("click", function() {// 绑定查询按扭
        var searchParams = $("#searchForm").serializeJson();
        grid.setOptions({
            data : searchParams
        });
    });
});