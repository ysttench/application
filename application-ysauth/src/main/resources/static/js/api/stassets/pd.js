$(function() {
	type()
});
function type() {
	var type = $('#type').val();
	var id = $('#id').val();
	var unit = "<tr><td>资产编码</td><td>资产名称</td><td>账存数量</td><td>初盘数量</td><td>初盘人</td><td>复盘数量</td><td>复盘人</td><td>初次盘点结果</td><td>复盘点结果</td><tr>"
	$.ajax({
				url : rootPath + "/stassets/fd",
				data : {
					"type" : type,
					"id":id
				},
				dataType : "json",
				success : function(data) {
					if (data.length != 0) {
						$.each(data, function(i, n) {
							var cpName =" ";
							var fpName =" ";
							var cpQty=" ";
							var fpQty=" ";
							var cpNum=" ";
							var fpNum=" ";
							if (n.cpName != null) {
								cpName = n.cpName;
							}
							if(n.fpName != null){
								fpName = n.fpName;
							}
							if(n.cpQty != null){
								cpQty = n.cpQty;
							}
							if(n.fpQty != null){
								fpQty = n.fpQty;
							}
							if(n.cpNum != null){
								cpNum = n.cpNum;
							}
							if(n.fpNum != null){
								fpNum = n.fpNum;
							}
							unit += "<tr><td>"+n.assetsNum+"</td><td>"+n.assetsName+"</td><td>"+n.zcNum+"</td><td>"+cpNum+"</td><td>"+cpName+"</td><td>"+fpNum+"</td><td>"+fpName+"</td><td>"+cpQty+"</td><td>"+fpQty+"</td><tr>"
						})
						$("#tab").empty().append(unit);
					}else{
						$("#tab").empty().append(unit);
						alert("该盘点单无盘盈盘亏数据");
					}
				}
			})
}
function pk() {
	alert(2);
}
function zp() {
	alert(3);
}