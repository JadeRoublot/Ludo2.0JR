package fr.siomd.ludo.entity;
// la classe Bourreau représente le gestionnaire de lettres. Elle permet de gérer le traitement de chaque lettre. Elle :
//   - indique la présence ou non de la lettre proposée dans le mot à chercher
//   - gère et mémorise l'état de chaque lettre du mot à trouver (trouvée ou non)
//   - gère les lettres au rebut (lettres proposées
//                                 qui ne sont pas dans le mot à trouver)
//   - indique si la recherche du mot est gagnée ou perdue

import java.util.ArrayList;

public class Bourreau {
    public static final int MAX_REBUT = 4;    // nombre maximum de lettres au rebut

    private Juge leJuge;           // le juge
    private Mot leMot;             // mot à chercher de type Mot
    private String leMotCherche;   // contenu du mot à chercher en majuscules
    private String leMotEnCours;  // mot avec des _ à la place de chaque lettre non trouvée
    private ArrayList<Character> lesLettresAuRebut = new ArrayList<Character>();  // liste des lettres au rebut
    private ArrayList<Character> lesLettresTrouvees = new ArrayList<Character>(); // liste des lettres trouvées

    // vrai si toutes les lettres du mot ont été trouvées
    public boolean isGagne() {
        return (leMotCherche == leMotEnCours);
    }

    // vrai si maximum de lettres au rebut atteint
    public boolean isPerdu() {
        return ( lesLettresAuRebut.size() == MAX_REBUT );
    }

    // retourne le mot en cours (avec des _ ) qui est à afficher
    public String getLeMotEnCours() {
        return leMotEnCours;
    }

    public ArrayList<Character> leMotChercheTableau(){
        ArrayList<Character> lesLettresMotCherche = new ArrayList<Character>();

        for (int i = 0; i < leMotCherche.length(); i++){
            lesLettresMotCherche.add(leMotCherche.charAt(i));
        }
        return lesLettresMotCherche;
    }

    // retourne les lettres au rebut (qui peut être affichée)
    public String getLesLettresAuRebut () {
        String lettresAuRebut = "";
        for (Character uneLettre : lesLettresAuRebut){
            lettresAuRebut = lettresAuRebut + uneLettre + " ";
        }
        return lettresAuRebut;
    }

    public Bourreau(Juge unJuge) {
        leJuge = unJuge;
        demarrer();
    }

    // initialisation des variables de travail utilisées pour la gestion d'un mot
    public void demarrer() {
        leMot =  leJuge.donnerMot();
        leMotCherche = leMot.getContenuMaj();
        lesLettresAuRebut.clear();
        leMotEnCours = "";
        // définir leMotEnCours : autant de caractères _  que de lettres dans le mot à trouver
        for (int i = 0; i < leMotCherche.length(); i++) {
            leMotEnCours += '_';
        }
    }

    // chercher la lettre dans le mot à trouver
    //     si la lettre est trouvée, le mémoriser
    //     sinon, mettre la lettre au rebut
    // si mot entièrement trouvé, alors mettre à jour le score
    public void executer(Character uneLettre) {
        if (leMotCherche.indexOf(uneLettre) == -1){
            lesLettresAuRebut.add(uneLettre);
        } else if (leMotCherche.indexOf(uneLettre) != 1){
            lesLettresTrouvees.add(uneLettre);
        } else if (isGagne()){
            leJuge.ajouterScore(leMot.getNbPoints());
        }
    }

    // chercher les positions de la lettre
    public ArrayList<Integer> getPositionLettre(Character laLettre){
        ArrayList<Integer> lesPositions = new ArrayList<Integer>();
        for(Character uneLettre: leMotChercheTableau()){
            if (uneLettre == laLettre){
                lesPositions.add(leMotChercheTableau().indexOf(uneLettre));
            }
        }
        return lesPositions;
    }
}
