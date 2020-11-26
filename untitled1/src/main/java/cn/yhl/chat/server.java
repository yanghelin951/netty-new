package cn.yhl.chat;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class server {
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private final int PORT=6667;

    public server()  {
        //初始化
        try {
            selector=Selector.open();
            listenChannel=ServerSocketChannel.open();
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            listenChannel.configureBlocking(false);
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //监听
    public void listen(){
        while (true){
            try {
                int count = selector.select(1000);
                System.out.println("count:"+count);
                if(count>0){//有事件
                    //获取sleectionKey
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    //得到SelectionKey 遍历
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while(iterator.hasNext()){
                        SelectionKey selectionKey = iterator.next();
                        //注册事件
                         if(selectionKey.isAcceptable()){
                            //创建SocketChannel
                             SocketChannel socketChannel = listenChannel.accept();
                             System.out.println(socketChannel.hashCode());
                             //设置无阻塞连接
                             socketChannel.configureBlocking(false);
                             //将通道注册在selector中
                             socketChannel.register(selector,SelectionKey.OP_READ,ByteBuffer.allocate(1024));
                            
                             //上线提醒功能 拿到被selector管理的SocketChannel 在给这些SocketChannel 发送消息 除自己以外
                             Set<SelectionKey> keys = selector.keys();
                             Iterator<SelectionKey> iterator1 = keys.iterator();
                             while (iterator1.hasNext()){
                                 SelectionKey next = iterator1.next();
                                 //防止获得到ServerSocketChanne
                                 try{
                                     SocketChannel channel = (SocketChannel) next.channel();
                                     String msg="恭喜"+socketChannel.hashCode()+"玩家上线 在线人数："+(keys.size()-1);
                                     //发送消息 xxx上线
                                     ByteBuffer wrap = ByteBuffer.wrap(msg.getBytes());
                                     channel.write(wrap);
                                 }catch (ClassCastException e){

                                 }



                             }
                         }
                         //write  服务器向客户端发送消息
                        if(selectionKey.isWritable()){
                            //通过key获得Channel
                            SocketChannel channel = (SocketChannel) selectionKey.channel();
                            //把要写入的值放在ByteBuffer中 在写入SocketChannel

                        }
                        //read 客户端想服务器发送消息
                        if(selectionKey.isReadable()){

                        }


                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) {
        server server = new server();
        server.listen();

    }
}
