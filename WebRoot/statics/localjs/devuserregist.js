$(function(){  
	$("#back").on("click",function(){
		window.location.href = "/AppInfo/dev/flatform/main";
	});
	
	$("#devCode").bind("blur",function(){
		//ajax后台验证--APKName是否已存在
		$.ajax({
			type:"GET",//请求类型
			url:"checkdevCode.json",//请求的url
			data:{devCode:$("#devCode").val()},//请求参数
			dataType:"json",//ajax接口（请求url）返回的数据类型
			success:function(data){//data：返回数据（json对象）
				if(data.devCode == "empty"){//参数devCode为空，错误提示
					alert("devCode为不能为空！");
				}else if(data.devCode == "exist"){//账号不可用，错误提示
					alert("该devCode已存在，不能使用！");
				}else if(data.devCode == "noexist"){//账号可用，正确提示
					alert("该devCode可以使用！");
				}
			},
			error:function(data){//当访问时候，404，500 等非200的错误状态码
				alert("请求错误！");
			}
		});
	});
	
	
	$("#devuserform").submit(function(){
		var devPassword = $("#devPassword").val();
		var reDevPassword = $("#reDevPassword").val();
		if(devPassword!=reDevPassword){
			alert("两次密码不同，请重新输入");
			$("#reDevPassword").focus();
			return false;
		}
		var devEmail = $("#devEmail").val();
		if (devEmail!=null && devEmail!="") {
			//alert(devEmail);
			var reg = new RegExp("^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$"); //正则表达式
			if(!reg.test(devEmail)){ //正则验证不通过，格式不对
				alert("邮箱格式有误!");
				return false;
			}else{
				return true;
			}
		}
		return true;
	});
});
      
      
      