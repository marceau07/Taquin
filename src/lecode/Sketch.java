package lecode;

//<editor-fold defaultstate="collapsed" desc="imports">
import donnees.Personne;
import donnees.Taquin;
import static donnees.Taquin.listeDesPersonnes;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import processing.core.PApplet;
//</editor-fold>

public class Sketch extends PApplet {
    
    int DIMCASE=80, NBCASES=4, COTE=DIMCASE*NBCASES, NBDEPMAX=1;
    
    List<Integer> resolution = new LinkedList();
    
    int colClick , ligClick, colVide, ligVide;
    int cpt=-1-NBDEPMAX;
    
    int niveau;
    
    long debutTemps; long finTemps;
    
    String nomChoisi; Scanner clavier = new Scanner(System.in);
    
    boolean reussi=false;
    
    int [][] etat={
                    { 1,  5,   9, 13}, // colonne d'abscisse 0 de la grille
                    { 2 , 6,  10, 14}, // colonne d'abscisse 0 de la grille
                    { 3,  7,  11, 15}, // colonne d'abscisse 2 de la grille
                    { 4,  8,  12,  0}  // colonne d'abscisse 3 de la grille
                  };

    int [][] etatI={
                    { 1,  5,   9, 13}, // colonne d'abscisse 0 de la grille
                    { 2 , 6,  10, 14}, // colonne d'abscisse 0 de la grille
                    { 3,  7,  11, 15}, // colonne d'abscisse 2 de la grille
                    { 4,  8,  12,  0}  // colonne d'abscisse 3 de la grille
                  };
    
    @Override
    public void setup() {
        System.out.println("Veuillez saisir votre pseudo:");
        nomChoisi = clavier.next();
        joueurs();
        
        background(220);
       
        frame.setTitle("CH'TAQUIN LÔ - Niveau " + niveau);
        
        size(COTE,COTE+55);
        smooth(); 
        
        //quadrillage();
        remplirGrille();
        melanger();
        
        debutTemps = System.currentTimeMillis();
    }
    
    @Override
    public void mousePressed() {
       
        colClick=mouseX/DIMCASE; ligClick=mouseY/DIMCASE;
        
        System.out.println("X= "+colClick+" Y= "+ligClick); 
      
        if(abs(colVide-colClick)+abs(ligVide-ligClick)==1){
        
            echangerPaveClickCaseVide();bravo();
        }        
    }
    
    @Override
    public void keyPressed(KeyEvent e){
        //Reboot + plus difficile
        if (e.getKeyCode() == KeyEvent.VK_R){
            NBDEPMAX++;
            niveau++;
            stop();
            setup();
            cpt = 0;
        }
        //Solution
        if (e.getKeyCode() == KeyEvent.VK_S){
            resolution();
        }
        
        if (e.getKeyCode() == KeyEvent.VK_M){
            System.out.println(resolution);
        }
    }
        
    @Override
    public void draw() {
         String message;
         if (!reussi){ 
            message="Temps ecoulé: "+Long.toString((System.currentTimeMillis()-debutTemps)/1000)+" secondes et "+Long.toString(cpt)+" deplacements";
         }
         else{
           //finTemps = System.currentTimeMillis();
           effacerPaveBas();
           paveWow();
           message="Bravo !" + "\nScore: " + score() + "\nVous avez mis " + Long.toString((finTemps-debutTemps)/1000) + " secondes pour le résoudre";
           
         }
       effacerPaveBas();
       fill(0);
       text(message,10,COTE+20);
       paveBas();
   }

    public void quadrillage() {
        stroke(180);
        strokeWeight(1);
        
        for(int nc=0;nc<NBCASES;nc++){ line(nc*DIMCASE,0,nc*DIMCASE,COTE); } // lignes verticales
        for(int nl=0;nl<NBCASES;nl++){ line(0,nl*DIMCASE,COTE,nl*DIMCASE); } //lignes horizontales
    }

    void remplirGrille() {
        
        for( int col=0;col<NBCASES;col++)for( int lig=0;lig<NBCASES;lig++){
        
           String n="";Integer valeur;   
            
           valeur=etat[col][lig];
           
           if(valeur!=0) { 
              
              n=Long.toString(valeur);
              this.dessinerPave(n, col, lig);

           }
           else{ colVide=col;ligVide=lig;}
          }  
    }
    
    void effacerPave(int col, int lig){
    
        stroke(220); // gris contour   pave = gris du fond
        fill(220);   // gris interieur pave = gris du fond  
        rect(2+DIMCASE*col,2+DIMCASE*lig,DIMCASE-4,DIMCASE-4,10,10);

        fill(220);   // couleur texte= couleur du fond     
        text("",col*DIMCASE+DIMCASE/2-2,lig*DIMCASE+DIMCASE/2-2);  
    }
    
