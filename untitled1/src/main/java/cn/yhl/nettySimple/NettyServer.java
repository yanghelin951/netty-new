package cn.yhl.nettySimple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {
    public static void main(String[] args) throws InterruptedException {

        //bossGroup 处理注册
        //workerGroup 处理 read write 不处理业务
        //都是轮询的无线循环
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
        //创建服务器的启动对象，配置参数
        ServerBootstrap bootstrap = new ServerBootstrap();

        bootstrap.group(bossGroup,workerGroup)//设置2个线程组
                .channel(NioServerSocketChannel.class)//服务器通道实现
                .option(ChannelOption.SO_BACKLOG,128)//设置线程队列得到连接个数
                .childOption(ChannelOption.SO_KEEPALIVE,true)//设置保持活动连接状态
                .childHandler(new ChannelInitializer<SocketChannel>() {//给workerGroup 的EventLoop对应的管道设置处理器
                    //给pipeline 设置处理器对象
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new NettyServerHandler());
                    }
                });
        System.out.println("服务器准备好了！！！");
        //给服务器绑定端口  .sync同步  并启动
        ChannelFuture channelFuture = bootstrap.bind(6668).sync ();

        //对关闭通道进行监听
        channelFuture.channel().closeFuture().sync();

        }finally {
            //关闭
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
