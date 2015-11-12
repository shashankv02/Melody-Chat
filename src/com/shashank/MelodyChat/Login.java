package com.shashank.MelodyChat;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;

public class Login extends JFrame {

	private JPanel contentPane;
	private JTextField textName;
	private JTextField textAddress;
	private JTextField textPort;
	private DatagramSocket socket;
	private InetAddress serverIp;



	/**
	 * Create the frame.
	 */
	public Login() {
		setTitle("Login");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 407, 643);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textName = new JTextField();
		textName.setBounds(102, 119, 196, 22);
		contentPane.add(textName);
		textName.setColumns(10);
	
		textAddress = new JTextField();
		textAddress.setColumns(10);
		textAddress.setBounds(102, 232, 196, 22);
		contentPane.add(textAddress);
		
		textPort = new JTextField();
		textPort.setColumns(10);
		textPort.setBounds(102, 344, 196, 22);
		contentPane.add(textPort);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name=textName.getText();
				String address=textAddress.getText();
				int port=Integer.parseInt(textPort.getText());
				doLogin(name, address, port);
			}
		});
		btnLogin.setBounds(130, 495, 140, 41);
		contentPane.add(btnLogin);
		
		JLabel lblName = new JLabel("Name");
		lblName.setBounds(184, 90, 33, 16);
		contentPane.add(lblName);
		
		JLabel lblAddress = new JLabel("Address");
		lblAddress.setBounds(172, 203, 56, 16);
		contentPane.add(lblAddress);
		
		JLabel lblPort = new JLabel("Port");
		lblPort.setBounds(186, 314, 28, 16);
		contentPane.add(lblPort);
	}
	
	private void doLogin(String name, String address, int port) {
		dispose();
		if(openConnection()) { 
			new Client(name,address, port, socket);
			System.out.println(name + " "+ address+":"+port);
		}
	}
	
	private boolean openConnection() {
		try {
			socket = new DatagramSocket();
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						Login frame = new Login();
						frame.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
}
