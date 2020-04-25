package org.explorer.chat.client.command;

class CommandRunner {

    private final RunCommand runCommand;

    CommandRunner(final RunCommand runCommand) {
        this.runCommand = runCommand;
    }

    void run() {
        System.out.println(this.getClass().getName() + ":run::wait client input");
        runCommand.openFrame();
        while (runCommand.mustRun()) {
            Thread.onSpinWait();
        }
        runCommand.after();
    }
}
