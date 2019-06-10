//单独验证某一个input  class="checkpass"
jQuery.validator.addMethod("checkacc", function(value, element) {
    debugger;
    return this.optional(element) || !(/^[\u4e00-\u9fa5]+$/.test(value));
}, "用户名由非中文字符组合构成");
jQuery.validator.addMethod("checkSFZH", function(value, element) {
    return this.optional(element) || (isIdCard(value));
}, "身份证号格式错误");
/*jQuery.validator.addMethod("uploadFile", function(value, element) {
    var fileName = value.substring(value.lastIndexOf('.')+1,value.length);
    if(value =""||(fileName !="jpg"&& fileName !="gif"&&fileName !="png"&&fileName !="bmp" )){
        return false;
    }else{
        return true
    }
}, "上传格式错误");*/
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
            "apiUserFormMap.userName" : {
                required : true,
                remote : { // 异步验证是否存在
                    type : "POST",
                    url : rootPath + '/apiUser/isExist',
                    data : {
                        "userName" : function() {
                            return $("#userName").val();
                        }
                    }
                }
            },
            "apiUserFormMap.email" : {
                remote : { // 异步验证是否存在
                    type : "POST",
                    url : rootPath + '/apiUser/isExist',
                    data : {
                        "email" : function() {
                            return $("#email").val();
                        }
                    }
                }
            },
            "apiUserFormMap.phone" : {
                remote : { // 异步验证是否存在
                    type : "POST",
                    url : rootPath + '/apiUser/isExist',
                    data : {
                        "phone" : function() {
                            return $("#phone").val();
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
                        }
                    }
                }
            },"apiUserFormMap.policeId" : {
                remote : { // 异步验证是否存在
                    type : "POST",
                    url : rootPath + '/apiUser/isExist',
                    data : {
                        "policeId" : function() {
                            return $("#policeId").val();
                        }
                    }
                }
            }
        },
        messages : {
            "apiUserFormMap.userName" : {
                required : "请输入用户名",
                remote : "该用户名已被使用"
            },
            "apiUserFormMap.email" : {
                remote : "该邮箱已被使用"
            },
            "apiUserFormMap.phone" : {
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

function checkPhone() {
    var phone = document.getElementById('phone').value;
    if (!(/^1[34578]\d{9}$/.test(phone))) {
        alert('手机号输入错误');
        return false;
    }
}
function isIdCard(idCard) {
    var iSum = 0;
    var sId = idCard;

    var aCity = {
        11 : "北京",
        12 : "天津",
        13 : "河北",
        14 : "山西",
        15 : "内蒙古",
        21 : "辽宁",
        22 : "吉林",
        23 : "黑龙 江",
        31 : "上海",
        32 : "江苏",
        33 : "浙江",
        34 : "安徽",
        35 : "福建",
        36 : "江西",
        37 : "山东",
        41 : "河南",
        42 : "湖 北",
        43 : "湖南",
        44 : "广东",
        45 : "广西",
        46 : "海南",
        50 : "重庆",
        51 : "四川",
        52 : "贵州",
        53 : "云南",
        54 : "西藏",
        61 : "陕西",
        62 : "甘肃",
        63 : "青海",
        64 : "宁夏",
        65 : "新疆",
        71 : "台湾",
        81 : "香港",
        82 : "澳门",
        91 : "国外"
    };

    if (!/^\d{17}(\d|x)$/i.test(sId)) {
        return false;
    }
    sId = sId.replace(/x$/i, "a");
    // 非法地区
    if (aCity[parseInt(sId.substr(0, 2))] == null) {
        return false;
    }
    ;

    var sBirthday = sId.substr(6, 4) + "-" + Number(sId.substr(10, 2)) + "-" + Number(sId.substr(12, 2));

    var d = new Date(sBirthday.replace(/-/g, "/"));

    // 非法生日
    if (sBirthday != (d.getFullYear() + "-" + (d.getMonth() + 1) + "-" + d.getDate())) {
        return false;
    }
    for (var i = 17; i >= 0; i--) {
        var p;
        if (sId.charAt(17 - i) == 'x' || sId.charAt(17 - i) == 'X') {
            p = '10';
        } else {
            p = sId.charAt(17 - i);
        }
        iSum += (Math.pow(2, i) % 11) * parseInt(sId.charAt(17 - i), 11);
    }

    if (iSum % 11 != 1) {
        return false;
    }
    return true;

}


function checkEmail() {
    var email = document.getElementById('email').value;
    var EML = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
    if (!(EML.test(email))) {
        alert('邮箱格式有误');
        return false;
    }
}
