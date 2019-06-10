var unit = "<option value=''>---全部----</option>";
var pageii = null;
var grid = null;
$(function() {
    grid = lyGrid({
        pagId : 'paging',
        height : '100%', // 表格宽度
        theadHeight : '28px', // 表格的thead高度
        tbodyHeight : '27px',// 表格body的每一行高度
        isFixed : true,// 是
        checkValue : "roleId",
        l_column : [ {
            colkey : "roleName",
            name : "角色名",
            isSort : true,
        }/*,{
          colkey:"apiName",
          name:"应用系统名称"
        }*/, 
        {
            colkey : "comment",
            name : "备注"
        }, 
        {
            colkey : "roleKey",
            name : "角色key"
        },
        {
            colkey : "createTime",
            name : "创建时间",
            isSort : true,
            renderData : function(rowindex, data, rowdata, column) {
                return new Date(data).format("yyyy-MM-dd hh:mm:ss");
            }
        }],
        jsonUrl : rootPath + '/apiRole/findByPage',
        checkbox : true
    });
    $("#search").click("click", function() {// 绑定查询按扭
        var searchParams = $("#searchForm").serializeJson();// 初始化传参数
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
    $("#yyxt").change("change", function() {// 绑定查询按扭
        var searchParams = $("#yyxt").serializeJson();// 初始化传参数
        grid.setOptions({
            data : searchParams
        });
    });
    $.ajax({
        url: rootPath +"/apiRole/findAllXT",
        data : {},
        dataType : "json",
        success : function(data){
            debugger;
            $.each(data,function(i,n){
                unit +="<option value='"+n.id+"'>"+n.apiName+"</option>"
            })
            $("#yyxt").empty().append(unit);
            $("#yyxt option").each(function(){
                var val =$(this).val();
                if($("#yyxt option[value='"+val+"']").size()>1){
                    $("#yyxt option[value='"+val+"']:gt(0)").remove();
                }
            })
        }
    })
    $("#addRole").click("click", function() {
        addRole();
    });
    $("#editRole").click("click", function() {
        editRole();
    });
    $("#delRole").click("click", function() {
        delRole();
    });
    $("#permissions").click("click", function() {
        permissions();
    });
    $("#recoverAccount").click("click", function() {
        recoverAccount();
    });
});
function editRole() {
    var cbox = grid.getSelectedCheckbox();
    if (cbox.length > 1 || cbox == "") {
        layer.msg("只能选中一个");
        return;
    }
    pageii = layer.open({
        title : "编辑",
        type : 2,
        area : [ "600px", "60%" ],
        content : rootPath + '/apiRole/editUI?id=' + cbox
    });
}
function permissions() {
    var cbox = grid.getSelectedCheckbox();
    if (cbox.length > 1 || cbox == "") {
        layer.msg("请选择一个对象！");
        return;
    }
    var url = rootPath + '/right/menuRight?roleId=' + cbox;
    pageii = layer.open({
        title : "分配权限",
        type : 2,
        area : [ "700px", "60%" ],
        content : url
    });
}
function addRole() {
    pageii = layer.open({
        title : "新增",
        type : 2,
        area : [ "600px", "60%" ],
        content : rootPath + '/apiRole/addUI'
    });
}
function delRole() {
    var cbox = grid.getSelectedCheckbox();
    if (cbox == "") {
        layer.msg("请选择删除项！！");
        return;
    }
    layer.confirm('是否删除？', function(index) {
        var url = rootPath + '/apiRole/deleteEntity';
        var s = CommnUtil.ajax(url, {
            ids : cbox.join(",")
        }, "json");
        if (s == "success") {
            layer.msg('删除成功');
            grid.loadData();
        } else if(s == "fail"){
            layer.msg('删除异常');
        } else {
            layer.msg('删除失败');
        }
    });
}
function recoverAccount() {
    pageii = layer.open({
        title : "恢复",
        type : 2,
        area : [ "600px", "80%" ],
        content : rootPath + '/apiRole/recoverUI'
    });

}