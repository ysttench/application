//单独验证某一个input  class="checkpass"
jQuery.validator.addMethod("checkRole", function(value, element) {
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
                        layer.alert('更新失败！', 3);
                    }
                }
            });
        },
        rules : {
            "apiRoleFormMap.roleName" : {
                required : true,
                remote : { // 异步验证是否存在
                    type : "POST",
                    url : rootPath + '/apiRole/isExist',
                    data : {
                        "roleName" : function() {
                            return $("#roleName").val();
                        },
                        "id" : function(){
                            return $("#id").val();
                        }
                    }
                }
            },
            "apiRoleFormMap.roleDesc" : {
                required : true
            },
        },
        messages : {
            "apiRoleFormMap.roleName" : {
                required : "请输入角色名",
                remote : "角色名已存在"
            },
            "apiRoleFormMap.roleDesc" : {
                required : "请输入角色描述"
            }
        },
        errorPlacement : function(error, element) {// 自定义提示错误位置
            $(".l_err").css('display', 'block');
            $(".l_err").html(error.html());
        },
        success : function(label) {// 验证通过后
            $(".l_err").css('display', 'none');
        }
    });
});



