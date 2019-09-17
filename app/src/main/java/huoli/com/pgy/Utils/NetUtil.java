package huoli.com.pgy.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class NetUtil {

    private  static  final String WIFI= Context.WIFI_SERVICE;
    private  static  final String CONNECTIVY= Context.CONNECTIVITY_SERVICE;
	/**
	 * 启动 Wifi
	 */
	public static  void  setWifiEnable(Context context){

		WifiManager wifiManager = (WifiManager)context.getSystemService(WIFI);
		if (!wifiManager.isWifiEnabled()) {
 			wifiManager.setWifiEnabled(true);
		}
	}

	public static void setWifiUnEnable(Context context){
		WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI);
		if (!wifiManager.isWifiEnabled()) {
 			wifiManager.setWifiEnabled(false);
		}
	}



	/**
	 * 获取本机的MAC地址
	 *
	 * @return 本机MAC地址
	 */
	public static String getLocalMacAddress(Context context) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI);
		if (!wifiManager.isWifiEnabled()) {
//			wifiManager.setWifiEnabled(true);
			return "null";
		}
		String macAddress = wifiManager.getConnectionInfo().getMacAddress();
		if (null == macAddress) {
			return "null";
		} else {
			return macAddress.replace(":", "").toLowerCase();
		}

	}
	/**
	 * 当前网络是否有网络链接
	 *
	 * @return 是或不是
	 */
	public static boolean isNetworkAvailable(Context context) {
		boolean isNetworkOK = false;
		try {
			ConnectivityManager conn = (ConnectivityManager)context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
            isNetworkOK = !(null == conn || null == conn.getActiveNetworkInfo());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isNetworkOK;
	}
	/**
	 * 当前网络是不是有网络连接
	 *
	 * @return 是或不是
	 */
	public static boolean isHasNetWork(Context context) {
		NetworkInfo localNetworkInfo = ((ConnectivityManager) context
				.getSystemService(CONNECTIVY)).getActiveNetworkInfo();
		return (localNetworkInfo != null);
	}
	/**
	 * 当前网络是不是手机状态
	 *
	 * @param paramContext
	 *            Context上下文对象,获取Manager
	 * @return 是或不是
	 */
	public static boolean isNetWork(Context paramContext) {
		NetworkInfo localNetworkInfo = ((ConnectivityManager) paramContext
				.getSystemService(CONNECTIVY)).getActiveNetworkInfo();
		return (localNetworkInfo != null) && (localNetworkInfo.getType() == 0);
	}

	/**
	 * 当前网络是不WIFI网络
	 * @return 是或不是
	 */
	public static boolean isWifi(Context context) {
		NetworkInfo localNetworkInfo = ((ConnectivityManager) context
				.getSystemService(CONNECTIVY)).getActiveNetworkInfo();
		return (localNetworkInfo != null) && (localNetworkInfo.getType() == 1);
	}

	/**
	 * 通过ping网址,判断是否有外网链接,子线程调用,会阻塞
	 *
	 * @return 能否联网
	 */
	public static boolean ping(Context context) {
		String result = null;
		try {
			String ip = "www.baidu.com";// ping 的地址，可以换成任何一种可靠的外网
			Process p = Runtime.getRuntime()
					.exec("/system/bin/ping -c 1 " + ip);
			// // 读取ping的内容，可以不加
			// InputStream input = p.getInputStream();
			// BufferedReader in = new BufferedReader(new
			// InputStreamReader(input));
			// StringBuffer stringBuffer = new StringBuffer();
			// String content = "";
			// while ((content = in.readLine()) != null) {
			// stringBuffer.append(content);
			// }
			// // ping的状态
			int status = p.waitFor();
			if (status == 0) {
				result = "success";
				return true;
			} else {
				result = "failed";
			}
		} catch (IOException e) {
			result = "IOException";
		} catch (InterruptedException e) {
			result = "InterruptedException";
		} finally {
			Log.i("god", "result = " + result);
		}
		return false;
	}

	/**
	 * 获取本机IP地址
	 * @return 本机IP地址
	 */
	public static String getLocalIpAddress(){
			  try {
			    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				      NetworkInterface intf = en.nextElement();
				      for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
				        InetAddress inetAddress = enumIpAddr.nextElement();
				        if (!inetAddress.isLoopbackAddress()&& (inetAddress instanceof Inet4Address)) {
				        return inetAddress.getHostAddress().toString().replace(".", "%2E");
				        }
				      }
				 }
			  } catch (SocketException ex) {

			  }
			  return null;
	}
	/**
	 * 获取本机wifi IP地址

	 * @return 本机IP地址
	 */
	public static String getLocalWifiIpAddress(Context context) {
		String ip = "";
		int ipAddress= ((WifiManager) context.getSystemService(WIFI))
				.getConnectionInfo().getIpAddress();
//		ip = (ipAddress & 0xFF ) + "%2E" +
//				((ipAddress >> 8 ) & 0xFF) + "%2E" +
//				((ipAddress >> 16 ) & 0xFF) + "%2E" +
//				( ipAddress >> 24 & 0xFF) ;
		ip = (ipAddress & 0xFF ) + "." +
				((ipAddress >> 8 ) & 0xFF) + "." +
				((ipAddress >> 16 ) & 0xFF) + "." +
				( ipAddress >> 24 & 0xFF) ;
		return ip;
	}

	public static String getIpAddress(Context context){
		if (isWifi(context)){
			return getLocalWifiIpAddress(context);
		}else {
			return getLocalIpAddress();
		}
	}
}
