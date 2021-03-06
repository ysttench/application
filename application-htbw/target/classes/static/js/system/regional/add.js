var unit = "<option value='0'>-选择上级区域-</option>";
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
            "regionalFormMap.name" : {
                required : true,
                remote : { // 异步验证是否存在
                    type : "POST",
                    url : rootPath + '/regionalinfo/isExist',
                    data : {
                        "name" : function() {
                            return $("#name").val();
                        }
                    }
                }
            }
        },
        messages : {
            "regionalFormMap.name" : {
                required : "请输入区域名称",
                remote : "该区域名称已存在"
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
        url : rootPath + "/regionalinfo/parentMsg",
        data : {},
        dataType : "json",
        success : function(data) {
        	if (data.length != 0) {
        		$.each(data,function(i,n){
        			unit += "<option value='"+n.id+"'>"+n.name+"</option>"
        		})
			}
        	$("#parentId").empty().append(unit);
            $("#parentId option").each(function(){
                var val =$(this).val();
                if($("#parentId option[value='"+val+"']").size()>1){
                    $("#parentId option[value='"+val+"']:gt(0)").remove();
                }
            })
        }
    })
}
