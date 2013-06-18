package pmf;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;


//Classe Persystence Manager Factory
// Qui permet de g�rer les instances du Manager Factory
public final class PMF {
    private static final PersistenceManagerFactory pmfInstance =
        JDOHelper.getPersistenceManagerFactory("transactions-optional");

    private PMF() {}

    public static PersistenceManagerFactory get() {
        return pmfInstance;
    }
}