package cn.yhl.NIOBufferAndChannel;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
/*
* 文件直接在内存中修改 FileChannel.map()
* */
public class MapperByteBufferDemo {
    public static void main(String[] args) throws Exception{
        RandomAccessFile randomAccessFile = new RandomAccessFile("C:\\Users\\PC\\IdeaProjects\\untitled1\\1.txt","rw");
        //通过流获得通道Channel
        FileChannel channel = randomAccessFile.getChannel();

        /*
            map方法有桑参数
            1.FileChannel.MapMode.READ_WRITE 使用读写模式
            2.  0 从下标0开始
            3.  5  映射内存的大小 就是1.txt文件映射到内存的大小5个字节
            可以直接修改的范围就是0-4 （0 1 2 3 4）
         */

        MappedByteBuffer map = channel.map(FileChannel.MapMode.PRIVATE, 0, 6);

        //操作文件
        map.put(3,(byte)'y');
        map.put(4,(byte)'h');
        map.put(5,(byte)'l');
        //关闭流
        randomAccessFile.close();
    }
}
