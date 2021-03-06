package layers;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

// A class that simulates the physical layer. In real life we are using a TCP socket
public class PhysicalLayer {
  //the server socket
  ServerSocket serverSocket;
  //the connection socket after the connection is established
  Socket senderSocket, connectionSocket;
  //stream to write out data
  DataOutputStream socketOut;
  //stream to read in data from socket
  InputStream inputStream;

  public PhysicalLayer(boolean server, int addr) {
    //if this is a server
    if(server) {
      try {
        //create socket with correct port number
        serverSocket = new ServerSocket(addr);
        reset();
      } catch (IOException e) {
        System.out.println(e);
      }
    }
    else {
      try {
        //create socket with correct port number
        senderSocket = new Socket("localhost", addr);
        reset();
      } catch (IOException e) {
        //will run if server was not listening
        System.out.println("Cannot Connect to server");
        System.exit(1);
      }
    }
  }

  public void reset() {
    try {
      if (serverSocket != null) {
        //accept connetcion from client
        connectionSocket=serverSocket.accept();
        //create an outputstream for writing data
        socketOut = new DataOutputStream(connectionSocket.getOutputStream());
        //create an inputstream for reading data
        inputStream = connectionSocket.getInputStream();
      } else {
        //create an outputstream for writing data
        socketOut = new DataOutputStream(senderSocket.getOutputStream());
        //create an inputstream for reading data
        inputStream = senderSocket.getInputStream();
      }
    } catch(IOException e) {
      System.out.println(e);
    }
  }

  public void close() {
    try {
      if (serverSocket != null) serverSocket.close();
      if (connectionSocket != null) connectionSocket.close();
      if (senderSocket != null) senderSocket.close();
      socketOut = null;
      inputStream = null;
    } catch(IOException e) {
      System.out.println(e);
    }
  }

  public void send(byte[] payload) {
    try {
      //send bytes out to socket
      // System.out.println("Physical layer sending data: " + payload.length + " bytes");
      socketOut.write(payload);
    } catch(Exception ex) {
      System.out.println(ex);
    }
  }

  //read bytes from socket
  public byte[] receive() {
    byte[] bytesRecieved = null;
    try {
      byte[] bytes = new byte[4096];
      int numBytes = inputStream.read(bytes);
      // System.out.println(numBytes);

      if (numBytes > 0) {

        bytesRecieved = new byte[numBytes];
        System.arraycopy(bytes, 0, bytesRecieved, 0, numBytes);
        // System.out.println("Physical Layer received: " + numBytes + " bytes");
      } else {
        reset();
        return receive();
      }
    } catch (IOException e) {
      System.out.println(e);
    }
    return bytesRecieved;
  }
}
