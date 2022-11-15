import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

class QuickSortTask implements Runnable {
    private ExecutorService threadPool;
    private int[] tableau;
    private int debut;
    private int fin;

    public QuickSortTask(ExecutorService threadPool, int[] tableau, int debut, int fin) {
        // System.out.println("inside the QuickSortTask constructor");
        this.threadPool = threadPool;
        this.tableau = tableau;
        this.debut = debut;
        this.fin = fin;
        // System.out.println("inside the QuickSortTask constructor");
    }

    @Override
    public void run() {
        trierRapidement(threadPool, tableau, debut, fin);
    }

    private void trierRapidement(ExecutorService threadPool, int[] t, int debut, int fin) {

        if (debut < fin) {
            int p = partitionner(t, debut, fin);

            int taillTabgauche = (p) - debut;
            if (t.length / 100.0 < taillTabgauche) {
                // System.out.println("tache gauche");
                threadPool.execute(new QuickSortTask(threadPool, t, debut, p - 1));
            } else {
                // System.out.println("recursion gauche");
                trierRapidement(threadPool, t, debut, p - 1);
            }

            int taillTabDroit = fin - (p);
            if (t.length / 100.0 < taillTabDroit) {
                // System.out.println("tache droite");
                threadPool.execute(new QuickSortTask(threadPool, t, p + 1, fin));
            } else {
                // System.out.println("recursion droite");

                trierRapidement(threadPool, t, p + 1, fin);
            }
        }
    }

    private void echangerElements(int[] t, int m, int n) {
        int temp = t[m];
        t[m] = t[n];
        t[n] = temp;
    }

    private int partitionner(int[] t, int début, int fin) {
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

}