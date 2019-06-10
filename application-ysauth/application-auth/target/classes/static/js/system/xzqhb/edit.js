jQuery.validator.addMethod("checkDm", function(value, element) {
    return this.optional(element) || (value.length == 6);
    return true;
}, "行政区代码由6位数字组成！");
jQuery.validator.addMethod("checkMc", function(value, element) {
    return this.optional(element) || ((value.length <= 10) && (value.length >= 2));
}, "行政区划名由2至10位中文组成！");
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
                        $(".l_err").css('display', 'block');
                        $(".l_err").html(data);
                        layer.alert('添加失败！', 3);
                    }
                }
            });
        },
        rules : {
            "xzqhbFormMap.dm" : {
                required : true,
                remote : { // 异步验证是否存在
                    type : "POST",
                    url : rootPath + '/xzqhb/isExistDMForEdit',
                    data : {
                        dm : function() {
                            return $("#dm").val();
                        },
                        dzbm : function() {
                            return $("#dzbm").val();
                        }
                    }
                }
            },
            "xzqhbFormMap.mc" : {
                required : true,
                checkMc : true,
                remote : { // 异步验证是否存在
                    type : "POST",
                    url : rootPath + '/xzqhb/isExistForEdit',
                    data : {
                        name : function() {
                            return $("#mc").val();
                        },
                        dzbm : function() {
                            return $("#dzbm").val();
                        }
                    }
                }
            }
        },
        messages : {
            "xzqhbFormMap.dm" : {
                required : "请输入行政区划代码",
                remote : "该行政区划代码已经存在"
            },
            "xzqhbFormMap.mc" : {
                required : "请输入行政区划名",
                remote : "该行政区划已经存在"
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
    //绑定取消按钮事件
    $(".cancel").click(function(){
        parent.layer.close(parent.pageii);
    });
    //修改页面行政区划名输入框获取焦点
    $("#mc").focus();
});
