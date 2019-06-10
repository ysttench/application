$(function() {
    //树形图
    showpic();
    // 统计
    yhNum();
    xtNum();
    bmNum();
})
function yhNum() {
    $.ajax({
        type : "post",
        datatype : "json",
        url : rootPath + "/apiUser/yhNum",
        success : function(msg) {
            $("#yh").empty().append(msg);
        }
    })
}
function xtNum() {
    $.ajax({
        type : "post",
        datatype : "json",
        url : rootPath + "/apisysteminfo/xtNum",
        success : function(msg) {
            $("#xt").empty().append(msg);
        }
    })
}
function bmNum() {
    $.ajax({
        type : "post",
        datatype : "json",
        url : rootPath + "/sysdepartment/bmNum",
        success : function(msg) {
            $("#bm").empty().append(msg);
        }
    })
}
function showpic() {
    $.ajax({
        type : "post",
        datatype : "json",
        url : rootPath + "/apiuserlogin/datalist",
        success : function(msg) {
            var obj = jQuery.parseJSON(msg);
            var time = new Array();
            var data = new Array();
            $.each(obj, function(i, n) {
                time.push(n.apiName);
                data.push(n.num);
            });
            option = {
                    tooltip : {
                        trigger : 'axis',
                        axisPointer : { // 坐标轴指示器，坐标轴触发有效
                            type : 'shadow' // 默认为直线，可选为：'line' | 'shadow'
                        }
                    },
                    xAxis : [ {
                        type : 'category',
                        data : [ '情报自助', '应用中心', '服务中心', '特征研判', '网警平台' ],
                    } ],
                    yAxis : [ {
                        type : 'value'
                    } ],
                    series : [ {
                        name : '数量',
                        type : 'bar',
                        barWidth : '60%',
                        data : [ 110, 52, 200, 334, 390 ]
                    } ]
                };
                var chart = echarts.init(document.getElementById("echart"));
                option.xAxis[0].data = time;
                option.series[0].data = data;
                chart.setOption(option);
        }
    });
}
