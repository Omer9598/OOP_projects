import java.util.Scanner;

public class Chat {
    public static void main(String[] args) {

        // illegal replies arrays
        String[] bot_1_illegal_replies = {"what ", "say I should say "};
        String[] bot_2_illegal_replies = {"whaaat ", "say "};

        // creating an array of bots
        ChatterBot[] bots_arr = {new ChatterBot("Sammy", bot_1_illegal_replies),
                new ChatterBot("Ruthy", bot_2_illegal_replies)};

        // create the first statement for the bots
        String statement =  "Omer";

        Scanner scanner = new Scanner(System.in);

        // starting the infinite loop
        while (true) {
            for (ChatterBot bot : bots_arr) {
                statement = bot.replyTo(statement);
                System.out.print(bot.getName() + ": " + statement + " ");
                // wait for the user to press enter
                scanner.nextLine();
            }
        }
    }
}

