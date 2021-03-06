//Jeremiah Hsieh ICSI 416 Project 3 Server
//build files with javac Server.java
//run files with java Server -p 61494

import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;


public class Server {
  public static void main(String args[]) throws IOException {
    //variables
    int portNum = 0, arabic = 0, flag = 0;
    String value = "", roman = "";
    ServerSocket listener = null;
    //parse command line
    for(int x = 0; x < args.length; x++) {
      //port number
      if(args[x].equals("-p")) {
        portNum = Integer.parseInt(args[x+1]);
      }
      //help info
      if (args[x].equals("-h")) {
        System.out.println("-p command line to assign port number");
        System.exit(0);
      }
    }
    
    //gui user interface
    //serverPort = Integer.parseInt(JOptionPane.showInputDialog("Enter server port: "));
    
    //connection listener
    try {
      listener = new ServerSocket(portNum);
    }
    catch(BindException e) {
      System.out.println("port " + portNum + " is in use.");
      System.exit(0);
    }
    try {
      while (true) {
        //new socket for client
        Socket socket = listener.accept();
        try {
          //input from socket
          BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
          value = input.readLine();
          
          //parse value         
          if (isNum(value) == true) {
            arabic = Integer.parseInt(value);
            roman = toRoman(arabic);
            flag = 0;
          }
          else {
            //roman to arabic
            arabic = toArabic(value.toUpperCase());
            flag = 1;
          }
          
          
          //output to socket
          PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
          if (flag == 0) {
            out.println(roman);
          }
          else {
            out.println(arabic);
          }
        } 
        finally {
          //close socket
          socket.close();
        }
      }
    }
    finally {
      //close connection
      listener.close();
      System.exit(0);
    }
    
  }
  
  //convert from roman to arabic
  private static int toArabic(String value) {
    //variables
    int result = 0, prev = 0, temp = 0;
    char roman[] = {'M', 'D', 'C', 'L', 'X', 'V', 'I'};
    int arabic[] = {1000, 500, 100, 50, 10, 5, 1};
    
    //get chars from right to left, parse and add to final value
    for(int i = value.length() - 1;  i >= 0; i--) {
      for(int x = 0; x < roman.length; x++) {
        if (roman[x] == value.charAt(i)) {
          temp = arabic[x];
        }
      }
      //lesser values subtract from greater values that come after ("before" in our loop case)
      if(temp < prev) {
        result -= temp;
      }
      //otherwise they are added
      else {
        result += temp;
      }
      prev = temp;
    }
    //return final value
    return result;
  }
  
  //convert from arabic to roman
  private static String toRoman(int value) {
    //variables
    String roman[] = {"M", "CM", "D", "C", "XC", "L", "X", "IX", "V", "I"};
    int[] arabic = { 1000, 900, 500, 100, 90, 50, 10, 9, 5, 1 };
    int current = 0;
    String result = "";
    
    //parse values and assign appropriate letters
    for (int i = 0; i < arabic.length; i++) {
      current = value / arabic[i];
      if (current == 0) {
        continue;
      }
      //add corresponding letters to string
      result += current == 4 && i > 0 ? roman[i] + roman[i - 1] : new String(new char[current]).replace("\0",roman[i]);
      value = value % arabic[i];
    }
    //return result
    return result;
  }
  
  //check if string is number
  private static boolean isNum(String s) {  
    //regex comparison
    return s.matches("[-+]?\\d*\\.?\\d+");  
  }  
}
