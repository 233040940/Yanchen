let checked;
let checkData;
$(function () {
    const url = "/findPermissions";
    table.render({
        elem: '#permissions',
        url: url,
        id: 'permissions',
        headers:{auth:$.cookie('adminToken')},
        cols: [[
            {checkbox: true, fixed: true},
            {
                field: 'code', title: '角色代码', templet: function (res) {
                    console.log(res);
                    return res.role.code;
                }
            },
            {
                field: 'note', title: '角色描述', templet: function (res) {
                    return res.role.note;
                }
            }, {
                field: 'permissionMenuList', title: '权限菜单列表', templet: function (res) {
                    const permissionMenus = res.menu;
                    console.log(permissionMenus);
                    let show = '';
                    if (permissionMenus.length > 1) {
                        for (let i = 1; i <= permissionMenus.length; i++) {
                            if (i != permissionMenus.length) {
                                show += permissionMenus[i - 1].name + ',';
                            } else {
                                show += permissionMenus[i - 1].name;
                            }
                        }
                        show = '[' + show + ']';
                    } else if (permissionMenus.length == 1) {
                        show = permissionMenus[0].name;
                    }
                    return show;
                }
            },
            {
                field: 'createTimeStamp', title: '创建时间', templet: function (res) {
                    return parseTimeStampToString(res.createTimeStamp);
                }
            }
        ]],
        page: true,
        parseData: function(res){return responseBody(res) }
    });
    table.on('checkbox(permissions)', function (obj) {
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
    initRoleSelect();
    initSelectMenus();

});

function click_add_role_permission() {
    layer.open({
        title: "角色权限配置",
        type: 1,
        content: $("#hide_click_add_role_permission_div"),
        btnAlign: "c",
        area: ['500px', '400px'],
        btn: ['确定', '取消'],
        btn1: function (index) {
            let nav_menus_ids = new Array();
            $("input:checkbox[name='add_nav_menus_checkbox']:checked").each(function (i) {
                nav_menus_ids[i] = $(this).val();
            });
            let operation_menu_ids=new Array();
            $("input:checkbox[name='add_operation_menus_checkbox']:checked").each(function (i) {
                operation_menu_ids[i] = $(this).val();
            });
            const role_id = $("select[name='select_roles']").val();
            let request_param = {roleID: role_id, menuIDs: nav_menus_ids.concat(operation_menu_ids)};
            console.log(request_param);
            const url = "/permissionConfig";
            $.ajax({
                type: "POST",
                contentType: "application/json",
                url: url,
                data: JSON.stringify(request_param),
                headers:{auth:$.cookie('adminToken')},
                dataType: "json",
                success: function (res) {
                    console.log(res);
                    if (res.code == 0) {
                        layer.close(index);
                        location.reload(true);
                    } else {
                        const msg = res.msg;
                        layer.alert(msg);
                    }
                }
            });
        }, success: function () {
            form.render('select');
            form.render('checkbox');
        }
    });
}

function initRoleSelect() {
    const url = "/findAllRoles"
    $.ajax({
        type: "GET",
        url: url,
        dataType: "json",
        async: false,
        headers:{auth:$.cookie('adminToken')},
        success: function (result) {
            console.log(result);
            if (result.code == 0) {
                const data = result.data;
                if (data != null && data.length > 0) {
                    for (let j = 0; j < data.length; j++) {
                        $("#select_roles").append($("<option/>").text(data[j].note).attr("value", data[j].id));
                    }
                } else {
                    layer.alert("不存在角色信息,请去往角色管理进行新增。");
                }
            } else {
                const show = result.msg;
                layer.alert(show);
            }
        }
    });
}

function initSelectMenus() {
    const url = "/findAllMenus";
    $.ajax({
        type: "GET",
        url: url,
        dataType: "json",
        headers:{auth:$.cookie('adminToken')},
        async: false,
        success: function (result) {
            console.log(result);
            if (result.code == 0) {
                const data = result.data;
                if (data != null && data.length > 0) {
                    for (let i = 0; i < data.length; i++) {
                        if(data[i].parentID==-1){
                            $("#add_select_nav_menus").append("<input type='checkbox' name='add_nav_menus_checkbox' value='" + data[i].id + "' title='" + data[i].name + "'>");
                        }else {
                            $("#add_select_operation_menus").append("<input type='checkbox' name='add_operation_menus_checkbox' value='" + data[i].id + "' title='" + data[i].name + "'>");
                        }
                    }
                }
            } else {
                const show = result.msg;
                layer.alert(show);
            }
        }
    });
}

function click_edit_permission() {
    if (checked) {
        edit_permission_select_change_event();
        layer.open({
            title: "编辑权限",
            type: 1,
            content: $("#hide_click_edit_permission_div"),
            btnAlign: "c",
            area: ['500px', '400px'],
            btn: ['确定', '取消'],
            btn1: function (index) {
                let nav_menus_ids = new Array();
                $("input:checkbox[name='edit_navigate_menus_checkbox']:checked").each(function (i) {
                    nav_menus_ids[i] = $(this).val();
                });
                let operation_menus_ids = new Array();
                $("input:checkbox[name='edit_operation_menus_checkbox']:checked").each(function (i) {
                    operation_menus_ids[i] = $(this).val();
                });
                const operation_type = $("select[name='select_operation_type']").val();
                const role_id = checkData.role.id;
                const permission_id = checkData.id;
                let request_param = {
                    permissionID: permission_id,
                    roleID: role_id,
                    menuIDs: nav_menus_ids.concat(operation_menus_ids),
                    source: operation_type
                };
                const url = "/editPermissionConfig";
                $.ajax({
                    type: "POST",
                    url: url,
                    contentType: "application/json",
                    data: JSON.stringify(request_param),
                    headers:{auth:$.cookie('adminToken')},
                    dataType: "json",
                    success: function (result) {
                        console.log(result);
                        if (result.code == 0) {
                            layer.close(index);
                            location.reload(true);
                        } else {
                            const show = result.msg;
                            layer.alert(show);
                        }
                    }
                });
            }, success: function () {
                form.render('select');
            }
        });
    } else {
        layer.alert("请选中单条权限信息");
    }
}

function edit_permission_select_change_event() {
    form.on('select(select_operation_type)', function (data) {
        clearCheckBoxCache();
        if (data.value == 'INSERT') {      //新增操作
            const menus = checkData.menu;
            console.log(menus);
            let menusID = new Array();
            for (let i = 0; i < menus.length; i++) {
                menusID[i] = menus[i].id;
            }
            console.log(menusID);
            const url = "/findAllMenusNotIds?ids=" + menusID;
            $.ajax({
                type: "GET",
                url: url,
                dataType: "json",
                headers:{auth:$.cookie('adminToken')},
                success: function (result) {
                    console.log(result);
                    if (result.code == 0) {
                        const data = result.data;
                        if (data != null && data.length > 0) {
                            for (let i = 0; i < data.length; i++) {
                                if(data[i].parentID==-1){
                                    $("#edit_navigate_menus").append("<input type='checkbox' name='edit_navigate_menus_checkbox' lay-filter='edit_navigate_menus_checkbox' value='" + data[i].id + "' title='" + data[i].name + "'>");
                                }else {
                                    $("#edit_operation_menus").append("<input type='checkbox' name='edit_operation_menus_checkbox' lay-filter='edit_operation_menus_checkbox' value='" + data[i].id + "' title='" + data[i].name + "'>");
                                }
                            }
                        }
                        form.render('checkbox');
                    } else {
                        const show = result.msg;
                        layer.alert(show);
                    }
                }
            });
        } else if (data.value == 'DELETE') {     //删除操作
            const menus = checkData.menu;
            if (menus.length > 0) {
                for (let i = 0; i < menus.length; i++) {
                    if(menus[i].parentID==-1){
                        $("#edit_navigate_menus").append("<input type='checkbox' name='edit_navigate_menus_checkbox' lay-filter='edit_navigate_menus_checkbox' value='" + menus[i].id + "' title='" + menus[i].name + "'>");
                    }else {
                        $("#edit_operation_menus").append("<input type='checkbox' name='edit_operation_menus_checkbox' lay-filter='edit_operation_menus_checkbox' value='" + menus[i].id + "' title='" + menus[i].name + "'>");
                    }
                }
                form.render('checkbox');
            }
        }
    });
}
function clearCheckBoxCache(){
    $("#edit_navigate_menus").empty();
    $("#edit_operation_menus").empty();
}