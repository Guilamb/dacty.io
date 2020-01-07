import extensions.CSVFile;
import extensions.*;
class Dacty_io extends Program{
	CSVFile fichier = loadCSV("./papillonShort.csv");
	long tempsDebut=0,tempsFin=0;
	String valider ="";//sert à mettre en pause l'execution du programme
	int score, miss, lvl;
	double temps;

	void testRegles(){
		boolean compréhention = true;
		assertTrue(compréhention);
	}
	void testTemps(){
		temps(true);
		delay(5000);
		double waitingTime = temps(false);
		assertLessThanOrEqual(waitingTime,5.00);
	}
	void testCleanGinna(){
		boolean ecran = true;
		assertTrue(ecran);
	}
	void testAffichage(){
		boolean ecran = true;
		assertTrue(ecran);
	}

	void algorithm(){
		
		boolean game=true;
		boolean manche;

		while(game){
			newGame();
			int entree= readInt();
			if (entree==1){

				boolean debut=true;
				 
				choixNiveau();
				lvl = readInt();
				manche=true;

				println("prêt ?");
				valider=readString();// on demarre le chrono après validation
				temps(debut);



				while(manche){
					//playSound("/ressources/musique.mp3",true);	//url à changer !!!!
					Mot unMot = new Mot();
					initialiser(unMot);
					tappation(unMot);	
				}

			} else if (entree==2) {
				regles();
			} else if (entree==3) {
				System.exit(0);
			} else if (entree==42) {
				beaubeau();
			}
		}
	}

	void regles(){      // Parce qu'elles le valent bien
		cleanGinna();

		println("Voici les regles du jeu :");
		println("");
		println("");
		println("Entrez le mot representé par l'image et l'ecriture en dessous le plus vite possible puis appuyez sur entree pour valider le mot et passer au mot suivant.");
		println("Si le mot rentre est faux il vous faudra recommencer jusqu'a avoir tape la bonne reponse");
		println("Une fois votre partie termine vous avez votre score, tentez de battre votre reccord !");
		valider = readString();
	}
	void choixNiveau(){
		cleanGinna();
		println("1 - 1min");
		println("2 - 5min");
		println("3 - 10min");
		println("4 - infini ! tapez stop pour arreter");
		println("");
		println("Entrez le niveau voulus : ");

	}

	void cleanGinna (){   // Pour remettre l'écran comme il faut
		clearScreen();
		cursor(4,0);
	}
	void affichage(Mot unMot){
		println("tapez : " + unMot.motstr); //Un monstre de mot
	}

	void newGame(){ //marche pas encore 
		cleanGinna();
		tempsDebut=0;
		tempsFin=0;
		score = 0;
		miss = 0;
		temps = 0;
		

		println("Bienvenue sur Dacty.io");
		println("");
		println("1 - Jouer");
		println("2 - Comment jouer ?");
		println("3 - Quitter");

		println("Ceci est un petit jeu où tu dois écrire le plus vite possible le nom de ce que tu vois.");

		
	}

	void initialiser(Mot unMot){ //on prends un mot dans la liste puis on lui met un type mot utiliser la notion de utilise
		do {
			unMot.motstr = getCell(fichier,((int)(random()*rowCount(fichier))),0);
			unMot.nblettres = length(unMot.motstr);

		} while (unMot.utilise!= false); // à implémenter 
	}

	double temps(boolean debut){
		if(debut) {
			tempsDebut = getTime()/1000;
		} else {
			tempsFin=getTime()/1000;
		}
		return (tempsFin-tempsDebut); 
    }

	void tappation(Mot unMot){
		boolean debut=true;
		cleanGinna();
		affichage(unMot);
		verification(unMot,readString());

		debut=false;
		temps = temps(debut); 
		cleanGinna();
		if (lvl < 4){
			chrono();	
		}
		
	}
	void verification(Mot unMot,String inputString){
		if (equals(inputString,unMot.motstr)) {
			score ++;
		}else if(equals(inputString,"stop")){
			resultat(score,miss,false); // on le met à false car c'est la fin
		} 
		else {
			miss ++;
		}
	}
	/*
	String toString(Mot unMot) {   // relique d'une version précédente, peut encore servir
		String res = "";
		for (int i=0; i<unMot.nblettres; i++) {
			res = res + unMot.lettres[i];
		}
		return res;
	}
	*/
	void resultat(int score, int miss, boolean debut){ // separer tout affichage pour faciliter la migration au mode graphique
			String after1 = "";
			String after2 = "";
			if (score==0) {
				after1 = " (._.)";
			} else if (score>1) {
				after1 = "s";
			}
			if (miss == 0) {
				after2 = "Et vous n'avez loupé aucun mot, bravo !!";
			} else if (miss == 1) {
				after2 = "Et vous avez loupé : " + miss +" mot";
			} else {
				after2 = "Et vous avez loupé : " + miss +" mots";
			}
			println("votre temps est de : " + (int)(temps(debut)) + " secondes");//pas bon
			println("Votre score est de : " + score + " point" + after1);
			println(after2);
			println("appuyez sur une touche pour continuer...");
			valider = readString();
			System.exit(0);
			newGame();//pas encore au point 
	}
	void beaubeau() {
		cleanGinna();
		Image celebrite = newImage("../ressources/BOBO.png");
		show(celebrite);
		println("COUCOU !!");
		valider = readString();
	}
	void chrono(){
		int limiteDeTemps = 0;
		if (lvl == 1){
			limiteDeTemps=60;
		}else if (lvl == 2) {
			limiteDeTemps=5*60;
		}else if (lvl == 3) {
			limiteDeTemps=10*60;
		}else{
			
		}
		if (temps(false)>=limiteDeTemps){// on arrete la mesure du temps
			resultat(score,miss,false);
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