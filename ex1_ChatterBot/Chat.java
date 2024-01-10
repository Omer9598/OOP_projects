import java.util.Scanner;

/**
 * This class contains the main function to run the code
 */
public class Chat {
    /**
     * The main function to run the code
     */
    public static void main(String[] args) {
        // Illegal replies arrays
        String[] bot1IllegalReplies = {"what ", "say I should say "};
        String[] bot2IllegalReplies = {"whaaat ", "say say "};
        // Legal replies arrays
        String[] bot1LegalReplies = {"You want me to say <phrase>, do you?" +
                " alright: <phrase>"};
        String[] bot2LegalReplies = {"say <phrase>? okay: <phrase>"};
        // Creating an array of bots
        ChatterBot[] botsArr = {new ChatterBot("Sammy", bot1IllegalReplies,
                bot1LegalReplies),
                new ChatterBot("Ruthy", bot2IllegalReplies,
                        bot2LegalReplies)};
        // Create the first statement for the bots
        String statement =  "Omer";
        Scanner scanner = new Scanner(System.in);
        // Starting the infinite loop
        while (true) {
            for (ChatterBot bot : botsArr) {
                statement = bot.replyTo(statement);
                System.out.print(bot.getName() + ": " + statement + " ");
                // wait for the user to press enter
                scanner.nextLine();
            }
        }
    }
}
