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
                    } else if (data == "fail") {
                        layer.alert('添加异常！', 3);
                    } else {
                        layer.alert('添加失败！', 3);
                    }
                }
            });
        },
        rules : {
            "ApiAssetsTypeFormMap.name" : {
                required : true,
                remote : { // 异步验证是否存在
                    type : "POST",
                    url : rootPath + '/assetsType/isExist',
                    data : {
                        "name" : function() {
                            return $("#name").val();
                        }
                    }
                }
            },
            "ApiAssetsTypeFormMap.code" : {
                remote : { // 异步验证是否存在
                    type : "POST",
                    url : rootPath + '/assetsType/isExist',
                    data : {
                        "code" : function() {
                            return $("#code").val();
                        }
                    }
                }
            }
        },
        messages : {
            "ApiAssetsTypeFormMap.name" : {
                required : "请输入资产分类名称",
                remote : "该入资产分类名称已存在"
            },
            "ApiAssetsTypeFormMap.code" : {
            	required :"请输入资产分类编码",
                remote : "该设资产分类编码已被使用"
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

