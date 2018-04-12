import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class organisingThread extends Thread {
    static Queue<String> q;
    static ArrayList<FileInputStream> Files;
    static ObjectInputStream requestsFromThreads;
    static BufferedReader inFromThreads;
    static ArrayList<Socket> sockets;
    static String[] files;
    static ArrayList<ClientThread> Clients;
    static boolean Done;
    
	@SuppressWarnings("static-access")
	public organisingThread(ArrayList<Socket> sockets) throws IOException{
    	File file = new File("C:/Users/Abdelhameed Emad/Documents/docroot");
		files = file.list();
    	q = new LinkedList<String>();
    	this.sockets = sockets;							//The connections with each clientThread
    	Clients = new ArrayList<ClientThread>();		//The threads connected to the clients
    	}

	@SuppressWarnings("static-access")
	public void run(){
    	while(true){
    		System.out.print("");
    		for (int i = 0; i < sockets.size(); i++) {		//Check if any Client sent a request
    			try {
					inFromThreads = new BufferedReader(new InputStreamReader(sockets.get(i).getInputStream()));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
    			try {
					if(inFromThreads.ready()){
						//if there is a client who sent a request the request is added to the queue
						System.out.println("The server adding a request to the queue");
						String s = inFromThreads.readLine();
						System.out.println(s);
						s = inFromThreads.readLine();
						System.out.println(s);
						q.add(s);
					}
					else
						continue;
				} catch (IOException e) {
					e.printStackTrace();
				}
    			}
    		if(!q.isEmpty()){
    		for (int i = 0; i <q.size(); i++) {
    			System.out.println("The server is processing a request");
				String h = q.remove();
				String[] wf = h.split(" ");
				String wantedFile = wf[0];
				String ip = wf[1];
				this.Done = false;
				for (int j = 0; j < files.length; j++) {
					if(wantedFile.equals(files[j])){	//If the wanted file exist in the files list
						FileInputStream fl = null;
						try {
							fl = new FileInputStream("C:/Users/Abdelhameed Emad/Documents/docroot/"+ files[j]);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}			//Extract the file from the docroot
						byte b[] = new byte[10000];
						try {
							fl.read(b,0,b.length);
						} catch (IOException e) {
							e.printStackTrace();
						}
						for (int k = 0; k < Clients.size(); k++) {
							if(Clients.get(k).getIp().equals(ip)){
								//Sending the file to the Client who sent the request using his IP address
								sendResponse(k, j, Done, b);
								System.out.println("done");
								Done = true;
								break;
							}}}
					
						}
			if (!Done) {
				sendError404(wantedFile, ip);
    		}}}
    		else
    			continue;
    		}}
	
	@SuppressWarnings("static-access")
	public void setSockets(ArrayList<Socket> sockets) {
		this.sockets = sockets;
	}

    public void setClients(ArrayList<ClientThread> clients) {
    	Clients = clients;}
    
    private void sendResponse(int k, int j, boolean Done, byte b[]){
		DataOutputStream oss = null;
		try {
			oss = new DataOutputStream(Clients.get(k)
					.getConnectionSocket().getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		OutputStream os = null;
		try {
			os = Clients.get(k).getConnectionSocket().getOutputStream();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			oss.writeBytes("200 OK" + " HTTP/1.1" +"\n");
			oss.writeBytes("Date: "+java.time.LocalDate.now().toString() + " " + "Time: " +
					java.time.LocalTime.now().toString() +"\n");
			oss.writeBytes(files[j].substring(files[j].indexOf(".")) +"\n");
			oss.writeBytes("Keep-alive" +"\n");
			oss.writeBytes(files[j] + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			os.write(b,0,b.length);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private void sendError404(String wantedFile, String IP){
		for (int k = 0; k < Clients.size(); k++) {
			if(Clients.get(k).getIp().equals(IP)){
				//Sending the file to the Client who sent the request using his IP address
				DataOutputStream oss = null;
				try {
					oss = new DataOutputStream(Clients.get(k)
							.getConnectionSocket().getOutputStream());
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {		//Sending a response
					oss.writeBytes("404 not Found" + " HTTP/1.1" +"\n");
					oss.writeBytes("Date: "+java.time.LocalDate.now().toString() + " " +  "Time: " + 
					java.time.LocalTime.now().toString() +"\n");
					oss.writeBytes(wantedFile.substring(wantedFile.indexOf(".")) +"\n");
					oss.writeBytes("Keep-alive" +"\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("Error: 404 not Found");
			}}}}