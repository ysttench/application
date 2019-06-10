
$(function(){
	getalldomain();
	getRelnfo();
 setInterval(function(){
		getRelnfo();
	},60000);
	$("#doc-buttons").change("change", function() {// 绑定查询按扭
		getRelnfo();
    });
})
function getalldomain(){
	var html="<option value='all' selected='selected'>全部区域</option>";
	$.ajax({
		type:"post",
		datatype : "json",
        url : rootPath + "/regionalinfo/getalldomain",
        success : function(msg) {
        	var obj = jQuery.parseJSON(msg);
        	 $.each(obj, function(i, n) {
        		 if (n.parentId==0) {
        			 html += "<option value='"+n.id+"'>"+n.name+"</option>";
				}else{
					html += "<option value='"+n.id+"'>"+n.name+"</option>";
				}
        	 });
        	 $("#doc-buttons").append(html);
        }
	});
}
function getRelnfo(){
	var equipmentName=$("#searchValue").val();
	$("#showechart2").empty();
	var id = $("#doc-buttons").val();
	var html="";
	$.ajax({
		type:"post",
		datatype : "json",
        data:{
        	id:id,
        	equipmentName:equipmentName
        },
        url : rootPath + "/equipment/getRelnfo",
        success : function(msg) {
        	debugger
        	var obj = jQuery.parseJSON(msg);
        	if (obj.length > 0) {
        		$.each(obj,function(i,n){
        			html+=" <table id='"+n.equipmentNum+"tab' style='background: #69F0AE;width: 260px;height: 220px;margin: 5px 10px 10px 10px; border-radius: 36px;float: left;'><input id='"+n.equipmentNum+"col' type='hidden' value='"+n.status+"'/><tr><td id='"+n.equipmentNum+"echart' rowspan='8' style='width:60px;height:250px;'></td><td></td><td></td><td id='"+n.equipmentNum+"rhechart' rowspan='8' style='width:60px;height:250px;'></td></tr><tr><td colspan='2' style='display: block;margin-top: 35px;'><span>NAME</span><span>:</span><span>"+n.equipmentName+"</span></td></tr><tr><td></td><td></td></tr><tr><td colspan='2' style='display: block;    margin-left: 10px;    margin-top: -20px;'><span>Temp</span><span>:</span><span id='"+n.equipmentNum+"echartValue'>"+n.echartValue+"</span><span>℃</span></td></tr><tr><td colspan='2' style='display: block;margin-left: 10px;margin-top: -20px;'><span>Hum</span><span>:</span><span id='"+n.equipmentNum+"rhechartValue'>"+n.rhechartValue+"</span><span>%</span></td></tr><tr></tr><tr><td></td><td></td></tr><script>temperature('"+n.equipmentNum+"echart', 0, 100, 20, '#e90b0a', '#c8c8c8', '#e90b0a', '"+n.equipmentNum+"echartValue','℃','"+n.time+"');</script><script>temperature('"+n.equipmentNum+"rhechart', 0, 100, 20, '#05b5f0', '#93c3e7', '#05b5f0', '"+n.equipmentNum+"rhechartValue','%RH','"+n.time+"');</script><script> if (document.getElementById('"+n.equipmentNum+"col').value == '1') { document.getElementById('"+n.equipmentNum+"tab').style.background = '#eee';}</script></table>";
        		})
			}else{
				html ="<div>该区域暂无设备存在</div>"
			}
        	$("#showechart2").append(html);
        }
	});
}
function search(){
	var equipmentName=$("#searchValue").val();
	var id = $("#doc-buttons").val();
	$("#showechart2").empty();
	var html="";
	$.ajax({
		type:"post",
		datatype : "json",
        data:{
        	id:id,
        	equipmentName:equipmentName
        },
        url : rootPath + "/equipment/getRelnfo",
        success : function(msg) {
        	var obj = jQuery.parseJSON(msg);
        	if (obj.length > 0) {
        		$.each(obj,function(i,n){
        			html+=" <table id='"+n.equipmentNum+"tab' style='background: #69F0AE;width: 260px;height: 220px;margin: 5px 10px 10px 10px; border-radius: 36px;float: left;'><input id='"+n.equipmentNum+"col' type='hidden' value='"+n.status+"'/><tr><td id='"+n.equipmentNum+"echart' rowspan='8' style='width:60px;height:250px;'></td><td></td><td></td><td id='"+n.equipmentNum+"rhechart' rowspan='8' style='width:60px;height:250px;'></td></tr><tr><td colspan='2' style='display: block;margin-top: 35px;'><span>NAME</span><span>:</span><span>"+n.equipmentName+"</span></td></tr><tr><td></td><td></td></tr><tr><td colspan='2' style='display: block;    margin-left: 10px;    margin-top: -20px;'><span>Temp</span><span>:</span><span id='"+n.equipmentNum+"echartValue'>"+n.echartValue+"</span><span>℃</span></td></tr><tr><td colspan='2' style='display: block;margin-left: 10px;margin-top: -20px;'><span>Hum</span><span>:</span><span id='"+n.equipmentNum+"rhechartValue'>"+n.rhechartValue+"</span><span>%</span></td></tr><tr></tr><tr><td></td><td></td></tr><script>temperature('"+n.equipmentNum+"echart', 0, 100, 20, '#e90b0a', '#c8c8c8', '#e90b0a', '"+n.equipmentNum+"echartValue','℃','"+n.time+"');</script><script>temperature('"+n.equipmentNum+"rhechart', 0, 100, 20, '#05b5f0', '#93c3e7', '#05b5f0', '"+n.equipmentNum+"rhechartValue','%RH','"+n.time+"');</script><script> if (document.getElementById('"+n.equipmentNum+"col').value == '1') {debugger; document.getElementById('"+n.equipmentNum+"tab').style.background = '#eee';}</script></table>";
        		})
        	}else{
        		html ="<div>查询设备未在该区域</div>"
        	}
        	$("#showechart2").append(html);
        }
	});

}

