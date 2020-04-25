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

public class ClientLaunchCommand implements ActionListener, NonStopCommand {

	private volatile boolean run = true;
    private final WindowListenerCreation windowListenerCreation = new WindowListenerCreation();

    private ClientLaunchFrame clientLaunchFrame;
    private ClientArgs clientArgs = null;
	
	private void openFrame(){
		clientLaunchFrame = new ClientLaunchFrame();
        clientLaunchFrame.prepareButtons(this);
        windowListenerCreation.define(this, clientLaunchFrame);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		final String serverIP = clientLaunchFrame.getTextComponent().getText();

		clientArgs = new ClientArgs(serverIP);
		
		clientLaunchFrame.setVisible(false);
		clientLaunchFrame.dispose();
        triggerStop();
	}

    @Override
    public void triggerStop() {
        run = false;
    }

    public void start() {
		System.out.println("start::wait client input");
		this.openFrame();
		// wait until the client has filled the inputs
        while (run) {
            Thread.onSpinWait();
        }
        this.stop();
	}

	private void stop() {
		System.out.println("" + this.getClass().getName() + ":stop::get client input");
		
		if(this.clientArgs != null){
			System.out.println("" + this.getClass().getName() + ":stop::run with server IP : " + clientArgs.getServerIP());
			connectToServer();
		}
	}
	
	private void connectToServer() {

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

                System.out.println("ClientLaunchCommand::connectToServer : client local port is " + localPort);

                chatActionCommand = new ChatActionCommand(inputStream, outputStream);
                chatActionCommand.start();

                System.out.println("ClientLaunchCommand::connectToServer:end with client local port " + localPort);

            } catch (IOException e) {
                e.printStackTrace();
                localPort++;
            } finally {
                System.out.println("connectToServer::finally");
            }
        }

        if(chatActionCommand == null) {
            System.out.println("ClientLaunchCommand::connectToServer : fail to connect");
        }
	}
}
