import java.io.*;
import java.net.*;
import javax.swing.*;

/**
 * Trivial client for the date server.
 */
public class DateClient {
  
  /**
   * Runs the client as an application.  First it displays a dialog
   * box asking for the IP address or hostname of a host running
   * the date server, then connects to it and displays the date that
   * it serves.
   */
  public static void main(String args[]) throws IOException {
    //variables
    String address = "", roman = "";
    int portNum = 0, arabic = 0;
    Socket s = null;
   
    
    //parse command line
    if (args.length > 7) {
      System.out.println("Too many arguments");
    }
    for(int x = 0; x < args.length; x++) {
      if(args[x] == "-s") {
        address = args[x+1]; 
      }
      else if (args[x] == "-p") {
        portNum = Integer.parseInt(args[x+1]);
      }
      else if (args[x] == "-r") {
        roman = args[x+1];
      }
      else if (args[x] == "-a") {
        arabic = Integer.parseInt(args[x+1]);
      }
      else if (args[x] == "-h") {
        System.out.println("-s flag for server name\n-p flag for port number\n-r flag for roman numeral translation\n-a for arabic translation");
      }
    }
    
    
    
    
    //user input
    address = JOptionPane.showInputDialog("Enter IP Address: ");
    portNum = Integer.parseInt(JOptionPane.showInputDialog("Enter server port: "));
    arabic = 50;

    
    
    //craete socket
    s = new Socket(address, portNum);
    //get return
    BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
    //output
    String answer = input.readLine();
    JOptionPane.showMessageDialog(null, answer);
    s.close();
    System.exit(0);
  }
}