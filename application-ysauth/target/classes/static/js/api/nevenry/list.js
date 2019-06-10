var pageii = null;
var grid = null;
$(function() {
    grid = lyGrid({
        id : 'paging',
        height : '100%', // 表格宽度
        theadHeight : '28px', // 表格的thead高度
        tbodyHeight : '27px',// 表格body的每一行高度
        isFixed : true,// 是
        checkValue : "FNUMBER",
        l_column : [ {
            colkey : "FNUMBER",
            name : "物料编码",
            isSort : true
        }, {
            colkey : "FNAME",
            name : "物料名称",
            isSort : true
        }, {
            colkey : "FLOT",
            name : "批号",
            isSort : true
        }, {
            colkey : "UNITNAME",
            name : "库存主单位",
            isSort : true
        }, {
            colkey : "MNAME",
            name : "仓库名称",
            isSort : true
        }, {
            colkey : "FBASEQTY",
            name : "库存量（主单位）",
            renderData : function(rowindex, data, rowdata, column) {
                return parseInt(data);
            },
            isSort : true
        }, {
            colkey : "ORGNAME",
            name : "库存组织",
            isSort : true
        }
        ],
        jsonUrl : rootPath + '/apinventory/findByPage',
        checkbox : true
    });
    $("#search").click("click", function() {// 绑定查询按扭
        var searchParams = $("#searchForm").serializeJson();// 初始化传参数
        grid.setOptions({
            data : searchParams
        });
    });
});
