package com.local.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.stereotype.Component;

/**
 * @author yc
 * @version 1.0
 * @project yanchen
 * @description TODO
 * @date 2020-06-07 20:39
 */

@Component
public class NettyServerBootStrapProvider {


    private  int port;

    private  String host;

    private EventLoopGroup parent;
    private EventLoopGroup child;

    public NettyServerBootStrapProvider(String host,int port){

        this.port=port;
        this.host=host;
    }

    private ServerBootstrap init(){

         parent=new NioEventLoopGroup();

         child=new NioEventLoopGroup();

        ServerBootstrap serverBootstrap=new ServerBootstrap();

        serverBootstrap.group(parent,child)
                       .channel(NioServerSocketChannel.class)
                       .localAddress(host,port)
                       .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                       .option(ChannelOption.SO_REUSEADDR,true)     //允许多个进程监听同一个端口
                       .option(ChannelOption.SO_BACKLOG,1024)       //设置服务端处理请求队列长度
                       .childOption(ChannelOption.SO_KEEPALIVE,true)
                       .childOption(ChannelOption.TCP_NODELAY,true)
                       .childHandler(new ChannelInitializer<SocketChannel>() {
                           @Override
                           protected void initChannel(SocketChannel socketChannel) throws Exception {

                               socketChannel.pipeline().addLast(new StringDecoder());
                               socketChannel.pipeline().addLast(new StringEncoder());
                               socketChannel.pipeline().addLast(new ServerHandler());

                           }
                       });

        return  serverBootstrap;

    }


    private  void bind() throws InterruptedException {

        ChannelFuture channelFuture = init().bind(host, port).sync();
        channelFuture.channel().closeFuture().sync();

    }
    public void startUp(){

        try {
            bind();

            System.out.println(String.format("Netty Server startUp host:%s,port:%s",host,port));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {

            parent.shutdownGracefully();
            child.shutdownGracefully();
        }

    }



}
