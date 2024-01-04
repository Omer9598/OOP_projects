import java.util.*;

/**
 * Base file for the ChatterBot exercise.
 * The bot's replyTo method receives a statement.
 * If it starts with the constant REQUEST_PREFIX, the bot returns
 * whatever is after this prefix. Otherwise, it returns one of
 * a few possible replies as supplied to it via its constructor.
 * In this case, it may also include the statement after
 * the selected reply (coin toss).
 *
 * @author Dan Nirel
 */
class ChatterBot {
    static final String REQUEST_PREFIX = "say ";
    private final String name;
    private final Random rand = new Random();
    private final String[] repliesToIllegalRequest;
    private final String[] legalRequestsReplies;

    ChatterBot(String name, String[] repliesToIllegalRequest,
               String[] legalRequestsReplies) {
        this.repliesToIllegalRequest = new
                String[repliesToIllegalRequest.length];
        this.legalRequestsReplies = new String[legalRequestsReplies.length];

        // Copying the arrays given into the Bot fields
        System.arraycopy(repliesToIllegalRequest, 0,
                this.repliesToIllegalRequest, 0,
                repliesToIllegalRequest.length);
        System.arraycopy(legalRequestsReplies, 0,
                this.legalRequestsReplies, 0,
                legalRequestsReplies.length);
        this.name = name;
    }

    String getName() {
        return this.name;
    }

    /**
     * This method replies to the statement given, based on the request
     * prefix
     */
    String replyTo(String statement) {
        if (statement.startsWith(REQUEST_PREFIX)) {
            // Legal request
            String phrase = statement.replaceFirst(REQUEST_PREFIX, "");
            return replyToLegalRequest(phrase);
        }
        // Illegal request
        return replyToIllegalRequest(statement);
    }

    /**
     * This method will deal with a legal statement
     */
    private String replyToLegalRequest(String phrase) {
        // Selecting a pattern randomly
        int randomIndex = rand.nextInt(legalRequestsReplies.length);
        String pattern = legalRequestsReplies[randomIndex];
        // Replace <phrase> with the phrase given
        return pattern.replaceAll("<phrase>", phrase);
    }

    /**
     * This method will deal with a statement that doesn't start with the
     * request prefix, and randomly pick a reply from the
     * "repliesToIllegalRequest" array if the "coin flip" is true
     */
    String replyToIllegalRequest(String statement) {
        int randomIndex = rand.nextInt(repliesToIllegalRequest.length);
        String reply = repliesToIllegalRequest[randomIndex];
        // Flipping a coin to determine if a word should be added
        if (rand.nextBoolean()) {
            reply = reply + statement;
        }
        return reply;
    }
}
