package org.explorer.chat.client.command;

interface RunCommand {

    void run();
    void openFrame();
    boolean mustRun();
    void after();
}
