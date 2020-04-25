package org.explorer.chat.client.command;

import org.explorer.chat.client.presentation.ChatClientFrame;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

class WindowListenerCreation {

    void define(final NonStopCommand command,
                final ChatClientFrame clientFrame){
        final WindowListener windowListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e){
                command.triggerStop();
            }
        };

        clientFrame.addWindowListener(windowListener);
    }
}
