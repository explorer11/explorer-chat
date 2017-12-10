package org.explorer.chat.client.presentation;

import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JRootPane;
import javax.swing.text.JTextComponent;

import org.explorer.chat.common.ChatMessage;

public interface IChatClientFrame {
	
	JRootPane getRootPane();

	JButton getSendButton();
	
	JTextComponent getTextComponent();
	
	ChatMessage chatMessage();
	
	void addWindowListener(WindowListener l);
}
