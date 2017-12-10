package org.explorer.chat.client.command;

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

import org.explorer.chat.client.ClientArgs;
import org.explorer.chat.client.presentation.ClientLaunchFrame;
import org.explorer.chat.client.presentation.IChatClientFrame;

public class ClientLaunchCommand implements ActionListener {

	private ClientLaunchFrame clientLaunchFrame;
	private BlockingQueue<String> queue;
	
	private ClientArgs clientArgs = null;
	
	protected IChatClientFrame activeFrame;
	
	protected void openFrame(){
		clientLaunchFrame = new ClientLaunchFrame();
		prepareClientFrameListening(clientLaunchFrame);
	}
	
	protected void prepareClientFrameListening(IChatClientFrame aClientFrame){
		activeFrame = aClientFrame;
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
		String serverIP = clientLaunchFrame.getTextComponent().getText();
		String localPortStr = clientLaunchFrame.getPortComponent().getText();
		
		final Integer localPort = Integer.valueOf(localPortStr);

		clientArgs = new ClientArgs(null, serverIP, localPort);
		
		clientLaunchFrame.setVisible(false);
		clientLaunchFrame.dispose();
		closeFrame();
	}
	
	public void start() throws InterruptedException {
		this.queue = new ArrayBlockingQueue<String>(1);
		System.out.println("start::wait client input");
		this.openFrame();
		// wait until the client has filled the inputs
		this.queue.take();
		this.end();
	}

	protected void end() throws InterruptedException {
		System.out.println("end::get client input");
		
		if(this.clientArgs != null){
			System.out.println("end::run with server IP : " + clientArgs.getServerIP() + " and local port " + clientArgs.getLocalPort());
			connectToServer();
		}
	}
	
	private void connectToServer() throws InterruptedException{
		
		try (
			Socket withServerSocket = new Socket(InetAddress.getByName(this.clientArgs.getServerIP()), 60000, InetAddress.getLocalHost(), this.clientArgs.getLocalPort());
				
			OutputStream outputStream = withServerSocket.getOutputStream();
			InputStream inputStream = withServerSocket.getInputStream();
		) {
			
			ChatActionCommand chatActionCommand = new ChatActionCommand(inputStream, outputStream);			
			chatActionCommand.start();
			
			System.out.println("ClientLaunchCommand::connectToServer:end");
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			System.out.println("connectToServer::finally");
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
