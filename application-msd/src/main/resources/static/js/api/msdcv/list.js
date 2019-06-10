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
            colkey : "msdNum",
            name : "MSD编号",
            isSort : true,
        }, {
            colkey : "flot",
            name : "批号"
        }, {
            colkey : "msdType",
            name : "MSD分类"
        }, {
            colkey : "msdLevel",
            name : "MSD等级"
        }, {
            colkey : "thick",
            name : "厚度（mm）"
        }, {
            colkey : "pmResistance",
            name : "包装物耐热性"
        }, {
            colkey : "status",
            name : "状态",
            width : '90px',
            isSort : true,
            renderData : function(rowindex, data, rowdata, column) {
                if (data == "0") {
                    return "<font color=green>可用</font>";
                } else {
                    return "<font color=red>不可用</font>";
                }
            }
        }, {
            colkey : "bakeStatus",
            name : "烘烤状态",
            renderData : function(rowindex, data, rowdata, column) {
            	if (data == "0") {
                    return "<font color=green>未烘烤</font>";
                } else if(data == "1") {
                    return "<font color=yellow>已烘烤1次</font>";
                }else if(data == "2") {
                    return "<font color=yellow>已烘烤2次</font>";
                }else if(data == "0") {
                    return "<font color=yellow>已烘烤3次</font>";
                }else{
                	return "<font color=red>不可烘烤</font>";
                }
            }
        }, {
            colkey : "jycl",
            name : "建议处理方式"
        }],
        jsonUrl : rootPath + '/msdcv/findByPage',
        checkbox : true,
        serNumber : true
    });
    $("#search").click("click", function() {// 绑定查询按扭
        var searchParams = $("#searchForm").serializeJson();// 初始化传参数
        grid.setOptions({
            data : searchParams
        });
    });
    $("#searchdeptcode").click("click", function() {// 绑定查询按扭
        var searchParams = $("#deptcodeId").serializeJson();// 初始化传参数
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
});
