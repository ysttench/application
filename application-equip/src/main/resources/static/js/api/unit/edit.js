$(function() {
    $("form").validate({
        submitHandler : function(form) {// 必须写在验证前面，否则无法ajax提交
            ly.ajaxSubmit(form, {// 验证新增是否成功
                type : "post",
                dataType : "json",
                success : function(data) {
                	debugger;
                    if (data == "success") {
                        layer.confirm('更新成功!是否关闭窗口?', function(index) {
                            parent.grid.loadData();
                            parent.layer.close(parent.pageii);
                            return false;
                        });
                        $("#form")[0].reset();
                    } else if (data == "fail") {
                        layer.alert('更新异常！', 3);
                    } else {
                        layer.alert('更新失败！', 3);
                    }
                }
            });
        },
        rules : {
            "apiUnitFormMap.name" : {
                required : true,
                remote : { // 异步验证是否存在
                    type : "POST",
                    url : rootPath + '/apiUnit/isExist',
                    data : {
                        "name" : function() {
                            return $("#name").val();
                        },
                        "id": function(){
                        	return $("#id").val();
                        }
                    }
                }
            },
            "apiUnitFormMap.code" : {
                remote : { // 异步验证是否存在
                    type : "POST",
                    url : rootPath + '/apiUnit/isExist',
                    data : {
                        "code" : function() {
                            return $("#code").val();
                        },
                        "id": function(){
                        	return $("#id").val();
                        }
                    }
                }
            }
        },
        messages : {
            "apiUnitFormMap.name" : {
                required : "请输入数量单位名称",
                remote : "该数量单位名称已存在"
            },
            "apiUnitFormMap.code" : {
            	required :"请输入数量单位编码",
                remote : "该设数量单位编码已被使用"
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
