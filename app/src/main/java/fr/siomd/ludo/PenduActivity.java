package fr.siomd.ludo;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import fr.siomd.ludo.dataaccess.DicoXml;
import androidx.appcompat.app.AppCompatActivity;
import fr.siomd.ludo.entity.Mot;
import fr.siomd.ludo.entity.Theme;
import fr.siomd.ludo.entity.Juge;
import fr.siomd.ludo.entity.Bourreau;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import fr.siomd.ludo.databinding.ActivityPenduBinding;

public class PenduActivity extends AppCompatActivity {
    private static final String TAG = "Pendu";
    private static final int NB_ESSAY = 4;
    private ActivityPenduBinding ui;
    private ArrayList<Theme> lesThemes;
    private String[] tbAnecdote = {"Jouer au pendu et changer de vie !", "Le pendu est un jeu a jouer au moin une foix dans une vie", "Je suis derrier toi", "trois, de , un , pret? PENDU !", "quelle jeux tendus pour une simple jeux de pendu"};
    private Bourreau leBourreauClopin;
    private Juge leJugeFrollo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ui = ActivityPenduBinding.inflate(getLayoutInflater());
        setContentView(ui.getRoot());
    // récupérer le dictionnaire avec les thèmes
        leJugeFrollo = new Juge(getResources().getXml(R.xml.dico));
        leBourreauClopin = new Bourreau(leJugeFrollo);
        Random leHasard = new Random();
        int indHasard = leHasard.nextInt(tbAnecdote.length-1);
        // afficher le mot en cours (avec des _ pour chaque lettre non trouvée)
        ui.tvPendu.setText (tbAnecdote[indHasard]);
        ui.tvMot.setText(leBourreauClopin.getLeMotEnCours());
        ui.btJouer3.setEnabled(true);
        lesThemes = DicoXml.getLesthemes(getResources().getXml(R.xml.dico));
        // afficher liste juste pour vérification
        for (Theme unTheme : lesThemes){
            Log.i("DICO-liste", "Theme = " + unTheme.getNom());
            for (Mot unMot : unTheme.getLesMots()) {
                Log.i("DICO-liste", "Mot = " + unMot.getContenu() + " - " + unMot.getNbPoints());
            }
        }

    }

    public void onClickbtLettre(View laVue) {

        CharSequence lalettre = ((Button) laVue).getText();

        // évidemment dans votre jeu de pendu, au lieu de faire un toast, il convient de traiter la lettre proposée
        int nbCord = 0;
        leBourreauClopin.executer(lalettre.charAt(0));
        String message = "";
        // si erreur (lettre non trouvée dans le mot)  adapter image pendu
        if (leBourreauClopin.getLesLettresAuRebut () == lalettre){
            ui.tvQui.setText(leBourreauClopin.getLesLettresAuRebut ());
            nbCord++;
            ui.imgPendu.setImageResource(getImageResource(nbCord));

        // si gagné --> afficher  "GAGNE" et afficher le score (leJuge.getScore())
         } else if ( leBourreauClopin.isGagne() == true) {
            ui.tvScore.setText(leJugeFrollo.getScore());
            message = String.format(Locale.getDefault(), "GAGNER ");

        } else if (leBourreauClopin.isPerdu() == true) {
            // si perdu --> afficher  "PERDU" et adapter image pendu
            ui.imgPendu.setImageResource(R.drawable.cor5);
            message = String.format(Locale.getDefault(), "PERDU ");

        }
        // si gagné ou perdu alors désactiver les boutons lettre (ou  traiter le cas gagne/perdu dans le code onClickbtLettre avant le reste)

    }

    public void onClickbtNouveauMot(View laVue) {
        // démarrer le bourreau
        leBourreauClopin.demarrer();
        // afficher le mot en cours
        ui.tvMot.setText(leBourreauClopin.getLeMotEnCours());
        // afficher image pendu du début
        ui.imgPendu.setImageResource(R.drawable.cor0);
        // activer les boutons lettres s'ils ont été désactivés

    }

    private int getImageResource(int nbImgCorde) {
        String nomImgCorde= String.format("cor%d", nbImgCorde);
        int resId = getResources().getIdentifier(nomImgCorde, "drawable", "fr.siomd.ludo");
        if (resId != 0) {
            return resId;
        }
        return R.drawable.cor0;
    }
}
