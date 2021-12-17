package fr.siomd.ludo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Locale;
import java.util.Random;

import fr.siomd.ludo.databinding.ActivityBatailleBinding;
import fr.siomd.ludo.entity.Carte;

public class BatailleActivity extends AppCompatActivity {

    private static final String TAG = "Bataille";
    private static final int NB_COUPS = 26;

    // déclarer les tableaux et les scores
    private String[] tbCouleurs = {"Coeur", "Carreau", "Pique", "Trèfle"};
    private String[] tbFigures = {"As", "Roi", "Dame", "Valet", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

    private Carte[] tbJeu = new Carte[NB_COUPS * 2];

    private Carte[] tbJoueurUn = new Carte[NB_COUPS];
    private Carte[] tbJoueurDeux = new Carte[NB_COUPS];

    // 1 coup = une confrontation d'une carte de chacun des 2 joueurs
    // il y'a 26 coups dans une partie (52/2*26)
    private int numCoup = 0;
    private int indiceAtout = 0;
    private String atout = tbCouleurs[indiceAtout]; // par défaut au départ
    private int scoreUn = 0;
    private int scoreDeux = 0;

    private int nbPointsCoup = 0;
    private int numJoueurGagnantCoup = 0;
    private int nbReponsesCorrectes = 0;

    private ActivityBatailleBinding ui; // ui : interface utilisateur

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // obligatoire
        super.onCreate(savedInstanceState);
        // mettre en place le layout (la mise en page)
        ui = ActivityBatailleBinding.inflate(getLayoutInflater());
        setContentView(ui.getRoot());

        // créer le jeu de 52 cartes à partir des couleurs et des figures
        int indCarte = 0;
        for (String couleur : tbCouleurs){
            for (String figure : tbFigures){
                tbJeu[indCarte] = new Carte(couleur, figure);
                indCarte++;
            }
        }
        afficherJeu();
        demarrerJeu();

    }

    private void demarrerJeu() {
        // mélanger le jeu de cartes
        Carte carteTempo;
        Random leHasard = new Random(); // générateur de nombres pseudo-aléatoires
        for (int i = 0; i < tbJeu.length; i++) {
            // on tire une position au hasard entre 0 et
            // La méthode Next retourne un nombre aléatoire non négatif, inférieur au nombre maximal spécifié
            int indHasard = leHasard.nextInt(tbJeu.length - 1);
            // on échange la ième carte avec celle à la position tirée au hasard
            carteTempo = tbJeu[indHasard];
            tbJeu[indHasard] = tbJeu[i];
            tbJeu[i] = carteTempo;
        }

        // dstribuer le jeu à 2 joueurs
        int indJoueur = 0;
        for (int i = 0; i < tbJeu.length; i += 2){
            tbJoueurUn[indJoueur] = tbJeu[i];
            tbJoueurDeux[indJoueur] = tbJeu[i + 1];
            indJoueur++;
        }

        scoreUn = 0;
        scoreDeux = 0;
        numCoup = -1;
        nbPointsCoup = 0;
        numJoueurGagnantCoup = 0;
        nbReponsesCorrectes = 0;

        // afficher les textes de départ
        ui.imgCarte1.setImageResource(R.drawable.back);
        ui.imgCarte2.setImageResource(R.drawable.back);
        ui.tvInfosCarte1.setText(" ");
        ui.tvInfosCarte2.setText(" ");
        ui.tvScore1.setText(String.format(Locale.getDefault(), "%d point", scoreUn));
        ui.tvScore2.setText(String.format(Locale.getDefault(), "%d point", scoreDeux));
        // effacer la zone de saisie du nombre de points
        ui.etNbPoints.setText("");
        // désactiver la zone de saisie du nombe de points
        ui.etNbPoints.setEnabled(false);
        // effacer le résultat des réponses de l'utilisateur
        ui.tvNbRepCorrectes.setText(String.format(Locale.getDefault(), "%d / %d", 0, NB_COUPS));

        // désactiver les boutons de réponse
        ui.btJoueur1.setEnabled(false);
        ui.btJoueur2.setEnabled(false);
        ui.btAucunJoueur.setEnabled(false);
        // activer le bouton Atout
        ui.btAtout.setEnabled(true);
    }

