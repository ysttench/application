jQuery.validator.addMethod("checkDm", function(value, element) {
    return this.optional(element) || (value.length == 6);
    return true;
}, "行政区代码由6位数字组成！");
//单独验证某一个input  class="checkpass"
jQuery.validator.addMethod("checkMc", function(value, element) {
    return this.optional(element) || ((value.length <= 10) && (value.length >= 2));
}, "行政区划名由2至10位字符组合构成");
$(function() {
    $("form").validate({
        submitHandler : function(form) {// 必须写在验证前面，否则无法ajax提交
            ly.ajaxSubmit(form, {// 验证新增是否成功
                type : "post",
                dataType : "json",
                success : function(data) {
                    if (data == "success") {
                        layer.confirm('添加成功!是否关闭窗口?', function(index) {
                            parent.grid.loadData();
                            parent.layer.close(parent.pageii);
                            return false;
                        });
                        $("#form")[0].reset();
                    } else {
                        layer.msg('添加失败！');
                    }
                }
            });
        },
        rules : {
            "xzqhbFormMap.dm" : {
                required : true,
                remote : { // 异步验证是否存在
                    type : "POST",
                    url : rootPath + '/xzqhb/isExistDM',
                    data : {
                        dm : function() {
                            return $("#dm").val();
                        }
                    }
                }
            },
            "xzqhbFormMap.mc" : {
                required : true,
                checkMc : true,
                remote : { // 异步验证是否存在
                    type : "POST",
                    url : rootPath + '/xzqhb/isExist',
                    data : {
                        name : function() {
                            return $("#mc").val();
                        }
                    }
                }
            }
        },
        errorPlacement : function(error, element) {// 自定义提示错误位置
            $(".l_err").css('display', 'block');
            $(".l_err").html(error.html());
        },   
        success : function(label) {//验证通过后
            $(".l_err").css('display', 'none');
        }
    });
    //绑定取消按钮事件
    $(".cancel").click(function(){
        parent.layer.close(parent.pageii);
    });
});
