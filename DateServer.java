import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

/**
 * A TCP server that runs on port 9090.  When a client connects, it
 * sends the client the current date and time, then closes the
 * connection with that client.  Arguably just about the simplest
 * server you can write.
 */
public class DateServer {
  
  /**
   * Runs the server.
   */
  public static void main(String args[]) throws IOException {
    int serverPort = 0, arabic = 0, flag = 0;
    String value = "", roman = "";
    for(int x = 0; x < args.length; x++) {
      System.out.println(args[x]);
      if(args[x] == "-p") {
        serverPort = Integer.parseInt(args[x+1]);
      }
      else if (args[x] == "-h") {
        System.out.println("-p for port number");
      }
    }
    
    
    
    
    serverPort = Integer.parseInt(JOptionPane.showInputDialog("Enter server port: "));
    




    //connection listener
    ServerSocket listener = new ServerSocket(serverPort);
    try {
      while (true) {
        //new socket for client
        Socket socket = listener.accept();
        try {
          //input from socket
          BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
          value = input.readLine();
          
          //parse value
          try{
            //arabic to roman
            arabic = Integer.parseInt(value);
            roman = toRoman(arabic);
          }
          catch (NumberFormatException e) {
            //roman to arabic
            arabic = toArabic(value.toUpperCase());
            flag = 1;
          }
          
          //output to socket
          PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
          //out.println(new Date().toString());
          if (flag == 0) {
            out.println(roman);
          }
          else {
            out.println(Integer.toString(arabic));
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
    }
  }
  
  //convert from roman to arabic
  public static int toArabic(String value) {
    int num = 0, prev = 0, temp = 0;
    //make value table
    Hashtable<Character, Integer> table = new Hashtable<Character, Integer>();
    table.put('i',1);
    table.put('x',10);
    table.put('c',100);
    table.put('m',1000);
    table.put('v',5);
    table.put('l',50);
    table.put('d',500);
    
    //get chars from right to left and add to final value
    for(int i = value.length() - 1;  i >= 0; i--) {
      temp = table.get(value.charAt(i));
      if(temp < prev)
        num -= temp;
      else
        num += temp;
      prev = temp;
    }
    return num;
  }
  
  private static String toRoman(int value) {
    String romanCharacters[] = { "M", "CM", "D", "C", "XC", "L", "X", "IX", "V", "I" };
    int[] romanValues = { 1000, 900, 500, 100, 90, 50, 10, 9, 5, 1 };
    String result = "";
    
    for (int i = 0; i < romanValues.length; i++) {
      int numberInPlace = value / romanValues[i];
      if (numberInPlace == 0) 
        continue;
      result += numberInPlace == 4 && i > 0? romanCharacters[i] + romanCharacters[i - 1]:
        new String(new char[numberInPlace]).replace("\0",romanCharacters[i]);
      value = value % romanValues[i];
    }
    return result;
  }
  
  
  
  //convert from arabic to roman
  /*public static String toRoman(int value) {
   * 
   TreeMap<Integer, String> table = new TreeMap<Integer, String>();
   table.put(1000, "M");
   table.put(900, "CM");
   table.put(500, "D");
   table.put(400, "CD");
   table.put(100, "C");
   table.put(90, "XC");
   table.put(50, "L");
   table.put(40, "XL");
   table.put(10, "X");
   table.put(9, "IX");
   table.put(5, "V");
   table.put(4, "IV");
   table.put(1, "I");
   
   int l =  table.floorKey(value);
   if ( value == l ) {
   return table.get(value);
   }
   return table.get(l) + toRoman(value-l);
   }*/
}