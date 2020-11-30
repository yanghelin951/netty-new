package cn.yhl.nettySimple.NettyChar;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;

public class ServerHandler extends SimpleChannelInboundHandler {

    //定义一个Channel组 管理所有的Channel
    private static DefaultChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
        Channel channel = ctx.channel();
        //提示 上线
        channels.writeAndFlush("用户-"+channel.remoteAddress()+"  加入聊天！！！");
        channels.add(ctx.channel());
        System.out.println("handlerAdded-上线："+ctx.channel().remoteAddress());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
        //提示 下线
        channels.writeAndFlush("handlerRemoved--下线："+ctx.channel().remoteAddress());
        System.out.println("handlerRemoved--下线："+ctx.channel().remoteAddress());
    }
    //
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println(ctx.channel().remoteAddress()+"   上线channelActive");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        System.out.println(ctx.channel().remoteAddress()+"   下线channelInactive");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel channel = ctx.channel();
        //遍历channelGroup 不给自己发消息
        for (Channel a:
        channels) {
            if(!a.equals(channel)){
                a.writeAndFlush(channel.remoteAddress()+": "+msg+"\n");
            }else{
                a.writeAndFlush(":"+msg);
            }

        }

    }
}
