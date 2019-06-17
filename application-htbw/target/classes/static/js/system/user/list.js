var pageii = null;
var grid = null;
$(function() {

    grid = lyGrid({
        pagId : 'paging',
        height : '100%', // 表格宽度
        theadHeight : '28px', // 表格的thead高度
        tbodyHeight : '27px',// 表格body的每一行高度
        isFixed : true,// 是
        checkValue : "userId",
        l_column : [  {
            colkey : "userName",
            name : "账号",
            isSort : true,
        }, {
            colkey : "name",
            name : "用户名",
            isSort : true,
        }, {
            colkey : "roleName",
            name : "所属角色",
            renderData : function(rowindex, data, rowdata, column) {
                return data;
            }
        }, {
            colkey : "locked",
            name : "账号状态",
            width : '90px',
            isSort : true,
            renderData : function(rowindex, data, rowdata, column) {
                if (data == "1") {
                    return "<font color=green>启用</font>";
                } else {
                    return "<font color=red>禁用</font>";
                }
            }
        }, {
            colkey : "description",
            name : "备注"
        }, {
            colkey : "createTime",
            name : "时间",
            isSort : true,
            renderData : function(rowindex, data, rowdata, column) {
                return new Date(data).format("yyyy-MM-dd hh:mm:ss");
            }
        }],
        jsonUrl : rootPath + '/user/findByPage',
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
    $("#addAccount").click("click", function() {
        addAccount();
    });
    $("#editAccount").click("click", function() {
        editAccount();
    });
    $("#delAccount").click("click", function() {
        delAccount();
    });
    $("#permissions").click("click", function() {
        permissions();
    });
    $('.deptcode').on('click',function(){
        var id = $(this).attr('id');
        var hid = $(this).parent().find('.deptcode[type="hidden"]').attr('id');
        pageii = layer.open({
            title : ["","text-align:center;font-weight:bold;"],
            type : 2,
            area : [ "800px", "400px" ],
            content : rootPath + '/sysdepartment/deptUI?id='+id+'&hid='+hid
        });
    });
});
function editAccount() {
    var cbox = grid.getSelectedCheckbox();
    if (cbox.length > 1 || cbox == "") {
        layer.msg("只能选中一个");
        return;
    }
    pageii = layer.open({
        title : "编辑",
        type : 2,
        area : [ "800px", "80%" ],
        content : rootPath + '/user/editUI?id=' + cbox
    });
}
function addAccount() {
    pageii = layer.open({
        title : "新增",
        type : 2,
        area : [ "800px", "80%" ],
        content : rootPath + '/user/addUI'
    });
}
function delAccount() {
    var cbox = grid.getSelectedCheckbox();
    if (cbox == "") {
        layer.msg("请选择删除项！！");
        return;
    }
    layer.confirm('是否删除？', function(index) {
        var url = rootPath + '/user/deleteEntity';
        var s = CommnUtil.ajax(url, {
            ids : cbox.join(",")
        }, "json");
        if (s == "success") {
            layer.msg('删除成功');
            grid.loadData();
        } else {
            layer.msg('删除失败');
        }
    });
}
function permissions() {
    var cbox = grid.getSelectedCheckbox();
    if (cbox.length > 1 || cbox == "") {
        layer.msg("请选择一个对象！");
        return;
    }
    var url = rootPath + '/menu/permissions?userId=' + cbox;
    pageii = layer.open({
        title : "分配权限",
        type : 2,
        area : [ "700px", "80%" ],
        content : url
    });
}