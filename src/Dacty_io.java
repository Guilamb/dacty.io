import extensions.CSVFile;
class Dacty_io extends Program{
	CSVFile fichier = loadCSV("./papillonShort.csv");
	long tempsDebut=0,tempsFin=0;
	String valider ="";//sert à mettre en pause l'execution du programme
	int score, miss;
	double temps;

	void algorithm(){
		
		boolean game=true;
		boolean manche;
		/*          Peut être après mais est devenu inutile 

		Mot[] liste = new Mot[rowCount(fichier)];
		String temp;
		for (int i=0; i<rowCount(fichier); i-=-1) {println(getCell(fichier, i, 0));
			temp = getCell(fichier, i, 1);
			liste [i].nblettres = 10;
			liste [i].lettres = new char [liste[i].nblettres];
			for (int j=0; j<liste[i].nblettres; j-=-1) {
				liste [i].lettres[j] = charAt(temp,j);
			}
			liste[i].utilise = false;
		}
		*/
		while(game){
			cleanGinna();

			println("Bienvenue sur Dacty.io");
			println("");
			println("1 - Jouer");
			println("2 - Comment jouer ?");
			println("3 - Quitter");

			println("Ceci est un petit jeu où tu dois écrire le plus vite possible le nom de ce que tu vois.");
			int entree= readInt();
			boolean debut=true;
			if (entree==1){
				manche = true;
				score = 0;
				miss = 0;
				temps = 0;
				
				println("prêt ?");

				valider=readString(); // on demarre le chrono après validation
				temps(debut);

				while(manche){
					//playSound("/home/guilamb/code/dacty.io/dacty.io/ressources/musique.mp3",true);	//url à changer !!!!
					Mot unMot = new Mot();
					initialiser(unMot);
					verification(unMot);	
				}
				
			} else if (entree==2) {
				regles();
			} else if (entree==3) {
				System.exit(0);
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

	void cleanGinna (){   // Pour remettre l'écran comme il faut
		clearScreen();
		cursor(4,0);
	}
	void affichage(Mot unMot){  // devenu inutile
		println("tapez : "+unMot.motstr);
	}
	void initialiser(Mot unMot){ //on prends un mot dans la liste puis on lui met un type mot utiliser la notion de utilise
		do {
			unMot.motstr = getCell(fichier,((int)(random()*rowCount(fichier))),0);
			unMot.nblettres = length(unMot.motstr);

		} while (unMot.utilise!= false); // à implémenter 
	}

	double temps(boolean debut){

		if(debut){
			tempsDebut = getTime()/1000;
		}else{
			tempsFin=getTime()/1000;
		}
		
		
		return(tempsFin-tempsDebut);
    }

	void verification(Mot unMot){
		boolean debut=true;
		cleanGinna();
		affichage(unMot);	
		//println(unMot.motstr);
		String inputString = readString();
		if (equals(inputString,unMot.motstr)) {
			score ++;
		}else if(equals(inputString,"stop")){
			resultat(score,miss,false); // on le met à false car c'est la fin
		} 
		else {
			miss ++;
		}

		debut=false;
		temps = temps + temps(debut);
		cleanGinna();
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
			println("votre temps est de : "+(int)(temps(debut)));//pas bon
			println("Votre score etait : "+score);
			println("Et vous avez loupé : "+miss+" mots.");
			System.exit(0);
	}
}
//TO DO :
/*	-debuger le temps
	-rajouter des listes de mots, demander au joueur quel liste il veut 
	-gerer les manches
	-gerer la musique en fonction des manches
	-preparer la migration console -> graphique (verrifier ou sont fait les prints)
	*/