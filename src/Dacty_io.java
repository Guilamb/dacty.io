import extensions.CSVFile;
import extensions.*;
class Dacty_io extends Program {
	CSVFile fichier = loadCSV("./papillonShort.csv");
	Image img = newImage("../ressources/menu.gif"); //pas utile
	String valider ="";  //sert à mettre en pause l'execution du programme
	int score, duree, miss, lvl, temps, entree, nbmot ,cpt;
	boolean manche, abort;

	void testTemps() {
		temps = 0;
		temps(50,100);
		assertEquals(temps,50);
	}
	void testChrono() {
		temps = 5000;
		duree = 30;
		manche = true;
		chrono();
		assertTrue(manche);
		temps = 30001;
		chrono();
		assertFalse(manche);
		manche = true;
		temps = 29999;
		chrono();
		assertTrue(manche);
		temps = 30000;
		chrono();
		assertFalse(manche);
	}
	void testCompteur() {
		manche = true;
		cpt = 30;
		nbmot = 40;
		compteur();
		assertTrue(manche);
		cpt = 40;
		compteur();
		assertFalse(manche);
	}
	void testNewGame() {
		abort = true;
		temps = 40000;
		assertTrue(abort);
		miss = 40;
		newGame();
		assertFalse(abort);
		score++;
		assertEquals(score,1);
		assertEquals(miss,0);
		assertEquals(lvl,0);
		assertEquals(temps,0);
	}
	void _testPartiePerso() {
		for (int i=0; i<3; i-=-1) {
				newGame();
				partiePerso();
			if (duree!=0) {
				assertGreaterThanOrEqual(10,duree);
				assertLessThanOrEqual(600,duree);
			} else {
				assertGreaterThanOrEqual(10,nbmot);
				assertLessThanOrEqual(400,nbmot);
			}
		}
	}
	void testVerification() {
		newGame();
		Mot unMot = new Mot();
		unMot.motstr = "toto";
		verification(unMot,"toto");
		Mot unMot2 = new Mot();
		unMot2.motstr = "tata";
		verification(unMot2,"tata");
		Mot unMot3 = new Mot();
		unMot3.motstr = "tata";
		verification(unMot3,"titi");
		Mot unMot4 = new Mot();
		unMot4.motstr = "bada";
		verification(unMot4,"stop");
		println(score);
		println(miss);
		assertEquals(score,2);
		assertTrue(abort);
		assertEquals(miss,1);
		assertFalse(manche);
	}
	/*void menuGraphique(){
		show(img);
	}
	void mouseChanged(String name, int x, int y, int button, String event){
		if(x>=150 && x<=445 && y>=53 && y<=124 ){
			entree=1;
		}else if (x>=150 && x<=445 && y>=168 && y<=240 ) {
			entree=2;
		}else if (x>=150 && x<=445 && y>=274 && y<=338 ) {
			entree=3;
		}
	}*/

	void algorithm(){
		
		boolean game = true;

		while(game){
			//menuGraphique();
			newGame();
			entree = readInt();
			if (entree==1) {
				boolean debut=true;
				while (lvl == 0 || lvl > 6) {
					choixNiveau();
					lvl = readInt();
				}
				if (lvl == 1 || lvl == 2) {
					duree = lvl*60;
				} else if (lvl == 3) {
					nbmot = 30;
				} else if (lvl == 4) {
					nbmot = 40;
				} else if (lvl == 6) {
					partiePerso();
					if (duree != 0) {
						lvl = 7;
					}
				}

				println("prêt ?");
				valider = readString(); // on demarre le chrono après validation
				playSound("../ressources/musique.mp3",true);
				manche = true;

				while (manche) {
					Mot unMot = new Mot();
					initialiser(unMot);
					cleanGinna();
					affichage(unMot);
					long tempsDebut = getTime();
					verification(unMot,readString());
					long tempsFin = getTime();
					if (!abort) {
						temps(tempsDebut,tempsFin);
					}
					cpt ++;
					cleanGinna();
					if (lvl < 3 || lvl == 7){
						chrono();
					}
					if (lvl == 3 || lvl == 4 || lvl == 6) {
						compteur();
					}
				}
				resultat();
			} else if (entree == 2) {
				regles();
			} else if (entree == 3) {
				System.exit(0);
			} else if (entree == 42) {
				beaubeau();
			}
		}
	}

	void regles() {      // Parce qu'elles le valent bien
		cleanGinna();

		println("Voici les regles du jeu :");
		println("");
		println("");
		println("Entrez le mot representé par l'image et l'ecriture en dessous le plus vite possible puis appuyez sur entree pour valider le mot et passer au mot suivant.");
		println("Si le mot rentre est faux il vous faudra recommencer jusqu'a avoir tape la bonne reponse");
		println("Une fois votre partie termine vous avez votre score, tentez de battre votre reccord !");
		valider = readString();
	}
	void choixNiveau() {
		cleanGinna();
		println("1 - 1 minute");
		println("2 - 2 minutes");
		println("3 - 30 mots");
		println("4 - 60 mots");
		println("5 - Pas de limite !! (tapez stop pour arreter)");
		println("6 - Partie personalisée");
		println("");
		println("Entrez le niveau voulus : ");
	}

