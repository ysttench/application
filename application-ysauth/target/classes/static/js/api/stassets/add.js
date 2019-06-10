$(function() {
	keepdepart();
	zcfl();
	$("#place").hide();
	superplace();
	$("#ke").hide();
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
            "apiStAssetsForMap.assetsNum" : {
                required : true,
                remote : { // 异步验证是否存在
                    type : "POST",
                    url : rootPath + '/stassets/isExist',
                    data : {
                        "assetsNum" : function() {
                            return $("#assetsNum").val();
                        }
                    }
                }
            }
        },
        messages : {
            "apiStAssetsForMap.assetsNum" : {
                required : "请输入资产编号",
                remote : "该资产编号已经存在"
            }
        },
        errorPlacement : function(error, element) {// 自定义提示错误位置
            $(".l_err").css('display', 'block');
            $(".l_err").html(error.html());
        },
        success : function(label) {// 验证通过后
            $(".l_err").css('display', 'none');
        }
    });
    $("#keepdepart").click("click", function() {
    	$("#ke").show();
    	keeper();
    });
    $("#superplace").change(function(){ 
    	$("#place").show();
        bangong();
    })
});
function keepdepart(){
	var unit = "<option value='null'>-请选择保管部门-</option>";
    $.ajax({
        url : rootPath + "/stassets/getkeepdepart",
        data : {},
        dataType : "json",
        success : function(data) {
        	if (data.length != 0) {
        		$.each(data,function(i,n){
        			unit += "<option value='"+n.fname+"'>"+n.fname+"</option>"
        		})
			}
        	$("#keepdepart").empty().append(unit);
            $("#keepdepart option").each(function(){
                var val =$(this).val();
                if($("#keepdepart option[value='"+val+"']").size()>1){
                    $("#keepdepart option[value='"+val+"']:gt(0)").remove();
                }
            })
        }
    })
}

function keeper(){
	var department = $("#keepdepart").val();
	var unit = "<option value='null'>-请选择保管人-</option>";
    $.ajax({
        url : rootPath + "/stassets/getkeeper",
        data : {"department":department},
        dataType : "json",
        success : function(data) {
        	if (data.length != 0) {
        		$.each(data,function(i,n){
        			debugger;
        			unit += "<option value='"+n.fname+"'>"+n.fname+"</option>"
        		})
			}
        	$("#keeper").empty().append(unit);
            $("#keeper option").each(function(){
                var val =$(this).val();
                if($("#keeper option[value='"+val+"']").size()>1){
                    $("#keeper option[value='"+val+"']:gt(0)").remove();
                }
            })
        }
    })
}
function zcfl() {
	var unit = "<option value='0'>-请选择资产分类-</option>";
    $.ajax({
        url : rootPath + "/assetsType/zcfl",
        data : {},
        dataType : "json",
        success : function(data) {
        	debugger;
        	if (data.length != 0) {
        		$.each(data,function(i,n){
        			unit += "<option value='"+n.name+"'>"+n.name+"</option>"
        		})
			}
        	$("#assetsType").empty().append(unit);
            $("#assetsType option").each(function(){
                var val =$(this).val();
                if($("#assetsType option[value='"+val+"']").size()>1){
                    $("#assetsType option[value='"+val+"']:gt(0)").remove();
                }
            })
        }
    })
}

function superplace() {
	var unit = "<option value='0'>-请选择主办公位置-</option>";
	var locationCode =  $('#superplace').val();
	if (locationCode == undefined) {
		locationCode= 0;
	}
    $.ajax({
        url : rootPath + "/apiOffice/superplace",
        data : {locationCode:locationCode},
        dataType : "json",
        success : function(data) {
        	if (data.length != 0) {
        		$.each(data,function(i,n){
        			unit += "<option value='"+n.locationName+"'>"+n.locationName+"</option>"
        		})
			}
        	$("#superplace").empty().append(unit);
            $("#superplace option").each(function(){
                var val =$(this).val();
                if($("#superplace option[value='"+val+"']").size()>1){
                    $("#superplace option[value='"+val+"']:gt(0)").remove();
                }
            })
        }
    })
}
function bangong() {
	debugger;
	var unit1 = "<option value='0'>-请选择办公位置-</option>";
	var locationCode =  $('#superplace').val();
	if (locationCode == undefined) {
		locationCode= 0;
	}
    $.ajax({
        url : rootPath + "/apiOffice/superplace",
        data : {locationCode:locationCode},
        dataType : "json",
        success : function(data) {
        	if (data.length != 0) {
        		$.each(data,function(i,n){
        			unit1 += "<option value='"+n.id+"'>"+n.locationName+"</option>"
        		})
			}
        	$("#place").empty().append(unit1);
            $("#place option").each(function(){
                var val =$(this).val();
                if($("#place option[value='"+val+"']").size()>1){
                    $("#place option[value='"+val+"']:gt(0)").remove();
                }
            })
        }
    })
}