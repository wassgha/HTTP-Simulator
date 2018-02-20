import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Connection {
  private TransportLayer transportLayer;
  private int port;

  public Connection(int port) {
    transportLayer = new TransportLayer(false, port);
  }

  public int getPort() {
    return port;
  }

  public void close() {
    transportLayer.close();
  }

  public void send(String message) {
    System.out.println("Connection send," + message);
    byte[] byteArray = message.getBytes();
    transportLayer.send(byteArray);
  }

  public String receive() {
    byte[] byteArray = transportLayer.receive();
    String str = new String(byteArray);
    return str;
  }
}