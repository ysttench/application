var pageii = null;
var grid = null;
$(function() {
    grid = lyGrid({
        id : 'paging',
        height : '100%', // 表格宽度
        theadHeight : '28px', // 表格的thead高度
        tbodyHeight : '27px',// 表格body的每一行高度
        isFixed : true,// 是
        checkValue : "FBILLNO",
        l_column : [ {
            colkey : "FBILLNO",
            name : "单据编号",
            isSort : true
        },{
            colkey : "FDATE",
            name : "日期",
            renderData : function(rowindex, data, rowdata, column) {
                return new Date(data).format("yyyy-MM-dd");
            },
            isSort : true
        },{
            colkey : "FNUMBER",
            name : "物料编码",
            isSort : true
        },{
            colkey : "FNAME",
            name : "物料名称",
            isSort : true
        },{
            colkey : "UNITNAME",
            name : "库存单位",
            isSort : true
        },{
            colkey : "FSTOCKACTUALQTY",
            name : "出料数量",
            renderData : function(rowindex, data, rowdata, column) {
                return parseInt(data);
            },
            isSort : true
        },{
            colkey : "ORGNAME",
            name : "发料组织",
            isSort : true
        },{
            colkey : "MANE",
            name : "仓库",
            isSort : true
        },{
            colkey : "FDOCUMENTSTATUS",
            name : "单据状态",
            renderData : function(rowindex, data, rowdata, column) {
                if (data == "A") {
                    return "<font color=green>新建</font>";
                } else if(data == "B") {
                    return "<font color=red>审核中</font>";
                }else if(data == "C") {
                    return "<font color=red>已审核</font>";
                }else if(data == "D") {
                    return "<font color=red>重新审核</font>";
                }else if(data == "Z") {
                    return "<font color=red>暂存</font>";
                }
            },
            isSort : true
        } ],
        jsonUrl : rootPath + '/product/findByPage',
        checkbox : true
    });
    $("#search").click("click", function() {// 绑定查询按扭
        var searchParams = $("#searchForm").serializeJson();// 初始化传参数
        grid.setOptions({
            data : searchParams
        });
    });
});
