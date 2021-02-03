let checked;
let checkData;
$(function () {
    const url ="/findMenus";
    table.render({
        elem: '#menus',
        url: url,
        headers:{auth:$.cookie('adminToken')},
        id:'menus',
        cols: [[
            {checkbox: true, fixed: true},
            {field: 'id', title: '菜单ID'},
            {field: 'name', title: '菜单名称'},
            {field: 'url', title: 'URL'},
            {field: 'note', title: '描述'},
            {field: 'menuSource', title: '菜单类型',templet:function (res) {
                    const menuSource=res.menuSource;
                    if(menuSource != null && menuSource!= undefined){
                        let show;
                        switch (menuSource) {
                            case 'NAVIGATE':show='导航';
                                break;
                            case 'OPERATION':show='功能操作';
                                break;
                            default:show='未知';
                        }
                        return show;
                    }else {
                        return "";
                    }
                }},
            {field: 'operationSource', title: '操作类型',templet:function (res){
                const operationSource=res.operationSource;
                if(operationSource != null && operationSource!= undefined){
                    let show;
                    switch (operationSource) {
                        case 'INSERT':show='新增';
                        break;
                        case 'DELETE':show='删除';
                        break;
                        case 'UPDATE':show='修改';
                        break;
                        case 'SELECT':show='查询';
                        break;
                        default:show='导航';
                    }
                    return show;
                }else {
                    return "";
                }
                }},
            {field: 'parentID', title: '上级菜单ID'},
            {field: 'createTimeStamp',title:'创建时间',templet: function (res) {
                   return parseTimeStampToString(res.createTimeStamp) ;
                }}
        ]],
        page: true,
        parseData:  function (res){return responseBody(res)}
    });
    table.on('checkbox(menus)', function (obj) {
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
function click_add_menu(type) {
    let layerHtml;
    let layerTitle;
    if(type == 0){   //表示添加导航菜单
        layerHtml=$("#hide_click_add_nav_menu_div");
        layerTitle="添加导航菜单";
    }else {
        if(checked){
            if(checkData.parentID==-1){
                layerHtml=$("#hide_click_add_operation_menu_div");
                layerTitle="添加功能操作菜单";
            }else {
                layer.alert("只能在导航菜单下添加功能操作菜单");
                return ;
            }
        }else {
            layer.alert("请选中单条数据进行添加功能操作菜单");
            return ;
        }
    }
    showLayer(type,layerTitle,layerHtml);
}

function showLayer(type,title,div) {
    layer.open({
        title: title,
        type: 1,
        content: div,
        btnAlign: "c",
        area: ['500px','400px'],
        btn: ['确定', '取消'],
        btn1: function (index) {
            let data;
            if(type==0){
                const nav_menu_url=$("#nav_menu_url").val();
                const nav_menu_note=$("#nav_menu_note").val();
                const nav_menu_name=$("#nav_menu_name").val();
                data={url:nav_menu_url,note:nav_menu_note,parentID:-1,operationSource:'DEFAULT',menuSource:'NAVIGATE',name:nav_menu_name};
            }else {
                const operation_menu_url=$("#operation_menu_url").val();
                const operation_menu_note=$("#operation_menu_note").val();
                const operation_menu_name=$("#operation_menu_name").val();
                const operation_menu_parentID=checkData.id;
                const operation_menu_operation_type=$("select[name='select_operation_type']").val();
                data={url:operation_menu_url,name:operation_menu_name,note:operation_menu_note,parentID:operation_menu_parentID,menuSource:'OPERATION',operationSource:operation_menu_operation_type};
            }
            const url="/addMenu";
            $.ajax({
                type:"POST",
                contentType:"application/json",
                url:url,
                data:JSON.stringify(data),
                dataType:"json",
                headers:{auth:$.cookie('adminToken')},
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

        },success: function (layero, index) {
            form.render('select');
        }
    });
}