package org.explorer.chat.elastic;

import java.io.IOException;
import java.util.Scanner;

import static org.explorer.chat.elastic.Action.EXIT;

public class Admin {

    public static void main(String[] args) throws IOException {
        try (Scanner keyboard = new Scanner(System.in)) {
            boolean askInput = true;
            while (askInput) {

                System.out.print("command> ");
                final String line = keyboard.nextLine();

                final Action action = Action.from(line);

                if(!EXIT.equals(action)) {
                    final ElasticAction elasticAction = action.getElasticAction();
                    elasticAction.execute(keyboard::nextLine);
                } else {
                    askInput = false;
                }

            }
        }
    }
}
