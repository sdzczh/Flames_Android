package huoli.com.pgy.Utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

/**
 * @描述 RSA(非对称)加密解密<br>
 * @author 陈之晶
 * @版本 v1.0.0
 * @日期 2017-8-5
 */
public class RSA {
	private RSA(){}

	/**
	 * @描述 生成公钥私钥<br>
	 * @return MAP<String,Object>(){"public":公钥对象,"private":私钥对象}
	 * @throws Exception
	 * @author 陈之晶
	 * @版本 v1.0.0
	 * @日期 2017-8-5
	 */
	public static Map<String, Object> getKey() throws Exception{
		HashMap<String, Object> map = new HashMap<String, Object>();
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
		keyPairGen.initialize(1536);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		map.put("public", publicKey);
		map.put("private", privateKey);
		return map;
	}

	/**
	 * @描述 获取公钥对象<br>
	 * @param publicKey 公钥Base64解码后字节数组
	 * @return 公钥对象
	 * @throws Exception
	 * @author 陈之晶
	 * @版本 v1.0.0
	 * @日期 2017-8-5
	 */
	public static RSAPublicKey getPublikKey(byte[] publicKey) throws Exception{
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
		KeyFactory factory = KeyFactory.getInstance("RSA");
		PublicKey p = factory.generatePublic(keySpec);
		return (RSAPublicKey) p;
	}

	/**
	 * @描述 获取私钥对象<br>
	 * @param privateKey 私钥Base64解码后字节数组
	 * @return 私钥对象
	 * @throws Exception
	 * @author 陈之晶
	 * @版本 v1.0.0
	 * @日期 2017-8-5
	 */
	public static RSAPrivateKey getPrivateKey(byte[] privateKey) throws Exception{
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
	}

	/**
	 * @描述 公钥加密<br>
	 * @param data 需要加密字符串 (<=60汉字) 字节长度<=180bytes的字符串(编码：UTF-8中，一个汉字=3bytes)
	 * @param publicKey 公钥对象
	 * @return 加密后字符串(Base64编码)
	 * @author 陈之晶
	 * @throws Exception
	 * @版本 v1.0.0
	 * @日期 2017-8-5
	 */
	public static String encode(String data,RSAPublicKey publicKey) throws Exception{
		Cipher cipher = Cipher.getInstance("RSA/None/NoPadding",new BouncyCastleProvider());
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		String result = BASE64.encoder(cipher.doFinal(data.getBytes()));
		return result;
	}

	/**
	 * @描述 私钥解密<br>
	 * @param data Base64解码后的字节数组
	 * @param privateKey 私钥对象
	 * RSA/None/NoPadding
	 * @return 明文
	 * @author 陈之晶
	 * @throws Exception
	 * @版本 v1.0.0
	 * @日期 2017-8-5
	 */
	public static String decode(byte[] data,RSAPrivateKey privateKey) throws Exception{
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding",new BouncyCastleProvider());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		String result =new String(cipher.doFinal(data));
		return result;
	}

	/**
	 * @描述 测试方法<br>
	 * @param data 明文  可接收NULl值，默认:测试中华人民共和国一测试中华人民共和国二测试中华人民共和国三测试中华人民共和国四测试中华人民共和国五测试中华人民共和国六
	 * @throws Exception
	 * @author 陈之晶
	 * @版本 v1.0.0
	 * @日期 2017-8-5
	 */
	public static void test(String data) throws Exception {

		if(data==null||"".equals(data)){
			data = "测试中华人民共和国一测试中华人民共和国二测试中华人民共和国三测试中华人民共和国四测试中华人民共和国五测试中华人民共和国六";//60
		}

		System.out.println("【明文】:"+data);

		Map<String, Object> map = RSA.getKey();
		RSAPublicKey publicKeyObj = (RSAPublicKey) map.get("public");
		RSAPrivateKey privateKeyObj = (RSAPrivateKey) map.get("private");
		System.out.println("【公钥对象】:"+publicKeyObj);
		System.out.println("-----------");
		System.out.println("【私钥对象】:"+privateKeyObj);
		System.out.println("===================================");

		String pubKey = BASE64.encoder(publicKeyObj.getEncoded());
		String priKey = BASE64.encoder(privateKeyObj.getEncoded());
		System.out.println("【公钥字符串】:"+pubKey);
		System.out.println("-----------");
		System.out.println("【私钥字符串】:"+priKey);
		System.out.println("===================================");




		RSAPublicKey publicKey = RSA.getPublikKey(BASE64.decoderByte(pubKey));
		RSAPrivateKey privateKey = RSA.getPrivateKey(BASE64.decoderByte(priKey));
		System.out.println("【重新获取公钥对象】:"+publicKey);
		System.out.println("-----------");
		System.out.println("【重新获取私钥对象】:"+privateKey);
		System.out.println("===================================");

		String enStr = RSA.encode(data, publicKey);
		System.out.println("【密文】:"+enStr);
		System.out.println("===================================");

		String deStr = RSA.decode(BASE64.decoderByte(enStr), privateKey);
		System.out.println("【解密明文】:"+deStr);
		System.out.println("===================================");
		System.out.println("*******************************************************");
	}

