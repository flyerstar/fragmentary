package net.pers.yothin.action;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownLoadThread extends Thread {

	private int startIndex;
	private int endIndex;
	private int threadId;
	private String path;
	
	public DownLoadThread(int startIndex,int endIndex,int threadId,String path){
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.threadId = threadId;
		this.path = path;
	}
	
	public void run(){
		URL url;
		try{
			url = new URL(path);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(8000);
			connection.setReadTimeout(8000);
			//在这里用HttpURLConnection的设置请求属性的方法，来请求获取每一段的信息（在这里的格式书写是一样的，固定的）
			connection.setRequestProperty("Range", "bytes="+startIndex+"-"+endIndex);
			
			//这里的206是代表上面的区段请求的响应的返回码
			if(connection.getResponseCode()==206){
				InputStream is=connection.getInputStream();
				
				File file=new File("QQ8.9.2.zip");
				RandomAccessFile ref=new RandomAccessFile(file,"rwd");
				ref.seek(startIndex);//标志开始位置，可以让线程在文件中从不同位置开始存储
				
				byte[] b=new byte[1024];
                int len=0;
                int total = 0;
                
                while((len=is.read(b))!=-1){

                    ref.write(b, 0, len);

                    total+=len;
                    System.out.println("第"+threadId+"条线程的下载"+total);

                }
                ref.close();
                System.out.println("第"+threadId+"条线程的下载结束");
				
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		super.run();
	}
}
