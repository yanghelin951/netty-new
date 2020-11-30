package cn.yhl.nettySimple.NettyChar;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

public class GroupChatClient1 {
    private  int port;
    private  String host;

    public GroupChatClient1(String host, int port) {
        this.port = port;
        this.host = host;
    }

    public void run() throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("decoder", new StringDecoder());
                            pipeline.addLast("Encoder", new StringEncoder());
                            pipeline.addLast(new GroupChatClienthandler());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            System.out.println("客户端"+channelFuture.channel().remoteAddress()+"准备好了！！！！！！！");

            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine())
            {
                String msg=scanner.nextLine();
                channelFuture.channel().writeAndFlush(msg+"\r\n");
            }
        }finally {
            group.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws InterruptedException {
        GroupChatClient1 localhost = new GroupChatClient1("127.0.0.1", 7000);
        localhost.run();

    }
}
