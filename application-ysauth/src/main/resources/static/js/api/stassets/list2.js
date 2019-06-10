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
            colkey : "assetsNum",
            name : "资产编号",
            isSort : true,
        },{
            colkey : "assetsName",
            name : "资产名称"
        },{
            colkey : "assetsType",
            name : "资产分类"
        },{
            colkey : "place",
            name : "位置"
        },{
            colkey : "keeper",
            name : "保管人"
        },{
            colkey : "xh",
            name : "型号"
        },{
            colkey : "keepdepart",
            name : "保管部门"
        },{
            colkey : "sqer",
            name : "录入人"
        },{
            colkey : "sper",
            name : "审批人"
        },{
            colkey : "organ",
            name : "所属组织"
        },{
            colkey : "status",
            name : "状态",
            renderData : function(rowindex, data, rowdata, column) {
                if (data == "0") {
                    return "<font color=green>未审批</font>";
                } else if (data == "1"){
                    return "<font color=red>审批</font>";
                } else if (data == "2"){
                    return "<font color=red>返审批</font>";
                }
            }
        }, {
            colkey : "gmdate",
            name : "购买时间",
            isSort : true,
            renderData : function(rowindex, data, rowdata, column) {
                return new Date(data).format("yyyy-MM-dd hh:mm:ss");
            }
        }],
        jsonUrl : rootPath + '/stassets/findByPage',
        checkbox : true,
        serNumber : true
    });
    $("#search").click("click", function() {// 绑定查询按扭
    	debugger
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
    $("#shAccount").click("click", function() {
    	shAccount();
    });
    $("#fshAccount").click("click", function() {
    	fshAccount();
    });
});
function shAccount() {
    var cbox = grid.getSelectedCheckbox();
    if (cbox == "") {
        layer.msg("请选择审核项！！");
        return;
    }
    layer.confirm('是否审核通过？', function(index) {
        var url = rootPath + '/stassets/shEntity';
        var s = CommnUtil.ajax(url, {
            ids : cbox.join(",")
        }, "json");
        if (s == "success") {
            layer.msg('审核成功');
            grid.loadData();
        } else if(s == "fail1"){
        	layer.msg('当前资产信息状态需是未审核');
        } else {
            layer.msg('审核失败');
        }
    });
}
function fshAccount() {
    var cbox = grid.getSelectedCheckbox();
    if (cbox == "") {
        layer.msg("请选择反审核项！！");
        return;
    }
    layer.confirm('是否反审核？', function(index) {
        var url = rootPath + '/stassets/fshEntity';
        var s = CommnUtil.ajax(url, {
            ids : cbox.join(",")
        }, "json");
        if (s == "success") {
            layer.msg('反审核成功');
            grid.loadData();
        } else if(s == "fail1"){
        	layer.msg('当前资产信息状态需是审核');
        }else {
            layer.msg('反审核失败');
        }
    });
}


