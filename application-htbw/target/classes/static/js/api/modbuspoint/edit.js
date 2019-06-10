$(function() {
	$("form").validate({
		submitHandler : function(form) {// 必须写在验证前面，否则无法ajax提交
			ly.ajaxSubmit(form, {// 验证新增是否成功
				type : "post",
				dataType : "json",
				success : function(data) {
					debugger;
					if (data == "success") {
						layer.confirm('更新成功!是否关闭窗口?', function(index) {
							parent.grid.loadData();
							parent.layer.close(parent.pageii);
							return false;
						});
						$("#form")[0].reset();
					} else if (data == "fail") {
						layer.alert('更新异常！', 3);
					} else {
						layer.alert('更新失败！', 3);
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
	getuppoint();
});
function getuppoint() {
	var upId = $("#led").val();
	var upName = $('#led').data("name");
	if (upName==undefined) {
		var unit = "<option value='0'>-请选择对应的LED屏-</option>";
	}else{
		var unit = "<option value='" + upId + "'>" + upName + "</option>";
	}
	$.ajax({
		url : rootPath + "/modbuspoint/getled",
		data : {
			"upId" : upId
		},
		dataType : "json",
		success : function(data) {
			if (data.length != 0) {
				$.each(data, function(i, n) {
					unit += "<option value='" + n.id + "'>" + n.pointName
							+ "</option>"
				})
			}
			$("#upId").empty().append(unit);
			$("#upId option").each(function() {
				var val = $(this).val();
				if ($("#upId option[value='" + val + "']").size() > 1) {
					$("#upId option[value='" + val + "']:gt(0)").remove();
				}
			})
		}
	})

}