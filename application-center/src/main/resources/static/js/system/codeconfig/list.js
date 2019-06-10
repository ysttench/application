var pubId = "C0001";
/* 初始化页面数据 */
$(function() {
    grid = getMainGrid();
    /* 加载点击查询页面 */
    init();

    $("#editCodeConfig").click("click", function() {
        editCodeConfig();
    });
    $("#delCodeConfig").click("click", function() {
        delCodeConfig();
    });
    $("#addCodeConfig").click("click", function() {
        addCodeConfig(pubId);
    });
    $("#search").click("click", function() {// 绑定查询按扭
        var searchParams = $("#searchName").serializeJson();// 初始化传参数
        grid.setOptions({
            data : searchParams
        });
    });
});
function lyGridUp() {
    var cbox = grid.getSelectedCheckbox();
    if (cbox == "") {
        layer.alert("请选择上移项！！");
        return;
    }
    var jsonUrl = rootPath + '/codeconfig/sortUpdate/';
    grid.lyGridUp(jsonUrl);
}
function lyGridDown() {
    var cbox = grid.getSelectedCheckbox();
    if (cbox == "") {
        layer.alert("请选择下移项！！");
        return;
    }
    var jsonUrl = rootPath + '/codeconfig/sortUpdate';
    grid.lyGridDown(jsonUrl);
}
function getMainGrid() {
    return lyGrid({
        pagId : 'paging',
        height : '100%', // 表格宽度
        theadHeight : '28px', // 表格的thead高度
        tbodyHeight : '27px',// 表格body的每一行高度
        serNumber : true,
        checkValue : "id",
        l_column : [ {
            colkey : "configName",
            name : "配置参数名",
            isSort : true,
            align : "left",
        }, {
            colkey : "configValue",
            name : "配置参数值",
            isSort : true,
        }, {
            colkey : "configCode",
            name : "名称代码",
            isSort : true,
        }, {
            colkey : "description",
            name : "描述",
            isSort : true,
            align : "left",
        }, {
            colkey : "detailFlag",
            name : "明细",
            isSort : true,
            renderData : function(rowindex, data, rowdata, column) {
                if (data == "0") {
                    return "<font color=green>无明细</font>";
                }
                if (data == "1") {
                    return "<font color=blue>有明细</font>";
                }
            }
        }, {
            colkey : "createTime",
            name : "更新时间",
            isSort : true,
            renderData : function(rowindex, data, rowdata, column) {
                return new Date(data).format("yyyy-MM-dd hh:mm:ss");
            }
        } ],
        jsonUrl : rootPath + '/codeconfig/findByPage?configCode=' + "C0001",
        checkbox : true
    });
}

function getMxGrid() {
    return lyGrid({
        pagId : 'paging',
        height : '100%', // 表格宽度
        theadHeight : '28px', // 表格的thead高度
        tbodyHeight : '27px',// 表格body的每一行高度
        isFixed : true, // 是
        checkValue : "id",
        l_column : [ {
            colkey : "parentName",
            name : "父类名称",
            align : "left",
        }, {
            colkey : "configName",
            name : "名称",
            isSort : true,
            align : "left",
        }, {
            colkey : "configValue",
            name : "数值",
        } ],
        jsonUrl : rootPath + '/codeconfig/findByPage?configCode=' + pubId,
        checkbox : true
    });
}

function addCodeConfig(pubId) {
    pageii = layer.open({
        title : "新增",
        type : 2,
        area : [ "600px", "530px" ],
        content : rootPath + '/codeconfig/addUI?configCode=' + pubId
    });
}

function editCodeConfig() {
    var cbox = grid.getSelectedCheckbox();
    if (cbox.length > 1 || cbox == "") {
        layer.msg("只能选中一个");
        return;
    }
    pageii = layer.open({
        title : "编辑",
        type : 2,
        area : [ "600px", "530px" ],
        content : rootPath + '/codeconfig/editUI?id=' + cbox,
    });
}

function delCodeConfig() {
    var cbox = grid.getSelectedCheckbox();
    if (cbox == "") {
        layer.msg("请选择删除项！！");
        return;
    }
    layer.confirm('是否删除？', function(index) {
        var url = rootPath + '/codeconfig/deleteEntity';
        var s = CommnUtil.ajax(url, {
            ids : cbox.join(",")
        }, "json");
        if (s == "success") {
            grid.loadData();
            init();
            layer.msg('删除成功');
        } else {
            layer.msg('删除失败');
        }
    });
}

function init() {
    $("#ulTab").html("");
    var strHtml = "";
    strHtml += "<li id='" + "C0001" + "' class='selected'>" + "参数配置" + "</li>";
    $.ajax({
        url : rootPath + "/codeconfig/findData",
        type : "POST", // 请求方式
        data : {}, // 请求参数
        dataType : "json",
        success : function(data) {
            if (data != null) {
                for (var i = 0; i < data.length; i++) {
                    if (data[i].deletedFlag != "1") {
                        strHtml += "<li id='" + data[i].configCode + "'>" + data[i].configName + "</li>";
                    }
                }
                $("#ulTab").append(strHtml);
                $("#ulTab li").on("click",function() {
                    $(this).parent().find(".selected").removeClass("selected");
                    $(this).addClass("selected");
                    pubId = $(this).attr("id");
                    $("#lyGridUp").remove();
                    $("#lyGridDown").remove();
                    if (pubId != "C0001") {
                        grid = getMxGrid();
                        $("#opdiv").append(
                            "<button type='button' id='lyGridUp' class='mini-btn border-color-blue none' onclick=lyGridUp() >上移</button>"
                            + "<button type='button' id='lyGridDown' class='mini-btn border-color-blue none' onclick=lyGridDown() >下移</button>");
                    } else {
                        grid = getMainGrid();
                    }
                });
            }
        }
    });
}
