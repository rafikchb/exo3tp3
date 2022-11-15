import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) throws Exception {
        int taille = 1000000;
        int[] tableau = new int[taille];
        int borne = 10 * taille;

        // initialisation du tableau.
        Random aléa = new Random();
        for (int i = 0; i < taille; i++) {
            tableau[i] = aléa.nextInt(2 * borne) - borne;
        }
        // affichage du tableau.
        System.out.print("Tableau initial : ");
        afficher(tableau, 0, taille - 1);

        // creation du theadpool
        ExecutorService threadPool = Executors.newFixedThreadPool(100); // creation du threadpool

        System.out.println("Démarrage du tri rapide.");

        // cration de la premiere tache
        Future<Boolean> promise = threadPool.submit(new QuickSortTask(threadPool, tableau, 0, taille - 1));
        promise.get();// attant bloquante que la promaise ce termine .
        threadPool.shutdown();// on arrete le threadpool
        System.out.print("Tableau final : ");
        afficher(tableau, 0, taille - 1);
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
