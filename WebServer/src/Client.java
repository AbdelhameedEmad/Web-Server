import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Random;

public class Client {
	static String sentence;
	static String modifiedSentence;
	static Socket clientSocket ;
	static String ip ;
	static BufferedReader inFromServer;
    static String[] files;
	
public static void main(String argv[]) throws Exception
	  {		
		File file = new File("C:/Users/Abdelhameed Emad/Documents/docroot");		//path of the docroot of the Server
		files = file.list();
		String f ="Available files:";												//Showing the files in the docroot
		for (int i = 0; i < files.length; i++) {
			f = f + "\n" + files[i];
		}
		System.out.println(f);
		Random rand = new Random();
		ip = (rand.nextInt(126)+1)+"."+(rand.nextInt(255)+1)+"."
		+(rand.nextInt(255)+1)+"."+(rand.nextInt(255)+1);					//Generating random IP
	    BufferedReader inFromUser =
	        	new BufferedReader(new InputStreamReader(System.in));
	    clientSocket = new Socket("localhost", 10000);
	    DataOutputStream  outToServer =
	        	new DataOutputStream(clientSocket.getOutputStream());
	     inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	    outToServer.writeBytes(ip+"\n");									//Sending IP to the server
	      while(true)
	       {
	        if(inFromUser.ready()){									//When the user inputs any text
	        	sentence = inFromUser.readLine();
	        		if(sentence.equals("close")){	        			//If the user wanted to end the connection
	        			clientSocket.close();
	        			System.out.println("Connection has been closed");
	        			break;
	        			}
	        		else if(sentence.contains("/") 
	        				&& sentence.contains(".")){			//Checking if the input of the user is a URL
	        			try {
	        				String[] parts = sentence.split("/"); //preparing the HTTPRequest
	        				String URL = "";
	        				String Host =parts[0];
	        				if(Host.contains("www.")||Host.contains("eee."))
	        					Host = Host.substring(4, Host.length()-1);
	        				String connection="keep-alive";
	        				String fileName = parts[parts.length - 1]
	        					.substring(0, parts[parts.length - 1].indexOf(".")); 
	        				System.out.println(fileName);
							String format = parts[parts.length - 1]
	        						.substring(parts[parts.length - 1].indexOf("."));
							System.out.println(format);
							URL = "/";
							for (int i = 1; i < parts.length ; i++) {
								if(i == parts.length-1)
									URL = URL + parts[i];
								else
									URL = URL + parts[i] +"/";
							}
							System.out.println(URL);	//Sending the request
							outToServer.writeBytes("GET " + URL + " HTTP/1.1" + "\n");
							outToServer.writeBytes(Host + "\n");
							outToServer.writeBytes(format + "\n");
							outToServer.writeBytes(connection + "\n");
						} 
	        			catch (Exception e) {
						System.out.println("Error: 404 not Found");	
						}}
	        		else
	        			System.out.println("Error: 404 not Found");
	        }	     if(inFromServer.ready()){
	    	    //Waiting for the name of the file then receiving the file and if just a confirmation message just print it
		    	 modifiedSentence = inFromServer.readLine();
		    	 for (int i = 0; i < files.length; i++) {
			    	 if(modifiedSentence.equals(files[i])){
			    		 	byte b[] = new byte[10000];
					        InputStream inFromServerFile = clientSocket.getInputStream();	//Receiving the file
					        @SuppressWarnings("resource")
							FileOutputStream fr = new FileOutputStream("C:/Users/Abdelhameed Emad/Desktop/"  
					        + modifiedSentence );
					        inFromServerFile.read(b, 0, b.length);
					        fr.write(b, 0, b.length);
			    	 }}
	    		 System.out.println(modifiedSentence);
	    		 }}}}