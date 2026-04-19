package pixelpanel;

public interface Function {

    /**
     * Calcule un nombre en fonction des coordonnées entrées.
     * Les coordonnées sont représentées par un Complexe.
     * @param d le Complexe représentant un point de l'espace 2D.
     * @return le nombre correspondant.
     */
    Number execute(Complex d);
}
