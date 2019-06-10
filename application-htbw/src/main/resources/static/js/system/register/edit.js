$(function() {
	$("form").validate({submitHandler : function(form) {// 必须写在验证前面，否则无法ajax提交
							ly.ajaxSubmit(form, {// 验证新增是否成功
								type : "post",
								dataType : "json",
								success : function(data) {
									if (data == "success") {
										layer.confirm('激活成功!是否返回登陆界面?', function(
												index) {
											window.location.href=rootPath+'/login';
										});
										$("#form")[0].reset();
									}else if(data == "fail1"){
										layer.alert('连接服务器失败！', 3);
									} else {
										layer.alert('注册失败！', 3);
									}
								}
							});
						},
						rules : {
							"sysSystemForMap.flag" : {
								required : true,
							}
						},
						messages : {
							"sysSystemForMap.flag" : {
								required : "请输入激活码"
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
