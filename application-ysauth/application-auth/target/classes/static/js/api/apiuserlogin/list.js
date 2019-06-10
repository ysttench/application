var unit = "<option value=''>---全部----</option>";
var pageii = null;
var grid = null;
$(function() {
    grid = lyGrid({
        id : 'paging',
        height : '100%', // 表格宽度
        theadHeight : '28px', // 表格的thead高度
        tbodyHeight : '27px',// 表格body的每一行高度
        isFixed : true,// 是
        l_column : [ {
            colkey : "id",
            name : "id",
            width : "50px",
            hide : true
        }, {
            colkey : "userName",
            name : "用户名"
        }, {
            colkey : "loginIp",
            name : "登录ip"
        }, {
            colkey : "loginTime",
            name : "登入时间",
            isSort : true,
            renderData : function(rowindex, data, rowdata, column) {
                return new Date(data).format("yyyy-MM-dd hh:mm:ss");
            }
        }, {
            colkey : "apiName",
            name : "登入系统"
        } ],
        jsonUrl : rootPath + '/apiuserlogin/findByPage',
        checkbox : true
    });
    $("#search").click("click", function() {// 绑定查询按扭
        var searchParams = $("#searchForm").serializeJson();
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
                unit +="<option value='"+n.apiName+"'>"+n.apiName+"</option>"
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
});
