//单独验证某一个input  class="checkpass"
jQuery.validator.addMethod("checkacc", function(value, element) {
    return this.optional(element) || ((value.length <= 30) && (value.length >= 3));
}, "账号由3至30位字符组合构成");
$(function() {

    $("form").validate({
        submitHandler : function(form) {// 必须写在验证前面，否则无法ajax提交
            ly.ajaxSubmit(form, {// 验证新增是否成功
                type : "post",
                dataType : "json",// ajaxSubmi带有文件上传的。不需要设置json
                success : function(data) {
                    if (data == "success") {
                        layer.confirm('更新成功!是否关闭窗口?', function(index) {
                            parent.grid.loadData();
                            parent.layer.close(parent.pageii);
                            return false;
                        });
                    } else if(data=="null"){
                        layer.alert('更新失败！请分配权限!',3);
                    }else {
                        layer.alert('更新失败！',3);
                    }
                }
            });
        },
        rules : {
            "apiUserFormMap.companyName" : {
                required : true,
                remote : { // 异步验证是否存在
                    type : "POST",
                    url : rootPath + '/apiuser/isExist',
                    data : {
                        "companyName" : function() {
                            return $("#companyName").val();
                        },
                        "id" : function() {
                            return $("#id").val();
                        }
                    }
                }
            }
        },
        messages : {

            "apiUserFormMap.companyName" : {
                required : "请输入客户公司名",
                remote : "该客户公司名已经存在"
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
