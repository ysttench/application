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
        checkValue : "userId",
        l_column : [   {
            colkey : "userName",
            name : "账号",
            isSort : true,
        }, {
            colkey : "enabledFlag",
            name : "启用状态",
            width : '90px',
            isSort : true,
            renderData : function(rowindex, data, rowdata, column) {
                if (data == "0") {
                    return "<font color=green>已启用</font>";
                }else if (data == "1"){
                    return "<font color=red>未启用</font>";
                }
            }
        },{
            colkey : "name",
            name : "姓名"
        }, {
            colkey : "sfzh",
            name : "身份证号"
        },{
            colkey : "sex",
            name : "性别",
            width : '90px',
            isSort : true,
            renderData : function(rowindex, data, rowdata, column) {
                if (data == "0") {
                    return "<font color=green>男</font>";
                } else {
                    return "<font color=red>女</font>";
                }
            }
        },{
            colkey : "phone",
            name : "手机号码"
        },{
            colkey : "email",
            name : "邮箱"
        }, {
            colkey : "avatar",
            name : "头像",
            renderData : function(rowindex, data, rowdata, column) {
                if(data){
                    return '<img src="data:image/jpeg|png|gif;base64,'+data+'" style="width: 43px;height: 33px;"  alt="暂无图片" />';
                }else{
                    return '<img src="'+rootPath+'/images/business_config_default.png" style="width: 43px;height: 33px;"  title="默认图标"/>';
                }
            }
        },{
          colkey :"policeId",
          name :"警号"
        },{
            colkey :"deptName",
            name :"部门"
          },{
            colkey : "createTime",
            name : "创建时间",
            isSort : true,
            renderData : function(rowindex, data, rowdata, column) {
                return new Date(data).format("yyyy-MM-dd hh:mm:ss");
            }
        },{
            colkey:"comment",
            name :"备注"
        }  ],
        jsonUrl : rootPath + '/apiUser/findByPage',
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
    $("#recoverAccount").click("click", function() {
        recoverAccount();
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
        content : rootPath + '/apiUser/editUI?id=' + cbox
    });
}
function addAccount() {
    pageii = layer.open({
        title : "新增",
        type : 2,
        area : [ "800px", "80%" ],
        content : rootPath + '/apiUser/addUI'
    });
}
function delAccount() {
    var cbox = grid.getSelectedCheckbox();
    if (cbox == "") {
        layer.msg("请选择删除项！！");
        return;
    }
    layer.confirm('是否删除？', function(index) {
        var url = rootPath + '/apiUser/deleteEntity';
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
        area : [ "800px", "80%" ],
        content : rootPath + '/apiUser/recoverUI'
    });
    
}