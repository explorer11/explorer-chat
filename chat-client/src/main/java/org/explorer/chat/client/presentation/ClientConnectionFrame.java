package org.explorer.chat.client.presentation;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import org.explorer.chat.common.ChatMessage;
import org.explorer.chat.common.ChatMessageType;

public class ClientConnectionFrame extends JFrame implements IChatClientFrame {

	private static final long serialVersionUID = 1L;
	
	private JLabel errorLabel;
	private JButton sendButton;
	private JTextComponent textComponent;

	public ClientConnectionFrame() {
		super("Chat Connection");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		setContentPane(buildJPanel());
		
		this.pack();
		setSize(400,150);
		setVisible(true);
	}
	
	private JPanel buildJPanel(){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		
		JLabel jLabel = new JLabel("Identifiant : ");
		panel.add(jLabel);
		
		JLabel errorJLabel = new JLabel("");
		this.errorLabel = errorJLabel;
		panel.add(errorJLabel);
		
		JTextField jTextField = new JTextField("");
		jTextField.setMaximumSize(new Dimension(400, 20));
		this.textComponent = jTextField;
		panel.add(jTextField);
		
		JButton bouton = new JButton("Envoyer");
		bouton.setPreferredSize(new Dimension(100, 20));
		this.sendButton = bouton;
		panel.add(bouton);
		
		return panel;
	}
	
	public JLabel getErrorLabel() {
		return errorLabel;
	}

	@Override
	public JButton getSendButton() {
		return sendButton;
	}

	@Override
	public JTextComponent getTextComponent() {
		return textComponent;
	}

	@Override
	public ChatMessage chatMessage() {
		return new ChatMessage.ChatMessageBuilder()
				.withMessageType(ChatMessageType.WELCOME)
				.withFromUserMessage(getTextComponent().getText())
				.withMessage("")
				.build();
	}

}
