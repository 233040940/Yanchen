
let checked;
let checkData;
$(function () {
    const url ="/findRoles";
    table.render({
        elem: '#roles',
        url: url,
        id:'roles',
        headers:{auth:$.cookie('adminToken')},
        cols: [[
            {checkbox: true, fixed: true},
            {field: 'code', title: '角色代码'},
            {field: 'note', title: '描述'},
            {field: 'createTimeStamp',title:'创建时间',templet: function (res) {
                 return parseTimeStampToString(res.createTimeStamp);
                }}
        ]],
        page: true,
        parseData:function (res) {
            return responseBody(res);
        }
    });
    table.on('checkbox(roles)', function (obj) {
        if (obj.checked == true && obj.type == "one") {
            checkData = obj.data;
            console.log(obj.data);
            checked = true;
        } else {
            checked = false;
        }
        // console.log(obj.checked); //当前是否选中状态
        // console.log(obj.data); //选中行的相关数据
        // console.log(obj.type); //如果触发的是全选，则为：all，如果触发的是单选，则为：one
        // console.log(table.checkStatus('table-organization').data); // 获取表格中选中行的数据

    });
});

function click_add_role(){
    layer.open({
        title: "添加角色信息",
        type: 1,
        content: $("#hide_click_add_role_div"),
        btnAlign: "c",
        area: '500px',
        btn: ['确定', '取消'],
        btn1: function (index) {
            const code=$("#code").val();
            const note=$("#note").val();
            const data=JSON.stringify({note:note,code:code});
            const url="/addRole";
            $.ajax({
                type:"POST",
                contentType:"application/json",
                url:url,
                data:data,
                headers:{auth:$.cookie('adminToken')},
                dataType:"json",
                success:function (result){
                    console.log(result);
                    if(result.code==0){
                        layer.close(index);
                        location.reload(true);
                    }else {
                        const show=result.msg;
                        layer.alert(show);
                    }
                }
            });

        }
    });
}

function click_config_user() {
  if(checked){
      layer.open({
          title: "设置用户角色",
          type: 1,
          content: $("#hide_click_config_user_div"),
          btnAlign: "c",
          area: '500px',
          btn: ['确定', '取消'],
          btn1: function (index) {
              const account=$("#account").val();
              const data=JSON.stringify({account:account,roleId:checkData.id});
              const url="/userRoleConfig";
              $.ajax({
                  type:"POST",
                  contentType:"application/json",
                  url:url,
                  data:data,
                  headers:{auth:$.cookie('adminToken')},
                  dataType:"json",
                  success:function (result){
                      console.log(result);
                      if(result.code==0){
                          layer.close(index);
                          location.reload(true);
                      }else {
                          const show=result.msg;
                          layer.alert(show);
                      }
                  }
              });

          }
      });
  }else {
      layer.alert("请选中单条记录");
  }
}