//单独验证某一个input  class="checkpass"
jQuery.validator.addMethod("checkacc", function(value, element) {
    return this.optional(element) || !(/^[\u4e00-\u9fa5]+$/.test(value));
}, "用户名由非中文字符组合构成");
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
                    }else if(data=="fail"){
                        layer.alert('添加失败！请分配权限!',3);
                    } else {
                        layer.alert('添加失败！', 3);
                    }
                }
            });
        },
        rules : {
            "sysUserFormMap.name" : {
                required : true,
            },
            "sysUserFormMap.userName" : {
                required : true,
                remote : { // 异步验证是否存在
                    type : "POST",
                    url : rootPath + '/user/isExist',
                    data : {
                        "userName" : function() {
                            return $("#userName").val();
                        }
                    }
                }
            },
            "sysUserFormMap.policeId" : {
                required : true,
                remote : { // 异步验证是否存在
                    type : "POST",
                    url : rootPath + '/user/isExist',
                    data : {
                        "policeId" : function() {
                            return $("#policeId").val();
                        }
                    }
                }
            }
        },
        messages : {
            "sysUserFormMap.name" : {
                required : "请输入姓名"
            },
            "sysUserFormMap.userName" : {
                required : "请输入用户名",
                remote : "该用户名已经存在"
            },
            "sysUserFormMap.policeId":{
                required : "请输入警号",
                remote : "该警号已被使用"
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
    $('.deptcode').on('click',function(){
        debugger;
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
