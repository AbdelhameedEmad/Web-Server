import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	static int i = 0 ;  
	static int id = 0 ;
    static ArrayList<ClientThread> Clients;
    static BufferedReader inFromClient;
    static ArrayList<Socket> sockets;
    static organisingThread Org;
    static ServerSocket requestSocket;

    
	public static void main(String argv[]) throws Exception
	 {	@SuppressWarnings("resource")							//Starting the server socket
		ServerSocket welcomeSocket = new ServerSocket(10000);	
	 	sockets = new ArrayList<Socket>();
	 	Org = new organisingThread(sockets);					//A thread to organise the HTTP requests
	 	Org.start();
	 	requestSocket = new ServerSocket(9000);			//A Socket to make the Server communicate with the Threads
	 	Clients = new ArrayList<ClientThread>();				//Array that contains the clients sockets
	 	
	      while(true) {
		     Socket connectionSocket = welcomeSocket.accept();		//Waiting for a new client
		     inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
		     String ip = inFromClient.readLine();					//Read the IP address
		     ClientThread ct = new ClientThread(connectionSocket,ip);
		     System.out.println(ip);
		     ct.start();
		     Clients.add(ct);
	      		Socket requestsConnectionSocket = requestSocket.accept();
	      	//Connection between the new Thread and the Organising thread
	      		sockets.add(requestsConnectionSocket);
	      		Org.setSockets(sockets);
	      		Org.setClients(Clients);
    	}}}