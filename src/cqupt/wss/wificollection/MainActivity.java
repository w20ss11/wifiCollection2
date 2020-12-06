package cqupt.wss.wificollection;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cqupt.wss.util.TvThread;

public class MainActivity extends Activity implements OnClickListener {

	TextView tv;
	TvThread thread = null;
	Handler mHandler;
	String time;
	boolean is_start_press = false;
	@SuppressLint("SdCardPath")
	final String filePath="/sdcard/Test/";
	@SuppressLint("HandlerLeak")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tv = (TextView) findViewById(R.id.textview);
		Button button_start = (Button) findViewById(R.id.start);
		Button button_stop = (Button) findViewById(R.id.stop);
		button_start.setOnClickListener(this);
		button_stop.setOnClickListener(this);
		//�����ļ���
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdir();
		}
		mHandler = new Handler(){  
			public void handleMessage(Message msg) {  
				super.handleMessage(msg);  
				switch(msg.what){  
				case 1:  
					String resPerSec1 = msg.obj.toString();
					tv.append(resPerSec1);
					break;  
				case 2:  
					String resPerSec2 = msg.obj.toString();
					tv.setText(resPerSec2);
					break;  
				default:  
					break;  
				}
			}  
		};
	}

	@SuppressLint({ "SimpleDateFormat", "ShowToast" })
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.start:
			if(!is_start_press){
				//�½��ÿ�ʼ������Ӧ��ʱ��txt
				tv.setText("");
				String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
				String fileName = filePath + time+".txt";
				File txtfile = new File(fileName);
				try {
					if (!txtfile.exists()) {
						txtfile.createNewFile();
						Toast.makeText(this, filePath + fileName+"����", 0).show();
					}
				} catch (Exception e) {
					System.err.println("error:"+e);
				}
				thread = new TvThread(this,txtfile,mHandler);
				thread.start();
				is_start_press = !is_start_press;
			}else {
				Toast.makeText(this, "���ȵ��ֹͣ", 0).show();
			}
			break;
		case R.id.stop:
			if(thread==null)
				Toast.makeText(this, "���ȵ����ʼ", 0).show();
			else{
				thread.stopThread();
				is_start_press = !is_start_press;
				Toast.makeText(this, "��ֹͣ�ɼ�wifi", 0).show();}
			break;
		}
	}
	
}
