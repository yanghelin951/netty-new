package cn.yhl.NIOBufferAndChannel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

//完成文件的复制 （Buffer 和Channel） 只使用1给buffer
//先读取文件 在吧文件复制到第二个文件
public class NIOFileChannel04 {
    public static void main(String[] args) throws IOException {
        //读
        //创建一个输入流 ——>——>—>— Channel
        FileInputStream fileInputStream = new FileInputStream("D:\\NIOFileChannel04-1.txt");
        //通过输出流得到FileChnnel 储存数据
        FileChannel channel = fileInputStream.getChannel();
        //创建一个缓冲区
        ByteBuffer allocate = ByteBuffer.allocate(16);
        //读数据
        channel.read(allocate);


        //写
        //创建一个输出流 吧NIOFileChannel04-1.txt里面的数据写入NIOFileChannel04-2.txt
        FileOutputStream fileOutputStream2 = new FileOutputStream("D:\\NIOFileChannel04-2.txt");
        FileChannel channel2 = fileOutputStream2.getChannel();
        //对buf进行反转 开始写了
        allocate.flip();
        //将buf的数据写入通道Channel
        channel2.write(allocate);
        //关闭流
        fileInputStream.close();
        fileOutputStream2.close();
    }
}
