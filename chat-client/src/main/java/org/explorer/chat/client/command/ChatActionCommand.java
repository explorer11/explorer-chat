package org.explorer.chat.client.command;

import org.explorer.chat.client.ServerMessageReader;
import org.explorer.chat.client.presentation.ChatClientFrame;
import org.explorer.chat.client.presentation.ClientConnectionFrame;
import org.explorer.chat.client.presentation.IChatClientFrame;
import org.explorer.chat.common.ChatMessage;
import org.explorer.chat.common.UsersList;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatActionCommand implements ActionListener, NonStopCommand, RunCommand {

    private volatile boolean mustRun = true;
    private final WindowListenerCreation windowListenerCreation = new WindowListenerCreation();
    private final CommandRunner commandRunner = new CommandRunner(this);

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
        windowListenerCreation.define(this, clientFrame);
	}

	@Override
	public void openFrame(){
		clientConnectionFrame = new ClientConnectionFrame();
		activeFrame = clientConnectionFrame;
        clientConnectionFrame.prepareButtons(this);
        windowListenerCreation.define(this, clientConnectionFrame);
	}

    private void closeConnectionFrame(){
		clientConnectionFrame.setVisible(false);
		clientConnectionFrame.dispose();
	}

	@Override
	public void actionPerformed(final ActionEvent actionEvent) {
		final ChatMessage chatMessage = activeFrame.chatMessage();
		activeFrame.getTextComponent().setText("");
		System.out.println("Command::actionPerformed chatMessage " + chatMessage);
		
		try {
			ObjectOutputStream out = new ObjectOutputStream(toServerOutputStream);
			out.writeObject(chatMessage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    @Override
    public void triggerStop() {
        mustRun = false;
    }

    @Override
    public void run() {
		final ServerMessageReader serverMessageReader= new ServerMessageReader(
				this.fromServerInputStream, this);
		this.messageReaderExecutorService.submit(serverMessageReader);
        commandRunner.run();
	}

    @Override
    public boolean mustRun() {
        return mustRun;
    }

    @Override
    public void after() {
		this.messageReaderExecutorService.shutdownNow();
		System.out.println("ChatActionCommand::after");
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
