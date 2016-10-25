package utility;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HttpsRequestsToMatrix {

	public static void sendMessageInRoom(String accessToken, String roomId, String message) throws IOException{  
		  int token = 1 + (int)(Math.random() * ((10000 - 1) + 1));
		  StringBuilder url=new StringBuilder(Constant.matrixServer).append("/_matrix/client/r0/rooms/").append(roomId).append("/send/m.room.message/").append(token).append("?access_token=").append(accessToken);
		  URL obj = new URL(url.toString());  
		  HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		  con.setRequestMethod("PUT");
		  con.setRequestProperty("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		  con.setRequestProperty("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
		  con.setRequestProperty("Access-Control-Allow-Origin", "*");
		  con.setRequestProperty("Content-Encoding", "gzip");
		  con.setRequestProperty("Content-Type", "application/json");
		  con.setRequestProperty("Server", "Synapse/0.18.2-rc1 (b=release-v0.18.2,t=v0.18.2-rc1,513e600)");
		  con.setRequestProperty("Transfer-Encoding", "chunked");
		  con.setRequestProperty("Date", "Mon, 24 Oct 2016 15:52:19 GMT");
		  String postJsonData = "{\"msgtype\":\"m.text\",\"body\":\""+message+"\"}";
		  con.setDoOutput(true);  
		  DataOutputStream wr = new DataOutputStream(con.getOutputStream());  
		  wr.writeBytes(postJsonData);  
		  wr.flush();
		  wr.close();
		  int responseCode = con.getResponseCode();
		  System.out.println("\nSending 'POST' request to URL : " + url);
		  System.out.println("Post Data : " + postJsonData);
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
	}
}
