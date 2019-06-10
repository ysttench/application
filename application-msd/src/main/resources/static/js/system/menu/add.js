$(function() {
    // 异步加载所有菜单列表
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
                        layer.alert('添加失败！', 3);
                    }
                }
            });
        },
        rules : {
            "sysMenuFormMap.name" : {
                required : true,
                remote : { // 异步验证是否存在
                    type : "POST",
                    url : rootPath + '/menu/isExist',
                    data : {
                        name : function() {
                            return $("#name").val();
                        }
                    }
                }
            },
            "sysMenuFormMap.menuKey" : {
                required : true,
                remote : { // 异步验证是否存在
                    type : "POST",
                    url : rootPath + '/menu/isExist',
                    data : {
                        menuKey : function() {
                            return $("#menuKey").val();
                        }
                    }
                }
            }
             ,
           "sysMenuFormMap.menuUrl" : {
               remote : { // 异步验证是否存在
                   type : "POST",
                   url : rootPath + '/menu/isExist',
                   data : {
                       menuKey : function() {
                           return $("#menuUrl").val();
                       }
                   }
               }
            }
        },
        messages : {
            "sysMenuFormMap.name" : {
                required : "菜单名称不能为空",
                remote : "该菜单名称已经存在"
            },
            "sysMenuFormMap.menuKey" : {
                required : "菜单标识不能为空",
                remote : "该标识已经存在"
            },
            "sysMenuFormMap.menuUrl" : {
                remote : "url连接已经存在"
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
    var url = rootPath + '/menu/menulists';
    var data = CommnUtil.ajax(url, null, "json");
    if (data != null) {
        var h = "<option value='0'>------顶级目录------</option>";
        for (var i = 0; i < data.length; i++) {
            h += "<option value='" + data[i].id + "'>" + data[i].name + "</option>";
        }
        $("#parentId").html(h);
    } else {
        layer.msg("获取菜单信息错误，请联系管理员！");
    }

    $("#parentId").on("change",function() {
        var parentId = $("#parentId").val();
        console.log(parentId);
        if(parentId == "0"){
            $("#type option[value='0']").attr("selected", "selected");
        }else{
            $.ajax({
                type : "post",
                dataType : "json",
                async : false,
                data : {
                    "parentId" : parentId
                },
                url : rootPath + "/menu/getLevel",
                success : function(msg){
                    console.log(msg);
                    $("#type").find("option").removeAttr("selected");
                    $("#type option[value='"+ msg +"']").prop("selected", true);
                },
                error: function(msg){
                    layer.msg("获取菜单类型失败");
                }
            });
        }
    });

});
function chkClick(obj) {
    if (obj.checked) {
        $("#isHide").val("1");
    } else {
        $("#isHide").val("0");
    }
}
