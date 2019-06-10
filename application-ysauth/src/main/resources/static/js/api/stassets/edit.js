$(function() {
	keepdepart();
	zcfl();
	keeper();
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
					} else if (data == "fail1") {
						layer.alert('审核状态的资产信息无法修改!', 3);
					} else if (data == "null") {
						layer.alert('更新失败！请分配权限!', 3);
					} else {
						layer.alert('更新失败！', 3);
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
						},
						"id" : function() {
							return $("#id").val();
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
			// element.css('border','3px solid #FFCCCC');
			$(".l_err").html(error.html());
		},
		success : function(label) {// 验证通过后
			$(".l_err").css('display', 'none');
		}
	});
	$("#keepdepart").change(function() {
		keeper1();
	})
	superplace();
	bangong();
	$("#superplace").change(function() {
		bangong1();
	})
/*	$("#place").change(function() {
		bangong();
	})*/
});
function keepdepart() {
	var department = $("#keepdepart1").val();
	var unit = "<option value='" + department + "'>" + department + "</option>";
	$
			.ajax({
				url : rootPath + "/stassets/getkeepdepart",
				data : {},
				dataType : "json",
				success : function(data) {
					if (data.length != 0) {
						$.each(data, function(i, n) {
							unit += "<option value='" + n.fname + "'>"
									+ n.fname + "</option>"
						})
					}
					$("#keepdepart").empty().append(unit);
					$("#keepdepart option")
							.each(
									function() {
										var val = $(this).val();
										if ($(
												"#keepdepart option[value='"
														+ val + "']").size() > 1) {
											$(
													"#keepdepart option[value='"
															+ val + "']:gt(0)")
													.remove();
										}
									})
				}
			})
}

function keeper() {
	var keeper = $("#keeper1").val();
	var department = $("#keepdepart1").val();
	var unit = "<option value='" + keeper + "'>" + keeper + "</option>";
	$.ajax({
		url : rootPath + "/stassets/getkeeper",
		data : {
			"department" : department
		},
		dataType : "json",
		success : function(data) {
			if (data.length != 0) {
				$.each(data, function(i, n) {
					debugger;
					unit += "<option value='" + n.fname + "'>" + n.fname
							+ "</option>"
				})
			}
			$("#keeper").empty().append(unit);
			$("#keeper option").each(function() {
				var val = $(this).val();
				if ($("#keeper option[value='" + val + "']").size() > 1) {
					$("#keeper option[value='" + val + "']:gt(0)").remove();
				}
			})
		}
	})
}
function keeper1() {
	var department = $("#keepdepart").val();
	var unit = "<option value='null'>-请选择保管人-</option>";
	$.ajax({
		url : rootPath + "/stassets/getkeeper",
		data : {
			"department" : department
		},
		dataType : "json",
		success : function(data) {
			if (data.length != 0) {
				$.each(data, function(i, n) {
					unit += "<option value='" + n.fname + "'>" + n.fname
							+ "</option>"
				})
			}
			$("#keeper").empty().append(unit);
			$("#keeper option").each(function() {
				var val = $(this).val();
				if ($("#keeper option[value='" + val + "']").size() > 1) {
					$("#keeper option[value='" + val + "']:gt(0)").remove();
				}
			})
		}
	})
}
function zcfl() {
	var zcfl = $("#type1").val();
	var unit = "<option value='" + zcfl + "'>-" + zcfl + "-</option>";
	$
			.ajax({
				url : rootPath + "/assetsType/zcfl",
				data : {},
				dataType : "json",
				success : function(data) {
					if (data.length != 0) {
						$.each(data, function(i, n) {
							unit += "<option value='" + n.name + "'>" + n.name
									+ "</option>"
						})
					}
					$("#assetsType").empty().append(unit);
					$("#assetsType option")
							.each(
									function() {
										var val = $(this).val();
										if ($(
												"#assetsType option[value='"
														+ val + "']").size() > 1) {
											$(
													"#assetsType option[value='"
															+ val + "']:gt(0)")
													.remove();
										}
									})
				}
			})
}
function superplace() {
	debugger;
	var code = $('#cur').val();
	var name = $('#cur').data("name");
	if (name == undefined) {
		name = "-请选择主办公位置-";
	}
	var unit = "<option value='" + name + "'>" + name + "</option>";
	var locationCode = $('#superplace').val();
	if (locationCode == undefined) {
		locationCode = 0;
	}
	$
			.ajax({
				url : rootPath + "/apiOffice/superplace",
				data : {
					locationCode : locationCode
				},
				dataType : "json",
				success : function(data) {
					if (data.length != 0) {
						$.each(data, function(i, n) {
							unit += "<option value='" + n.locationName + "'>"
									+ n.locationName + "</option>"
						})
					}
					$("#superplace").empty().append(unit);
					$("#superplace option")
							.each(
									function() {
										var val = $(this).val();
										if ($(
												"#superplace option[value='"
														+ val + "']").size() > 1) {
											$(
													"#superplace option[value='"
															+ val + "']:gt(0)")
													.remove();
										}
									})
				}
			})
}
function bangong() {
	debugger
	var name = $('#cur1').data("name");
	var code = $('#cur1').val();
	if (name == undefined) {
		name = "-请选择办公位置-";
	}
	var unit1 = "<option value='" + code + "'>" + name + "</option>";
	var locationCode = $('#cur').data("name");
	if (locationCode == undefined) {
		locationCode = 0;
	}
	$.ajax({
		url : rootPath + "/apiOffice/superplace",
		data : {
			locationCode : locationCode
		},
		dataType : "json",
		success : function(data) {
			debugger;
			if (data.length != 0) {
				$.each(data, function(i, n) {
					unit1 += "<option value='" + n.id + "'>"
							+ n.locationName + "</option>"
				})
			}
			$("#place").empty().append(unit1);
			$("#place option").each(function() {
				var val = $(this).val();
				if ($("#place option[value='" + val + "']").size() > 1) {
					$("#place option[value='" + val + "']:gt(0)").remove();
				}
			})
		}
	})
}
function bangong1() {
	debugger
	var unit1 = "<option value='0'>-请选择办公位置-</option>";
	var locationCode = $('#superplace').val();
	if (locationCode == undefined) {
		locationCode = 0;
	}
	$.ajax({
		url : rootPath + "/apiOffice/superplace",
		data : {
			locationCode : locationCode
		},
		dataType : "json",
		success : function(data) {
			if (data.length != 0) {
				$.each(data, function(i, n) {
					unit1 += "<option value='" + n.id + "'>"
							+ n.locationName + "</option>"
				})
			}
			$("#place").empty().append(unit1);
			$("#place option").each(function() {
				var val = $(this).val();
				if ($("#place option[value='" + val + "']").size() > 1) {
					$("#place option[value='" + val + "']:gt(0)").remove();
				}
			})
		}
	})
}
