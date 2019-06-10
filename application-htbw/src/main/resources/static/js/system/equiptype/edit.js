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
                    }else if (data == "gfail") {
                        layer.alert('该区域不可选上级区域！', 3);
                    } else {
                        layer.alert('更新失败！', 3);
                    }
                }
            });
        },
        rules : {
            "equipTypeFormMap.equipmentType" : {
                required : true,
                remote : { // 异步验证是否存在
                    type : "POST",
                    url : rootPath + '/equiptype/isExist',
                    data : {
                        "name" : function() {
                            return $("#name").val();
                        },
                        "id": function(){
                        	return $("#id").val();
                        }
                    }
                }
            }
        },
        messages : {
        	"equipTypeFormMap.equipmentType" : {
                required : "请输入区域名称",
                remote : "该区域名称已存在"
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
