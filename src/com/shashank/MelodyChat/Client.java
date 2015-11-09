package com.shashank.MelodyChat;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TexturePaint;
import java.awt.font.TextMeasurer;

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

public class Client extends JFrame {

	private JPanel contentPane;
	private JTextField textMessage;
	private String name;
	private String address;
	private int port;
	private JTextArea txtrHistory;
	
	public Client(String name, String address, int port) {
		/*this.name=name;
		this.address=address;
		this.port=port;*/
		createClientWindow();
		sendToHistory(name+" connected from ");
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
		textMessage.setColumns(10);
		
		JButton btnSend = new JButton("Send");
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
	
	public void sendToHistory(String message) {
		if(message!=null){
			txtrHistory.append(	message);
		}
		
	}
}

	
