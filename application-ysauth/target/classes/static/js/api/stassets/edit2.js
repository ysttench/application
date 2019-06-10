$(function() {
	getPdname();
    $("form").validate({
        submitHandler : function(form) {// 必须写在验证前面，否则无法ajax提交
            ly.ajaxSubmit(form, {// 验证新增是否成功
                type : "post",
                dataType : "json",// ajaxSubmi带有文件上传的。不需要设置json
                success : function(data) {
                    if (data == "success") {
                        layer.confirm('添加到盘点单成功!是否关闭窗口?', function(index) {
                            parent.grid.loadData();
                            parent.layer.close(parent.pageii);
                            return false;
                        });
                    }else if(data=="null"){
                        layer.alert('添加到盘点单失败！请分配权限!',3);
                    }else if(data=="fail1"){
                        layer.alert('建单失败！存在未审核项!',3);
                    }else {
                        layer.alert('添加到盘点单失败！',3);
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
});
function getPdname() {
	var unit = "<option value='0'>请选择盘点单</option>";
	$
			.ajax({
				url : rootPath + "/stassets/getPdname",
				data : {},
				dataType : "json",
				success : function(data) {
					if (data.length != 0) {
						$.each(data, function(i, n) {
							unit += "<option value='" + n.id + "'>"
									+ n.pdName + "</option>"
						})
					}
					$("#pdName").empty().append(unit);
					$("#pdName option")
							.each(
									function() {
										var val = $(this).val();
										if ($(
												"#pdName option[value='"
														+ val + "']").size() > 1) {
											$(
													"#pdName option[value='"
															+ val + "']:gt(0)")
													.remove();
										}
									})
				}
			})
}