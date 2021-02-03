const accountID = $.cookie('accountID');

const accountToken = $.cookie('token');

if(accountID==null || accountToken==null||accountID.length<=0||accountID.length<=0){
    location.href="login.html"
}