function updatePasswordLayer(userName) {
    // 加载层
    var index = layer.load(0, {
        shade : false
    }); // 0代表加载的风格，支持0-2
    // iframe层-禁滚动条
    layer.open({
        type : 2,
        title : '修改密码',
        area : [ '550px', '360px' ],
        skin : 'layui-layer-rim', // 加上边框
        content : [ rootPath + '/user/updatePassword?userName='+userName, 'no' ]
    });
    // 关闭加载效果
    layer.close(index);
}

// 校验密码是否相同
function same(pwd) {
    var oldPwd = $("#newpassword").val();
    if (oldPwd == pwd) {
        return false;
    } else {
        // 更改表单中确认密码的name的属性名
        $("#confirmpassword").attr("name", "sysUserFormMap.password");
        return true;
    }
}

jQuery.validator.addMethod("same", function(value, element) {
    return this.optional(element) || same(value);
}, "新密码和确认密码不一致");

// 加入数据校验证
$(function() {
    $("#formUpdatePwd").validate({
        submitHandler : function(form) {// 必须写在验证前面，否则无法ajax提交
            ly.ajaxSubmit(form, {// 验证新增是否成功
                type : "post",
                dataType : "json",// ajaxSubmi带有文件上传的。不需要设置json
                success : function(data) {
                    if (data == "success") {
                            layer.confirm('修改密码成功!是否关闭窗口?', 
                            {
                                btn: ['YES','NO'], //按钮
                                shade: false //不显示遮罩
                            },
                            function(index) {
                                debugger;
                                layer.close(index);
                                var pi = parent.layer.getFrameIndex(window.name);
                                parent.window.location.href=rootPath+"/logout";
                                parent.layer.close(pi);
                            },
                            function(index) {
                                layer.close(index);
                            }
                        );
                    } else {
                        layer.msg('修改密码失败！', {
                            icon : 5
                        });
                    }
                }
            });
        },
        rules : {
            "sysUserFormMap.newpassword" : {
                minlength : 6,
                required : true
            },
            "sysUserFormMap.confirmpassword" : {
                required : true,
                minlength : 6,
                // same:true,
                equalTo : "#newpassword"
            }
        },
        messages : {
            "sysUserFormMap.newpassword" : {
                required : "请输入新密码",
                minlength : jQuery.format("密码不能小于{0}个字 符")
            },
            "sysUserFormMap.confirmpassword" : {
                required : "请输入确认密码",
                minlength : jQuery.format("密码不能小于{0}个字 符"),
                equalTo : "新密码和确认密码不一致"
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