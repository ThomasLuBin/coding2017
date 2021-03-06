package com.coderising.download;

import java.io.File;

import com.coderising.download.api.Connection;
import com.coderising.download.api.ConnectionManager;
import com.coderising.download.api.DownloadListener;


public class FileDownloader {
	
	String url;
	String path;
	int threadNum;//线程数
	int fileLength;//要下载的文件的大小
	File file;
	
	DownloadListener listener;
	
	ConnectionManager cm;
	
	public FileDownloader(){
		
	}
	public FileDownloader(String _url,String _path,int _threadNum) {
		this.url = _url;
		this.path=_path;
		this.threadNum=_threadNum;
	}
	
	
	public void execute(){
		// 在这里实现你的代码， 注意： 需要用多线程实现下载
		// 这个类依赖于其他几个接口, 你需要写这几个接口的实现代码
		// (1) ConnectionManager , 可以打开一个连接，通过Connection可以读取其中的一段（用startPos, endPos来指定）
		// (2) DownloadListener, 由于是多线程下载， 调用这个类的客户端不知道什么时候结束，所以你需要实现当所有
		//     线程都执行完以后， 调用listener的notifiedFinished方法， 这样客户端就能收到通知。
		// 具体的实现思路：
		// 1. 需要调用ConnectionManager的open方法打开连接， 然后通过Connection.getContentLength方法获得文件的长度
		// 2. 至少启动3个线程下载，  注意每个线程需要先调用ConnectionManager的open方法
		// 然后调用read方法， read方法中有读取文件的开始位置和结束位置的参数， 返回值是byte[]数组
		// 3. 把byte数组写入到文件中
		// 4. 所有的线程都下载完成以后， 需要调用listener的notifiedFinished方法
		
		// 下面的代码是示例代码， 也就是说只有一个线程， 你需要改造成多线程的。
		Connection conn = null;
		try {
			conn = cm.open(this.url);//打开网络连接
			
			file=new File(path);
			if(!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
			}
			if(file.exists()){
				file.delete();
			}
			file.createNewFile();//创建 文件
			
			
			fileLength = conn.getContentLength();//获取要下载的文件的大小		
			System.out.println("文件总长度："+fileLength+"字节");
			
			int blockSize=fileLength/threadNum; //每个线程平均下载的块的大小
			
			for(int i=1;i<=threadNum;i++){
				int startPos=(i-1)*blockSize;
				int	endPos=i*blockSize-1;
				if(i==threadNum){
					endPos=fileLength;
				}
				System.out.println("线程"+i+"下载"+startPos+"字节~"+endPos+"字节");
				
				new Thread(new DownloadThread(conn,startPos,endPos,file)).start();
				
			}
			
			
		} catch (Exception e) {			
			e.printStackTrace();
		}finally{
			if(conn != null){
				conn.close();
			}
		}
		
		while(file.length()<fileLength){
			
		}
		listener.notifyFinished();

	}
	
	public void setListener(DownloadListener listener) {
		this.listener = listener;
	}

	public void setConnectionManager(ConnectionManager ucm){
		this.cm = ucm;
	}
	
	public DownloadListener getListener(){
		return this.listener;
	}
	
}
