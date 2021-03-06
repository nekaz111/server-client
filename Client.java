//Jeremiah Hsieh ICSI 416 Project 3 Client
//build files with javac Client.java
//run files with java Client -s itsuix.albany.edu -p 61494 -a 88

import java.io.*;
import java.net.*;
import javax.swing.*;

public class Client {
  
  public static void main(String args[]) throws IOException {
    //variables
    String address = "", value = "";
    int portNum = 0, flag = 0;
    InetAddress check = null;
    Socket socket = null;
    PrintWriter out = null;
    BufferedReader input = null;
     
    
    //simple error check command line
    if (args.length > 7) {
      System.out.println("Too many arguments");
      System.exit(0);
    }
    //parse command line
    for(int x = 0; x < args.length; x++) {
      //server hostname
      if(args[x].equals("-s")) {
        address = args[x+1]; 
        //error checking
        try {
          check = InetAddress.getByName(address);
        }
        catch(UnknownHostException e) {
          System.out.println(address + " is an invalid host name");
          System.exit(0);
        }
      }
      //port number
      else if (args[x].equals("-p")) {
          portNum = Integer.parseInt(args[x+1]);
      }
      //value to be converted
      else if (args[x].equals("-r") || args[x].equals("-a")) {
        if (flag == 0) {
          value = args[x+1];
          flag = 1;
        }
        //more than 1 -r or -a  error
        else {
          System.out.println("Can't have -a and -r arguements at the same time");
          System.exit(0);
        }
      }
      //help text
      else if (args[x].equals("-h")) {
        System.out.println("-s flag for server name\n-p flag for port number\n-r flag for roman numeral translation\n-a for arabic translation\ncan't use -a and -r and the same time");
        System.exit(0);
      }
    }
    
    //gui user input (testing purposes)
    /*address = JOptionPane.showInputDialog("Enter IP Address: ");
    portNum = Integer.parseInt(JOptionPane.showInputDialog("Enter server port: "));
    value = JOptionPane.showInputDialog("Enter number value: ");
    */
    
    //create socket
    try {
      socket = new Socket(address, portNum);
    }
    //error checking
    catch (BindException e) {
      System.out.println(portNum + " is in use");
      System.exit(0);
    }
    catch (ConnectException e) {
      System.out.println("Connection Refused. Please start Server or enter correct port number");
      System.exit(0);
    }
    //link output and send data
    out = new PrintWriter(socket.getOutputStream(), true);
    out.println(value);
    //get return
    input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    //output result
    System.out.println(input.readLine());
    //close and exit
    socket.close();
  }
}
