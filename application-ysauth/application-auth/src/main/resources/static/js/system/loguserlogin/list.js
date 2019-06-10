var pageii = null;
var grid = null;
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
            width : "50px",
            hide : true
        }, {
            colkey : "userName",
            name : "账号"
        }, {
            colkey : "loginTime",
            name : "登入时间",
            isSort : true,
            renderData : function(rowindex, data, rowdata, column) {
                return new Date(data).format("yyyy-MM-dd hh:mm:ss");
            }
        }, {
            colkey : "loginIp",
            name : "登入IP"
        } ],
        jsonUrl : rootPath + '/loguserlogin/findByPage',
        checkbox : true
    });
    $("#search").click("click", function() {// 绑定查询按扭
        var searchParams = $("#searchForm").serializeJson();
        grid.setOptions({
            data : searchParams
        });
    });
});
