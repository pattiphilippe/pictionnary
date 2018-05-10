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
    DRAW_LINE,
    GUESS,
    WON,
    ERROR,
    EXIT,
    EXIT_TABLE,
    SERVER_CLOSED,
    GAME_STATE;
}
