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
    GAME_INIT,
    //TODO
    DRAW_LINE,
    //TODO
    GUESS,
    ERROR,
    EXIT,
    EXIT_TABLE;
}
