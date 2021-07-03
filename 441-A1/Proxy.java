// Name: Mohammad Chama UCID: 10138553

import java.net.*;
import java.io.*;
import java.util.*;
import java.nio.charset.StandardCharsets;

public class Proxy {
    /** Port for the proxy */
    private static int port;
    /** Socket for client connections */
	private static ServerSocket socket;

    /** Create the Proxy object and the socket */
    public static void init(int p) {
	port = p;
	try {
	    socket = new ServerSocket(port); /* Server Socket */
	} catch (IOException e) {
	    System.out.println("Error creating socket: " + e);
	    System.exit(-1);
	}
    }

    public static void handle(Socket client) {
	Socket server = null;
	HttpRequest request = null;
	HttpResponse response = null;

	/* Process request. If there are any exceptions, then simply
	 * return and end this request. This unfortunately means the
	 * client will hang for a while, until it timeouts. */

	/* Read request */
	try {
	    BufferedReader fromClient = new BufferedReader(new InputStreamReader(client.getInputStream())); /* Used to read the request from the Client */
	    request = new HttpRequest(fromClient); /* Initalize a request from the Buffered Reader Client */
	} catch (IOException e) {
	    System.out.println("Error reading request from client: " + e);
	    return;
	}
	/* Send request to server */
	try {
	    /* Open socket and write request to socket */
	    server = new Socket(request.getHost(), request.getPort()); /* Intialize a socket for server */
	    DataOutputStream toServer = new DataOutputStream(server.getOutputStream()); /* Initialize a data output stream to the server*/
		toServer.writeBytes(request.toString()); /* Write the request to the socket by converting the bytes to string */

	} catch (UnknownHostException e) {
	    System.out.println("Unknown host: " + request.getHost());
	    System.out.println(e);
	    return;
	} catch (IOException e) {
	    System.out.println("Error writing request to server: " + e);
	    return;
	}
	/* Read response and forward it to client */
	try {
	    DataInputStream fromServer = new DataInputStream(server.getInputStream()); /* Initialize a DataInputStream from the Server*/
	    response = new HttpResponse(fromServer); /* Initalize an HttpResponse from the Server */
		DataOutputStream toClient = new DataOutputStream(client.getOutputStream()); /* Forward the http response to the client*/
	
			System.out.println("Write response to client:");
			System.out.println("Write headers to client;"); 
			
			System.out.println(response.toString()); 
			toClient.writeBytes(response.toString());
		
		
		/*Modifications made to all text, hyperlinks and images in this if statement.
		It checks if the response headers contain UTF-8 character set. If so, modifications 
		are made as specified by the assignment in order to send back to the client*/
		if(response.headers.contains ("UTF-8"))
		{
			String s = new String(response.body, StandardCharsets.UTF_8);
			s = s.replaceAll("Drummond", "Kobe-B24");
			s = s.replaceAll("World", "Titan");
			s = s.replaceAll("NBA", "TBA");
			s = s.replaceAll("2019", "2219");
			byte[] array1 = s.getBytes("UTF-8");
			toClient.write(array1);
		}
		else{
			toClient.write(response.body); /* Forward the rest to the client */
		}
	
		client.close();
	    server.close();
		/* Insert object into the cache */
	    /* Fill in (optional exercise only) */
	} catch (IOException e) {
	    System.out.println("Error writing response to client: " + e);
	}
    }


    /** Read command line arguments and start proxy */
    public static void main(String args[]) {
	int myPort = 0;
	
	try {
	    myPort = Integer.parseInt(args[0]);
	} catch (ArrayIndexOutOfBoundsException e) {
	    System.out.println("Need port number as argument");
	    System.exit(-1);
	} catch (NumberFormatException e) {
	    System.out.println("Please give port number as integer.");
	    System.exit(-1);
	}
	
	init(myPort);

	/** Main loop. Listen for incoming connections and spawn a new
	 * thread for handling them */
	Socket client = null;
	
	while (true) {
	    try {
		client = socket.accept(); /* Listens for a connection to be made to this socket and accepts it */
		handle(client);
	    } catch (IOException e) {
		System.out.println("Error reading request from client: " + e);
		/* Definitely cannot continue processing this request,
		 * so skip to next iteration of while loop. */
		continue;
	    }
	}

    }
}