package c.acdi.master.jderamaix.suaps;


/**
 * Interface permettant de lié la classe Discovery à l'activité principal
 */
public interface InterfaceDecouverteReseau {
    /**
     * Méthode devant être redéfini afin de pouvoir récupérer l'adresse du serveur
     * @see Discovery.NetworkAsync#onPostExecute(String)
     */
    void recuperationIpServer();

}
