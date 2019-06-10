$(function() {
    var parentId = $("#parentId").val();
    if ("0" == parentId) {
        $("#divParentName").hide();
    } else {
        $("#divDetailFlag").hide();
        $("#divConfigCode").hide();
    }
    $("form").validate({
        submitHandler : function(form) {// 必须写在验证前面，否则无法ajax提交
            ly.ajaxSubmit(form, {// 验证新增是否成功
                type : "post",
                dataType : "json",// ajaxSubmi带有文件上传的。不需要设置json
                success : function(data) {
                    if (data == "success") {
                        parent.grid.loadData();
                        if ("0" == parentId) {
                            parent.init();
                        }
                        layer.confirm('更新成功!是否关闭窗口?', function(index) {
                            parent.layer.close(parent.pageii);
                            return false;
                        });
                    } else if (data == "nameCodeNull") {
                        layer.msg('名称代码为空！更新失败！');
                    } else {
                        layer.msg('更新失败！', 3);
                    }
                }
            });
        },
        rules : {
            "sysCodeConfigFormMap.configName" : {
                required : true,
                remote : { // 异步验证是否存在
                    type : "POST",
                    url : rootPath + '/codeconfig/isExistConfigName',
                    data : {
                        id : function() {
                            return $("#id").val();
                        },
                        parentId : function() {
                            return $("#parentId").val();
                        },
                        configName : function() {
                            return $("#configName").val();
                        }
                    }
                }
            },
            "sysCodeConfigFormMap.configValue" : {
                remote : { // 异步验证是否存在
                    type : "POST",
                    url : rootPath + '/codeconfig/isExistConfigValue',
                    data : {
                        id : function() {
                            return $("#id").val();
                        },
                        parentId : function() {
                            return $("#parentId").val();
                        },
                        configValue : function() {
                            return $("#configValue").val();
                        }
                    }
                }
            },
            "sysCodeConfigFormMap.configCode" : {
                remote : { // 异步验证是否存在
                    type : "POST",
                    url : rootPath + '/codeconfig/isExistConfigCode',
                    data : {
                        id : function() {
                            return $("#id").val();
                        },
                        configCode : function() {
                            return $("#configCode").val();
                        }
                    }
                }
            },
        },
        messages : {
            "sysCodeConfigFormMap.configName" : {
                required : "请输入参数配置名称！",
                remote : "该参数配置名称已经存在！"
            },
            "sysCodeConfigFormMap.configCode" : {
                remote : "该参数配置编码已经存在！"
            },
            "sysCodeConfigFormMap.configValue" : {
                remote : "该参数数值已经存在！"
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
    // 绑定取消按钮事件
    $(".cancel").click(function() {
        parent.layer.close(parent.pageii);
    });
    // 添加时账号输入框自动获取焦点
    $("#configName").focus();
});