    public void onClickbtNouveauJeu(View arg0) {
        demarrerJeu();
    }

    public void onClickbtAtout(View arg0) {
        Context contexte =getApplicationContext();
        // à chaque clic on passe à la couleur suivante
        indiceAtout = (indiceAtout +1) % 4;
        atout = tbCouleurs[indiceAtout];
        switch (indiceAtout) {
            case 0:{
                // on change l'image d'arrière plan du bouton pour afficher la couleur
                ui.btAtout.setBackground(ContextCompat.getDrawable(contexte, R.drawable.coeur));
                break;
            }
            case 1: {
                ui.btAtout.setBackground(ContextCompat.getDrawable(contexte, R.drawable.carreau));
                break;
            }
            case 3: {
                ui.btAtout.setBackground(ContextCompat.getDrawable(contexte, R.drawable.pic));
                break;
            }
            case 4: {
                ui.btAtout.setBackground(ContextCompat.getDrawable(contexte, R.drawable.trefle));
                break;
            }
        }
    }

    public void onClickbtJouer(View arg0) {
        if (numCoup == -1) {
            // désactiver le bouton Atout
            ui.btAtout.setEnabled(false);
            // activer la zone de saisie du nombre de points
            ui.etNbPoints.setEnabled(true);
            // activer les boutons de réponse
            ui.btJoueur1.setEnabled(true);
            ui.btJoueur2.setEnabled(true);
            ui.btAucunJoueur.setEnabled(true);
            numCoup=0;
        }
        if (numCoup < NB_COUPS) {
            // jouer 1 coup avec calcul des scores
            nbPointsCoup = tbJoueurUn[numCoup].getValeur() + tbJoueurDeux[numCoup].getValeur();
            if (tbJoueurUn[numCoup].isAtout(atout) == tbJoueurDeux[numCoup].isAtout(atout)){
                if (tbJoueurUn[numCoup].getValeur() > tbJoueurDeux[numCoup].getValeur()) {
                    scoreUn = scoreUn + nbPointsCoup;
                    numJoueurGagnantCoup = 1;
                } else {
                    if (tbJoueurDeux[numCoup].getValeur() > tbJoueurUn[numCoup].getValeur()) {
                        scoreDeux = scoreDeux + nbPointsCoup;
                        numJoueurGagnantCoup = 2;
                    } else {
                        numJoueurGagnantCoup = 3;
                    }
                }
            } else {
                if (tbJoueurUn[numCoup].isAtout(atout)) {
                    scoreUn = scoreUn + nbPointsCoup;
                    numJoueurGagnantCoup = 1;
                } else {
                    scoreDeux = scoreDeux + nbPointsCoup;
                    numJoueurGagnantCoup = 2;
                }
            }

            // afficher les information du coup
            ui.imgCarte1.setImageResource(getCarteResource(tbJoueurUn[numCoup].getNomImg()));
            ui.imgCarte2.setImageResource(getCarteResource(tbJoueurDeux[numCoup].getNomImg()));
            ui.tvInfosCarte1.setText(String.format(Locale.getDefault(), "%s : %d points", tbJoueurUn[numCoup].getNom(), tbJoueurUn[numCoup].getValeur()));
            ui.tvInfosCarte2.setText(String.format(Locale.getDefault(), "%s : %d points", tbJoueurDeux[numCoup].getNom(), tbJoueurDeux[numCoup].getValeur()));
            // passer au coup suivant
            numCoup++;
            // effacer la zone de saisie du nombre de points
            ui.etNbPoints.setText("");

        }  else {
            // formater le message pour afficher le gagnant
            // soit : Le joueur Un a gagné avec n points contre n
            // soit : Le joueur Deux a gagné avec n points contre n
            // soit : Les 2 joueurs sont à égalité avec n points chacun
            String message = "";
            if (scoreUn > scoreDeux) {
                message = String.format(Locale.getDefault(), "Le joueur Un a gagné avec %d points contre %d", scoreUn, scoreDeux);
            } else {
                if (scoreDeux > scoreUn) {
                    message = String.format(Locale.getDefault(), "Le joueur Deux a gagné avec %d points contre %d", scoreDeux, scoreUn);
                } else {
                    message = String.format(Locale.getDefault(), "Les 2 joueurs sont à égalité avec %d points chacun", scoreUn);
                }
            }
            ui.tvResultat.setText(message);
            // mettre à jour le score du joueur dans les préférences
            // récupérer le stockage des préférences
            SharedPreferences prefs =
                    PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            // début de la transaction
            SharedPreferences.Editor editeur = prefs.edit();
            // modification du score
            editeur.putString("prefs_bataille_score", Integer.toString(nbReponsesCorrectes) +
                    " / " + NB_COUPS);
            // fin de la transaction
            editeur.commit();
        }
    }

