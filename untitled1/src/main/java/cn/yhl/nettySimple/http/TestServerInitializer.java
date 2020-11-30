package cn.yhl.nettySimple.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class TestServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //向管道 加入处理器
        ChannelPipeline pipeline = ch.pipeline();
        //加入netty提供的http编解码器（httpServerCodec）
        pipeline.addLast("MyhttpServerCodec",new HttpServerCodec());
        //添加自定义的Handler
        pipeline.addLast("MyTestHttpServerHandler",new TestHttpServerHandler());
    }

}
