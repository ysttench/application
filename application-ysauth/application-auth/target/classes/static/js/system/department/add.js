jQuery.validator.addMethod("checkacc", function(value, element) {
    return this.optional(element) || (/^\d{6}$/.test(value));
}, "行政区划由6位数字组合构成");
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
                        $(".l_err").css('display', 'block');
                        $(".l_err").html(data);
                        layer.alert('添加失败！', 3);
                   }
                }
            });
        },
        rules : {
            "sysDepartmentFormMap.deptCode" : {
                required : true,
                remote : { // 异步验证是否存在
                    type : "POST",
                    url : rootPath + '/sysdepartment/isExistDeptCode',
                    data : {
                        deptCode : function() {
                            return $("#deptCode").val();
                        }
                    }
                },
                maxlength:12
            },
            "sysDepartmentFormMap.deptName" : {
                required : true,
                remote : { // 异步验证是否存在
                    type : "POST",
                    url : rootPath + '/sysdepartment/isExistDeptName',
                    data : {
                        deptName : function() {
                            return $("#deptName").val();
                        }
                    }
                }
            },
        },
        messages : {
            "sysDepartmentFormMap.deptCode" : {
                required : "请输入部门编码！",
                remote : "该部门编码已经存在！",
                maxlength:"部门编码不能超过12"
            },
            "sysDepartmentFormMap.deptName" : {
                required : "请输入部门名称！",
                remote : "该部门名称已经存在！"
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
    //添加时账号输入框自动获取焦点
    $("#id").focus();

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

/*    $.ajax({
        url : rootPath+"/xzqhb/getAllData",
        data:{},
        type: "post",
        dataType:"json",
        success : function(data){
            if(data.length > 0){
                var unit = "";
                $.each(data,function(i,n){
                    unit += "<option value='"+n.dm+"'>"+n.mc+"</option>"; 
                })
                $("#xzqh").empty().append(unit);
            }else{
                layer.msg('暂无行政区划数据');
            }
        },
        error : function(){
            layer.msg('加载数据失败');
        }
    })*/
    // getAllDepartment($('#deptcode'));
});

