package cn.yhl.NIOSelector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

//服务器
// Selector（选择器）是Java NIO中能够检测一到多个NIO通道，
// 并能够知晓通道是否为诸如读写事件做好准备的组件。这样，一个单独的线程可以管理多个channel，从而管理多个网络连接。
public class NIOServer {
    public static void main(String[] args) throws IOException {
        //先创建ServerSocketChannel ->SocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //创建Selector对象
        Selector selector = Selector.open();
        //ServerSocketChannel绑定端口6666，在服务器监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        //设置非阻塞模式
        serverSocketChannel.configureBlocking(false);
        //吧serverSocketChannel注册到Selector中 关心连接事件（Accept）
        SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);//注册事件


        //循环等待连接
        while(true){

            //selector.select(1000)在一秒内接收通道的事件 有事件就返回>0 无事件=0
            if(selector.select(2000)==0){ //监听事件 selector.select
                System.out.println("无连接");
                continue;
            }
            //处理事件
            //有事件就获取相关的Selectionkey 看是什么事件
            //通过selector.selectedKeys()拿到集合 遍历出selectedKey事件
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while(iterator.hasNext()){
                //拿到selectedKey
                SelectionKey key = iterator.next();
                //根据key 对应的通带发生的事件做对应的处理 获得对应的SocketChannel 新连接就创建 其他事件就通过SelectionKey反向获得

                //连接事件
               if(key.isAcceptable()){//Accept，有新的客户端连接
                   //该客户端生成一个SocketChannel 并注册到Selector中
                   SocketChannel socketChannel = serverSocketChannel.accept();
                   System.out.println("客户端连接成功"+socketChannel.hashCode());
                   //将socketChannel设为非阻塞 设置之后，就可以在异步模式下调用connect(), read() 和write()了
                   socketChannel.configureBlocking(false);
                   SelectionKey register = socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
               }
               //读事件
                if(key.isReadable()){
                    //通过SelectionKey反向获得对应的Channel 从而获得buffer类容
                    SocketChannel channel = (SocketChannel)key.channel();
                    //通过key获得buffer
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    //读
                    channel.read(buffer);
                    System.out.println("from 客户端 "+new String(buffer.array()));
                }
                //操作完后删除key 以防重复操作
                iterator.remove();
            }

        }
    }
}
