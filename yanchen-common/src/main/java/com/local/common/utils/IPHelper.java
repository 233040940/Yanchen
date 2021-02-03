package com.local.common.utils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.SocketAddress;

/**
 * @Create-By: yanchen 2020/10/17 14:58
 * @Description: ip工具类
 */
public class IPHelper {

    /**
     * @create-by: yanchen 2020/10/17 15:23
     * @description: 拆分socket地址
     * @param address   socket地址
     * @return: java.lang.String[]
     */
    private static final String[] split(SocketAddress address) {
        return null == address ? null : SplitterHelper.splitToArray(":", address.toString());
    }

   /**
    * @create-by: yanchen 2020/10/17 15:24
    * @description: 获取IP地址
    * @param address  socket地址
    * @return: java.lang.String
    */
    public static final String getIp(SocketAddress address) {
        String[] addressArray = split(address);
        return null != addressArray ? addressArray[0] : null;
    }

    /**
     * @create-by: yanchen 2020/10/17 15:25
     * @description: 获取端口号
     * @param address socket地址
     * @return: java.lang.String
     */
    public static final String getPort(SocketAddress address) {
        String[] addressArray = split(address);
        return null != addressArray ? addressArray[1] : null;
    }
    
    /**
     * @create-by: yanchen 2020/10/17 15:25
     * @description: 通过 HttpServletRequest获取客户端请求地址
     * @param request
     * @return: java.lang.String
     */      
    public static String getHttpIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (ip.equals("127.0.0.1")) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ip = inet.getHostAddress();
            }
        }
        // 多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return  ip;
    }
}
