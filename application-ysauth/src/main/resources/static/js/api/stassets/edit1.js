$(function() {
    $("form").validate({
        submitHandler : function(form) {// 必须写在验证前面，否则无法ajax提交
            ly.ajaxSubmit(form, {// 验证新增是否成功
                type : "post",
                dataType : "json",// ajaxSubmi带有文件上传的。不需要设置json
                success : function(data) {
                    if (data == "success") {
                        layer.confirm('建单成功!是否关闭窗口?', function(index) {
                            parent.grid.loadData();
                            parent.layer.close(parent.pageii);
                            return false;
                        });
                    }else if(data=="null"){
                        layer.alert('建单失败！请分配权限!',3);
                    }else if(data=="fail1"){
                        layer.alert('建单失败！存在未审核项!',3);
                    }else {
                        layer.alert('建单失败！',3);
                    }
                }
            });
        },
        rules : {
			"lgDForMap.pdName" : {
				required : true,
				remote : { // 异步验证是否存在
					type : "POST",
					url : rootPath + '/stassets/isExist2',
					data : {
						"pdName" : function() {
							return $("#pdName").val();
						}
					}
				}
			}
		},
        messages : {
			"lgDForMap.pdName" : {
				required : "请输入盘点单名称",
				remote : "该盘点单名称已经存在"
			}
		},
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
