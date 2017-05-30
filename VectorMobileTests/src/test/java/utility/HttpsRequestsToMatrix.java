package utility;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpsRequestsToMatrix {
	/**
	 * Logs a user and returns his fresh access token.
	 * @param user
	 * @param password
	 * @return
	 * @throws IOException
	 */
	public static String login(String user, String password) throws IOException{ 
		String initialDeviceDisplayName="https://riot.im/app/ via Chrome on Mac OS";
		String accessToken;
		StringBuilder url=new StringBuilder(MatrixUtilities.getHomeServerUrlForRequestToMatrix(false)).append("/_matrix/client/r0/login?");
		System.out.println(url);
		String postJsonData = "{\"initial_device_display_name\":\""+initialDeviceDisplayName+"\",\"password\":\""+password+"\",\"type\":\"m.login.password\",\"user\":\""+user+"\"}";		  
		String reponseStr=executeUrl(url.toString(), "POST", postJsonData).toString();
		accessToken=reponseStr.substring(reponseStr.indexOf("access_token\":")+15,reponseStr.indexOf("\",\"home_server"));
		return accessToken;
	}
	
	public static void sendMessageInRoom(String accessToken, String roomId, String message) throws IOException{  
		int token = 1 + (int)(Math.random() * ((10000 - 1) + 1));
		StringBuilder url=new StringBuilder(MatrixUtilities.getHomeServerUrlForRequestToMatrix(false)).append("/_matrix/client/r0/rooms/").append(roomId).append("/send/m.room.message/").append(token).append("?access_token=").append(accessToken);
		String postJsonData = "{\"msgtype\":\"m.text\",\"body\":\""+message+"\"}";		  
		executeUrl(url.toString(), "PUT", postJsonData);
	}

	public static void sendInvitationToUser(String accessToken, String roomId, String invitedUserAdress) throws IOException{
		//https://matrix.org/_matrix/client/r0/rooms/!ECguyzzDCnAZarUOSW%3Amatrix.org/invite?access_token=MDAxOGxvY2F0aW9uIG1hdHJpeC5vcmcKMDAxM2lkZW50aWZpZXIga2V5CjAwMTBjaWQgZ2VuID0gMQowMDI1Y2lkIHVzZXJfaWQgPSBAamVhbmdiOm1hdHJpeC5vcmcKMDAxNmNpZCB0eXBlID0gYWNjZXNzCjAwMWRjaWQgdGltZSA8IDE0NzU1OTQxNjYwNTAKMDAyZnNpZ25hdHVyZSDofV-Ok8f6xSEPDNnKuZ9tM8YO_TXiwoKcfuvQrDLilwo
		StringBuilder url=new StringBuilder(MatrixUtilities.getHomeServerUrlForRequestToMatrix(false)).append("/_matrix/client/r0/rooms/").append(roomId).append("/invite").append("?access_token=").append(accessToken);
		String postJsonData = "{\"user_id\":\""+invitedUserAdress+"\"}";
		executeUrl(url.toString(), "POST", postJsonData);
	}

	public static void joinRoom(String accessToken, String roomId) throws IOException{
		StringBuilder url=new StringBuilder(MatrixUtilities.getHomeServerUrlForRequestToMatrix(false)).append("/_matrix/client/r0/join/").append(roomId).append("?access_token=").append(accessToken);
		executeUrl(url.toString(), "POST", null);
	}

	public static void leaveRoom(String accessToken, String roomId) throws IOException{
		StringBuilder url=new StringBuilder(MatrixUtilities.getHomeServerUrlForRequestToMatrix(false)).append("/_matrix/client/r0/rooms/").append(roomId).append("/leave").append("?access_token=").append(accessToken);
		executeUrl(url.toString(), "POST", null);
	}
	
	public static void forgetRoom(String accessToken, String roomId) throws IOException{
		StringBuilder url=new StringBuilder(MatrixUtilities.getHomeServerUrlForRequestToMatrix(false)).append("/_matrix/client/r0/rooms/").append(roomId).append("/forget").append("?access_token=").append(accessToken);
		executeUrl(url.toString(), "POST", null);
	}
	
	public static void kickUser(String accessToken, String roomId, String kickedUserAdress) throws IOException{
		//https://matrix.org/_matrix/client/r0/rooms/!ECguyzzDCnAZarUOSW%3Amatrix.org/invite?access_token=MDAxOGxvY2F0aW9uIG1hdHJpeC5vcmcKMDAxM2lkZW50aWZpZXIga2V5CjAwMTBjaWQgZ2VuID0gMQowMDI1Y2lkIHVzZXJfaWQgPSBAamVhbmdiOm1hdHJpeC5vcmcKMDAxNmNpZCB0eXBlID0gYWNjZXNzCjAwMWRjaWQgdGltZSA8IDE0NzU1OTQxNjYwNTAKMDAyZnNpZ25hdHVyZSDofV-Ok8f6xSEPDNnKuZ9tM8YO_TXiwoKcfuvQrDLilwo
		StringBuilder url=new StringBuilder(MatrixUtilities.getHomeServerUrlForRequestToMatrix(false)).append("/_matrix/client/r0/rooms/").append(roomId).append("/kick").append("?access_token=").append(accessToken);
		String postJsonData = "{\"user_id\":\""+kickedUserAdress+"\"}";
		executeUrl(url.toString(), "POST", postJsonData);
	}

	/**
	 * TODO : write this function
	 * @param accessToken
	 * @param roomId
	 */
	public static void uploadPicture(String accessToken, String roomId){

	}

	public static void sendPicture(String accessToken, String roomId, String pictureUrl) throws IOException{
		int token = 1 + (int)(Math.random() * ((10000 - 1) + 1));
		StringBuilder url=new StringBuilder(MatrixUtilities.getHomeServerUrlForRequestToMatrix(false)).append("/_matrix/client/r0/rooms/").append(roomId).append("/send/m.room.message/").append(token).append("?access_token=").append(accessToken);
		String postJsonData="{\"body\":\"small_koala.jpg\",\"info\":{\"size\":29051,\"mimetype\":\"image/jpeg\",\"w\":205,\"h\":154},\"msgtype\":\"m.image\",\"url\":\""+pictureUrl+"\"}";
		executeUrl(url.toString(), "PUT", postJsonData);
	}
	
	/**
	 * Create a room and return the roomId of the created room.
	 * @param accessToken
	 * @param roomName
	 * @return
	 * @throws IOException
	 */
	public static String createRoom(String accessToken, String roomName) throws IOException{
		StringBuilder url=new StringBuilder(MatrixUtilities.getHomeServerUrlForRequestToMatrix(false)).append("/_matrix/client/r0/createRoom?access_token=").append(accessToken);
		String postJsonData="{\"name\":\""+roomName+"\",\"preset\":\"private_chat\",\"visibility\":\"private\",\"initial_state\":[{\"content\":{\"guest_access\":\"can_join\"},\"type\":\"m.room.guest_access\",\"state_key\":\"\"}]}";
		
		String reponseStr=executeUrl(url.toString(), "POST", postJsonData).toString();
		String roomId=reponseStr.substring(reponseStr.indexOf("room_id\":")+10,reponseStr.length()-2);
		return roomId;
	}
	/**
	 * Create a direct chat room and return the roomId of the created room.
	 * @param accessToken
	 * @param invitedUserMatrixId
	 * @return
	 * @throws IOException
	 */
	public static String createDirectChatRoom(String accessToken, String invitedUserMatrixId) throws IOException{
		StringBuilder url=new StringBuilder(MatrixUtilities.getHomeServerUrlForRequestToMatrix(false)).append("/_matrix/client/r0/createRoom?access_token=").append(accessToken);
		String postJsonData="{\"preset\":\"trusted_private_chat\",\"visibility\":\"private\",\"invite\":[\""+invitedUserMatrixId+"\"],\"is_direct\":true,\"initial_state\":[{\"content\":{\"guest_access\":\"can_join\"},\"type\":\"m.room.guest_access\",\"state_key\":\"\"}]}";
		String reponseStr=executeUrl(url.toString(), "POST", postJsonData).toString();
		String roomId=reponseStr.substring(reponseStr.indexOf("room_id\":")+10,reponseStr.length()-2);
		
//		url=new StringBuilder(MatrixUtilities.getHomeServerUrlForRequestToMatrix()).append("/_matrix/client/r0/user/"+invitedUserMatrixId+"/account_data/m.direct?access_token=").append(accessToken);
//		postJsonData="{\"@riotuser2:jeangb.org\":[\"!dSAquyGetQwxXygtOt:jeangb.org\"],\"}";
//		reponseStr=executeUrl(url.toString(), "OPTIONS", null).toString();
		return roomId;
	}
	
	/**
	 * Enable encryption in a room.
	 * @param accessToken
	 * @param roomId
	 * @throws IOException
	 */
	public static void enableEncryptionInRoom(String accessToken, String roomId) throws IOException{
		StringBuilder url=new StringBuilder(MatrixUtilities.getHomeServerUrlForRequestToMatrix(false)).append("/_matrix/client/r0/rooms/").append(roomId).append("/state/m.room.encryption").append("?access_token=").append(accessToken);
		String postJsonData = "{\"algorithm\":\"m.megolm.v1.aes-sha2\"}";
		executeUrl(url.toString(), "PUT", postJsonData);
	}
	
	private static StringBuffer executeUrl(String url, String typeRequete, String jsonData) throws IOException{
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[]{
		        new X509TrustManager() {
		            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
		                return null;
		            }
		            public void checkClientTrusted(
		                    java.security.cert.X509Certificate[] certs, String authType) {
		            }
		            public void checkServerTrusted(
		                    java.security.cert.X509Certificate[] certs, String authType) {
		            }
		        }
		};
		// Install the all-trusting trust manager
		try {
		    SSLContext sc = SSLContext.getInstance("SSL");
		    sc.init(null, trustAllCerts, new java.security.SecureRandom());
		    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
		}
		
		URL obj = new URL(url);  
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		con.setRequestMethod(typeRequete);
		con.setDoOutput(true);  
		con.setHostnameVerifier(new Verifier());
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());  
		if(jsonData!=null)wr.writeBytes(jsonData);  
		wr.flush();  
		wr.close();  

		int responseCode = con.getResponseCode();  
		System.out.println("\nSending '"+typeRequete+"' request to URL : " + url);  
		if(jsonData!=null)System.out.println(typeRequete+" Data : " + jsonData);  
		System.out.println("Response Code : " + responseCode);  

		BufferedReader in = new BufferedReader(  
				new InputStreamReader(con.getInputStream()));  
		String output;  
		StringBuffer response = new StringBuffer();  

		while ((output = in.readLine()) != null) {  
			response.append(output);  
		}  
		in.close();  
		//printing result from response  
		System.out.println(response.toString());
		return response;
	}
}
