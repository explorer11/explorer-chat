package org.explorer.chat.client.command;

import org.explorer.chat.client.presentation.IChatClientFrame;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

class WindowListenerCreation {

    void define(final NonStopCommand command,
                final IChatClientFrame clientFrame){
        final WindowListener windowListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e){
                command.triggerStop();
            }
        };

        clientFrame.addWindowListener(windowListener);
    }
}
