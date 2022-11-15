import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {
    public static void main(String[] args) throws Exception{
        int taille = 1000;
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
        ExecutorService threadPool = Executors.newFixedThreadPool(4); // craetion du threadpool
        // cration de la premiere tache

        Future<Boolean> promise = threadPool.submit(new QuickSortTask(threadPool, tableau, 0, taille - 1));
        System.out.println("Démarrage du tri rapide.");
        long débutDuTri = System.nanoTime();
        System.out.println(promise.get());
        threadPool.shutdown();
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
