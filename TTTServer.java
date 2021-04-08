//Jeremiah Hsieh ICSI 416 Project 4 Tic Tac Toe Server
//build files with javac TTTServer.java
//run files with java TTTServer -p 61494

import java.io.*;
import java.net.*;
import java.util.*;


public class TTTServer {
  public static void main(String args[]) throws IOException {
    //variables
    int portNum = 0, status = 0;
    String board[][] = new String[3][3];
    ServerSocket listener = null;
    
    //parse command line
    for(int x = 0; x < args.length; x++) {
      //port number
      if(args[x].equals("-p")) {
        portNum = Integer.parseInt(args[x+1]);
      }
      //help info
      else if (args[x].equals("-h")) {
        System.out.println("-p command line to assign port number");
        System.exit(0);
      }
    }
    
    //gui user interface
    //portNum = Integer.parseInt(JOptionPane.showInputDialog("Enter server port: "));
    
    //connection listener
    try {
      listener = new ServerSocket(portNum);
    }
    //port already in use
    catch(BindException e) {
      System.out.println("port " + portNum + " is in use.");
      System.exit(0);
    }
    try {
      while (true) {
        //new socket for client
        Socket socket = listener.accept();
        try {
          //input/output from socket
          ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
          ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
          //keep connection open until game ends
          while (status == 0) {
            //read array
            try {
              board = (String[][]) input.readObject();
            }
            catch(ClassNotFoundException e) {
              System.out.println("ClassNotFoundException");
            }
            //check if won/draw board
            status = won(board);
            //make a random move
            if (status == 0) {
              turn(board);
            }
            //output array to socket    
            out.writeObject(board);
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
  
  //tic tac toe random move
  private static String[][] turn(String board[][]) {
    int flag = 0;
    while (flag == 0) {
      //random move
      Random randnum = new Random();
      int move = randnum.nextInt(9) + 1;
      //check board
      for(int x = 0; x < 3; x++) {
        for(int y = 0; y < 3; y++) {
          //check valid move
          if(board[x][y].equals(Integer.toString(move))) {
            //set move
            board[x][y] = "o";
            flag = 1;
          }
        }
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
    //no full tiles so end in tie
    if(full == 9) {
      return -1;
    }
   
    //no win or draw
    return 0;
  }
}