	public static void main(String[] args) throws Exception {

		String pk="MIHfMA0GCSqGSIb3DQEBAQUAA4HNADCByQKBwQCqLArc5UaxxdCb6A94KHinCQpj+m1VDFpnuEhSagVL38eUXifQaoreEKuXe8NUI2uWQoHNoJCaqR4B6Xqlp3ybMNKaKeB+DXiRt5ncpsTFMYacGLrtrpTivfEG+y8bISl2YdYyjQV0bJjGS0yaJTrspAixo5WV2t0zMAZn0rDNFz7cN805UjG9LV1r3DdMZbQOpFPYNzv4497FN5OOeYOjDBnxCdWSCwEpj0e/Nr5+E5M8lvsle52BhlAWTEYfXVkCAwEAAQ==";
		RSAPublicKey publicKey = RSA.getPublikKey(BASE64.decoderByte(pk));
		String data = "321";
		String enStr = RSA.encode(data, publicKey);
		System.out.println(enStr);
		System.out.println("----------------------");
		String prk="MIIDlwIBADANBgkqhkiG9w0BAQEFAASCA4EwggN9AgEAAoHBALOwXELi08uvwD321v28wcL7eNCBW69tsbLbvfobtkQ3CPe1y0LLox2pgEXBeX27V6qbjOLvlqKCq1Fq9El/YBf8D0bb1eZ2VsGgr1uL/w7jsksFipYeMatYfa1oY132lR81nIOCRk4TRXhA0uoEjpwtmzvm/g5qht8pzab1ElvJMgMGYGDM5vEv+nzR6ZyhH3eYqwhUY//IKadA+9vwqyRhLJZ1avvy5KEEVBmV0rlFd0pTN970RHns5CXjwVwIDAQABAoHAF6ljnNMtvKa6E+7LRMJ0aW6f7CT8o884FStmiubXS4hLwALKb1NNeXx2jbj6fJoM40R0evW9ykyWrrP80yjYyI/CmzwboKt4pSq5rL50qctGESb7puT2+W8/rXQPkbIEnHcDgEAT/K1FhDd1EyTYei0j6F2YRr1RMMRzjZIuLoWVcBWOzXurLwB3GTrz58sQ6mHYDQLORlVrHfUsdBd/T21cnl5ddDL9skHy85CWNQ2cqYA/jPm2x1MbISUlGJNRAmEA4XW91yfa1Ru5xdcYBBVMgROqN0XyNWZ5QlOk6A0VE9kYJ4+UhOM/4Wg/OEdHFZcRxirzv0dEpJ/wJ0Rq/8qroafQKrDgFeX+4D7ypcRoKicSLe1WK/Ic6IpEE7mIB9t9AmEAzAdrmVJI69kLn4SRO22ygIENCRfrn2ukTZXHY0/SEcdI2tdbGuesy+tidn5/kuxtejykao5vWEa1ZROkt4m6vnWQGotEU2mKYebNj1go1RYlkXc32XwqiT445/6FfireantjAmB0aMSiSJO2oOAkhFCEGzMrGioy68yJQeWuF1336nlNjDPqiTnX+zXKfnbV6geL4DpvJVJBGt0YWvQ2ch8E/3Hqv6i9wiGRbPVSd231pRlbWl0KlZZ+jj3a2zkXLh5MYEkCYQCOq9GuEUA0zD24enckW30rJxL6fOOjQx5NsqMO6042aGilv1pR4hePMTZVLff/wZx8kdNXP+rUi/1khOv1/DW94W+3YBOTUIYYF9E+n6NN/oKeygMf7hfAiOY6NFzGc4kCYQDTpSXhlAB00LhAJCtdjvge6grroCJQo16IlFAk1yDwej65KI9S9pjdtJTby8vWteNGUWMB5ZFfAiXKuIw5bssTGEB1fNhO0r40tqrTEzaI26W58Jk8Disl1IE3yT40YXk=";
		RSAPrivateKey privateKey = RSA.getPrivateKey(BASE64.decoderByte(prk));
		String md="STImSODb0RytzB8INw2jx5RkjMH569PP0knT1fTfuffIK+rhFlmILsTkpq9E6zSfJR6NTlCFIM/oWLx8cLYShOb743osklpSt2MyZBha4Q1Dqp4+LJE6vGlFA0vghTHpt1cxC7EVcnYIusskpxPOzFsOJShkLUV7hLzc+Oz9WUNTQZATCsmnO6nkbg6bI9Lze2JA/8QerEcwGH1WlzB2nNO1VjeH3S/CCrCGy7XqjmIz9fvJvgGjcOflxz22SHg+";
		String deStr = RSA.decode(BASE64.decoderByte(md), privateKey);
		System.out.println("【解密明文】:"+deStr);

//		test(null);
	}
}
