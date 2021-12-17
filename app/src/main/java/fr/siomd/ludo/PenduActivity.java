package fr.siomd.ludo;
import java.util.ArrayList;
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
    lesThemes = DicoXml.getLesthemes(getResources().getXml(R.xml.dico));
    // afficher liste juste pour vérification
    for (Theme unTheme : lesThemes) {
        Log.i("DICO-liste", "Theme = " + unTheme.getNom());
        for (Mot unMot : unTheme.getLesMots()) {
            Log.i("DICO-liste", "Mot = " + unMot.getContenu() + " - " +
                    unMot.getNbPoints());
        }
    }
        demarrerPendu();
  }

    private void demarrerPendu() {
        //initialisation des parametre
        leBourreauClopin.demarrer();
        Random leHasard = new Random();
        int indHasard = leHasard.nextInt(tbAnecdote.length-1);

        //application des parametre
        ui.btJouer3.setEnabled(true);
        ui.tvPendu.setText (tbAnecdote[indHasard]);
        ui.tvMot.setText(leBourreauClopin.getLeMotEnCours());
        ui.tvQui.setText(leBourreauClopin.getLesLettresAuRebut ());
    }

    public void onClickbtNouveauPendu( View arg0){demarrerPendu();}


    public void  onClickbtJouerPendu(View arg0){


    }

    private void traitementReponsePendu(){

    }

 public void onClickbtLettre (View laVue) {
       CharSequence lalettre = ((Button)laVue).getText();
        Toast toast = Toast.makeText(getApplicationContext(), "lettre = " + lalettre, Toast.LENGTH_LONG);
       toast.show();
    }

    private int getImageResource(String nbImgCorde) {
        String nomImgCorde= String.format("cor%d", nbImgCorde);
        int resId = getResources().getIdentifier(nomImgCorde, "drawable", "fr.siomd.ludo");
        if (resId != 0) {
            return resId;
        }
        return R.drawable.cor0;
    }
}
