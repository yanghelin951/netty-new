package cn.yhl.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class NIOClinet {
    public static void main(String[] args) throws IOException {
        //创建SocketChannel连接到服务器
        SocketChannel socketChannel = SocketChannel.open();
        //设置非阻塞
        socketChannel.configureBlocking(false);
        //设置端口和ip
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6667);
        //连接不成功等待一会
        if(!socketChannel.connect(inetSocketAddress)){
            while(!socketChannel.finishConnect()){
                System.out.println("正在连接，请稍等！！！！");
            }
        }
//        String str="hello 李一凡";
//        //创建Buffer
//        ByteBuffer wrap = ByteBuffer.wrap(str.getBytes());
//        //写入Channel
//        socketChannel.write(wrap);
//        System.in.read();


    }
}
