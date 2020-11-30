package cn.yhl.nettySimple;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClinet {
    public static void main(String[] args) throws InterruptedException {
        //创建事件循环组
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();

        try {
            //创建客户端启动对象 Bootstrap
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(nioEventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyClinetHandler());
                        }
                    });

            //连接服务器
            ChannelFuture connect = bootstrap.connect("127.0.0.1", 6668);
            connect.channel().closeFuture().sync();
        }finally {
            nioEventLoopGroup.shutdownGracefully();
        }
    }
}
