package net.pers.yothin.main;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import net.pers.yothin.action.DownLoadThread;

public class Main {

	public static final String PATH = "http://psoft.xpgod.com:801/small/qqzuixinban_xpgod.zip";//资源路径
	public static int threadCount = 4;//进行下载的线程量
	
	public static void main(String[] args) {
		try{
			System.out.println("begin");
			URL url = new URL(PATH);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            System.out.println(connection.getResponseCode());
            
            if(connection.getResponseCode()==200){
            	int length=connection.getContentLength();
            	
            	//在这里先占个位置，也就是创建一个文件的大小的区间
                File file=new File("qqzuixinban_xpgod.zip");
                
                //随机文件，让该文件拥有需下载的文件的大小，从而达到占位置的作用
                //在这里的第二个参数一班为rwd，不仅让他可读可写，并且会将数据刻在硬件上
                //RandomAccessFile的唯一父类是Object，与其他流父类不同。是用来访问那些保存数据记录的文件的，这样你就可以用seek( )方法来访问记录，并进行读写了。这些记录的大小不必相同；但是其大小和位置必须是可知的。
                RandomAccessFile ref=new RandomAccessFile(file,"rwd"); 
                ref.setLength(length);

                ref.close();

                int size=length/threadCount;
                
                for(int id=0;id<threadCount;id++){
                    //1.确定每个线程的下载区间
                    //2.确定完后，开始对应的子线程


                    ///在这里确定开始位置和结束位置
                    int startIndex=id*size;
                    int endIndex=(id+1)*size-1;

                    ///将最后一个线程的结束为止拿出单独，将剩余的长度全给最后一个
                    if(id==threadCount-1){
                        endIndex=length-1;
                    }

                    System.out.println("第"+id+"个线程的下载区间为"+startIndex+"-"+endIndex);

                    new DownLoadThread(startIndex, endIndex, id, PATH).start();
                }
            }
            
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
