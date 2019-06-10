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
            colkey : "pdNum",
            name : "盘点编号",
            isSort : true,
        },{
            colkey : "pdName",
            name : "盘点名称",
            isSort : true,
        },{
            colkey : "organ",
            name : "所属组织",
            isSort : true,
        },{
            colkey : "status",
            name : "状态",
            renderData : function(rowindex, data, rowdata, column) {
                if (data == "0") {
                    return "<font color=green>未审核</font>";
                } else if (data == "1"){
                    return "<font color=red>审核</font>";
                } else if (data == "2"){
                    return "<font color=red>返审核</font>";
                }
            }
        }, {
            colkey : "jdDate",
            name : "建单时间",
            isSort : true,
            renderData : function(rowindex, data, rowdata, column) {
                return new Date(data).format("yyyy-MM-dd hh:mm:ss");
            }
        }],
        jsonUrl : rootPath + '/stassets/findByPage3',
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
    $("#pyAccount").click("click", function() {
    	pyAccount();
    });
    $("#zpAccount").click("click", function() {
    	zpAccount();
    });
    $("#pkAccount").click("click", function() {
    	pkAccount();
    });
});
function pyAccount() {
    var cbox = grid.getSelectedCheckbox();
    if (cbox.length > 1 || cbox == "") {
        layer.msg("只能选中一个");
        return;
    }
    pageii = layer.open({
        title : "盘盈信息",
        type : 2,
        area : [ "800px", "80%" ],
        content : rootPath + '/stassets/pyMsg?id=' + cbox
    });
}
function zpAccount() {
    var cbox = grid.getSelectedCheckbox();
    if (cbox.length > 1 || cbox == "") {
        layer.msg("只能选中一个");
        return;
    }
    pageii = layer.open({
        title : "总盘信息",
        type : 2,
        area : [ "800px", "80%" ],
        content : rootPath + '/stassets/zpMsg?id=' + cbox
    });
}
function pkAccount() {
    var cbox = grid.getSelectedCheckbox();
    if (cbox.length > 1 || cbox == "") {
        layer.msg("只能选中一个");
        return;
    }
    pageii = layer.open({
        title : "盘亏信息",
        type : 2,
        area : [ "800px", "80%" ],
        content : rootPath + '/stassets/pkMsg?id=' + cbox
    });
}