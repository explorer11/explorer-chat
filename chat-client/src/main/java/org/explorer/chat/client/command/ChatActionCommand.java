package org.explorer.chat.client.command;

import org.explorer.chat.client.ServerMessageReader;
import org.explorer.chat.client.presentation.ChatClientFrame;
import org.explorer.chat.client.presentation.ClientConnectionFrame;
import org.explorer.chat.client.presentation.IChatClientFrame;
import org.explorer.chat.common.ChatMessage;
import org.explorer.chat.common.UsersList;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatActionCommand implements ActionListener {

    private volatile boolean run = true;

	private ChatClientFrame clientFrame;
	private ClientConnectionFrame clientConnectionFrame;
	private IChatClientFrame activeFrame;
	
	private final InputStream fromServerInputStream;
	private final OutputStream toServerOutputStream;
	
	private final ExecutorService messageReaderExecutorService = Executors.newFixedThreadPool(1);
	
	ChatActionCommand(InputStream inputStream, OutputStream outputStream) {
		super();
		this.fromServerInputStream = inputStream;
		this.toServerOutputStream = outputStream;
	}

	private void openClientFrame(){
		clientFrame = new ChatClientFrame();
        activeFrame = clientFrame;
        clientFrame.prepareButtons(this);
        listenClose(clientFrame);
	}

	private void openFrame(){
		clientConnectionFrame = new ClientConnectionFrame();
		activeFrame = clientConnectionFrame;
        clientConnectionFrame.prepareButtons(this);
        listenClose(clientConnectionFrame);
	}

    private void listenClose(IChatClientFrame clientFrame){
        WindowListener windowListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e){
                run = false;
            }
        };
        clientFrame.addWindowListener(windowListener);
    }

    private void closeConnectionFrame(){
		clientConnectionFrame.setVisible(false);
		clientConnectionFrame.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ChatMessage chatMessage = activeFrame.chatMessage();
		activeFrame.getTextComponent().setText("");
		System.out.println("Command::actionPerformed chatMessage " + chatMessage);
		
		try {
			ObjectOutputStream out = new ObjectOutputStream(toServerOutputStream);
			out.writeObject(chatMessage);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void start() {
		ServerMessageReader serverMessageReader= new ServerMessageReader(
				this.fromServerInputStream, this);
		this.messageReaderExecutorService.submit(serverMessageReader);
        System.out.println(this.getClass().getName() + ":start::wait client input");
        this.openFrame();
        while (run) {
            Thread.onSpinWait();
        }
        this.stop();
	}
	
	private void stop() {
		this.messageReaderExecutorService.shutdownNow();
		System.out.println("ChatActionCommand::stop");
	}
	
	public void performServerMessage(ChatMessage chatMessage) {
		System.out.println("ChatActionCommand::performServerMessage :" + chatMessage.getMessageType());
		switch(chatMessage.getMessageType()) {
		case WELCOME :
			closeConnectionFrame();
			openClientFrame();
			break;
		case CONNECTION_ERROR :
			clientConnectionFrame.getErrorLabel().setText(chatMessage.getMessage());
			break;
		case LIST :
			String usersListAsText = new UsersList().getUsersListAsText(
					chatMessage.getMessage());
			clientFrame.getUsersTextArea().setText(usersListAsText);
			break;
		case LEAVING:
			String messageDepart = chatMessage.getMessage() + " est parti";
			clientFrame.getMessagesTextArea().append(messageDepart + "\n");
			break;
		case ARRIVAL :
			String messageArrivee = chatMessage.getMessage() + " arrive";
			clientFrame.getMessagesTextArea().append(messageArrivee + "\n");
			break;
		case SENTENCE :
			String messagePhrase = chatMessage.getFromUserMessage() + " : " + chatMessage.getMessage();
			clientFrame.getMessagesTextArea().append(messagePhrase + "\n");
			break;
		default :
		    break;
		}
	}

}
