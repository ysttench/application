//单独验证某一个input  class="checkpass"
jQuery.validator.addMethod("checkacc", function(value, element) {
    return this.optional(element) || ((value.length <= 10) && (value.length >= 3));
}, "角色名由3至10位字符组合构成");
$(function() {
    $("form").validate({
        submitHandler : function(form) {// 必须写在验证前面，否则无法ajax提交
            ly.ajaxSubmit(form, {// 验证新增是否成功
                type : "post",
                dataType : "json",
                success : function(data) {
                    if (data == "success") {
                        layer.confirm('更新成功!是否关闭窗口?', function(index) {
                            parent.grid.loadData();
                            parent.layer.close(parent.pageii);
                            return false;
                        });
                    } else {
                        layer.msg('更新失败！');
                    }
                }
            });
        },
        rules : {
            "sysRoleFormMap.name" : {
                required : true,
                remote : { // 异步验证是否存在
                    type : "POST",
                    url : rootPath + '/role/isExist',
                    data : {
                        "name" : function() {
                            return $("#name").val() ;
                        },
                        "roleId" : function(){
                            return $("#roleId").val();
                        }
                    }
                }
            }
        },
        messages : {
            "sysRoleFormMap.name" : {
                required : "请输入角色名",
                remote : "该角色名已经存在"
            }
        },        errorPlacement : function(error, element) {// 自定义提示错误位置
            $(".l_err").css('display', 'block');
            // element.css('border','3px solid #FFCCCC');
            $(".l_err").html(error.html());
        },
        success : function(label) {// 验证通过后
            $(".l_err").css('display', 'none');
        }
    });
});
