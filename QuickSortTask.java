import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

class QuickSortTask implements Callable<Boolean> {
    // list des tache en prevu pour etre executer (Futures).
    public static List<Future<Boolean>> listTask = Collections.synchronizedList(new LinkedList<Future<Boolean>>());
    // le threadpool au quelle les tache seron affecter 
    private ExecutorService threadPool;
    // refereance vers le tebleau trier 
    private int[] tableau;
    private int debut;
    private int fin;

    // constructeur a fin de crer un nouvelle tache .
    public QuickSortTask(ExecutorService threadPool, int[] tableau, int debut, int fin) {
        this.threadPool = threadPool;
        this.tableau = tableau;
        this.debut = debut;
        this.fin = fin;
    }

    // method call du tache
    @Override
    public Boolean call() {
        trierRapidement(threadPool, tableau, debut, fin);
        return true;
    }
    // method trier utiliser par les tache .
    private void trierRapidement(ExecutorService threadPool, int[] t, int debut, int fin) {

        if (debut < fin) {
            int p = partitionner(t, debut, fin);
            int taillTabgauche = (p) - debut;
            if (t.length / 100.0 < taillTabgauche) {
                listTask.add(threadPool.submit(new QuickSortTask(threadPool, t, debut, p - 1)));
            } else {
                trierRapidement(threadPool, t, debut, p - 1);
            }
            int taillTabDroit = fin - (p);
            if (t.length / 100.0 < taillTabDroit) {

                threadPool.submit(new QuickSortTask(threadPool, t, p + 1, fin));
            } else {
                trierRapidement(threadPool, t, p + 1, fin);
            }
        }
    }

    // method tirer sequentielle utiliser pour faire le trie sequentielle.
    public static void trierRapidementSequetielle(int[] t, int début, int fin) {
        if (début < fin) {                             // S'il y a un seul élément, il n'y a rien à faire!
            int p = partitionner(t, début, fin) ;      
            trierRapidementSequetielle(t, début, p-1) ;
            trierRapidementSequetielle(t, p+1, fin) ;
        }
    }

    private static  void echangerElements(int[] t, int m, int n) {
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

}