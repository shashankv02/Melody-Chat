package com.shashank.MelodyChat.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.ws.handler.MessageContext;

public class Server implements Runnable {
	
	private DatagramSocket socket;
	private Thread serverTh, manageTh, sendTh, receiveTh;
	private boolean running = true;
	private int port;
	
	
	private List<ClientDetails> clients = new ArrayList<ClientDetails>();
	
	public Server(int port) {
		this.port=port;
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
			return;
		}
		serverTh = new Thread(this,"Server Thread" );
		serverTh.start();
	}
	
	public void run() {
		//running=true;
		System.out.println("Server started on port "+port);
		//manageClients();
		receive();
		manageClients();
		
	}
	
	private void manageClients() {
		manageTh = new Thread("Manage Thread") {
			public void run () {
				while(running) {
					sendToAll("/p/ping");
					try {
						sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					for(int i=0;i<clients.size();i++) {
						if(++(clients.get(i).attempt)>5) {
							String name = clients.get(i).name;
							clients.remove(i);
							System.out.println(name+ " has timed out");
							sendToAll("/m/"+name+ " has timed out");
						}
						
					}
				}
			}
		};
		manageTh.start();
		
	}
	
	private void processData(DatagramPacket packet) {
		String string = new String(packet.getData(),0,packet.getLength());
		
		if(string.startsWith("/p/")) {   //client ping responce message will be of format /p/uid
			int clientUid=Integer.parseInt(string.substring(3));
			System.out.println("recieved ping");
			for(int i=0;i<clients.size();i++) {
				if(clients.get(i).getId() == clientUid) {
					clients.get(i).attempt = 0;
					break;
				}
			}
		}
		
		else if(string.startsWith("/m/")){
			String uidplusmessage = string.substring(3); //3 is begin index of the message. first 3 bytes are /m/
			///m/4556/hey
		//	String message=uidplusmessage.substring(2);
			int clientUid = Integer.parseInt(uidplusmessage.split("/")[0]); 
			String message = uidplusmessage.split("/")[1];
			//System.out.println("clientUid");
			String clientName="user";
			for(int i=0;i<clients.size();i++) {
				if(clients.get(i).getId() == clientUid) {
					clientName = clients.get(i).name;
					break;
				}
			}
			//System.out.println(clientName);
			sendToAll("/m/"+clientName+": "+message); 
			//System.out.println("/m/"+clientName+": "+message);
		}
		
		else if(string.startsWith("/c/")){ 
			int id = UniqueIdentifier.getIdentifier(); //Don't call this method carelessly.. Each call returns a new id.
			String name = string.substring(3,string.length());
			clients.add(new ClientDetails(name,packet.getAddress(),packet.getPort(),id));
			System.out.println(name+" connected from "+packet.getAddress().toString()+":"+packet.getPort());
			System.out.println("Added "+name+" to client list, UID: "+id);
			send("/c/"+id, packet.getAddress(),packet.getPort());
		}
		
		else if(string.startsWith("/d/")) {
			int clientUid=Integer.parseInt(string.substring(3));
			System.out.println("DC request from "+packet.getAddress().toString());
			handleDisconnect(clientUid);
		}
		
		else {
			System.out.println("Unrecognized data");
		}
		
	}
	
	private void sendToAll(String message) {
		for(int i=0;i<clients.size();i++) {
			ClientDetails client = clients.get(i);
			send(message, client.address, client.port);
		}
	}
	
	private void send(String message, InetAddress address, int port) {
		sendTh = new Thread("Send Thread") {
			public void run() {
				byte[] data = new byte[1024];
				data = message.getBytes();
				DatagramPacket packet = new DatagramPacket(data,data.length);
				packet.setAddress(address);
				packet.setPort(port);
				try {
					socket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		sendTh.start();
	}
	
	private void receive() {
		//System.out.println("Inside receive function");
		receiveTh = new Thread("Receive Thread") {
			public void run() { 
				//System.out.println("Inside receive thread");
				//Implement receive
				while(running) {
					//System.out.println("Inside receive thread loop");
					byte[] data =new byte[1024]; //1 KB
					
					DatagramPacket packet=new DatagramPacket(data, data.length);
					try {
						//System.out.println("going to receive wait state");
						socket.receive(packet);
						//System.out.println("not blocked by receive!!");
					} catch (IOException e) {
						e.printStackTrace();
					}
					processData(packet);
				}
			}
		};
		receiveTh.start();
		
	}
	
	private void handleDisconnect(int uid) {
		boolean flag=false;
		ClientDetails disconnectedClient=null;
		for(int i=0;i<clients.size();i++) {
			if(clients.get(i).getId() == uid) {
				disconnectedClient = clients.get(i);
				clients.remove(i);
				flag=true;
				break;
			}
		}		
		if(flag) {
			String dcMessage=disconnectedClient.name+" has disconnected";
			sendToAll("/m/"+dcMessage);
		}
		
	}
	
		
		
		
}


