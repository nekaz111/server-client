//Jeremiah Hsieh ICSI 416 Project 4 Tic Tac Toe Client
//build files with javac TTTClient.java
//run files with java TTTClient -s itsuix.albany.edu -p 61494

import java.io.*;
import java.net.*;
import java.util.*;

public class TTTClient {
  
  public static void main(String args[]) throws IOException {
    //variables
    String address = "";
    int portNum = 0, status = 0;
    String board[][] = new String[][] {{"1", "2", "3"}, {"4", "5", "6"}, {"7", "8", "9"}};
    Socket socket = null;
    ObjectOutputStream out = null;
    ObjectInputStream input = null;
    
    //simple error check command line
    if (args.length > 6) {
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
          InetAddress.getByName(address);
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
      //help text
      else if (args[x].equals("-h")) {
        System.out.println("-s flag for server name\n-p flag for port number\nenter the number you wish to put an x in on the grid");
        System.exit(0);
      }
    }
    
    //gui user input (testing purposes)
    //address = JOptionPane.showInputDialog("Enter IP Address: ");
    //portNum = Integer.parseInt(JOptionPane.showInputDialog("Enter server port: "));
     
    
    //create socket
    try {
      socket = new Socket(address, portNum);
    }
    //error checking
    catch (BindException e) {
      System.out.println(portNum + " is in use");
      System.exit(0);
    }
    //link input/output and send data
    out = new ObjectOutputStream(socket.getOutputStream()); 
    input = new ObjectInputStream(socket.getInputStream());
    
    //loop until game over
    while(status == 0) {
      displayBoard(board);
      turn(board);
      status = won(board);
      if (status == 1) {
        System.out.println("Player 1 wins");
        //close and exit
        socket.close();
        System.exit(0);
      }
      if (status == -1) {
        System.out.println("Draw Game");
        //close and exit
        socket.close();
        System.exit(0);
      }
      out.writeObject(board);
      //get return 
      try {
        board = (String[][]) input.readObject();
      }
      catch(ClassNotFoundException e) {
        System.out.println("ClassNotFoundException");
      }
      status = won(board);
      if (status == 1) {
        System.out.println("Player 2 wins");
        //close and exit
        socket.close();
        System.exit(0);
      }
      if (status == -1) {
        System.out.println("Draw Game");
        //close and exit
        socket.close();
        System.exit(0);
      }
    }
  }
  
  //player turn move
  private static String[][] turn(String board[][]) {
    int flag = 0;
    String move = "";
    //get input
    Scanner input = new Scanner(System.in);
    System.out.println("Please enter your move: ");
    move = input.next();
    
    while(Integer.parseInt(move) < 1 || Integer.parseInt(move) > 9) {
      System.out.println("Number is not within scope. Please enter your move: ");
      move = input.next();
    }
       
    //set move
    while (flag == 0) {
      //check board
      for(int x = 0; x < 3; x++) {
        for(int y = 0; y < 3; y++) {
          //check valid move
          if(board[x][y].equals(move)) {
            //set move
            board[x][y] = "x";
            flag = 1;
          }
        }
      }
      if (flag == 0) {
        //invalid move
        displayBoard(board);
        System.out.println("This is not a valid move. Please enter your move: ");
        move = input.next();
      }
    }
    return board;
  }
  
  //if someone won
  private static int won(String board[][]) {
    int full = 0;
    //lazy check whole board for winner
    //i seem to have forgotten how to do this more cleanly
    //vertical 
    for(int x = 0; x < 3; x++) {
      if (board[x][0].equals(board[x][1]) && board[x][0].equals(board[x][2])) {
        return 1;
      }
    }
    //horizontal
    for(int y = 0; y < 3; y++) {
      if(board[0][y].equals(board[1][y]) && board[0][y].equals(board[2][y])) {
        return 1;
      }
    }
    //diagonal
    if((board[0][0].equals(board[1][1]) && board[0][0].equals(board[2][2])) || (board[0][2].equals(board[1][1]) && board[0][2].equals(board[2][0]))) {
      return 1;
    }
    
    //if board is full
    for(int x = 0; x < 3; x++) {
      for(int y = 0; y < 3; y++) {
        if (board[x][y].equals("x") || board[x][y].equals("o")) {
          full++;
        }
      }
    }
    //full tiles so end in tie
    if(full == 9) {
      return -1;
    }
    
    //no win or draw
    return 0;
  }
  
  //print game board
  private static void displayBoard(String board[][]) {
    System.out.println("_" + board[0][0] + "_|_" + board[0][1] + "_|_" + board[0][2] + "_");
    System.out.println("_" + board[1][0] + "_|_" + board[1][1] + "_|_" + board[1][2] + "_");
    System.out.println(" " + board[2][0] + " | " + board[2][1] + " | " + board[2][2]);
  }
}
