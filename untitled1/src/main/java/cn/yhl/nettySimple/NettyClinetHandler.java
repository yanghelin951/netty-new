package cn.yhl.nettySimple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class NettyClinetHandler extends ChannelInboundHandlerAdapter{
    //当通道就绪时 触发
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client:"+ctx);
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello server !!!!", CharsetUtil.UTF_8));

    }

    //读
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf msg1 = (ByteBuf) msg;

        System.out.println("服务器："+msg1.toString(CharsetUtil.UTF_8));
        System.out.println("服务器地址："+ctx.channel().remoteAddress());
    }

    //异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        ctx.close();
    }
}
