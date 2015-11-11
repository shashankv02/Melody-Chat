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

public class Client extends JFrame {

	private JPanel contentPane;
	private JTextField textMessage;
	private String name;
	private String serverAddress;
	private int port;
	private JTextArea txtrHistory;
	
	private InetAddress serverIp;
	private Thread send;
	private DatagramSocket socket;
	
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
		String stringToServer = name +"connected from "+ serverAddress+":"+port;
		send(stringToServer.getBytes());
	}
	

	

	private String receive() {
		byte[] data = new byte[1024];
		DatagramPacket packet=new DatagramPacket(data, data.length);
		try {
			socket.receive(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String recvdMessage=new String(packet.getData());
		return recvdMessage;
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
					send((textMessage.getText()).getBytes());
					sendToHistory();
					//System.out.println(textMessage.getText());
					
				}
			}
		});
		textMessage.setColumns(10);
		
		JButton btnSend = new JButton("Send");
		
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			
				send((textMessage.getText()).getBytes());
				sendToHistory();
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(txtrHistory, GroupLayout.DEFAULT_SIZE, 868, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(textMessage, GroupLayout.PREFERRED_SIZE, 764, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnSend, GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(txtrHistory, GroupLayout.PREFERRED_SIZE, 476, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(textMessage, GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
						.addComponent(btnSend, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);
		textMessage.requestFocus();
		
	}
	
	private void sendToHistory(String message) {
		if(message!=null){
			txtrHistory.append(	message+"\r\n");
		}	
	}
	
	private void sendToHistory() {
		String message = textMessage.getText();
		txtrHistory.append("You: "+message+"\r\n");
		textMessage.setText("");
	}
}

	
