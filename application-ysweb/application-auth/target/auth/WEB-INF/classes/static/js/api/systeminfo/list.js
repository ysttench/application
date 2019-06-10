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
        l_column : [   {
            colkey : "apiName",
            name : "应用系统名称"
        }, {
            colkey : "apiUrl",
            name : "应用系统地址"
        },{
            colkey : "sysKey",
            name : "key"
        }, {
            colkey : "apiDeveloper",
            name : "开发商"
        },{
            colkey : "deptName",
            name : "管理部门",
            width : '90px'
        },{
            colkey:"comment",
            name :"说明"
        }  ],
        jsonUrl : rootPath + '/apisysteminfo/findByPage',
        checkbox : true,
        serNumber : true
    });
    $("#search").click("click", function() {// 绑定查询按扭
        var searchParams = $("#searchForm").serializeJson();// 初始化传参数
        grid.setOptions({
            data : searchParams
        });
    });
    $("#lookAccount").click("click", function() {
        lookAccount();
    });
});
function lookAccount() {
    var cbox = grid.getSelectedCheckbox();
    if (cbox.length > 1 || cbox == "") {
        layer.msg("只能选中一个");
        return;
    }
    pageii = layer.open({
        title : "查看",
        type : 2,
        area : [ "800px", "80%" ],
        content : rootPath + '/apisysteminfo/editUI?id=' + cbox
    });
}
