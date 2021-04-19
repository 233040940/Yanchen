let layer;
let table;
let element;
let form;
let laydate;

$(function () {
    layui.use(['layer','table','element','form','laydate'],function () {
        layer=layui.layer;
        table=layui.table;
        element=layui.element;
        form=layui.form;
        laydate=layui.laydate;
    });
    const accountID = $.cookie('account');
    $("#accountID").text(accountID);

});

function parseTimeStampToString(timeStamp) {
    if(timeStamp!=null&&timeStamp!=undefined){
        const date = new Date(timeStamp);
        const year = date.getFullYear() + '-';
        const month = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
        let day = date.getDate()
        if(day <= 9){
            day='0'+day+' ';
        }else {
            day=day+' ';
        }
        const hour = date.getHours() + ':';
        const minute = date.getMinutes() + ':';
        const second = date.getSeconds();
        return year+month+day+hour+minute+second;
    }else{
        return "";
    }
}

function parseBooleanToString(bool) {

    if(bool!=null){
        if(bool==true){
            return "是";
        }else {
            return "否";
        }
    }else{
        return "";
    }
}


function isNull(value) {
    if(value==null){
        return "";
    }
    return value;
}
function responseBody(res){
    if(res.code==0){
        return {
            "code":0,
            "data":res.data,
            "count":res.count
        }
    }
}


