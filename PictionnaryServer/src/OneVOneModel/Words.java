package OneVOneModel;

/**
 *
 * @author Philippe
 */
enum Words {
    CHAT,
    CHIEN,
    CANARD,
    TABLE,
    POMME,
    LIT,
    MAIN,
    BINAIRE,
    ORDINATEUR,
    CERF,
    SERRE,
    VERT,
    VER,
    VERRE,
    VERTICAL,
    ROUGE,
    SAPIN,
    ENFANT,
    RIEN;

    static String random() {
        Words[] values = values();
        return values[(int) (Math.random() * values.length)].toString();
    }
}
