//登录
function doLogin() {
    const account = $("#account").val();
    const password = $("#password").val();
    const flag = valid(account, password);
    if (flag != true) {
        layer.alert(flag)
        return;
    }
    const url = "/auth/login";
    const data = {
        "username": account,
        "password": password
    };
    $.ajax({
        url: url,
        type: "POST",
        data: JSON.stringify(data),
        contentType: "application/json",
        dataType:"json",
        success: function (result) {
            console.log(result);
            if (result.code== 0) {
                const token=result.data;
                $.cookie("adminToken", token, {
                    path: '/',
                    expire: 7
                });
                   location.href="index.html";
            }else {
                layer.alert("账号或密码不匹配");
            }
        }
    });
}


function valid(account, password) {             //验证参数合法性
    if (account.length < 5 || account.length > 10) {
        return "账号长度只能为5-10个合法字符"
    }
    if (password.length < 5 || password.length > 10) {
        return "密码长度只能为5-10个合法字符"
    }
    return true;

}