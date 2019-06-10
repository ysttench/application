
$(function() {
    $("form").validate({
        submitHandler : function(form) {// 必须写在验证前面，否则无法ajax提交
            ly.ajaxSubmit(form, {// 验证新增是否成功
                type : "post",
                dataType : "json",
                success : function(data) {
                    if (data == "success") {
                        layer.confirm('编辑成功!是否关闭窗口?', function(index) {
                            parent.grid.loadData();
                            parent.layer.close(parent.pageii);
                            return false;
                        });
                        $("#form")[0].reset();
                    }else if(data=="fail"){
                        layer.alert('编辑失败！请分配权限!',3);
                    } else {
                        layer.alert('编辑失败！', 3);
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
    sldw();
});
function sldw() {
	var name = $('#dw').data("name");
	var code = $('#dw').val();
	if (name == undefined) {
		name = "-请选择数量单位-";
	}
	var unit = "<option value='" + code + "'>" + name + "</option>";
	$.ajax({
		url : rootPath + "/apiUnit/sldw",
		data : {},
		dataType : "json",
		success : function(data) {
			if (data.length != 0) {
				$.each(data, function(i, n) {
					unit += "<option value='" + n.code + "'>" + n.name
							+ "</option>"
				})
			}
			$("#unit").empty().append(unit);
			$("#unit option").each(function() {
				var val = $(this).val();
				if ($("#unit option[value='" + val + "']").size() > 1) {
					$("#unit option[value='" + val + "']:gt(0)").remove();
				}
			})
		}
	})
}
