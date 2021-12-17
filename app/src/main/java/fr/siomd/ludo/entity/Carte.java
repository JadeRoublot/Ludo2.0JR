package fr.siomd.ludo.entity;

public class Carte {

    private String figure;
    private String couleur;

    public String getCouleur() {
        return couleur;
    }

    public String getFigure() {
        return figure;
    }

    /**
     * @param pCouleur
     * @param pFigure
     */
    public Carte(String pCouleur, String pFigure) {
        couleur = pCouleur;
        figure = pFigure;
    }

    public int getValeur(){
        int laValeur;
        switch (figure) {
            case "As": laValeur = 14; break;
            case "Roi": laValeur = 13; break;
            case "Dame": laValeur = 12; break;
            case "Valet": laValeur = 11; break;
            default: laValeur = Integer.parseInt(figure); break;
        }
        return laValeur;
    }
    public String getNom() {
        return String.format("%s de %s", getFigure(), getCouleur());
    }

    public String getNomImg() {
        String leNomImg = "";
        switch (couleur) {
            case "Coeur":leNomImg= String.format("co%d",getValeur()); break;
            case "Carreau" : leNomImg = String.format("ca%d", getValeur()); break;
            case "Pique" : leNomImg = String.format("p%d", getValeur()); break;
            case "Trèfle" : leNomImg = String.format("t%d", getValeur()); break;

        }
        return leNomImg;
    }

    public boolean isAtout(String pCouleur){
        return (couleur.equals(pCouleur));
    }

}