    public void onClickbtJoueur1(View arg0) {
        traitementReponse(1);
    }

    public void onClickbtJoueur2(View arg0) {
        traitementReponse(2);
    }

    public void onClickbtAucunJoueur(View arg0) {
        traitementReponse(3);
    }

    private void traitementReponse(int numClickReponse) {
        // afficher les scores des 2 joueurs
        ui.tvScore1.setText(String.format(Locale.getDefault(), "%d points", scoreUn));
        ui.tvScore2.setText(String.format(Locale.getDefault(), "%d points", scoreDeux));

        // traiter les réponses de l'utilisateur
        String repJoueur = "";
        int nbRepCorrectes = 0;
        if (numClickReponse == numJoueurGagnantCoup) {
            // réponse joueur correcte
            repJoueur = String.format(Locale.getDefault(), "OUI, Joueur%d !", numJoueurGagnantCoup);
            nbRepCorrectes++;
        } else {
            repJoueur = String.format(Locale.getDefault(), "Non, c'est Joueur%d !", numJoueurGagnantCoup);
        }

        String strNbPoints = ui.etNbPoints.getText().toString();
        int nbPoints = 0;
        if (!TextUtils.isEmpty((strNbPoints))) {
            nbPoints = Integer.parseInt(strNbPoints);
        }
        String repNbPoints = "";
        if (nbPoints == nbPointsCoup) {
            // réponse total points correcte
            repNbPoints = String.format(Locale.getDefault(), "OUI, %d points !", nbPointsCoup);
            nbRepCorrectes++;
        } else {
            repNbPoints = String.format(Locale.getDefault(), "NON, c'est %d points !", nbPointsCoup);
        }

        // mise à jour réponse utilisateur
        if (nbRepCorrectes == 2) {
            nbReponsesCorrectes++;
            ui.tvNbRepCorrectes.setText(String.format(Locale.getDefault(), "%d / %d", nbReponsesCorrectes, NB_COUPS));
        }

        Toast toast = Toast.makeText(getApplicationContext(), repNbPoints + " " + repJoueur, Toast.LENGTH_LONG);
        toast.show();// affiche le message sur le terminal
    }

    private int getCarteResource(String nomImgCarte) {
        int resId = getResources().getIdentifier(nomImgCarte, "drawable", "fr.siomd.ludo");
        if (resId != 0) {
            return resId;
        }
        return R.drawable.back;
    }


    private void afficherJeu(){
        for (Carte uneCarte : tbJeu) {
            Log.i(TAG, "carte = " + uneCarte.getNom()); // affiche un message dans logcat
        }
    }
}