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
        checkValue : "0",
        l_column : [   {
            colkey : "1",
            name : "列1"
        },   {
            colkey : "2",
            name : "列2"
        },{
            colkey : "3",
            name : "列3"
        }, {
            colkey : "4",
            name : "列4"
        },{
            colkey : "5",
            name : "列5"
        },{
            colkey : "6",
            name : "列6"
        } ],
        jsonUrl : rootPath + '/excel/findByPage',
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
        content : rootPath + '/excel/addUI'
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
        var url = rootPath + '/excel/print';
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