	void cleanGinna () {   // Pour remettre l'écran comme il faut
		clearScreen();
		cursor(4,0);
	}
	void affichage(Mot unMot){
		println("tapez : " + unMot.motstr); //Un monstre de mot
	}

	void compteur() {
		if (cpt == nbmot) {
			manche = false;
		}
	}

	void newGame() {
		cleanGinna();
		score = 0;
		miss = 0;
		temps = 0;
		lvl = 0;
		nbmot = 0;
		duree = 0;
		entree = 0;
		nbmot = 0;
		cpt = 0;
		abort = false;
		println("Bienvenue sur Dacty.io");
		println("");
		println("1 - Jouer");
		println("2 - Comment jouer ?");
		println("3 - Quitter");

		println("Ceci est un petit jeu où tu dois écrire le plus vite possible le nom de ce que tu vois.");
	}

	void partiePerso() {
		String tagada = "";
		while (equals(tagada,"")&&!equals(tagada,"1")&&!equals(tagada,"2")) {
			do {
				cleanGinna();
				println("1 - Définir une limite de temps ?");
				println("2 - Définir un nombre de mots ?");
				println("");
				tagada = readString();
			} while (!equals(tagada,"1")&&!equals(tagada,"2"));
			
		}
		if (equals(tagada,"1")) {
			do {
				cleanGinna();
				println("Combien de temps ? (en secondes, entre 10 et 600 (10 minutes)) :");
				println("");
				duree = readInt();
			} while (duree > 600 || duree < 10);
			
		} else {
			do {
				cleanGinna();
				println("Combien de mots ? (entre 10 et 400) :");
				println("");
				nbmot = readInt();
			} while (nbmot > 400 || nbmot < 10);
		}
	}

	void initialiser(Mot unMot) { //on prends un mot dans la liste puis on lui met un type mot utiliser la notion de utilise
		do {
			unMot.motstr = getCell(fichier,((int)(random()*rowCount(fichier))),0);
			unMot.nblettres = length(unMot.motstr);
		} while (unMot.utilise!= false); // à implémenter 
	}

	void temps(long deb, long fin){
		temps = temps + (int)(fin - deb);
    }

	void verification(Mot unMot,String inputString){
		if (equals(inputString,unMot.motstr)) {
			score ++;
		} else if (equals(inputString,"stop")) {
			abort = true;
			manche = false; // on le met à false car c'est la fin
		} else {
			miss ++;
		}
	}

	/* String toString(Mot unMot) {   // relique d'une version précédente, peut encore servir
		String res = "";
		for (int i=0; i<unMot.nblettres; i++) {
			res = res + unMot.lettres[i];
		}
		return res;
	}*/

	void resultat(){ // separer tout affichage pour faciliter la migration au mode graphique
		cleanGinna();
		String after1 = ".";
		String after2 = "";
		if (score==0) {
			after1 = " (._.)";
		} else if (score>1) {
			after1 = "s.";
		}
		if (miss>1 && score>miss) {
			after2 = "s.";
		} else if (miss>1 && miss>=score) {
			after2 = "  (._.)";
		} else {
			after2 = "";
		}
		if (abort && lvl != 5) {
			println("La partie a été interrompue !!");
		} else {
			println("La partie est terminée !!");
		}
		valider = readString();
		cleanGinna();
		println("Fin de partie :");
		println("");
		if (duree != 0 || lvl == 5 || abort) {
			println("Ta as correctement tapé " + score + " mot" + after1);
		} else {
			println("Tu a fait tes " + nbmot + " mots.");
		}
		if (miss > 0) {
			println("En en ayant raté " + miss + ".");
		} else {
			println("Sans en rater un seul !!");
		}
		if (!abort && duree != 0) {
			println("Le tout en " + duree + " secondes.");
		} else {
			println("Le tout en " + (int)(((int)temps/10)/100) + " secondes.");
		}
		println("");
		println("appuyez sur une touche pour continuer...");
		valider = readString();
		re();
	}
	void beaubeau() {
		cleanGinna();
		Image celebrite = newImage("../ressources/BOBO.png");
		show(celebrite);
		println("COUCOU !!");
		valider = readString();
	}
	void chrono(){
		if ((int)(temps/1000) >= duree) { // on arrete la mesure du temps
			manche = false;
		}
	}
	void re() {
		cleanGinna();
		println("On en refait une ? o/n");
		String yn = readString();
		if (equals(yn,"n") || equals(yn,"N") || equals(yn,"non") || equals(yn,"Non") || equals(yn,"NON")) {
			System.exit(0);
		}
	}
}
//TO DO :
/*	-debuger le temps
	-rajouter des listes de mots, demander au joueur quel liste il veut 
	-gerer les manches
	-gerer la musique en fonction des manches
	-preparer la migration console -> graphique (verrifier ou sont fait les prints)
	*/