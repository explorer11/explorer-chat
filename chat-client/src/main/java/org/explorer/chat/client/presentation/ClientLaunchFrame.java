package org.explorer.chat.client.presentation;

import java.awt.Dimension;
import java.io.Serial;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import org.explorer.chat.common.ChatMessage;
import org.explorer.chat.common.ChatMessageType;

public class ClientLaunchFrame extends JFrame implements ChatClientFrame {

	@Serial
	private static final long serialVersionUID = -6123304171671691527L;
	
	private JButton sendButton;
	private JTextComponent serverIPComponent;

	public ClientLaunchFrame() {
		super("Chat Client Launch");
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		setContentPane(buildJPanel());
		
		this.pack();
		setSize(400,150);
		setVisible(true);
	}
	
	private JPanel buildJPanel(){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		
		JLabel jLabel = new JLabel("server ip (the server should have given it to you) : ");
		panel.add(jLabel);
		
		JTextField jTextField = new JTextField("");
		jTextField.setMaximumSize(new Dimension(400, 20));
		this.serverIPComponent = jTextField;
		panel.add(jTextField);
		
		JButton button = new JButton("Envoyer");
		button.setPreferredSize(new Dimension(100, 20));
		this.sendButton = button;
		panel.add(button);
		
		return panel;
	}

	@Override
	public JButton getSendButton() {
		return sendButton;
	}

	@Override
	public JTextComponent getTextComponent() {
		return serverIPComponent;
	}

	@Override
	public ChatMessage chatMessage() {
		return new ChatMessage(ChatMessageType.WELCOME, "", "");
	}

}
