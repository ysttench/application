var unit = "<option value='all'>---请选择----</option>";
jQuery.validator.addMethod("checkRole", function(value, element) {
    return this.optional(element) || ((value.length <= 10) && (value.length >= 3));
}, "角色名由3至10位字符组合构成");
$(function() {
    $.ajax({
        url: rootPath +"/apiRole/findAllXT",
        data : {},
        dataType : "json",
        success : function(data){
            $.each(data,function(i,n){
                unit +="<option value='"+n.id+"'>"+n.apiName+"</option>"
            })
            debugger;
            $("#yyxtlist").empty().append(unit);
            $("#yyxtlist option").each(function(){
                var val =$(this).val();
                if($("#yyxtlist option[value='"+val+"']").size()>1){
                    $("#yyxtlist option[value='"+val+"']:gt(0)").remove();
                }
            })
        }
    })
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
                    } else if(data=="fail"){
                        layer.msg('添加失败！请选择应用系统!');
                    } else {
                        layer.msg('添加异常！');
                    }
                }
            });
        },
        rules : {
            "apiRoleFormMap.roleName" : {
                required : true,
                remote : { // 异步验证是否存在
                    type : "POST",
                    url : rootPath + '/apiRole/isExist',
                    data : {
                        "roleName" : function() {
                            return $("#roleName").val();
                        }
                    }
                }
            },
            "apiRoleFormMap.roleDesc" : {
                required : true
            },
            "apiRoleFormMap.applyId" : {
                required : true
            }
        },
        messages : {
            "apiRoleFormMap.roleName" : {
                required : "请输入角色名",
                remote : "角色名已存在"
            },
            "apiRoleFormMap.roleDesc" : {
                required : "请输入角色描述"
            },
            "apiRoleFormMap.applyId" : {
                required : "请选择应用系统"
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
