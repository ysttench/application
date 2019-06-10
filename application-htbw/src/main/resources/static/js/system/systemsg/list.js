$(function() {

    $("form").validate({
        submitHandler : function(form) {// 必须写在验证前面，否则无法ajax提交
            ly.ajaxSubmit(form, {// 验证新增是否成功
                type : "post",
                dataType : "json",// ajaxSubmi带有文件上传的。不需要设置json
                success : function(data) {
                    if (data == "success") {
                    	layer.alert('更新成功！',3);
                    } else if(data=="null"){
                        layer.alert('更新失败！请分配权限!',3);
                    }else {
                        layer.alert('更新失败！',3);
                    }
                }
            });
        },
        rules : {
            "sysSystemFormMap.systemName" : {
                required : true,
            }
        },
        messages : {
            "sysSystemFormMap.systemName" : {
                required : "请输入系统名称"
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
