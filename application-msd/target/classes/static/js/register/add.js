jQuery.validator
		.addMethod(
				"chenckMobile",
				function(value, element) {
					var reg = /^((\+?86)|(\(\+86\)))?(13[0-9]{9}|15[0-9]{9}|18[0-9]{9}|147[0-9]{8}|1349[0-9]{7}|17[0-9]{9})$/;
					if (!reg.test(value)) {
						return false;
					}
					return true;
				}, "手机号码格式错误!");
jQuery.validator.addMethod("chenckEmail", function(value, element) {
	var reg = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;
	if (!reg.test(value)) {
		return false;
	}
	return true;
}, "邮箱格式错误!");
$(function() {
	$("form")
			.validate(
					{
						submitHandler : function(form) {// 必须写在验证前面，否则无法ajax提交
							ly.ajaxSubmit(form, {// 验证新增是否成功
								type : "post",
								dataType : "json",
								success : function(data) {
									if (data == "success") {
										layer.confirm('注册成功!是否关闭窗口?', function(
												index) {
											window.location.href=rootPath+'/login';
										});
										$("#form")[0].reset();
									} else {
										layer.alert('注册失败！', 3);
									}
								}
							});
						},
						rules : {
							"apiUserFormMap.companyName" : {
								required : true,
								remote : { // 异步验证是否存在
									type : "POST",
									url : rootPath + '/register/checkField',
									data : {
				                        fieldName : function() {
				                            return "companyName";
				                        },
				                        value : function(){
				                            return $('input[name="apiUserFormMap.companyName"]').val();
				                        }
				                    }
								}
							},
							"apiUserFormMap.email" : {
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
							"apiUserFormMap.mobile" : {
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
								chenckMobile : true
							}
						},
						messages : {
							"apiUserFormMap.companyName" : {
								required : "请输入公司名称",
								remote : "该公司已经激活"
							},
							"apiUserFormMap.email" : {
								required : "请输入邮箱号码",
								remote : "该邮箱已存在"
							},
							"apiUserFormMap.mobile" : {
								required : "请输入手机号码",
								remote : "该手机号码已存在"
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
