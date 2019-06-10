$(function() {
    $("form").validate({
        submitHandler : function(form) {// 必须写在验证前面，否则无法ajax提交
            ly.ajaxSubmit(form, {// 验证新增是否成功
                type : "post",
                dataType : "json",
                success : function(data) {
                    if (data == "success") {
                        layer.confirm('恢复成功!是否关闭窗口?', function(index) {
                            parent.grid.loadData();
                            parent.layer.close(parent.pageii);
                            return false;
                        });
                        $("#form")[0].reset();
                    } else if(data == "success"){
                        layer.alert('恢复异常！', 3);
                    } else if(data == "isnotexist") {
                        layer.alert('该用户名不存在！', 3);
                    } else {
                        layer.alert('恢复失败！', 3);
                    }
                }
            });
        },
        rules : {
            "userName" : {
                required : true,
                remote : { // 异步验证是否存在
                    type : "POST",
                    url : rootPath + '/apiUser/recoverUserName',
                    data : {
                        userName : function() {
                            return $("#userName").val();
                        }
                    }
                }
            }
        },
        messages : {
            "userName" : {
                required : "请输入用户名",
                remote : "该用户不存在"
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
