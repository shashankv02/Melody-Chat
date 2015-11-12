package com.shashank.MelodyChat.server;

import java.net.InetAddress;

public class ClientDetails {
	
	public String name;
	public InetAddress address;
	public int port;
	private final int Id;
	public int attempt = 0;
	
	
	public ClientDetails(String name, InetAddress address, int port, final int Id) {
		this.name = name;
		this.address = address;
		this.port = port;
		this.Id = Id;
	}
	
	public int getId() {
		return Id;
	}

}
