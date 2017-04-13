package utility;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * Used to verify hostname to perform https request to local custom server.</br>
 * Found here: http://www.mkyong.com/webservices/jax-ws/java-security-cert-certificateexception-no-name-matching-localhost-found/
 */
public  class Verifier implements HostnameVerifier{
	public boolean verify(String arg0, SSLSession arg1) {
		return true;   // mark everything as verified
	}
}
