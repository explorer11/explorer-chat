package org.explorer.chat.client.presentation;

import java.awt.Dimension;
import java.awt.LayoutManager;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import org.explorer.chat.common.ChatMessage;
import org.explorer.chat.common.ChatMessageType;

public class ChatClientFrame extends JFrame implements IChatClientFrame {

	private static final long serialVersionUID = 1L;
	
	private JTextComponent textComponent;
	private JTextArea messagesTextArea;
	private JTextArea usersTextArea;
	private JButton sendButton;

	public ChatClientFrame() {
		super("Chat window");
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		setContentPane(buildJPanel());
		
		setSize(800,600);
		setVisible(true);
		this.pack();
	}
	
	private JPanel buildJPanel(){
		JPanel panel = new JPanel();
		panel.setLayout(defineGroupLayout(panel));
		return panel;
	}
	
	private JScrollPane buildMessagesTextArea(){
		JTextArea jTextArea = new JTextArea();
		jTextArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(jTextArea);
		scrollPane.setPreferredSize(new Dimension(600,500));
		this.messagesTextArea = jTextArea;
		return scrollPane;
	}
	
	private JScrollPane buildUsersTextArea(){
		JTextArea jTextArea = new JTextArea();
		jTextArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(jTextArea);
		scrollPane.setPreferredSize(new Dimension(200,500));
		this.usersTextArea = jTextArea;
		return scrollPane;
	}
	
	private JTextComponent buildTextInput(){
		JTextField jTextField = new JTextField("");
		jTextField.setMaximumSize(new Dimension(600, 20));
		this.textComponent = jTextField;
		return jTextField;
	}
	
	private JButton buildSendButton(){
		sendButton = new JButton("Envoyer");
		sendButton.setPreferredSize(new Dimension(100, 20));
		return sendButton;
	}

	private LayoutManager defineGroupLayout(JPanel panneau){
		GroupLayout layout = new GroupLayout(panneau);
		panneau.setLayout(layout);
        JScrollPane messagesScrollPane = buildMessagesTextArea();
        JScrollPane usersScrollPane = buildUsersTextArea();
        JTextComponent jTextComponent = buildTextInput();
        JButton jButton = buildSendButton();
        layout.setHorizontalGroup(
                layout.createParallelGroup()
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(messagesScrollPane)
                        .addGap(5)
                        .addComponent(usersScrollPane)
                    )
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jTextComponent)
                        .addGap(5)
                        .addComponent(jButton)
                        .addGap(5)
                        
                    )
                );
       
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup()
                        .addComponent(messagesScrollPane)
                        .addComponent(usersScrollPane)
                    )
                    .addGap(5)
                    .addGroup(layout.createParallelGroup()
                        .addComponent(jTextComponent)
                        .addComponent(jButton)
                    )
                );
        return layout;
	}
	
	public JTextComponent getTextComponent() {
		return textComponent;
	}
	
	@Override
	public ChatMessage chatMessage() {
		return new ChatMessage.ChatMessageBuilder()
				.withMessageType(ChatMessageType.SENTENCE)
				.withFromUserMessage("")
				.withMessage(getTextComponent().getText())
				.build();
	}

	public JTextArea getMessagesTextArea() {
		return messagesTextArea;
	}

	public JTextArea getUsersTextArea() {
		return usersTextArea;
	}

	@Override
	public JButton getSendButton() {
		return sendButton;
	}
}

