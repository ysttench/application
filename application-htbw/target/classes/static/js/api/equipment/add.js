$(function() {
	updomain();
	equiptype();
	$("#domain").hide();
	$("form").validate({
		submitHandler : function(form) {// 必须写在验证前面，否则无法ajax提交
			ly.ajaxSubmit(form, {// 验证新增是否成功
				type : "post",
				dataType : "json",
				success : function(data) {
					if (data == "success") {
						layer.confirm('添加成功!是否关闭窗口?', function(index) {
							parent.grid.loadData();
							parent.layer.close(parent.pageii);
							return false;
						});
						$("#form")[0].reset();
						keepsocket();
					} else if (data == "fail") {
						layer.alert('添加异常！', 3);
					} else {
						layer.alert('添加失败！', 3);
					}
				}
			});
		},
		rules : {},
		messages : {},

		errorPlacement : function(error, element) {// 自定义提示错误位置
			$(".l_err").css('display', 'block');
			// element.css('border','3px solid #FFCCCC');
			$(".l_err").html(error.html());
		},
		success : function(label) {// 验证通过后
			$(".l_err").css('display', 'none');
		}
	});
	$("#updomain").change(function() {
		var chose = $("#updomain").val();
		if ("0" == chose) {
			$("#domain").hide();
		}else{
			$("#domain").show();
			domain();
		}
	})
});
function keepsocket(){
	$.ajax({
		url : rootPath + "/equipment/keepsocket",
		data : {},
		dataType : "json",
		success : function(data) {}
	})
}
function updomain() {
	var unit = "<option value='0'>-请选择上级区域-</option>";
	$.ajax({
		url : rootPath + "/regionalinfo/parentMsg",
		data : {},
		dataType : "json",
		success : function(data) {
			if (data.length != 0) {
				$.each(data, function(i, n) {
					unit += "<option value='" + n.id + "'>" + n.name
							+ "</option>"
				})
			}
			$("#updomain").empty().append(unit);
			$("#updomain option").each(function() {
				var val = $(this).val();
				if ($("#updomain option[value='" + val + "']").size() > 1) {
					$("#updomain option[value='" + val + "']:gt(0)").remove();
				}
			})
		}
	})
}
function equiptype() {
	var unit = "<option value='0'>-请选择设备分类-</option>";
	$.ajax({
		url : rootPath + "/equiptype/getMsg",
		data : {},
		dataType : "json",
		success : function(data) {
			if (data.length != 0) {
				$.each(data, function(i, n) {
					unit += "<option value='" + n.id + "'>" + n.equiptypeName
							+ "</option>"
				})
			}
			$("#equiptypeId").empty().append(unit);
			$("#equiptypeId option").each(function() {
				var val = $(this).val();
				if ($("#equiptypeId option[value='" + val + "']").size() > 1) {
					$("#equiptypeId option[value='" + val + "']:gt(0)").remove();
				}
			})
		}
	})
}
function domain() {
	var unit1 = "<option value='0'>-请选择区域-</option>";
	var updomain = $("#updomain").val();
	$.ajax({
		url : rootPath + "/regionalinfo/getdomain",
		data : {
			"updomain" : updomain
		},
		dataType : "json",
		success : function(data) {
			if (data.length != 0) {
				$.each(data, function(i, n) {
					unit1 += "<option value='" + n.id + "'>" + n.name
							+ "</option>"
				})
			}
			$("#domain").empty().append(unit1);
			$("#domain option").each(function() {
				var val = $(this).val();
				if ($("#domain option[value='" + val + "']").size() > 1) {
					$("#domain option[value='" + val + "']:gt(0)").remove();
				}
			})
		}
	})
}