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
            "apiSystemSettingFormMap.sysKey" : {
                required : true,
                remote : { // 异步验证是否存在
                    type : "POST",
                    url : rootPath + '/apiSetting/isExist',
                    data : {
                        name : function() {
                            return $("#sysKey").val();
                        }
                    }
                }
            },
            "apiSystemSettingFormMap.sysValue" : {
                required : true
            }
        },
        messages : {
            "apiSystemSettingFormMap.sysKey" : {
                required : "请输入系统参数key",
                remote : "系统参数key已存在"
            },
            "apiSystemSettingFormMap.sysValue" : {
                required : "请输入系统参数Value"
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
