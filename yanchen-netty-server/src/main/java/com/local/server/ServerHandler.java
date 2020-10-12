package com.local.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yc
 * @version 1.0
 * @project yanchen
 * @description TODO
 * @date 2020-06-07 20:51
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {


    private static final ConcurrentHashMap<ChannelId,ChannelHandlerContext>  CHANNEL_MAP=new ConcurrentHashMap<>();


    /**
      * @Description 存在客户端连接执行
      * @Param [ctx]
      * @return void
      * @Author yc
      * @Date 2020-06-08 22:05
      * @version 1.0
      */

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        InetSocketAddress socketAddress = (InetSocketAddress)ctx.channel().remoteAddress();

        String host = socketAddress.getHostString();

        int port = socketAddress.getPort();


    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        super.channelRead(ctx, msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
