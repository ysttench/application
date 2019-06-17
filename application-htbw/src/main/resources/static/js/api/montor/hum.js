
function hum(id,equipmentNum,equipmentName){
    $.ajax({
        type : "post",
        datatype : "json",
        data:{
        	id:id,
        	equipmentName:equipmentName,
        },
        url : rootPath + "/equipment/getdatalist",
        success : function(msg) {
            var obj = jQuery.parseJSON(msg);
            var time = new Array();
            var temdata = new Array();
            var mintem = new Array();
            var maxtem = new Array();
            var humdata = new Array();
            var minhum = new Array();
            var maxhum = new Array();
            $.each(obj, function(i, n) {
            	temdata.push(n.tempValue);
            	time.push(new Date(n.date).format("dd hh:mm").toLocaleString());
            	mintem.push(n.alarmMinTemperature);
            	maxtem.push(n.alarmMaxTemperature);
            	humdata.push(n.humValue);
            	minhum.push(n.alarmMinHumidity);
            	maxhum.push(n.alarmMaxHumidity);
            });
            option = {
        	    title: {
        	        text: '折线图堆叠'
        	    },
        	    tooltip: {
        	        trigger: 'axis'
        	    },
        	    legend: {
        	        data:['温度','温度下限','温度上限','湿度','湿度下限','湿度上限']
        	    },
        	    grid: {
        	        left: '3%',
        	        right: '4%',
        	        bottom: '3%',
        	        containLabel: true
        	    },
        	    xAxis: {
        	    	type: 'category',
        	        boundaryGap: false,
        	    },
        	    yAxis: {
        	        type: 'value',
        	    },
        	    series: [
        	        {
        	            name:'温度',
        	            type:'line',
        	            stack: '温度',
        	            data:[120, 132, 101, 134, 90, 230, 210]
        	        },
        	        {
        	            name:'温度下限',
        	            type:'line',
        	            stack: '温度下限',
        	            data:[220, 182, 191, 234, 290, 330, 310]
        	        },
        	        {
        	            name:'温度上限',
        	            type:'line',
        	            stack: '温度上限',
        	            data:[150, 232, 201, 154, 190, 330, 410]
        	        },
        	        {
        	            name:'湿度',
        	            type:'line',
        	            stack: '湿度',
        	            data:[320, 332, 301, 334, 390, 330, 320]
        	        },
        	        {
        	            name:'湿度下限',
        	            type:'line',
        	            stack: '湿度下限',
        	            data:[820, 932, 901, 934, 1290, 1330, 1320]
        	        },
        	        {
        	            name:'湿度上限',
        	            type:'line',
        	            stack: '湿度上限',
        	            data:[820, 932, 901, 934, 1290, 1330, 1320]
        	        }
        	    ]
        	};
        var chart = echarts.init(document.getElementById(equipmentNum));
        option.xAxis.data = time;
        option.series[0].data = temdata;
        option.series[1].data = mintem;
        option.series[2].data = maxtem;
        option.series[3].data = humdata;
        option.series[4].data = minhum;
        option.series[5].data = maxhum;
        option.title.text=equipmentName;
        chart.setOption(option);
        }
    });
}
