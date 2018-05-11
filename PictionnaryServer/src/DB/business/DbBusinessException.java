package DB.business;

/**
 *
 * @author Philippe
 */
public class DbBusinessException extends Exception {

    public DbBusinessException(String msg) {
        super(msg);
    }

    public DbBusinessException(Throwable t) {
        super(t);
    }

    public DbBusinessException(String msg, Throwable t) {
        super(msg + " : " + t.getMessage(), t);
    }

}
