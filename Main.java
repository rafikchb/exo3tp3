import java.util.Random;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) throws Exception {
        int taille = 20;
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
        ExecutorService threadPool = Executors.newFixedThreadPool(4); // creation du threadpool
        CompletionService<Boolean> completionService = new ExecutorCompletionService<Boolean>(threadPool);
        System.out.println("Démarrage du tri de.");

        // cration de la premiere tache
        threadPool.execute(new QuickSortTask(threadPool, tableau, 0, taille - 1));

        // wait for all tasks to be done
        while (QuickSortTask.cpt.get() > 0) ;
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
