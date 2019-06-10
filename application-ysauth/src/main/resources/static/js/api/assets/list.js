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
        checkValue : "id",
        l_column : [   {
            colkey : "fnumber",
            name : "卡片编号",
            isSort : true
        },   {
            colkey : "fassetno",
            name : "资产编号",
            isSort : true
        },{
            colkey : "fname",
            name : "资产名称",
            isSort : true
        }, {
            colkey : "typename",
            name : "资产分类",
            isSort : true
        },{
            colkey : "placename",
            name : "资产位置",
            isSort : true
        },{
            colkey : "departname",
            name : "所属部门",
            isSort : true
        },{
            colkey : "username",
            name : "保管人",
            isSort : true
        },{
            colkey : "orgname",
            name : "资产组织",
            isSort : true
        }, {
            colkey : "fquantity",
            name : "数量",
            renderData : function(rowindex, data, rowdata, column) {
                return parseInt(data);
            },
            isSort : true
        }, {
            colkey : "unitname",
            name : "单位",
            isSort : true
        }, {
            colkey : "fcreatedate",
            name : "时间",
            isSort : true,
            renderData : function(rowindex, data, rowdata, column) {
				return new Date(data).format("yyyy-MM-dd hh:mm:ss");
			},
            isSort : true
        } ],
        jsonUrl : rootPath + '/assets/findByPage',
        checkbox : true,
        serNumber : true
    });
    $("#search").click("click", function() {// 绑定查询按扭
        var searchParams = $("#searchForm").serializeJson();// 初始化传参数
        grid.setOptions({
            data : searchParams
        });
    });
    $("#print").click("click", function() {
        print();
    });
});
function print() {
	debugger;
    var cbox = grid.getSelectedCheckbox();
    if (cbox == "") {
        layer.msg("请选择打印项！！");
        return;
    }
    layer.confirm('是否打印？', function(index) {
        var url = rootPath + '/assets/print';
        var s = CommnUtil.ajax(url, {
            ids : cbox.join(",")
        }, "json");
        if (s == "success") {
            layer.msg('打印成功');
            grid.loadData();
        } else {
            layer.msg(s);
        }
    });
}