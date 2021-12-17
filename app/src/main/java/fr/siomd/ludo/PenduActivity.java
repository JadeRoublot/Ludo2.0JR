package fr.siomd.ludo;
import java.util.ArrayList;
import fr.siomd.ludo.dataaccess.DicoXml;
import androidx.appcompat.app.AppCompatActivity;
import fr.siomd.ludo.entity.Mot;
import fr.siomd.ludo.entity.Theme;

import android.os.Bundle;
import android.util.Log;

import fr.siomd.ludo.databinding.ActivityPenduBinding;

public class PenduActivity extends AppCompatActivity {

    private ActivityPenduBinding ui;
    private ArrayList<Theme> lesThemes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ui = ActivityPenduBinding.inflate(getLayoutInflater());
        setContentView(ui.getRoot());
    // récupérer le dictionnaire avec les thèmes
    lesThemes = DicoXml.getLesthemes(getResources().getXml(R.xml.dico));
    // afficher liste juste pour vérification
    for (Theme unTheme : lesThemes) {
        Log.i("DICO-liste", "Theme = " + unTheme.getNom());
        for (Mot unMot : unTheme.getLesMots()) {
            Log.i("DICO-liste", "Mot = " + unMot.getContenu() + " - " +
                    unMot.getNbPoints());
        }
    }
  }
}
