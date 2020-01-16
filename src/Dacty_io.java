import extensions.CSVFile;
import extensions.*;

class Dacty_io extends Program {

	CSVFile fichier = loadCSV("./current.csv");
	CSVFile textecsv = loadCSV("./texte.csv");
	String valider ="";  //sert à mettre en pause l'execution du programme
	int score, duree, miss, lvl, temps, entree, nbmot ,cpt;
	boolean manche, abort;
	String [][] texte = new String[columnCount(textecsv)][rowCount(textecsv)];

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
	void testPartiePerso() {
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
	/*void testTriRapide() {
		String[] tab = new String[]{"tutu","tata","toto","totu","titi","ababab","babab","cacac","dfghj","qlmnop","rtyu","thy","hj","fhj","kuik","dtfrh","jdj","dyk"};
		triRapide(tab,0,17);
		assertEquals("ababab",tab[0]);
		assertEquals("babab",tab[1]);
		assertEquals("cacac",tab[2]);
		assertEquals("dfghj",tab[3]);
		assertEquals("dtfrh",tab[4]);
		assertEquals("dyk",tab[5]);
		assertEquals("fhj",tab[6]);
		assertEquals("hj",tab[7]);
		assertEquals("jdj",tab[8]);
		assertEquals("kuik",tab[9]);
	}*/

	void algorithm() {
		boolean game = true;
		for (int i=0; i<rowCount(textecsv); i++) {
			for (int j=0; j<columnCount(textecsv); j++) {
			 	if (equals(getCell(textecsv,i,j),"vide")) {
			 	 	texte[j][i] = "";
			 	 } else {
			 	 	texte[j][i] = getCell(textecsv,i,j);
			 	 }
			 } 
		}
		
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

				println(texte[3][0]);
				valider = readString(); // on demarre le chrono après validation
				playSound("musique.mp3",true);
				manche = true;
				delay(10);
				cleanGinna();
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
				cleanGinna();
				System.exit(0);
			} else if (entree == 42) {
				beaubeau();
			}
		}
	} // fin algo.

	void regles() {      // Parce qu'elles le valent bien
		cleanGinna();
		for (int i=0; i<8; i++) {
			println(texte[2][i]);
		}
		valider = readString();
	}
	void choixNiveau() {
		cleanGinna();
		for (int i=0; i<8; i++) {
			println(texte[1][i]);
		}
	}

	void cleanGinna () {   // Pour remettre l'écran comme il faut
		clearScreen();
		cursor(4,0);
	}
	void affichage(Mot unMot){
		println(texte [3][1]+ unMot.motstr); //Un monstre de mot
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
		cpt = 0;
		abort = false;
		for (int i=0; i<14; i++) {
			println(texte[0][i]);
		}
	}

	void partiePerso() {
		int tagada = 0;
		int menu1 = 0;
		for (int re = 0; re<9; re=re+3) {
			tagada = 0;
			do {
				cleanGinna();
				for (int i=0; i<3; i++) {
					println(texte[4][re+i]);
				}
				tagada = readInt();
				if (tagada == 0) {
					tagada = 3;
				}
			} while (((tagada != 1 && (tagada != 2)) && re==0) || (((tagada>600) || (tagada<10)) && re == 3) || (((tagada>400) || (tagada<10)) && re == 6));
			if (tagada == 2){
				re = 6;
			} else if (tagada>9) {
				re = 10;
			}
		}
		if (tagada == 1) {
			duree = tagada;
		} else {
			nbmot = tagada;
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
	void triRapide(String[] tab, int deb, int fin) {
		if (deb < fin) {
			int index = decoupage(tab, deb, fin);
			triRapide(tab, deb, index-1);
			triRapide(tab, index+1, fin);
		}
	}
	int decoupage(String[] tab, int deb, int fin) {
		String pivot = tab[fin];
		int petit = (deb-1);
		for (int idx=deb; idx<fin; idx++) {
			int ca = 0;
			while (charAt(tab[idx],ca) == charAt(pivot,ca) && length(tab[idx])>ca&&length(pivot)>ca) {
				ca++;
			}
			if (charAt(tab[idx],ca) < charAt(pivot,ca)) {
				petit++;
				String temp = tab[petit];
				tab[petit] = tab[idx];
				tab[idx] = temp;
			}
		}
		String temp = tab[petit+1];
		tab[petit+1] = tab[fin];
		tab[fin] = temp;
		return petit+1;
	}

	void resultat() { // separer tout affichage pour faciliter la migration au mode graphique
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
		if (score == 0 && miss == 0) {
			println(texte[5][0]);
		} else if (abort && lvl != 5) {
			println(texte[5][1]);
		} else {
			println(texte[5][2]);
		}
		valider = readString();
		cleanGinna();
		if (score > 0 || miss > 0) {
			println(texte[5][3]);
		}
		println(texte[5][4]);
		if (score == 0 && miss == 0) {
			println(texte[5][5]);
		} else if (duree != 0 || lvl == 5 || abort) {
			println(texte[5][6] + score + " mot" + after1);
		} else {
			println(texte[5][7] + nbmot + " mots.");
		}
		if (miss > 0) {
			println(texte[5][8] + miss + ".");
		} else if (score > 0) {
			println(texte[5][9]);
		}
		if (!abort && duree != 0 && (score > 0|| miss > 0)) {
			println(texte[5][10] + duree + texte[5][11]);
		} else if (score > 0|| miss > 0) {
			println(texte[5][10] + (int)(((int)temps/10)/100) + texte[5][11]);
		}
		println("");
		println(texte[5][12]);
		valider = readString();
		nouvellePartie();
	}
	void beaubeau() { //no comment
		cleanGinna();println(",,,,,,,,,,,,,,,,,,,,,,,,,,,,,.+*++*+*+****###@@*###+##*+****#+,,,,,,,,,,+***");println(",,,,,,,,,,,,,,,,,,,,,,,,,,,:++:++++*****#*#@##@@@*@#*##****##+,,,,,,,,,,+***");println(",,,,,,,,,,,,,,,,,,,,,,,,.:+++:::++*#####@@##@@@@@@@@#*##**###+,,,,,,,,,,****");println(",,,,,,,,,,,,,,,,,,,,,,.+*+**++*:+*+*####*##@@@@@@@@@@##*#***#:,,,,,,,,,.****");println(",,,,,,,,,,,,,,,,,,,,:**+##**+*++#*#*###*##@@###@@@@@@@@##**#+:,,,,,,,,,.***#");println(",,,,,,,,,,,,,,,,,,:+*+**#**#**#@@#@@####***###@#@@@@@@#@#*#*#+:,,,,,,,,.**##");println(",,,,,,,,,,,,,,,,,+*#***#***+*#@@#@@####*#@@@#@W@@#@@@##@###***+.,,,,,,,.*###");println(",,,,,,,,,,,,,,,.+##***#*#@**####**############@@##@####@@******+.,,,,,,.*###");println(",,,,,,,,,,,,,,,:##**##****#####@#@#*#####*#@@#@#@*#@##@##*###***::,,,,,.*###");println(",,..,,,,,,,,,,,##*########**#*@@@##+*##****#@@*#######*#@@@*@###**:,,,,.*###");println(",...,,,,,,,,,,*###*#@@##@#****@@#+*+*+**##+*###***####**####*#*###+.,,,.####");println("...,,,,,,,,,,.*#####@@####*######******###+*****###*#*##****#######*.,,.####");println(".....,,,,,,,,.#@#@#@@####*********++**+**#*+*##**######@#*###*#@####+.,:####");println(".....,,,,,,,,:##*@@W@***+*++*++++*+++++****+*#****@####@#######@#####.,:####");println(",..,.........#*++:::::::::::::::::::::::..:::+++++++++**##@#@@@@@W@W@#######");println(".............#***++:+:::+:+:::::+::::::::.:::+++++++++**###@##@@@@@@#@*#####");println("....,,,......##@####**++++:::::++++:::::::::::+++++*++**#@@@###@@W@@@#+#####");println("....,,,,,,...###@@@@@@##*+++::++*+++*****+*++:::+++++++*#@@@@##@@@@@@**#####");println(".....,,,,,,..***####@@@##*++:++**###@@@####***++:+++++**#@@@@@@#@@@@#**#####");println(".......,,,,:.+#*####*####*+::++*###@@@@@@@###*#*++++++**#@@@#@@#@@#@#+*#####");println("....,,,,,,,,,+#@#@@#**+++#*++++++++:++*****+**@****#@@@#@@@#@##@##@@#:.:####");println("....,,,,,,,,,+#@@@@*++*::*::+:::::::++:+++*+**#++*##@#@@*#@##@#@@#@###.:####");println("..,,,,,,,,,,,+@@@@#*+:+:+*::::::::::++*+++++****++####@@@@@#@@#@@@@@#*.+####");println("..,,,,,,,,,,,+@@@#**+:+:+*::::...::.++::++++*+**+####@#@@@@@@##@@@#@##.+####");println(".,,,,,,,,...,*@@@**+::::+*::......::+++++:++++*#*#*##@@@@@@@@@@@W@@@##.+####");println("..,,,,,,.....*#W#+++:::::+:::....:::+++:::::+++**####@@@@@@@@W@@@@@#@##+####");println("..,,,,,,.....##@*+:::.::+::::.....::+++::::::+++*####@@@@@@@@@@@@@@####*####");println(".,,,,,,.....:###+::::::::::......:::++::::::+:+**####@@@@@@@W@@@W@@@@@######");println(",,,,,,.......@#*+::::::::::.....::::++:::::::++**##@###@@@@@@@@@WW@###@#####");println(",,,,,,......:##++::::::::::..:..::+:+::::::+::+****#####@@@@@@@@@W@#@###*###");println(",,,,,...:...:*#++:::::::::::::::.::++::::::+::+***#######@@@@@@@@@@@@@######");println("..,,........:**++:::::::::::::::::::+::::::::++*+***#*####@@#@@#@@@@##@#####");println("............:**++:+:::::::::::::::+::::::::::++++****#**###@@@@##@@@##@#####");println("............:#**++::::::::::::::::::::::::::+++++*+*****###@##@@@@@@@#######");println(".............#*+++:::::::::.:::..:.:::::::::+++++++++++*###@@##@@@@@@#######");println(",........,..:#**+:::::::::.::.::.:..:::::::::+++**+++++*##@#@@#@#@@@@##*####");println(",............#*++:::::::::::::::.:.....::::::++++++++++*###@@##@@W@@@##*####");println("...........:.+*#######*##*+:::+*########*#####***+++++++#@@@@@#@@@@##**#####");println(".....::::+::.+##@##@@##*##+:::+**###@####*#*###*#+++++++*@@@@@#@@@@@**######");println(".....::::*+++*#@#*@@#@####:..:+***###@#@@@@#@###**++++++*@@W@@@#@@@#**######");println("....:::::+++.*###*@@@@@##*..+++**####*@W#W@@*###**++++++*@@W@@W@@@@@**######");println(":.::::::+++*:+****+***###+...:++***#*+#@@@###+*##**+::++*@WWW@W@@W@#**######");println(":::+++++*+**+:+*****#***+:...:#+++****+*#****####*+++++*#@WWW@WWW@@#**######");println(":+++++***+**+:+*****#**#+..,.++*::+******+++*+++*+++++++*#@@@@WW@+****######");println("+++++****+**+::++***++:*:.,,:+++:::+*****+**+++:+:::+++++#@@@@#+:::+**######");println("**+*********:::::::::::+.,,.::++::::++*****++:::::::+++++#@@@##++##***######");println("************::::.:::::::.,.::::::::::::+::::::::::::++++*#@@@**####***######");println("***********+:::..:::::::.,.::::::::.:...:....:::::+:++++*#@@#*##*#****######");println("***********+:.::::::+*::.,..:.::.:..........:..:::+++++**#@###**+****#######");println("***********++:::::++*:+:.,...:.:+::...........:::::+++***#@*#@*++*+**#######");println("***********++:::+++++.:..,.:::..:+*:::........:::++++******++##*+++**#######");println("***********++++++***::::...::...:+**+:::.....::::++++*****++:***++***#######");println("************+++***##++:::..:....:****+:::::::::::+++******++++++++*#*#######");println("******************#*##*++:::+:::+*****+++++::::::++******+++++:::+***#######");println("*******************+*@#***********+*#**+++++++++++**#*****+++::::****#######");println("************######*+*#@#####@@@@#+++*#**+++++*+****##*#****++:::#**#*#######");println("************#####****##@@@@@@@##*++++*#**********#####****++::+#***#########");println("*************##******###@@@@#****+++++*#*****#####*#*******+++*****#########");println("#***************#*****##@@@#*****+++++*###*######**#******:::******#########");println("*******************++***##@#*****+++****########*********+:::+******########");println("********************++*******+*+**+*++***######******#***+:::********#######");println("***************+**##*###*+****++++***************************###*****#######");println("***************+*+***###########*++*****************#****@#@@#*******#######");println("*******+*******+*++**+++*#########@@##***++***+****###**###W@#*******#######");println("*************#***+****++:+::::++**#******++**+***#####**###W@*******########");println("********+****#****+#*++++++++*+++++++***+++**++*######*####W#*******########");println("*******+++*****#****##**+++++++***+++**+++**+***######*****@#**+****########");println("****+***++++**##****#@#########**+++++*++**++*#######*#++**@#*******########");println("*********++++*#*****##@@@@@@@##********+##***#######***+***@#*******########");println("**********+++*##*****###@####****#******#***########**+++**@*********#######");println("**************@#**+***#####************#**##########**+*+**#*********#######");println("+++***********@@#*++********+*+*+*******###########*#+++++***********#######");println("++++++++++++++@@##++++******++*+++++***###########*#++++**+**********#######");println("*****+++++++++@@@#*************++*+***####@########*+++++*+********###@@####");println("##*************@W##********#*#******########@#####*+++++++*****##@@@@@@@@@@#");println("##**************@@####*****#@@#######@#####@@@###*++++*+++++*#@@@@@@@@@@@@@@");println("###***************@########@@@@#*###@@@@#@@@@##@*+++++++++++@WWW@@@@@@@@@@@@");println("##****************#@@#@##@@#######@@@@@@@@@@##@*++++++++++++*@WWW@@@@@@@@@@@");println("#*+++++**##********@@@@@@@@@@@#@@@@@@@@@@@@@##***+++++++:++++*@@@@@@@@@@@@@@");println("::::....::+**::#****#@@@@@@@@@@@@@@@@@@@@@@@#***++*+*++++::::++@@@@@@@@@@@@@");println(".::::::::..+...*##*****@@@@@@@@@@@@@@@@@@@##*****++++++++::::::+@@@@@@@@@@@@");println(".:...::+*:.:..:+##******#@@@@@@@@@@@@@@@@##*****+++++++++:::::::+@@WW@W@@@@@");println("........:..+:..:###*****#@@@@@@@@@@@@@@####****+++++++++::::::::::@@WWW@@@@@");println("::.......,.:..,.###*****#@@@@@@@@@@@@@####****++++++++++:::::::::::@@@@@@@@@");println(":.......,..::..:###******###@@@@@@@@@#####***++++++++++:::::::.:::+@@@@@@@@@");println("::......,.:::...:####****###@@@@@@@@@####****++++++++++::::::.::+@@@@@@@@@@@");println("+**++::..:***:..:+@#####@@###@@@@@@@@###*****+++++++++::::::..:@@@@@@@@@@@@@");println(":.+####:.*@@#*.:*##@#@@@@@##@@@@#@######*****+++++++++::::...:@@@@@@@@@@@@@@");println(":..*#@#::##@#*:.*#####@@@@@@@@@@#@######***++++++++++:::::.:+@@@@@@@W@@@@@@@");println("+...:#+:+#####:.:+**#*#@@W@#@@@@########****+++++++++::::::*@@@@@WWWWWWWWWW@");println("::...+*+###*#*:...::+#@WWW@#@@@########***+*++++++++:::::.#@@@W@WWWWWWWWWWWW");println("::...+**##***+::::+#@@WWW@W##@@########*****++++++++::::*@@@WWWWWWWWWW@@@WWW");println("::::.:.:+***++:.:#@@@WWWWWW##@@@######******+++++++:::*@@@WWWWWWWWWW@@@@@@@@");println("......,,:+***++*@@@WWWWWWWW###########******++++++::#@@@@@WWW@@WWWW@@@@@@@@@");println("::::.::::++***#@@@@@WWWWWWW@##########*******++++*@@@@@WWWW@@WWW@W@@@@@@@@@@");println("+::..:******#@@@WWWWWWWWWWW@###########*****+++#@@@@@WWWWW@WWWWW@@@@@@@@@@@@");println("+:...+*****#@@WWWWWWWWWWWWW@###########*****@@@@@@@@@@WWWWWWWWW@@@@@@@@@@@@@");println("...,....:+*@W@WWWWWWWWWWWWW@@@@@#######*#@@@@@@@@WW@WWW@@WWWW@@WW@@@@@@@@@@@");println("++++:::::*@@WWWWWWWWWWWWWWW@WW@@@@###@@@@@@@@@@WWWWWWW@WWWWWW@W@@@@@@@@@@@@@");println("*********@@WWWWWWWWWWWWWWWW@WWW@@@@WWWW@@@@@@@@WWWWWW@WWWW@@@W@@@@@@@@@@@@@@");println("...::::+#W@WWWWWWWWWWWWWWWW@WWWWWWWWWWWWW@@@@@WWWWW@WWWW@@@WW@@@@@@@@@@@@@@@");
		println("");
		valider = readString();
		cleanGinna();
		Image celebrite = newImage("../ressources/BOBO.png");
		show(celebrite);
		println("COUCOU !!");
		cleanGinna();
		valider = readString();
	}
	void chrono(){
		if ((int)(temps/1000) >= duree) { // on arrete la mesure du temps
			manche = false;
		}
	}
	void nouvellePartie() {
		cleanGinna();
		println("On en refait une ? o/n");
		String yn = toUpperCase(readString());
		if (equals(yn,"N") ||equals(yn,"NON")) {
			cleanGinna();
			System.exit(0);
		}
	}
}