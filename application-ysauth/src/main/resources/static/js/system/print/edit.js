
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
                        $("#form")[0].reset();
                    } else {
                        layer.msg('添加失败！');
                    }
                }
            });
        },
        rules : {
            "sysPrintForMap.organ" : {
                required : true,
                remote : { // 异步验证是否存在
                    type : "POST",
                    url : rootPath + '/print/isExist',
                    data : {
                        "organ" : function() {
                            return $("#organ").val();
                        },
                        "id" : function() {
                            return $("#id").val();
                        },
                        "type": function(){
                        	return $("#type").val();
                        }
                    }
                }
            },
            "sysPrintForMap.wsdlUrl" : {
                required : true,
                remote : { // 异步验证是否存在
                    type : "POST",
                    url : rootPath + '/print/isExist',
                    data : {
                        "wsdlUrl" : function() {
                            return $("#wsdlUrl").val();
                        },
                        "id" : function() {
                            return $("#id").val();
                        },
                        "type": function(){
                        	return $("#type").val();
                        }
                    }
                }
            }
        },
        messages : {
            "sysPrintForMap.organ" : {
                required : "请输入组织名称",
                remote : "该组织名称已经存在"
            }, 
            "sysPrintForMap.wsdlUrl" : {
                required : "请输入接口地址",
                remote : "该接口地址已经存在"
            }
        },
        errorPlacement : function(error, element) {// 自定义提示错误位置
            $(".l_err").css('display', 'block');
            //element.css('border','3px solid #FFCCCC');
            $(".l_err").html(error.html());
        },
        success : function(label) {//验证通过后
            $(".l_err").css('display', 'none');
        }
    });
});
