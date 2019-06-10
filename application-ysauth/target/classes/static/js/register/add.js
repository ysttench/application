jQuery.validator.addMethod("checksfzh", function(value, element) {
    var reg = /^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/;
    if (!reg.test(value)){
        return false;
    }
    return true;
}, "身份证号码格式错误!");

jQuery.validator.addMethod("chenckMobile", function(value, element) {
    var reg = /^((\+?86)|(\(\+86\)))?(13[0-9]{9}|15[0-9]{9}|18[0-9]{9}|147[0-9]{8}|1349[0-9]{7}|17[0-9]{9})$/;
    if (!reg.test(value)){
        return false;
    }
    return true;
}, "手机号码格式错误!");
jQuery.validator.addMethod("chenckEmail", function(value, element) {
    var reg = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;
    if (!reg.test(value)){
        return false;
    }
    return true;
}, "邮箱格式错误!");
jQuery.validator.addMethod("checkPicSize", function(value,element) {
    var fileSize=element.files[0].size;
    var maxSize = 65*1024;
    if(fileSize > maxSize){
        return false;
    }else{
        return true;
    }
}, "请上传大小在65K一下的图片");
$(function() {
    $("form").validate({
        submitHandler : function(form) {// 必须写在验证前面，否则无法ajax提交
            ly.ajaxSubmit(form, {// 验证新增是否成功
                type : "post",
                dataType : "json",
                success : function(data) {
                    if (data == "success") {
                        layer.confirm('添加成功!是否关闭窗口?', function(index) {
                            history.go(-1);
                        });
                        $("#form")[0].reset();
                    } else {
                        layer.alert('添加失败！', 3);
                    }
                }
            });
        },
        rules : {
            "apiUserFormMap.userName" : {
                required : true,
                remote : { // 异步验证是否存在
                    type : "POST",
                    url : rootPath + '/register/checkField',
                    data : {
                        fieldName : function() {
                            return "userName";
                        },
                        value : function(){
                            return $('input[name="apiUserFormMap.userName"]').val();
                        }
                    }
                }
            },
            "apiUserFormMap.password":{
                required : true
            },
            "apiUserFormMap.password_again":{
                required : true,
                equalTo:"#password"
            },
            "apiUserFormMap.policeId":{
                required : true,
                remote : { // 异步验证是否存在
                    type : "POST",
                    url : rootPath + '/register/checkField',
                    data : {
                        fieldName : function() {
                            return "policeId";
                        },
                        value : function(){
                            return $('input[name="apiUserFormMap.policeId"]').val();
                        }
                    }
                }
            },
            "apiUserFormMap.sfzh":{
                required : true,
                remote : { // 异步验证是否存在
                    type : "POST",
                    url : rootPath + '/register/checkField',
                    data : {
                        fieldName : function() {
                            return "sfzh";
                        },
                        value : function(){
                            return $('input[name="apiUserFormMap.sfzh"]').val();
                        }
                    }
                },
                checksfzh : true
            },
            "apiUserFormMap.email":{
                required : true,
                remote : { // 异步验证是否存在
                    type : "POST",
                    url : rootPath + '/register/checkField',
                    data : {
                        fieldName : function() {
                            return "email";
                        },
                        value : function(){
                            return $('input[name="apiUserFormMap.email"]').val();
                        }
                    }
                },
                chenckEmail : true
            },
            "apiUserFormMap.mobile":{
                required : true,
                remote : { // 异步验证是否存在
                    type : "POST",
                    url : rootPath + '/register/checkField',
                    data : {
                        fieldName : function() {
                            return "mobile";
                        },
                        value : function(){
                            return $('input[name="apiUserFormMap.mobile"]').val();
                        }
                    }
                },
                chenckMobile:true
            },
            "apiUserFormMap.comment":{
                maxlength : 500
            }
        },
        messages : {
            "apiUserFormMap.userName" : {
                required : "请输入账号",
                remote : "该账号已经存在"
            },
            "apiUserFormMap.password":{
                required : "请输入密码"
                
            },
            "apiUserFormMap.password_again":{
                required : "请输入确认密码",
                equalTo:"请与原密码保持一致"
            },
            "apiUserFormMap.policeId":{
                required : "请输入警号",
                remote : "该警号已存在"
            },
            "apiUserFormMap.sfzh":{
                required : "请输入身份证号",
                remote : "该身份证号已存在"
            },
            "apiUserFormMap.email":{
                required : "请输入邮箱号码",
                    remote : "该邮箱已存在"
            },
            "apiUserFormMap.mobile":{
                required : "请输入手机号码",
                remote : "该手机号码已存在"
            },
            "apiUserFormMap.comment":{
                maxlength : "备注长度不能超过500"
            }
        },
        errorPlacement : function(error, element) {// 自定义提示错误位置
            $(".l_err").show();
            // element.css('border','3px solid #FFCCCC');
            $(".l_err").html(error.html());
        },
        success : function(label) {// 验证通过后
            $(".l_err").hide();
        }
    });
});
