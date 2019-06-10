$(function() {
	var type =  $('#userType').val();
	if(0 == type){
		$('#pa').show();
		$('#zz').show();
	}
	zz();
    $("form").validate({
        submitHandler : function(form) {// 必须写在验证前面，否则无法ajax提交
            ly.ajaxSubmit(form, {// 验证新增是否成功
                type : "post",
                dataType : "json",// ajaxSubmi带有文件上传的。不需要设置json
                success : function(data) {
                    if (data == "success") {
                        layer.confirm('更新成功!是否关闭窗口?', function(index) {
                            parent.grid.loadData();
                            parent.layer.close(parent.pageii);
                            return false;
                        });
                    } else if(data=="null"){
                        layer.alert('更新失败！请分配权限!',3);
                    }else {
                        layer.alert('更新失败！',3);
                    }
                }
            });
        },
        rules : {
            "sysUserFormMap.name" : {
                required : true
            },
            "sysUserFormMap.userName" : {
                required : true,
                remote : { // 异步验证是否存在
                    type : "POST",
                    url : rootPath + '/user/isExist',
                    data : {
                        "userName" : function() {
                            return $("#userName").val();
                        },
                        "userId" : function() {
                            return $("#userId").val();
                        }
                    }
                }
            },
            "sysUserFormMap.password" : {
                required : true,
                minlength : 6
            }
        },
        messages : {
            "sysUserFormMap.name" : {
                required : "请输入用户名"
            },
            "sysUserFormMap.userName" : {
                required : "请输入账号",
                remote : "该账号已经存在"
            },
            "sysUserFromMap.password" : {
                required : "请输入密码",
                minlength : $.validator.format("123456")
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
function zz() {
	var org =  $('#org').val();
	var unit = "<option value='"+org+"'>"+org+"</option>";
    $.ajax({
        url : rootPath + "/print/getMsg",
        data : {},
        dataType : "json",
        success : function(data) {
        	debugger;
        	if (data.length != 0) {
        		$.each(data,function(i,n){
        			unit += "<option value='"+n.organ+"'>"+n.organ+"</option>"
        		})
			}
        	$("#organ").empty().append(unit);
            $("#organ option").each(function(){
                var val =$(this).val();
                if($("#organ option[value='"+val+"']").size()>1){
                    $("#organ option[value='"+val+"']:gt(0)").remove();
                }
            })
        }
    })
}