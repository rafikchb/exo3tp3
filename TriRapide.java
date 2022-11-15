import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

// class principale dexecuion .
public class TriRapide {
    public static void main(String[] args) throws Exception {
        int taille = 10000000;
        int[] multiTachTab = new int[taille]; // multiTachTab utiliser pour le tri sequentielle.
        int[] sequentielTab = new int[taille]; // multiTachTab utiliser pour le trie multiTache.
        int borne = 10 * taille;
        // initialisation du multiTachTab.
        Random aléa = new Random();
        for (int i = 0; i < taille; i++) {
            sequentielTab [i] = multiTachTab[i] = aléa.nextInt(2 * borne) - borne;
        }
       
        // affichage du tableau initial .
        System.out.print("tableau initial : ");
        afficher(multiTachTab, 0, taille - 1);

        // debut de la partie sequentielle
        Long debutDuTri = System.nanoTime();
        QuickSortTask.trierSequetiellement(sequentielTab, 0, taille - 1);
        Long finDuTri = System.nanoTime();
        Long dureeDuTriSequentiel = (finDuTri - debutDuTri) / 1_000_000;
        System.out.println("Version séquentielle : " + dureeDuTriSequentiel + " ms .");
        System.out.print("Tableau trié : ");
        afficher(sequentielTab, 0, taille - 1);
        // fin de la partie sequentielle.

        // debut de la partie parall`ele
        // creation du threadpool de 4 thread.
        ExecutorService threadPool = Executors.newFixedThreadPool(4); // creation du threadpool
        debutDuTri = System.nanoTime();
        // lancement du tri.
        QuickSortTask.listTask.add(threadPool.submit(new QuickSortTask(threadPool, multiTachTab, 0, taille - 1)));
        // attendre que toute les tache termine .
        while (!QuickSortTask.listTask.isEmpty()) {
            Future<Boolean> future = QuickSortTask.listTask.remove(0);
            future.get();
        }
        // arret du thread pool
        threadPool.shutdown();
        finDuTri = System.nanoTime();
        Long dureeDuTriParallele = (finDuTri - debutDuTri) / 1_000_000;
        System.out.println("Version paralléle " + dureeDuTriParallele + " ms.");
        System.out.print("Tableau trié : ");
        afficher(multiTachTab, 0, taille - 1);
        System.out.println("Gain observé : " + (dureeDuTriSequentiel*1.0/dureeDuTriParallele) );
       
        // TODO : verifier si les deux tableau sont choerant .
       
     

    }

    private static void afficher(int[] t, int début, int fin) {
        for (int i = début; i <= début + 3; i++) {
            System.out.print(" " + t[i]);
        }
        System.out.print("...");
        for (int i = fin - 3; i <= fin; i++) {
            System.out.print(" " + t[i]);
        }
        System.out.println();
    }
}
