package cqupt.wss.util;

import java.io.File;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;//////////////////zhongidan
import android.os.Handler;
import android.os.Message;

public class TvThread extends Thread{
	private boolean stop = false;
	private WifiManager mWifiManager;
	File txtfile;
	Handler mHandler;
	SimpleDateFormat dateFormat;
	@SuppressLint({ "SdCardPath", "SimpleDateFormat" })
	public TvThread(Context context,File file,Handler mHandler) {
		this.mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		this.txtfile = file;
		this.mHandler = mHandler;
		dateFormat= new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
	}

	public void stopThread() {
		stop = true;
	}

	public void run() {
		int i=1;
		while (!stop) {
			mWifiManager.startScan();
			List<ScanResult> mWifiList=mWifiManager.getScanResults();
			String resPerSec = "";
			for(ScanResult scanResult:mWifiList){
				resPerSec = resPerSec+scanResult.SSID+","+scanResult.BSSID+","+scanResult.level+"##";
			}
			// 每次写入时，都换行写
			String strContent = resPerSec + dateFormat.format(new Date())+ "\r\n";
			try {
				RandomAccessFile raf = new RandomAccessFile(txtfile, "rwd");
				raf.seek(txtfile.length());
				raf.write(strContent.getBytes());
				raf.close();
				Message msg = new Message();
				msg.what = 1;
				if(i%30==0)
					msg.what = 2;
                msg.obj = (i++)+" "+resPerSec.substring(0,40)+"......\n";
                mHandler.sendMessage(msg);
				Thread.sleep(1000);  
			} catch (Exception e) {
				System.err.println("TestFile Error on write File:" + e);
			}
		}
		System.out.println("stopped!!!");
	}
}
