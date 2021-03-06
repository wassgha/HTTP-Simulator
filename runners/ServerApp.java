package runners;

import endsystems.Server;


/*
  Server - Runs the server app and passes the delay arguments to it

*/

public class ServerApp {
  public static void main(String[] args) throws Exception{
    int TRANSMISSION_DELAY_RATE = 1;
    int PROPAGATION_DELAY = 100;
    if (args.length >= 2) {
      TRANSMISSION_DELAY_RATE = Integer.parseInt(args[0]);
      PROPAGATION_DELAY = Integer.parseInt(args[1]);
    }

    // Create a new instance of a server
    Server  server = new Server(TRANSMISSION_DELAY_RATE, PROPAGATION_DELAY);
    server.run();
  }
}
