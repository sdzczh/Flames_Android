package app.com.pgy.Utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES1 {
	/** 填充模式 */  
    private static final String transformation = "AES/CBC/PKCS5Padding";
    private static final String IV_STRING = "xpLbp7JdqU49LJuz";
    /** 
     * 加密 
     *  
     * @param content 需要加密的内容 
     * @param password 加密密码 
     * @return 
     */  
    public static String encrypt(String content, String password) {  
        try {  
            IvParameterSpec zeroIv = new IvParameterSpec(IV_STRING.getBytes());
            SecretKeySpec key1 = new SecretKeySpec(password.getBytes(),"AES");  
            Cipher cipher = Cipher.getInstance(transformation);  
            cipher.init(Cipher.ENCRYPT_MODE, key1, zeroIv);  
            byte[] encryptedData = cipher.doFinal(content.getBytes());  
            //String encryptResultStr = parseByte2HexStr(encryptedData);  
            return BASE64.encoder(encryptedData);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
  
    /** 
     * 解密 
     *  
     * @param content 待解密内容 
     * @param password 解密密钥 
     * @return 
     */  
    public static String decrypt(String content, String password) {  
        try {  
              
            //byte[] decryptFrom = parseHexStr2Byte(content);  
        	byte[] decryptFrom = BASE64.decoderByte(content);
            IvParameterSpec zeroIv = new IvParameterSpec(IV_STRING.getBytes());
            SecretKeySpec key1 = new SecretKeySpec(password.getBytes(),"AES");  
            Cipher cipher = Cipher.getInstance(transformation);  
            cipher.init(Cipher.DECRYPT_MODE, key1, zeroIv);  
            byte decryptedData[] = cipher.doFinal(decryptFrom);  
             return new String(decryptedData);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
    
    
    public static void main(String[] args) {
		String str = "123";
		String key = "1504b9fd30878bd2";
		String enStr = AES2.encrypt(str, key);
		String deStr = AES2.decrypt(enStr, key);
		System.out.println("明文-->"+str);
		System.out.println("密文-->"+enStr);
		System.out.println("解密后-->"+deStr);
	}
      
    /**将二进制转换成16进制  
     * @param buf  
     * @return  
     */    
    public static String parseByte2HexStr(byte buf[]) {    
        StringBuffer sb = new StringBuffer();    
        for (int i = 0; i < buf.length; i++) {    
                String hex = Integer.toHexString(buf[i] & 0xFF);    
                if (hex.length() == 1) {    
                        hex = '0' + hex;    
                }    
                sb.append(hex.toUpperCase());    
        }    
        return sb.toString();    
    }    
      
    /**将16进制转换为二进制  
     * @param hexStr  
     * @return  
     */    
    public static byte[] parseHexStr2Byte(String hexStr) {    
        if (hexStr.length() < 1)    {
                return null;    }
        byte[] result = new byte[hexStr.length()/2];    
        for (int i = 0;i< hexStr.length()/2; i++) {    
                int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);    
                int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);    
                result[i] = (byte) (high * 16 + low);    
        }    
        return result;    
    }    
}
