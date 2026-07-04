package pixelpanel;

public interface Function {

    /**
     * Calcule un nombre en fonction des coordonnées entrées.
     * Les coordonnées représentent un point de l'espace 2D (partie réelle et
     * partie imaginaire d'un complexe).
     * @param re la partie réelle du point.
     * @param im la partie imaginaire du point.
     * @return le nombre correspondant, dans l'intervalle [0, 1].
     */
    double execute(double re, double im);
}
