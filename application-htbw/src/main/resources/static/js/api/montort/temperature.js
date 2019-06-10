//时间格式化
function curDateTime(y, m, d, h, mi, s) {
    var month = m;
    var date = d;
    var Hours = h;
    var Minutes = mi;
    var Seconds = s;
    var curDateTime = y;
    if (month > 9)
        curDateTime = curDateTime + '-' + month;
    else
        curDateTime = curDateTime + "-0" + month;
    if (date > 9)
        curDateTime = curDateTime + '-' + date;
    else
        curDateTime = curDateTime + "-0" + date;
    if (Hours > 9)
        curDateTime = curDateTime + ' ' + Hours;
    else
        curDateTime = curDateTime + " 0" + Hours;
    if (Minutes > 9)
        curDateTime = curDateTime + ':' + Minutes;
    else
        curDateTime = curDateTime + ":0" + Minutes;
    if (Seconds > 9)
        curDateTime = curDateTime + ':' + Seconds;
    else
        curDateTime = curDateTime + ":0" + Seconds;
    return curDateTime;
}
/**
 * 湿度
 */
var humidityEcharts;//echart名
var humidityOption;//echart配置
/**
 * 温度计分装
 * name 名称
 * startNum 开始数
 * endNum 结束数字
 * spacingNum 间距
 * mercuryColor 水银色
 * borderColor 边框色
 * hoverColor 鼠标移上去的圆背景色
 * valueName 值的名
 * */
function temperature(name, startNum, endNum, spacingNum, mercuryColor, borderColor, hoverColor, valueName,b ,time) {
    var kd = [];
    // 刻度使用柱状图模拟，短设置3，长的设置5；构造一个数据
    for (var i = 0, len = endNum + spacingNum; i <= len; i++) {
        if (i > endNum || i < spacingNum) {
            kd.push('0')
        } else {
            if (i % spacingNum === 0) {
                kd.push('3');
            } else {
                kd.push('1');
            }
        }

    }
    humidityEcharts = echarts.init(document.getElementById(name));
    humidityOption = {
		tooltip: {
            trigger: 'axis',
          position: [0, 100],
            formatter: function (params) {
                var data = '';
                if (params[0]) {
                    var d = new Date();
                    data = '采集时间<br/>'+time + '<br/>';
                }
                var marker = '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:' + hoverColor + '"></span>';
                data = data + marker;
                data = data + $('#' + valueName).html() + b + '<br/>';
                return data;
            }
        },
        yAxis: [{
            show: false,
            min: 0,
            max: endNum + spacingNum
        }, {
            show: false,
            data: [],
            min: 0,
            max: endNum + spacingNum
        }],
        xAxis: [{
            show: false,
            data: []
        }, {
            show: false,
            data: []
        }, {
            show: false,
            data: []
        }, {
            show: false,
            min: -13,
            max: 8,

        }],
        series: [{
            name: '条',
            type: 'bar',
            // 对应上面XAxis的第一个对象配置
            xAxisIndex: 0,
            data: [],
            barWidth: 5,
            itemStyle: {
                normal: {
                    color: mercuryColor,
                    barBorderRadius: 0,
                }
            },
            label: {
                normal: {
                    show: false,
                    position: 'bottom',
                    formatter: function (param) {
                        return "";
                    },
                    textStyle: {
                        color: '#000',
                        fontSize: '12',
						fontWeight: 'normal',
                    }
                }
            },
            z: 2
        }, {
            name: '白框',
            type: 'bar',
            xAxisIndex: 1,
            barGap: '-100%',
            data: [endNum + spacingNum - 1],
            barWidth: 10,
            itemStyle: {
                normal: {
                    color: '#ffffff',
                    barBorderRadius: 50,
                }
            },
            z: 1
        }, {
            name: '外框',
            type: 'bar',
            xAxisIndex: 2,
            barGap: '-100%',
            data: [endNum + spacingNum],
            barWidth: 20,
            itemStyle: {
                normal: {
                    color: borderColor,
                    barBorderRadius: 50,
                }
            },
            z: 0
        }, {
            name: '圆',
            type: 'scatter',
            hoverAnimation: false,
            data: [0],
            xAxisIndex: 0,
            symbolSize: 20,
            itemStyle: {
                normal: {
                    color: mercuryColor,
                    opacity: 1,
                }
            },
            z: 2
        }, {
            name: '白圆',
            type: 'scatter',
            hoverAnimation: false,
            data: [0],
            xAxisIndex: 1,
            symbolSize: 30,
            itemStyle: {
                normal: {
                    color: '#ffffff',
                    opacity: 1,
                }
            },
            z: 1
        }, {
            name: '外圆',
            type: 'scatter',
            hoverAnimation: false,
            data: [0],
            xAxisIndex: 2,
            symbolSize: 40,
            itemStyle: {
                normal: {
                    color: borderColor,
                    opacity: 1,
                }
            },
            z: 0
        }, {
            name: '刻度',
            type: 'bar',
            yAxisIndex: 1,
            xAxisIndex: 3,
            label: {
                normal: {
                    show: true,
                    position: 'right',
                    distance: 2,
                    color: '#000',
                    fontSize: 10,
                    formatter: function (params) {
                        // 因为柱状初始化为0，温度存在负值，所以，原本的0-100，改为0-130，0-30用于表示负值
                        if (params.dataIndex > endNum || params.dataIndex < spacingNum) {
                            return '';
                        } else {
                            if (params.dataIndex % spacingNum === 0) {
                                return params.dataIndex + startNum - spacingNum;
                            } else {
                                return '';
                            }
                        }
                    }
                }
            },
            barGap: '-100%',
            data: kd,
            barWidth: 1,
            itemStyle: {
                normal: {
                    color: '#fff',
                    barBorderRadius: 10,
                }
            },
            z: 0
        }]
    };
    // 因为柱状初始化为0，温度存在负值，所以，原本的0-100，改为0-130，0-30用于表示负值
    humidityOption.series[0].data = [Number($('#' + valueName).html()) + spacingNum];
    humidityEcharts.setOption(humidityOption);
}