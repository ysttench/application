$(function(){
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
                        layer.alert(' 更新失败！', 3);
                    }
                }
            });
        },
        rules : {
            "sysDepartmentFormMap.deptCode" : {
                required : true,
                maxlength:12
            },
            "sysDepartmentFormMap.deptName" : {
                required : true
            },
        },
        messages : {
            "sysDepartmentFormMap.deptCode" : {
                required : "请输入部门编码！",
                maxlength:"部门编码不能超过12"
            },
            "sysDepartmentFormMap.deptName" : {
                required : "请输入部门名称！"
            },
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
    //deptcode
    $('.deptcode').on('click',function(){
        var id = $(this).attr('id');
        var hid = $(this).parent().find('.deptcode[type="hidden"]').attr('id');
        pageii = layer.open({
            title : ["","text-align:center;font-weight:bold;"],
            type : 2,
            area : [ "800px", "400px" ],
            content : rootPath + '/sysdepartment/deptUI?id='+id+'&hid='+hid
        });
    });
});
