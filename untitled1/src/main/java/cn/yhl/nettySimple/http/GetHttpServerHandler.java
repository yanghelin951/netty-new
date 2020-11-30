package cn.yhl.nettySimple.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.util.Map;

public class GetHttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest) throws Exception {
        System.out.println(fullHttpRequest);
        
        int count=0;
        RequestParser requestParser = new RequestParser(fullHttpRequest);

        Map<String, String> parse = requestParser.parse();
        for(String value :parse.values()){
            count+=Integer.valueOf(value);
        }
        //向浏览器发送消息
        ByteBuf byteBuf = Unpooled.copiedBuffer("相加为：" + count, CharsetUtil.UTF_16);
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);

        //设置返回的类型
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
        //设置返回的长度
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());
        //将构建好的response返回
        ctx.writeAndFlush(response);


    }

   

}
