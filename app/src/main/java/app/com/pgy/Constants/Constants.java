package app.com.pgy.Constants;

import android.os.Environment;

/**
 * 创建日期：2017/11/22 0022 on 上午 11:23
 * 描述:静态变量
 *
 * @author 徐庆重
 */
public class Constants {

	/**上线时，将Debug设为false,确保logUtils里debug为false，
	 * 修改IM的APPkey在IMLib的Mainfest中*/
	public static final boolean DEBUG = false;

	/**若有SD卡，图片放在LOCAL目录下，否则放在ROOT目录下*/
	public static final String LOCAL_PHOTO_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/myphoto";
	public static final String ROOT_PHOTO_PATH = "/data/data/com/myphoto/";
	/**短连接host，测试、线上*/
	// release
	public static final String HTTP_URL = "http://47.56.87.149:5880";
	public static final String HTTP_URL_DEBUG = "http://47.56.87.149:5880";
	//debug
//	public static final String HTTP_URL = "http://192.168.1.1:8080";
//	public static final String HTTP_URL_DEBUG = "http://192.168.1.1:8080";
	/**长连接host，测试、线上*/
	// release http://47.104.142.76:1606
			//denbug  192.168.2.165:1606
	public static final String WS_URL_DEBUG = "ws://47.56.87.149:1606";//ws://45.249.244.103:1606
	public static final String WS_URL = "ws://47.56.87.149:1606";
	//debug
//	public static final String WS_URL_DEBUG = "ws://192.168.1.1:1606";//ws://45.249.244.103:1606
//	public static final String WS_URL = "ws://192.168.1.1:1606";
	/**长连接action*/
	public static final String SOCKET_ACTION = "com.pgy.socket";
	/**apk 文件存储路径*/
	public static final String SP_DOWNLOAD_PATH = "download.path";
}
