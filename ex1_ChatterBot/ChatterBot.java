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
    static final String PLACEHOLDER_FOR_REQUESTED_PHRASE = "<phrase>";
    static final String PLACEHOLDER_FOR_ILLEGAL_REQUEST = "<request>";
    private final String name;
    private final Random rand = new Random();
    private final String[] repliesToIllegalRequest;
    private final String[] repliesToLegalRequest;

    /**
     * ChatterBot constructor
     * @param name The name of the bot
     * @param repliesToIllegalRequest an array of replies to an illegal request
     * @param legalRequestsReplies an array of replies to a legal request
     */
    ChatterBot(String name, String[] repliesToIllegalRequest,
               String[] legalRequestsReplies) {
        this.repliesToIllegalRequest = new
                String[repliesToIllegalRequest.length];
        this.repliesToLegalRequest = new String[legalRequestsReplies.length];
        // Copying the arrays given into the Bot fields
        System.arraycopy(repliesToIllegalRequest, 0,
                this.repliesToIllegalRequest, 0,
                repliesToIllegalRequest.length);
        System.arraycopy(legalRequestsReplies, 0,
                this.repliesToLegalRequest, 0,
                legalRequestsReplies.length);
        this.name = name;
    }

    /**
     * Getter to the name of the bot
     */
    public String getName() {
        return this.name;
    }

    /**
     * This method replies to the statement given, based on the request
     * prefix
     */
    public String replyTo(String statement) {
        if (statement.startsWith(REQUEST_PREFIX)) {
            // Legal request
            return replyToLegalRequest(statement);
        }
        // Illegal request
        return replyToIllegalRequest(statement);
    }

    /**
     * This method will reply to legal or illegal request
     */
    public String replacePlaceholderInARandomPattern(String[] repliesArr,
                                                     String phrase,
                                                     String placeHolder) {
        // Select a pattern randomly
        int randomIndex = rand.nextInt(repliesArr.length);
        String pattern = repliesArr[randomIndex];
        // Replace the placeholder in the pattern
        return pattern.replaceAll(placeHolder, phrase);
    }

    /**
     * This method will deal with a legal statement, removing the request
     * prefix
     */
    public String replyToLegalRequest(String statement) {
        String phrase = statement.replaceFirst(REQUEST_PREFIX, "");
        return replacePlaceholderInARandomPattern(repliesToLegalRequest, phrase,
                PLACEHOLDER_FOR_REQUESTED_PHRASE);
    }

    /**
     * This method will deal with a statement that doesn't start with the
     * request prefix, and randomly pick a reply from the
     * "repliesToIllegalRequest" array if the "coin flip" is true
     */
    public String replyToIllegalRequest(String statement) {
        return replacePlaceholderInARandomPattern(repliesToIllegalRequest, statement,
                PLACEHOLDER_FOR_ILLEGAL_REQUEST);
    }
}
