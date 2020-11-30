package cn.yhl.nettySimple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/*
自定义Handler 需要继承官方给的类（ *HandlerAdapter） 才能成为一个Handler

 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    //可以读取客户端的消息
    /*
    ChannelHandlerContext ctx, 上下文对象 （管道 通道 地址。。）
    Object msg 客户端发送的消息
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        boolean a = ctx.channel().equals(ctx.pipeline().channel());
        System.out.println("ctx.channel().equals(ctx.pipeline().channel())::"+a);

        ByteBuf msg1 = (ByteBuf) msg;
        System.out.println("客户端： "+msg1.toString());
        System.out.println("客户端： "+msg1.toString(CharsetUtil.UTF_8));
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello 客户端~~~~111111111", CharsetUtil.UTF_8));
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ctx.writeAndFlush(Unpooled.copiedBuffer("Runnable :客户端~~~~3333333333", CharsetUtil.UTF_8));
            }
        });ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ctx.writeAndFlush(Unpooled.copiedBuffer("Runnable :客户端2222~~~~3333333333", CharsetUtil.UTF_8));
            }
        });
        ctx.writeAndFlush(Unpooled.copiedBuffer("Runnable :客户端~~~~2222222222", CharsetUtil.UTF_8));
    }

    //消息读取完毕 调用的方法 通过ChannelHandlerContext 发消息写到管道 发回去
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //向客户端发送消息
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello 客户端~~~~", CharsetUtil.UTF_8));
    }

    //异常处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //关闭通道
        ctx.close();
    }
}
