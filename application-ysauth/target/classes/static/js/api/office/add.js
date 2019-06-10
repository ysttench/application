var unit = "<option value='0'>-主办公位置-</option>";
$(function() {
	sjbgwz();
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
            "apiOfficeFormMap.locationName" : {
                required : true,
                remote : { // 异步验证是否存在
                    type : "POST",
                    url : rootPath + '/apiOffice/isExist',
                    data : {
                        "locationName" : function() {
                            return $("#locationName").val();
                        }
                    }
                }
            },
            "apiOfficeFormMap.locationCode" : {
                remote : { // 异步验证是否存在
                    type : "POST",
                    url : rootPath + '/apiOffice/isExist',
                    data : {
                        "locationCode" : function() {
                            return $("#locationCode").val();
                        }
                    }
                }
            }
        },
        messages : {
            "apiOfficeFormMap.locationName" : {
                required : "请输入位置名称",
                remote : "该位置名称已存在"
            },
            "apiOfficeFormMap.locationCode" : {
            	required :"请输入位置编码",
                remote : "该位置编码已被使用"
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
function sjbgwz() {
    $.ajax({
        url : rootPath + "/apiOffice/sjbgwz",
        data : {},
        dataType : "json",
        success : function(data) {
        	if (data.length != 0) {
        		$.each(data,function(i,n){
        			unit += "<option value='"+n.id+"'>"+n.locationName+"</option>"
        		})
			}
        	$("#superCode").empty().append(unit);
            $("#superCode option").each(function(){
                var val =$(this).val();
                if($("#superCode option[value='"+val+"']").size()>1){
                    $("#superCode option[value='"+val+"']:gt(0)").remove();
                }
            })
        }
    })
}
