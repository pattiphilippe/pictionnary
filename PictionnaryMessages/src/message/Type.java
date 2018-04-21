package message;

/**
 * The <code> Type </code> represents the type of a message send between a user
 * and the server.
 */
public enum Type {

    PROFILE,
    TABLES,
    CREATE,
    JOIN,
    //TODO
    GAME_INIT,
    //TODO
    DRAW,
    //TODO
    GUESS,
    ERROR,
    EXIT;
}
