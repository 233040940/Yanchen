$(function () {
    const url ="/user";
    //填充账号管理列表
    table.render({
        elem: '#account',
        url: url,
        //     headers:{auth:$.cookie('adminToken')},
        id:'allUserBills',
        where:{name:'yanchen',age:'20'},
        contentType:"application/json;charset=UTF-8",
        cols: [[
            {checkbox: true, fixed: true},
            {field: 'id', title: '账号ID'},
            {field: 'name', title: '名称'},
            {field: 'password', title: '密码'},
            {field: 'salt', title: '盐值'},
            {field: 'createTimeStamp', title: '创建时间',templet:function (data){return parseTimeStampToString(data.createTimeStamp)}},
            {field: 'updateTimeStamp',title: '修改时间',templet:function (data){return parseTimeStampToString(data.updateTimeStamp)}}
        ]],
        page: true,
        parseData:function(res){
            const code=res.code;
            if(code ==200){
                return {
                    "code":0,
                    "data":res.data,
                    "count":res.count
                }
            }else {
                const msg=res.msg;
                layer.alert(msg);
            }

        }

    });
    // table.on('checkbox(userBills)', function (obj) {
    //
    //     if (obj.checked == true && obj.type == "one") {
    //         checkData = obj.data;
    //         console.log(obj.data);
    //         checked = true;
    //
    //     } else {
    //         checked = false;
    //
    //     }
    //     // console.log(obj.checked); //当前是否选中状态
    //     // console.log(obj.data); //选中行的相关数据
    //     // console.log(obj.type); //如果触发的是全选，则为：all，如果触发的是单选，则为：one
    //     // console.log(table.checkStatus('table-organization').data); // 获取表格中选中行的数据
    //
    // });

    // laydate.render({
    //     elem:'#search_date',
    //     range:'~',
    //     format:'yyyy-MM-dd HH:mm:ss'
    // });
});