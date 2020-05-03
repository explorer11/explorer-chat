package org.explorer.chat.client.command;

import org.explorer.chat.client.ClientArgs;
import org.explorer.chat.client.presentation.ClientLaunchFrame;
import org.explorer.chat.common.ServerPort;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ClientLaunchCommand implements ActionListener, NonStopCommand, RunCommand {

	private volatile boolean mustRun = true;
    private final WindowListenerCreation windowListenerCreation = new WindowListenerCreation();
    private final CommandRunner commandRunner = new CommandRunner(this);

    private ClientLaunchFrame clientLaunchFrame;
    private ClientArgs clientArgs = null;
	
	@Override
    public void openFrame(){
		clientLaunchFrame = new ClientLaunchFrame();
        clientLaunchFrame.prepareButtons(this);
        windowListenerCreation.define(this, clientLaunchFrame);
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		final String serverIP = clientLaunchFrame.getTextComponent().getText();

		clientArgs = new ClientArgs(serverIP);
		
		clientLaunchFrame.setVisible(false);
		clientLaunchFrame.dispose();
        triggerStop();
	}

    @Override
    public void triggerStop() {
        mustRun = false;
    }

    @Override
    public boolean mustRun() {
        return mustRun;
    }

    @Override
    public void run() {
        commandRunner.run();
    }

    @Override
	public void after() {
		System.out.println("ClientLaunchCommand:after::get client input");
		
		if(this.clientArgs != null){
			System.out.println("ClientLaunchCommand:after::run with server IP : " + clientArgs.getServerIP());
			serverConnection();
		}
	}
	
	private void serverConnection() {

        ChatActionCommand chatActionCommand = null;
        int localPort = 4444;
        while(chatActionCommand == null && localPort < 5000) {
            try (
                    Socket withServerSocket = new Socket(InetAddress.getByName(this.clientArgs.getServerIP()),
                            ServerPort.PORT,
                            InetAddress.getLocalHost(), localPort);

                    OutputStream outputStream = withServerSocket.getOutputStream();
                    InputStream inputStream = withServerSocket.getInputStream()
            ) {

                System.out.println("ClientLaunchCommand::serverConnection : client local port is " + localPort);

                chatActionCommand = new ChatActionCommand(inputStream, outputStream);
                chatActionCommand.run();

                System.out.println("ClientLaunchCommand::serverConnection:end with client local port " + localPort);

            } catch (IOException e) {
                e.printStackTrace();
                localPort++;
            } finally {
                System.out.println("serverConnection::finally");
            }
        }

        if(chatActionCommand == null) {
            System.out.println("ClientLaunchCommand::serverConnection : fail to connect");
        }
	}
}
