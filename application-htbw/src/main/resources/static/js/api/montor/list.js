$(function() {
	getalldomain();
	showpic();
    setInterval(function(){
		showpic();
	},60000);
    $("#doc-buttons").change("change", function() {// 绑定查询按扭
    	showpic();
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
function showpic() {
	var equipmentName=$("#searchValue").val();
	$("#showechart1").empty();
	var id = $("#doc-buttons").val();
	var html="";
    $.ajax({
        type : "post",
        datatype : "json",
        data:{
        	id:id,
        	equipmentName:equipmentName
        	},
        url : rootPath + "/equipment/datalist",
        success : function(msg) {
            var obj = jQuery.parseJSON(msg);
            var equipmentName;
            var equipmentNum;
            if (obj.length > 0) {
            $.each(obj, function(i, n) {
            		html += "<div class='echart' id='"+n.equipmentNum+"' style='width: 50%; height: 400px;border:1px solid #000;margin:10px'></div><script>hum("+n.id+","+n.equipmentNum+",'"+n.equipmentName+"')</script>"
            });
            }else{
            	html ="<div>该区域暂无设备存在</div>"
            }
            $("#showechart1").append(html);
       }
            
    });
}
function search(){
	var id = $("#doc-buttons").val();
    var equipmentName=$("#searchValue").val();
	$("#showechart1").empty();
	var html="";
    $.ajax({
        type : "post",
        datatype : "json",
        data:{
        	id:id,
        	equipmentName:equipmentName,
        },
        url : rootPath + "/equipment/datalist",
        success : function(msg) {
            var obj = jQuery.parseJSON(msg);
            var equipmentName;
            var equipmentNum;
            if (obj.length > 0) {
            $.each(obj, function(i, n) {
            		html += "<div class='echart' id='"+n.equipmentNum+"' style='width: 50%; height: 400px;border:1px solid #000;margin:10px'></div><script>hum("+n.id+","+n.equipmentNum+",'"+n.equipmentName+"')</script>"
            });
            }else{
            	html ="<div>该区域暂无设备存在</div>"
            }
            $("#showechart1").append(html);
       }
    });
}