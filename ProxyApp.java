import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

//This class represents the client application
public class ProxyApp {
  public static void main(String[] args) throws Exception {
    //create a new transport layer for proxy server (hence true), on the correct port
    TransportLayer transportLayer = new TransportLayer(true, TransportLayer.PROXY_LISTENING_PORT);
    //open a client connection to the server (hence false), on the correct port
    // TransportLayer transportLayer2 = new TransportLayer(false, TransportLayer.WEB_LISTENING_PORT);


    HTTP http = new HTTP();
    while(true) {
      //get line from client
      byte[] byteArray = transportLayer.receive();
      if(byteArray == null) {
        TimeUnit.MILLISECONDS.sleep(250);
        continue;
      }
      String str = new String ( byteArray );
      System.out.println("Request: " + str);

      int index = str.indexOf("HTTP/") + 5;
      String version = str.substring(index, index + 3);
      String uri = str.substring(str.indexOf(" ") + 1, index - 6);

      HTTP.HTTPResponse response = http.get(version, uri, TransportLayer.WEB_LISTENING_PORT);
      System.out.println("Response: " + response.data);
      byteArray = response.data.getBytes();
      transportLayer.send(byteArray);
    }
  }

  public static String createResponse(String version, int code, String data) {
    String msg = "Ok";
    if (code == 404) msg = "Not Found";
    if (code == 304) msg = "Not Modified";

    String response = "HTTP/" + version + " " + code + " " + msg + "\n";
    response += "Connection: " + (version.equals("1.1") ? "keep-alive" : "close") + "\n";
    //TODO: Add Date and Last-Modified
    response += "Content-Length: " + data.length() + "\n";
    response += "Content-Type: text/clht\n";
    response += data;

    return response;
  }
}
