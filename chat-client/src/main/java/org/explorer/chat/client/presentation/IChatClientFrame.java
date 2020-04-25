package org.explorer.chat.client.presentation;

import java.awt.event.ActionListener;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JRootPane;
import javax.swing.text.JTextComponent;

import org.explorer.chat.common.ChatMessage;

public interface IChatClientFrame {
	
	JRootPane getRootPane();

	JButton getSendButton();
	
	JTextComponent getTextComponent();

    default void prepareButtons(final ActionListener actionListener){
        getSendButton().addActionListener(actionListener);
        getRootPane().setDefaultButton(this.getSendButton());
    }
	
	ChatMessage chatMessage();
	
	void addWindowListener(WindowListener l);
}
