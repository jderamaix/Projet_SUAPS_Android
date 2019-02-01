package c.acdi.master.jderamaix.suaps;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.List;
import android.os.AsyncTask;
import android.util.Log;


/**
 * Classe utilisé pour la découverte de l'adresse IP du serveur. Cette classe permet d'effectuer un
 * broadcast sur un port défini ainsi qu'avec un message dans la requête.
 */
public class Discovery
{
    /**
     * Délai d'attente avant d'interrompre la réception d'un message du serveur
     */
    public static final int RECEIVING_TIMEOUT = 10000;

    /**
     * Message qui sera transmis lors du broadcast
     */
    private static final String MESSAGE = "AppSUAPS";
    /**
     * Socket qui sert à communiquer le message.
     */
    private DatagramSocket socket;
    /**
     * Port réseau sur lequel le message sera transmis
     */
    private int mPort;
    /**
     * Objet devant être défini afin que l'activité puisse intérargir avec cette classe
     */
    private InterfaceDecouverteReseau _decouvrirReseau = null;
    /**
     * Adresse IP du serveur
     */
    private String ip = null;


    /**
     * Constucteur de la classe Discovery, ce dernier permet de rendre l'objet Discovery utilisable
     *
     * @param p Port réseau qui sera utilisé pour la transmission du message
     * @param interfaceDecouverteReseau objet InterfaceDecouverteReseau qui permet d'interagir avec
     *                                  l'activité sans en connaitre sa nature.
     */
    public Discovery(int p, InterfaceDecouverteReseau interfaceDecouverteReseau)
    {
        mPort = p;
        ip = new String();

        /**
         * La classe MainActivity implents l'interface InterfaceDecouverteReseau
         * Cela permet de lié la méthode défini dans la classe principale
         * à l'objet ici présent.
         */
        _decouvrirReseau = interfaceDecouverteReseau;
    }

    /**
     * Cette méthode ferme le socket de communication
     */
    public void stop()
    {

        try
        {
            socket.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Cette méthode à pour unique but de lancer
     * la tâche Asynchrone lançant le broadcast
     */
    public void getServerIp(){
        new NetworkAsync().execute(MESSAGE);
    }


    /**
     *
     * A partir d'une InetAdress il est possible de récupérer l'adresse
     * de broadcast associé à cette dernière
     *
     * @param myIpAddress Ce paramètre est l'adresse IP du téléphone et avec cette dernière
     *                    il est possible de récupérer son adresse de broadcast
     * @return Adresse de broadcast
     */
    public static InetAddress getBroadcast(InetAddress myIpAddress) {

        NetworkInterface temp;
        InetAddress iAddr = null;
        try {
            temp = NetworkInterface.getByInetAddress(myIpAddress);
            List<InterfaceAddress> addresses = temp.getInterfaceAddresses();

            for (InterfaceAddress inetAddress : addresses) {
                if(inetAddress.getBroadcast() == null)
                    Log.e("returnBroadcast","objet de retour null pas de broadcast disponible");
                else{
                    Log.e("returnBroadcast","objet de retour positif ; une adresse de broadcast est disponible");
                    iAddr = inetAddress.getBroadcast();
                }

            }
            //System.out.println("iAddr=" + iAddr);
            return iAddr;

        } catch (SocketException e) {

            e.printStackTrace();
            //System.out.println("getBroadcast" + e.getMessage());
        }
        return null;
    }

    /**
     * Cette méthode permet d'envoyer un broadcast sur le réseau et attendre de recevoir une
     * réponse
     *
     *
     * @param data Message qui sera dans la requête broadcast
     * @return  Si il y a un retour positif, cela permet de renvoyer
     *          l'adresse IP du serveur sous une forme de String
     * @throws Exception Lorsqu'un comportement innatendu survient, une exception est lancé
     *                   Cette dernière peut être intercepté afin de voir l'origine de l'erreur.
     */
    private String sendBroadcast(String data) throws Exception
    {
        /**
         * Récupération de l'adresse IP du téléphone, et l'adresse de broadcast associé
         * Création d'un DatagramSocket permettant l'envoi de données sur le réseau
         * setBroadcast permet d'autoriser le socket à faire des broadcast
         */
        InetAddress myAdress = Utils.getIPAddress(true);
        InetAddress monbrdcst = getBroadcast(myAdress);
        socket = new DatagramSocket();
        socket.setBroadcast(true);

        /**
         * Boucle permettant de répeter l'envoi du Broadcast afin que le serveur reçoive
         * le message
         */
        DatagramPacket packet = new DatagramPacket(data.getBytes(), data.length(), monbrdcst, mPort);
        for(int i =0 ; i<3;i++){
            socket.send(packet);
        }


        /**
         * Une fois le message envoyé il faut se préparer à le recevoir
         * la reception se fait sous une forme de tableau d'octets, le packet doit donc
         * être préparé.
         * setSoTimeout, permet de mettre un compte à rebours sur le temps qu'un socket attends avant
         * d'interrompre son attente de reception d'un message
         */
        byte[] buf = new byte[1024];
        packet = new DatagramPacket(buf, buf.length);
        socket.setSoTimeout(RECEIVING_TIMEOUT);


        /**
         * Si le socket ne reçoit rien à la fin du TimeOut, une Exception est levé
         */
        try{
            socket.receive(packet);
        }catch (SocketTimeoutException e){
            Log.e("DEPASSÉ",e.toString());
        }
        String s = packet.getAddress().getHostAddress(); //récupération de l'ip du serveur sous une forme
                                                        //de chaine de caractère
        stop();
        return s;
    }

    /**
     * Méthode permettant d'obtenir l'adresse du serveur
     *
     * @return Adresse IP du serveur
     */
    public String getIp() {
        return ip;
    }

    /**
     * Classe héritant de AsyncTask. NetworkAsync est la classe qui envoie le broadcast. En
     * effet il est interdit par Android d'effectuer cette action via le thread d'interface, d'où
     * l'importance d'utiliser une tâche asynchrone.
     */
    public class NetworkAsync extends AsyncTask<String,Void,String>{
        /**
         * L'execution dans une tache asynchrone est obligatoire lorsque des requêtes sur le réseau
         * sont effectué
         *
         * @param strings ce tableau contient à la case 0 le message qui sera transmis dans le broadcast
         * @return l'adresse du serveur SUAPS
         */
        @Override
        protected String doInBackground(String... strings) {
            try {
                String s = sendBroadcast(strings[0]);
                return s;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * Une fois l'execution fin la méthode onPostExecute se passe dans le thread d'interface.
         * @param s Il s'agit de l'adresse IP qui a été récupéré. Et qui correspond au serveur avec
         *          qui il faut communiquer
         */
        @Override
        protected void onPostExecute(String s) {

            ip = s;
            //Permet d'appeler la méthode recuperationIpServer dans la MainActivity
            _decouvrirReseau.recuperationIpServer();
            super.onPostExecute(s);
        }
    }
}