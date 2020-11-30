package cn.yhl.nettySimple.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    //读取客户端数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        //判断msg是不是HttpRequest 请求
        if (msg instanceof HttpRequest) {
            System.out.println("msg 类型=" + msg.getClass());
            System.out.println("客户端地址：" + ctx.channel().remoteAddress());
//            //获取到uri 过滤指定资源
//            HttpRequest httpRequest = (HttpRequest) msg;
//            Uri uri = new Uri(httpRequest.uri());
//            if("/favicon.ico".equals(uri.getPath())){
//                System.out.println("请求了favicon.ico 但不做响应");
//                return;
//            }

            //向浏览器发送消息
            ByteBuf byteBuf = Unpooled.copiedBuffer("I'm 服务器", CharsetUtil.UTF_16);
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
            //设置返回的类型
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            //设置返回的长度
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());
            //将构建好的response返回
            ctx.writeAndFlush(response);
        }
    }


}
