import java.net.*; 
import java.io.*; 
  
public class Client 
{ 
    // initialize socket and input output streams 
    private Socket socket = null; 
    private DataInputStream input = null; 
    private DataInputStream messageBack = null;
    private DataOutputStream out = null; 
    private DataOutputStream msg = null; 
    private String line;
  
    // constructor to put ip address and port 
    public Client(String address, int port) 
    { 
        // establish a connection 
        try
        {
        	// initializes the socket
            socket = new Socket(address, port);
            // print for the client
            System.out.println("Welcome! I am the client end of the client-server.");

            // takes input from terminal 
            input = new DataInputStream(System.in);
            
            // initializes input stream to receive message back from server
            messageBack = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            // sends output to the socket 
            out = new DataOutputStream(socket.getOutputStream());
            
        } 
        catch(UnknownHostException u) 
        { 
            System.out.println(u); 
        } 
        catch(IOException i) 
        { 
            System.out.println(i); 
        } 
  
        // string to read message from input 
       line = "";
       
       // runs the client
       runClient();
    } 
    
    public void runClient() {
    	// prompts client to enter message
    	System.out.println("\nEnter your message: ");
		messageToServer();
		// prints menu selection
		printMenu();
  
        // keep reading until "0" is input 
        while (true) 
        {
            try
            {
            	line = input.readLine();
            	// prompts user to input proper commands 
            	if(!(line.equals("1") || line.equals("0"))) {
            		System.out.println("Please choose 1 or 0.");
            	}
            	switch(line) {
            	
            		case "1":
            			printCommands();
            			messageToServer();
            			messageFromServer();
            			printMenu();
            			break;
            		case "0":
            			System.out.println("\nYou have succesfully exited the program.");
            			socket.close();
            			System.exit(1);
            	}
            } 
            catch(IOException i) 
            { 
                System.out.println(i);
                System.exit(0);
            } 
        }
      
    }
    
    // function for sending message to Master Server
    public void messageToServer() {
    	try {
			line = input.readLine();
		} catch (IOException e1) {
			System.out.println("Could not read client input");
		}
		try {
			out.writeUTF(line);
		} catch (IOException e) {
			System.out.println("Could not write message to OutputStream");
		}
    }
    
    // function for receiving message from Master Server
    public void messageFromServer() {
    	String message = "";
		try {
			message = messageBack.readUTF();
		} catch (IOException e) {
			System.out.printf("Message could not be retrieved from the Server...%s", e);
		}
		System.out.printf("\nAnswer recieved from server: %s\n", message);
    }
    
    // Print Menu
    public static void printMenu() {
    	System.out.println("\nPlease choose from the following selections:");
    	System.out.printf("\n\t1 - Enter a command");
    	System.out.printf("\n\t0 - Exit program");
    	System.out.printf("\nYour desired menu selection? ");
    }
    
    // Print Commands
    public static void printCommands() {
    	System.out.printf("\n\t1 - Echo Message");
    	System.out.printf("\n\t2 - Reverse Message");
    	System.out.printf("\n\t3 - Uppercase Message");
    	System.out.printf("\n\t4 - Lowercase Message");
    	System.out.printf("\n\t5 - Caesar Message");
    	System.out.printf("\n\t6 - Capitalize Vowels Message\n");
    	System.out.printf("\nEnter any of the following numerical command(s): ");
    }
    
    // main method to run the client
    public static void main(String args[]) 
    { 
    	if (args.length < 2) {
    		System.out.println("Please pass in the IP/Host and Port Number");
    	}
    	String host = null;
    	host = args[0];
    	int port = 0;
    	port = Integer.parseInt(args[1]);
        Client client = new Client(host, port); 
        
    } 
} 