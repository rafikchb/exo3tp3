import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import javax.swing.text.StyledEditorKit.BoldAction;

class QuickSortTask implements Callable<Boolean> {
    private ExecutorService threadPool = null;
    private int[] tableau = null;
    private int debut;
    private int fin;

    // Ref 3ela debut ou fin .
    public QuickSortTask(ExecutorService threadPool, int[] tableau, int debut, int fin) {
        this.threadPool = threadPool;
        this.tableau = tableau;
        this.debut = debut;
        this.fin = fin;
    }

    @Override
    public Boolean call() {
        return trierRapidement(threadPool, tableau, debut, fin);
    }

    private static void echangerElements(int[] t, int m, int n) {
        int temp = t[m];
        t[m] = t[n];
        t[n] = temp;
    }

    private static int partitionner(int[] t, int début, int fin) {
        int v = t[fin]; // Choix (arbitraire) du pivot : t[fin]
        int place = début; // Place du pivot, à droite des éléments déplacés
        for (int i = début; i < fin; i++) { // Parcours du *reste* du tableau
            if (t[i] < v) { // Cette valeur t[i] doit être à droite du pivot
                echangerElements(t, i, place); // On le place à sa place
                place++; // On met à jour la place du pivot
            }
        }
        echangerElements(t, place, fin); // Placement définitif du pivot
        return place;
    }

    private static Boolean trierRapidement(ExecutorService threadPool, int[] t, int debut, int fin) {
        // declaration des deux promesse .
        Future<Boolean> promise1 = null;
        Future<Boolean> promise2 = null;

        if (debut < fin) {
            int p = partitionner(t, debut, fin);

            int taillTabgauche = p - debut;
            if ((taillTabgauche * 100 / t.length) > 1) {
                System.out.println("task");
                promise1 = threadPool.submit(new QuickSortTask(threadPool, t, debut, p - 1));
            } else {
                System.out.println("recursive");

                trierRapidement(threadPool, t, debut, p - 1);
            }

            int taillTabDroit = fin - debut;
            if ((taillTabDroit * 100 / t.length) > 1) {
                System.out.println("task");
                promise2 = threadPool.submit(new QuickSortTask(threadPool, t, p + 1, fin));
            } else {
                System.out.println("recursive");
                trierRapidement(threadPool, t, p + 1, fin);
            }

            try {
                if (promise1 != null)
                    promise1.get();
                if (promise2 != null)
                    promise2.get();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;// pour dire la tache c'est terminer .
        }
        return true;
    }

}