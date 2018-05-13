package pixelpanel;

public interface Function {

    /**
     * Calcule un nombre en fonction des coordonnées entrées.
     * Les coordonnées sont un tableau de <i>n</i> dimensions.
     * @param d les <i>n</i> coordonnées de l'espace.
     * @return le nombre correspondant.
     */
    Number execute(Complex d);
}
