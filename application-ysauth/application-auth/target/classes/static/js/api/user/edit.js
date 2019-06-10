/*jQuery.validator.addMethod("uploadFile", function(value, element) {
    debugger;
    var fileName = value.substring(value.lastIndexOf('.')+1,value.length);
    if(value =""||(fileName !="jpg"&& fileName !="gif"&&fileName !="png"&&fileName !="bmp" )){
        return false;
    }else{
        return true
    }
}, "上传格式错误");*/
$(function() {
    var oldSfzh = $("#oldSfzh").val();
    var newSfzh = $("#sfzh").val();
    if (oldSfzh == newSfzh) {
        $("#l_err").html("1");
    }
});
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
                    } else if (data == "fail") {
                        layer.alert('更新异常！', 3);
                    } else if (data == "userRoleFail") {
                        layer.alert('删除原先对应用户角色对应关系！', 3);
                    } else if (data == "roleFail") {
                        layer.alert('分配角色异常！', 3);
                    } else {
                        layer.alert('更新失败！', 3);
                    }
                }
            });
        },
        rules : {
            "apiUserFormMap.email" : {
                remote : { // 异步验证是否存在
                    type : "POST",
                    url : rootPath + '/apiUser/isExist',
                    data : {
                        "email" : function() {
                            return $("#email").val();
                        },
                        "userId" : function() {
                            return $("#userId").val();
                        }
                    }
                }
            },
            "apiUserFormMap.mobile" : {
                remote : { // 异步验证是否存在
                    type : "POST",
                    url : rootPath + '/apiUser/isExist',
                    data : {
                        "mobile" : function() {
                            return $("#mobile").val();
                        },
                        "userId" : function() {
                            return $("#userId").val();
                        }
                    }
                }
            },
            "apiUserFormMap.sfzh" : {
                remote : { // 异步验证是否存在
                    type : "POST",
                    url : rootPath + '/apiUser/isExist',
                    data : {
                        "sfzh" : function() {
                            return $("#sfzh").val();
                        },
                        "userId" : function() {
                            return  $("#userId").val();
                        }
                    }
                }
            },
            "apiUserFormMap.policeId" : {
                remote : { // 异步验证是否存在
                    type : "POST",
                    url : rootPath + '/apiUser/isExist',
                    data : {
                        "policeId" : function() {
                            return $("#policeId").val();
                        },
                        "userId" : function() {
                            return  $("#userId").val();
                        }
                    }
                }
            }
        },
        messages : {
            "apiUserFormMap.email" : {
                remote : "该邮箱已被使用"
            },
            "apiUserFormMap.mobile" : {
                remote : "该手机号已被使用"
            },
            "apiUserFormMap.sfzh" : {
                remote : "该身份证号已被使用"
            },
            "apiUserFormMap.policeId" : {
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

function checkPhone1() {
    var phone = document.getElementById('mobile').value;
    if (!(/^1[34578]\d{9}$/.test(phone))) {
        alert('手机号输入错误');
        return false;
    }
}

function checkSFZH1() {
    var sfzh = document.getElementById('sfzh').value;
    var IDCard = /^\d{6}(18|19|20)?\d{2}(0[1-9]|1[12])(0[1-9]|[12]\d|3[01])\d{3}(\d|X)$/;
    if (!(IDCard.test(sfzh))) {
        alert('身份证号格式有误');
        return false;
    }
}

function checkEmail1() {
    var email = document.getElementById('email').value;
    var Eml = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
    if (!(Eml.test(email))) {
        alert('邮箱格式有误');
        return false;
    }
}
