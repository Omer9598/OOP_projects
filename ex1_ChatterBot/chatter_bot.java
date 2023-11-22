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
    String name;
    Random rand = new Random();
    String[] repliesToIllegalRequest;

    // creating an array of 2 bots with 2 different "reply" arrays
    ChatterBot(String name, String[] repliesToIllegalRequest) {
        this.repliesToIllegalRequest = new
                String[repliesToIllegalRequest.length];
        System.arraycopy(repliesToIllegalRequest, 0,
                this.repliesToIllegalRequest, 0,
                repliesToIllegalRequest.length);
        this.name = name;
    }

    String getName() {
        return this.name;
    }

    /**
     * this method replies to the statement given, based on the request
     * prefix
     */
    String replyTo(String statement) {
        if (statement.startsWith(REQUEST_PREFIX)) {
            //we don’t repeat the request prefix, so delete it from the reply
            return statement.replaceFirst(REQUEST_PREFIX, "");
        }
        return respondToIllegalRequest(statement);
    }

    /**
     * this method will deal with a statement that doesn't start with the
     * request prefix, and randomly pick a reply from the
     * "repliesToIllegalRequest" array if the "coin flip" is true
     */
    String respondToIllegalRequest(String statement) {
        int randomIndex = rand.nextInt(repliesToIllegalRequest.length);
        String reply = repliesToIllegalRequest[randomIndex];
        if (rand.nextBoolean()) {
            reply = reply + statement;
        }
        return reply;
    }
}
