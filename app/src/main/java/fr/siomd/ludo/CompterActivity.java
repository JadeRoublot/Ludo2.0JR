package fr.siomd.ludo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import java.util.Locale;
import java.util.Random;

import fr.siomd.ludo.databinding.ActivityCompterBinding;

public class CompterActivity extends AppCompatActivity {

    private static final String TAG = "hallowcompt";
    private static final int NB_QUESTIONS = 15;

    private int[] tbNombre2 = new int[NB_QUESTIONS];

    private int numQuestion2 = 0;
    private int nbcitrouilleQuestion2 = 0;
    private int nbReponsesCorrectes2 = 0;
    private int indiceDeco2 = 0;
    private int indiceTheme2 = 0;

    private ActivityCompterBinding ui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ui = ActivityCompterBinding.inflate(getLayoutInflater());
        setContentView(ui.getRoot());

        // créer le jeu de carte de Citrouille
        int indCarte = 0;
        for (int i = 1; i < 16; i++){
            tbNombre2[indCarte] = i;
            indCarte++;
        }
        demarrerComptage();
    }

    private void demarrerComptage() {
        int nbTempo;
        Random leHasard = new Random();
        for (int i = 0; i < tbNombre2.length; i++){
            int indHasard = leHasard.nextInt(tbNombre2.length);
            nbTempo = tbNombre2[indHasard];
            tbNombre2[indHasard] = tbNombre2[i];
            tbNombre2[i] = nbTempo;
        }
        //mise a jour du texte en fonction du theme (evite les bug de residue de theme de partie presedente)
        if (indiceTheme2 == 0) {
            ui.tvResultat2.setText("Combien il y a de Citrouilles?");
        }else {
            ui.tvResultat2.setText("Combien il y a de Cranes?");
        }


        numQuestion2 = -1;
        nbcitrouilleQuestion2 = 0;
        nbReponsesCorrectes2 = 0;
        ui.imgCartecitrouille2.setImageResource(R.drawable.back2);
        ui.etNbcitrouille2.setText("");
        ui.etNbcitrouille2.setEnabled(false);
        ui.btDeco2.setEnabled(true);
        ui.tvNbRepCorrectes2.setText(String.format(Locale.getDefault(), "%d / %d", 0, NB_QUESTIONS));
        ui.btJouer2.setEnabled(true);
        ui.btValider2.setEnabled(false);

    }
    //parametre du bouton Deco
    public void onClickbtDeco(View arg0) {
        Context contexte =getApplicationContext();
        // à chaque clic on passe au theme suivant
        indiceDeco2 = (indiceDeco2 +1) % 2; //2 pour 2 theme different "Citrouille" et "Crane"
        switch (indiceDeco2) {
            case 0:{
                // on change l'image  du bouton
                ui.btDeco2.setBackground(ContextCompat.getDrawable(contexte, R.drawable.pum));
                indiceTheme2 = 0;
                ui.tvResultat2.setText("Combien il y a de Citrouilles?");
                break;
            }
            case 1: {
                ui.btDeco2.setBackground(ContextCompat.getDrawable(contexte, R.drawable.skull));
                indiceTheme2 = 1;
                ui.tvResultat2.setText("Combien il y a de Cranes?");
                break;
            }

        }
    }

    public void onClickbtNouveauComptage(View arg0){
        demarrerComptage();
    }

    public void  onClickbtComptage(View arg0){

        if (numQuestion2 == -1){
            ui.btValider2.setEnabled(true);
            ui.etNbcitrouille2.setEnabled(true);
            ui.btDeco2.setEnabled(false);
            numQuestion2=0;
        }
        if (numQuestion2 < NB_QUESTIONS){
            nbcitrouilleQuestion2= tbNombre2[numQuestion2];
            ui.imgCartecitrouille2.setImageResource(getCarteResource(tbNombre2[numQuestion2]));
            numQuestion2++;
            ui.etNbcitrouille2.setText("");
        } else {
            String message = "";
            if (nbReponsesCorrectes2 > 5) {
                message = String.format(Locale.getDefault(), "Tu as %d réponses correcte, Tu a gagner un Bonbon ! ", nbReponsesCorrectes2);
            } else {
                message = String.format(Locale.getDefault(), "Tu as %d réponses correcte, tu peux y'arriver !", nbReponsesCorrectes2);
            }
            ui.tvResultat2.setText(message);
            ui.btValider2.setEnabled(false);
            ui.btJouer2.setEnabled(false);
        }

    }

    public void onClickbtValider(View arg0) {
        traitementReponse();
        onClickbtComptage(arg0);
    }

    private void traitementReponse(){
        String strNbcitrouille = ui.etNbcitrouille2.getText().toString();
        int nbcitrouille = 0;
        if (!TextUtils.isEmpty((strNbcitrouille))) {
            nbcitrouille = Integer.parseInt(strNbcitrouille);
        }
        String repNbcitrouille = "";
        int nbRepCorrectes = 0;
        if (indiceTheme2 == 0) {
            if (nbcitrouille == nbcitrouilleQuestion2) {
                repNbcitrouille = String.format(Locale.getDefault(), "Oui, %d citrouilles !", nbcitrouilleQuestion2);
                nbRepCorrectes++;
            } else {
                repNbcitrouille = String.format(Locale.getDefault(), "NON,il y a %d citrouilles !", nbcitrouilleQuestion2);
            }
        } else {
            if (nbcitrouille == nbcitrouilleQuestion2) {
                repNbcitrouille = String.format(Locale.getDefault(), "Oui, %d cranes !", nbcitrouilleQuestion2);
                nbRepCorrectes++;
            } else {
                repNbcitrouille = String.format(Locale.getDefault(), "NON, il y a %d cranes !", nbcitrouilleQuestion2);
            }
        }
        if ( nbRepCorrectes == 1 ) {
            nbReponsesCorrectes2++;
            ui.tvNbRepCorrectes2.setText(String.format(Locale.getDefault(), "%d / %d", nbReponsesCorrectes2, NB_QUESTIONS ));
        }

        Toast toast = Toast.makeText(getApplicationContext(), repNbcitrouille, Toast.LENGTH_LONG);
        toast.show();
    }

    //Fonction responsable des image des carte afficher
    private int getCarteResource(int nbcitrouilleCarte) {

        if (indiceTheme2 == 0) {
            String nomImgcitrouilleCarte = String.format("c%d", nbcitrouilleCarte);
            int resId = getResources().getIdentifier(nomImgcitrouilleCarte, "drawable", "fr.siomd.ludo");
            if (resId != 0) {
                return resId;
            }
            return R.drawable.back2;
        } else {
            String nomImgcitrouilleCarte = String.format("s%d", nbcitrouilleCarte);
            int resId = getResources().getIdentifier(nomImgcitrouilleCarte, "drawable", "fr.siomd.ludo");
            if (resId != 0) {
                return resId;
            }
            return R.drawable.back2;
        }
    }
}

