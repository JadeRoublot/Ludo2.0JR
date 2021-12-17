package fr.siomd.ludo.entity;
// la classe Juge représente le gestionnaire de mots. Elle permet de :
//   - lire les mots et thèmes
//   - gérer le score par thème
//   - donner un mot à trouver



import android.content.res.Resources;

import java.util.ArrayList;
import java.util.Random;

import fr.siomd.ludo.R;
import fr.siomd.ludo.dataaccess.DicoXml;
import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;



public class Juge {
    //générateur de nombres pseudo-aléatoires
    private Random leHasard = new Random();
    // score
    private int score;
    // les thèmes chargés
    private ArrayList<Theme> lesThemes;
    // le numéro de thème (index dans la liste des thèmes) sélectionné par l'utilisateur
    //  ou -1 si aucun thème n'a été sélectionné
    private int numeroThemeSelectionne;

    // retourne la liste des noms de thèmes
    public ArrayList<String> getLesNomsThemes() {
        ArrayList<String> lesNomsThemes = new ArrayList<String>();
        for(Theme unTheme: lesThemes){
            lesNomsThemes.add(unTheme.getNom());
        }
        return lesNomsThemes;
    }

    // retourne le score
    public int getScore() {
        return score;
    }

    // retourne le numéro de thème sélectionné
    public int getNumeroThemeSelectionne() {
        return numeroThemeSelectionne;
    }

    //      positionne le numéro de thème sélectionné
    //      poitionne le score à 0
    public void setNumeroThemeSelectionne(int unNumeroTheme) {
        score = 0;
        numeroThemeSelectionne = unNumeroTheme;
    }

    public Juge(XmlPullParser monXmlPullParser)   {
        leHasard = new Random();
        setNumeroThemeSelectionne(-1);
        lireThemes(monXmlPullParser);
    }

    // lire les thèmes à partir du fichier XML pour positionner la liste lesThemes
    private void lireThemes(XmlPullParser monXmlPullParser ) {
       lesThemes = DicoXml.getLesthemes(monXmlPullParser);
    }


   //  si aucun thème sélectionné, sélectionner un thème au hasard
    //  donner un mot au hasard dans le thème (sélectionné ou pris au hasard)
    public Mot donnerMot()  {
        if(numeroThemeSelectionne == -1){
            int indHasard = leHasard.nextInt(lesThemes.size()-1);
            Theme leThemeSelectionne = lesThemes.get(indHasard);
            indHasard = leHasard.nextInt(leThemeSelectionne.getLesMots().size()-1);
             return leThemeSelectionne.getLesMots().get(indHasard);
        } else {
            Theme leThemeSelectionne = lesThemes.get(numeroThemeSelectionne);
            int indHasard = leHasard.nextInt(leThemeSelectionne.getLesMots().size()-1);
            return leThemeSelectionne.getLesMots().get(indHasard);
        }
    }

    // ajouter nbPoints au score
    public void ajouterScore(int unNbPoints) {
        score += unNbPoints;
    }
}
