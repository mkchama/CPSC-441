import java.net.*;
import java.util.*;
import java.io.*; 

public class MasterServer{
 
	 //initialize socket and input stream 
	
    private Socket socket = null; 
    private ServerSocket server = null; 
    private DataInputStream in = null;
    private DataOutputStream sendBack = null;
    private ArrayList<Integer> arr;
    private String str, line, message;
    private DatagramSocket toMicroServer;
    private boolean recieve;
    //Main server will be the datagram sender. And the microservers will be the receiver 
    // constructor with port 
    public MasterServer(int port) throws Exception 
    { 
    	
        // starts server and waits for a connection 
        try
        { 
        	toMicroServer = new DatagramSocket(7700);
        	toMicroServer.setSoTimeout(4500);
        	
            server = new ServerSocket(port); 
            System.out.println("Welcome! I am the main server for Assignment 2."); 
  
            System.out.printf("Main server is listening on TCP port %s\n ", port + "..."); 
  
            socket = server.accept(); 
            System.out.println("Client accepted"); 
  
            // takes input from the client socket 
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream())); 
            
            // initializes the strings used
            str = "";
            line = "";
            message = "";

            // reads message from the client
            message = in.readUTF();
            runServer();

        } 
        catch(IOException i) 
        { 
        	System.out.println("Connection lost"); 
        	  
            // close connection 
            socket.close(); 
            in.close(); 
        } 
    } 
    
    // method to run the server. Takes in the commands from the user and processes them
    // as an array list to perform each command, that will be sent to the appropriate
    // micro server
    public void runServer() throws Exception{
    	while (true) 
        {
        	recieve = false;
            try
            {
                System.out.printf("Child process received message: %s\n", message);
                System.out.println("Child about to send message: OK. Command?\n");
                line = in.readUTF();
                
                // parses the commands as a long then initializes it  to an array list
                long numbers = Long.parseLong(line);
                ArrayList<Long> array = new ArrayList<Long>();
                do {
                	array.add(0, numbers % 10);
                	numbers /= 10;
                } 
                while(numbers > 0 );
                str = message;
                
                // loops through the commands and applies the appropriate transformation based on order
                for (Long num : array) {
                	recieve = false;
                	while(!recieve && num < 7 && num >0) {
                		sendMicroServer(str, num);
                		fromMicroServer();
                    }	
                }
                // writes the message back to the client after successfully transforming it
                sendBack = new DataOutputStream(socket.getOutputStream()); 
        		sendBack.writeUTF(str);
        		str = message;
            } 
            catch(IOException i) 
            { 	
                System.out.println("Client has exited the program.");
                System.exit(1);
  
            } 
        } 
    }
    
    // function that initializes DatagramPackets to establish UDP connection with Micro Services
    // also sends over the message that the user sent, based on the command selected
    public void sendMicroServer(String message, Long num) throws Exception {
        InetAddress ip = InetAddress.getByName("localhost");
    	DatagramPacket dp = new DatagramPacket(message.getBytes(), message.length(), ip, (int) (7000+num));
    	toMicroServer.send(dp);
        System.out.printf("Sending message '%s' to micro-service %s\n", message, num);
	
    }
    
    // receives the message from the micro servers, and ensures that we are able to receive
    // messages, so long as the micro server is up and running
    public void fromMicroServer() throws Exception {
    	byte[] buf = new byte[1024];
		DatagramPacket dp = new DatagramPacket(buf, 1024);
		try{
			toMicroServer.receive(dp);
			recieve = true;
		}
		catch (IOException e){
			System.out.println("Connection timed out, retrying...");
			recieve = false;
			return;
		}
        str = new String(dp.getData(), 0, dp.getLength());
		System.out.printf("Child about to send message: %s \n", str);

    }

    // main method to run the program, initialized to port 7700 
    public static void main(String args[]) throws Exception 
    { 
    	try {
    		MasterServer server = new MasterServer(7700); 
    	}
    	catch (IOException e){
    		System.out.println("Closed");
    	}
        
    } 
} 