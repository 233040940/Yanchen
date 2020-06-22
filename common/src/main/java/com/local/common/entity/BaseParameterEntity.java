package com.local.common.entity;

/**
 * @author yc
 * @version 1.0
 * @project yanchen
 * @description TODO 普通的请求参数实体
 * @date 2020-06-02 20:30
 */
public class BaseParameterEntity {

    private String account;         //账号

    private String accountIP;       //登录账号所在ip

    private String host;            //请求接口主机

    private String port;            //请求接口端口

    private String interfaceName;   //请求接口名称

    private String params;          //请求接口参数

    public BaseParameterEntity(String account, String accountIP, String host, String port, String interfaceName, String params) {
        this.account = account;
        this.accountIP = accountIP;
        this.host = host;
        this.port = port;
        this.interfaceName = interfaceName;
        this.params = params;
    }

    public String getAccount() {
        return account;
    }

    public String getAccountIP() {
        return accountIP;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public String getParams() {
        return params;
    }
}
