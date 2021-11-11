package org.explorer.chat.save;

import org.explorer.chat.common.ChatMessage;

import java.io.IOException;
import java.util.List;

public interface MessageRead {

    List<ChatMessage> findAll() throws IOException;
}
