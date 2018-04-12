import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientThread extends Thread{
	Socket connectionSocket ;
	String clientSentence, ip;
	BufferedReader inFromClient;
	DataOutputStream  outToClient;	
	String request = "";
	Socket mainSocket;
	DataOutputStream outToServer;
	
	public ClientThread(Socket connectionSocket,String ip) throws IOException
		{
			this.connectionSocket = connectionSocket;			//The connection between the client and the server
			inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			this.ip = ip;
	        mainSocket = new Socket("localhost", 9000);	
	        //Connection between this thread and the organisingThread
	        outToServer =  new DataOutputStream(mainSocket.getOutputStream());
		}
		public void run(){
			      while(true) {
						try {
			 	         if(connectionSocket.isClosed())
			 	        	 break;
			 	         if(inFromClient.equals(null))
			 	        	 break;
			 	         if(inFromClient.ready()){
			 	        	 outToClient.writeBytes("We are preparing ur request" + "\n");
			 	        	 HTTPRequest req = new HTTPRequest();		//preparing HTTP request
			 	        	 int i = 0;
			 	        	 clientSentence = inFromClient.readLine();
			 	        	 String[] parts = clientSentence.split(" ");
			 	        	 req.setIp(ip);
			 	        	 req.setMethod(parts[0]);
			 	        	 req.setURL(parts[1]);
			 	        	 req.setVersion(parts[2]);
			 				String[] wf = req.getURL().split("/");
							String wantedFile = wf[wf.length-1];
			 	        	 while(i<3){
			 	        		 if(i==0){
			 	        		 clientSentence = inFromClient.readLine();
			 	        		 req.setHost(clientSentence);
			 	        		 }
			 	        		 else if(i==1){
				 	        		 clientSentence = inFromClient.readLine();
				 	        		 req.setAcceptedFormat(clientSentence);
				 	        		 }
			 	        		 else if(i==2){
				 	        		 clientSentence = inFromClient.readLine();
				 	        		 req.setConnection(clientSentence);
				 	        		 }
			 	        		 else{
			 	        			i++;
			 	        			continue;
			 	        		 }
			 	        		 i++;
			 	        		 }
			 	        	 outToServer.writeBytes("ready" + "\n");
			 	        	 outToServer.writeBytes(wantedFile + " " + ip + "\n");
				 	        outToClient.writeBytes("ur request is being processed" + "\n");
			 	        	 }
						}
		      catch (Exception e) {
		    	  System.out.println("exception");
		    	  if(connectionSocket.isClosed() == false)
		    	  this.start();
		    	  else
		    		  break;
		      }}}
		public String getIp() {
			return ip;
		}
		public Socket getConnectionSocket() {
			return connectionSocket;
		}}