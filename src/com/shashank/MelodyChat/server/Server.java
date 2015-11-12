package com.shashank.MelodyChat.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
		
	}
	
	private void manageClients() {
		manageTh = new Thread("Manage Thread") {
			public void run () {
				
			}
		};
		manageTh.start();
		
	}
	
	private void processData(DatagramPacket packet) {
		String string = new String(packet.getData(),0,packet.getLength());
		if(string.startsWith("/c/")){ 
			int id = UniqueIdentifier.getIdentifier(); //Don't call this method carelessly.. Each call returns a new id.
			String name = string.substring(3,string.length());
			clients.add(new ClientDetails(name,packet.getAddress(),packet.getPort(),id));
			System.out.println(name+" connected from "+packet.getAddress().toString()+":"+packet.getPort());
			System.out.println("Added "+name+" to client list, UID: "+id);
		}
		else {
			System.out.println(string);
		}
		
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
	
		
		
		
}


