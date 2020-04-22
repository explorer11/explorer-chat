package org.explorer.chat.client.command;

import org.explorer.chat.client.ClientArgs;
import org.explorer.chat.client.presentation.ClientLaunchFrame;
import org.explorer.chat.client.presentation.IChatClientFrame;
import org.explorer.chat.common.ServerPort;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ClientLaunchCommand implements ActionListener {

	private ClientLaunchFrame clientLaunchFrame;
	private BlockingQueue<String> queue;
	
	private ClientArgs clientArgs = null;
	
	protected void openFrame(){
		clientLaunchFrame = new ClientLaunchFrame();
		prepareClientFrameListening(clientLaunchFrame);
	}
	
	void prepareClientFrameListening(IChatClientFrame aClientFrame){
		aClientFrame.getSendButton().addActionListener(this);
		aClientFrame.getRootPane().setDefaultButton(aClientFrame.getSendButton());
		
		WindowListener windowListener = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e){
				closeFrame();
			}
		};
		
		aClientFrame.addWindowListener(windowListener);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		final String serverIP = clientLaunchFrame.getTextComponent().getText();

		clientArgs = new ClientArgs(serverIP);
		
		clientLaunchFrame.setVisible(false);
		clientLaunchFrame.dispose();
		closeFrame();
	}
	
	public void start() throws InterruptedException {
		this.queue = new ArrayBlockingQueue<>(1);
		System.out.println("start::wait client input");
		this.openFrame();
		// wait until the client has filled the inputs
		this.queue.take();
		this.end();
	}

	protected void end() throws InterruptedException {
		System.out.println("end::get client input");
		
		if(this.clientArgs != null){
			System.out.println("end::run with server IP : " + clientArgs.getServerIP());
			connectToServer();
		}
	}
	
	private void connectToServer() throws InterruptedException{

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
	
	private void closeFrame() {
		try {
			queue.put("");
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
}
