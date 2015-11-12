package com.shashank.MelodyChat;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TexturePaint;
import java.awt.font.TextMeasurer;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JScrollPane;

public class Client extends JFrame {

	private JPanel contentPane;
	private JTextField textMessage;
	private String name;
	private String serverAddress;
	private int port;
	private JTextArea txtrHistory;
	
	private Thread recv;
	private InetAddress serverIp;
	private Thread send;
	private DatagramSocket socket;
	private int uid;
	
	public Client(String name, String servrAddress, int port, DatagramSocket socket) {
		this.name=name;
		this.serverAddress=serverAddress;
		this.port=port;
		this.socket=socket;
		createClientWindow();
		sendToHistory("Connecting to "+servrAddress+":"+port+" as "+name+"\r\n");
		try {
			serverIp=InetAddress.getByName(serverAddress);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		//System.out.println("Constrcuting stringToServer");
		String connectString = "/c/"+name;
		send(connectString.getBytes());
		receive();
	}
	

	private void receive() {
		recv = new Thread("Client Listen Thread") {
			public void run() {
				while(true){
					byte[] data = new byte[1024];
					DatagramPacket packet=new DatagramPacket(data, data.length);
					try {
						socket.receive(packet);
					} catch (IOException e) {
						e.printStackTrace();
					}
					String recvdMessage=new String(packet.getData(),0,packet.getLength());   //reconstructing problem
					//return recvdMessage;
					
					if(recvdMessage.startsWith("/p/")){
						String responce = "/p/"+uid;
						send(responce.getBytes());
					}
					else if(recvdMessage.startsWith("/m/")) {
						sendToHistory(recvdMessage.substring(3));   
					}
					else if(recvdMessage.startsWith("/c/")) {
						uid=Integer.parseInt(recvdMessage.substring(3, recvdMessage.length()));
						sendToHistory("Connected to Server");
					}
					else if(recvdMessage.startsWith("/d/")) {
						sendToHistory("Server shutting down");
					}
				}
				
			}
		};
		recv.start();
				
	}
		
	
	
	private void send(final byte[] data) {
		send = new Thread("Send") {
			public void run() {
				//System.out.println("Constructing packet to send");
				DatagramPacket packet = new DatagramPacket(data,data.length,serverIp,port);
				try {
			//		System.out.println("calling send");
					socket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		send.start();
	}
	
	private String collectMessage() {
		String message = textMessage.getText();
		message="/m/"+uid+"/"+message;
		return message;
	}
	
	private void createClientWindow() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setVisible(true);
		setResizable(false);
		setTitle("Melody Chat");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 869, 580);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		txtrHistory = new JTextArea();
		txtrHistory.setEditable(false);
		
		textMessage = new JTextField();
		textMessage.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER) {
					String message = collectMessage();
					send(message.getBytes());
					//sendToHistory();
					//System.out.println(textMessage.getText());
					
				}
			}
		});
		textMessage.setColumns(10);
		
		JButton btnSend = new JButton("Send");
		
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			
				send(collectMessage().getBytes());
				sendToHistory();
			}
		});
		
		JScrollPane scrollPane = new JScrollPane();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(txtrHistory, GroupLayout.DEFAULT_SIZE, 841, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(textMessage, GroupLayout.PREFERRED_SIZE, 764, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnSend, GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(txtrHistory, GroupLayout.PREFERRED_SIZE, 476, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED))
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(226)))
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(textMessage, GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
						.addComponent(btnSend, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);
		textMessage.requestFocus();
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				sendDisconnect();
			}
		});
		
	}
	
	private void sendToHistory(String message) {
		if(message!=null){
			txtrHistory.append(message+"\r\n");
			textMessage.setText("");
		}	
	}
	
	private void sendToHistory() {
		String message = textMessage.getText();
		txtrHistory.append(message+"\r\n");
		textMessage.setText("");
	}
	
	private void sendDisconnect() {
		String dcMessage="/d/"+uid;
		send(dcMessage.getBytes());
	}
}

	
