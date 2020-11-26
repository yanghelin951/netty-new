package cn.yhl.NIOBufferAndChannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * 离散聚合Demo
 * 通过Buffer数组实现  通过buffer数组可以依次读和写
 */
public class ScatteringAndGatheringTest {

    public static void main(String[] args) throws IOException {
        //ServerSocketChannel是一个可以监听新进来的TCP连接的通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //InetAddress:类的主要作用是封装IP及DNS，
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);
        //绑定端口到Socket并启动
        serverSocketChannel.socket().bind(inetSocketAddress);
        //创建buffer数组 用于存储数据
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0]=ByteBuffer.allocate(5);
        byteBuffers[1]=ByteBuffer.allocate(3);

        //等待连接
        SocketChannel socketChannel = serverSocketChannel.accept();

        int messageLength=8;

        while(true){
            int byteRead=0;
            while(byteRead<messageLength){
                long l = socketChannel.read(byteBuffers);
                byteRead+=l;
                Arrays.asList(byteBuffers).stream().map(buffer ->"postion"+buffer.position()+",limit="+buffer.limit()).forEach(System.out::println);
            }
            //将所有的buffer反转 filp
            Arrays.asList(byteBuffers).forEach(buffer-> buffer.flip());
            //将数据显示到客户端
            long byteWirte=0;
            while (byteWirte<messageLength){
                long l = socketChannel.read(byteBuffers);
                byteWirte+=l;
            }
            //将所有的buffer进行claer
            Arrays.asList(byteBuffers).forEach(buffer -> {
                buffer.clear();
            });
            System.out.println("byteRead:"+byteRead+"byteWrite:"+byteWirte+"messageLength:"+messageLength);
        }

    }
}
