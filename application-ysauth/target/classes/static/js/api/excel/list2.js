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
            colkey : "lh",
            name : "料号"
        },   {
            colkey : "pm",
            name : "品名"
        },{
            colkey : "gg",
            name : "规格"
        }, {
            colkey : "sl",
            name : "数量"
        },{
            colkey : "ph",
            name : "批号"
        },{
            colkey : "xh",
            name : "S/N"
        } ],
        jsonUrl : rootPath + '/excel/findByPage1',
        checkbox : true,
        serNumber : true
    });
    $("#upload").click("click", function() {
    	upload();
    });
    $("#print").click("click", function() {
        print();
    });
});
function upload(){
    pageii = layer.open({
        title : "载入excel",
        type : 2,
        area : [ "800px", "80%" ],
        content : rootPath + '/excel/addUI1'
    });
}
function print() {
	debugger;
    var cbox = grid.getSelectedCheckbox();
    if (cbox == "") {
        layer.msg("请选择打印项！！");
        return;
    }
    layer.confirm('是否打印？', function(index) {
        var url = rootPath + '/excel/print1';
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