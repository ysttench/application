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
            colkey : "wDesc",
            name : "故障描述",
            isSort : true,
        },{
            colkey : "witchCode",
            name : "故障码",
            isSort : true,
        }, {
            colkey : "witchDate",
            name : "故障时间时间",
            isSort : true,
            renderData : function(rowindex, data, rowdata, column) {
                return new Date(data).format("yyyy-MM-dd hh:mm:ss");
            }
        }, {
            colkey : "equipmentName",
            name : "设备名称",
            isSort : true,
        }],
        jsonUrl : rootPath + '/witch/findByPage',
    });
    $("#search").click("click", function() {// 绑定查询按扭
        var searchParams = $("#searchForm").serializeJson();// 初始化传参数
        grid.setOptions({
            data : searchParams
        });
    });
});