    void dessinerPave(String texte,int col, int lig){
    
        stroke(180);         // gris du contour du pave
        fill(255,255,0,50);  // couleur remplissage du pave
        rect(2+DIMCASE*col,2+DIMCASE*lig,DIMCASE-4,DIMCASE-4,10,10); //dessin du pave
        
        fill(0,0,0);        // couleur du texte
        text(texte,col*DIMCASE+DIMCASE/2-2,lig*DIMCASE+DIMCASE/2-2); // Affichage de l'etiquette       
    }
        
    void echangerPaveClickCaseVide() {
       
        etat[colVide][ligVide]=etat[colClick][ligClick];
        String n=Long.toString(etat[colVide][ligVide]);
        dessinerPave(n,colVide,ligVide);
        
        etat[colClick][ligClick]=0;
        colVide=colClick;ligVide=ligClick;
        effacerPave(colVide,ligVide);
        cpt++;
    }
    
    public void deplacerCaseVide(int dir){
        // dir  0:nord 1:Est 2: Sud 3:Ouest
        
        System.out.println(dir);
        
        switch ( dir){
        
            case 0: ligClick=ligVide-1;colClick=colVide; break;
            case 1: colClick=colVide+1;ligClick=ligVide; break;
            case 2: ligClick=ligVide+1;colClick=colVide; break;
            case 3: colClick=colVide-1;ligClick=ligVide; break;
                            
        }
    
        echangerPaveClickCaseVide();
        resolution.add(dir);
    
    }

    void melanger() {
        
        int nDep=0;
        int dirPred=4;
        
        while(nDep<=NBDEPMAX){
        
            int n= (int) random(4);
            
            if(  !(ligVide==0&&n==0 || ligVide==3&&n==2  || colVide==0&&n==3 || colVide==3&&n==1)  ){
             
             if( !(dirPred==0&&n==2 || dirPred==2&&n==0 || dirPred==1&&n==3 || dirPred==3&&n==1  ) ){
                     
                deplacerCaseVide(n);nDep++;dirPred=n; 
             }    
            
            }
        }
    }
    
    void bravo() {
        int n=0;
        
        for(int nc=0;nc<NBCASES;nc++)  for(int nl=0;nl<NBCASES;nl++){
         
            if (etat[nc][nl]== etatI[nc][nl]) n++;
        } 
        
        if(n==NBCASES*NBCASES){
            reussi=true;
            finTemps = System.currentTimeMillis();
            try {
                remplirFichier();
            } catch (IOException ex) {
                Logger.getLogger(Sketch.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    } 
    
    void paveBas(){
        noStroke();
        fill(0,0,54,20);
        rect(2,COTE+2,
                COTE-4,                    
                50,
                10,10
                );
    }
    
    void effacerPaveBas(){
        noStroke();
        fill(220);
        rect(2,COTE+2,
                COTE-4,                    
                50,
                10,10
                ); 
    }
     
    void paveWow(){
        noStroke();
        fill(0,54,0,200);
        rect(4,COTE+2,COTE-4,50,10,10);
    }
     
    public void resolution(){
        Collections.reverse(resolution);
        melanger();
    }
     
    public float score(){
        //666-(nbDeplacement*1.1)-(temps*1.5)+(niveau*1.3)
         float score = (((0-(cpt*1.1f)+((finTemps-debutTemps)*1.5f)+(niveau*1.3f))*100))/1000;
        
         return score;
    }
    
     public void joueurs(){
        //On créé le joueur
        Personne pseudo = new Personne();
        pseudo.prenom = nomChoisi;
        pseudo.score = (int) score();
        listeDesPersonnes.add(pseudo);
    }
     
    /**
     *
     * @throws IOException
     */
    public void lireFichier() throws IOException{
         BufferedWriter sortie = new BufferedWriter(new FileWriter("pseudo.txt"));
         for (Personne p : listeDesPersonnes){
             sortie.newLine();
             sortie.write(p.prenom);sortie.write(" ");
             sortie.write(String.valueOf(p.score));sortie.write(";");
             sortie.newLine();
         }
         sortie.close();
     }
     
     public void remplirFichier() throws IOException {
         BufferedReader entree = new BufferedReader(new FileReader("pseudo.txt"));
         System.out.println("Liste des joueurs :");
         
         String ligne = entree.readLine();
         while (ligne != null){
             String[] infos = ligne.split(";");
             
             for(String s : infos){
                 System.out.printf("-20%s",s);
             }
             
             System.out.println();
             
             ligne = entree.readLine();
         }
         System.out.println();
     }
     
     public void afficherJoueurs(){
         
     }
}