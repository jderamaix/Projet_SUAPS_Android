package c.acdi.master.jderamaix.suaps;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RFIDActivity extends AppCompatActivity {

    /**
     * L'adaptateur gérant l'intéraction avec le téléphone pour le NFC.
     */
    private NfcAdapter nfcAdapter;
    /**
     * Tag repérant cet Activity dans les messages d'erreur.
     * @see ServiceGenerator#Message(Context, String, Throwable)
     */
    public String TAG = "RFIDActivity";
    /**
     * Int utilisé dans la gestion des messages informatifs concernant les utlisateurs,
     * permettant de pouvoir réafficher le message initial de demande de badgeage.
     */
    public int compteur = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rfid_activite);

        /*
         * Initialize NFCAdapter
         */
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null) {
            /*
             * Matériel NFC absent
             */
            Toast.makeText(this,"NFC absent sur le téléphone, fermeture de l'application ",Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if (!nfcAdapter.isEnabled()) {
            /*
             * Si le NFC n'est pas activé alors on ouvre le menu pour
             * que l'utilisateur puisse activer le module NFC
             */
            Toast.makeText(this,"Il faut activer le NFC",Toast.LENGTH_LONG).show();
            startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
        }
    }

    /**
     * Méthode permettant de mettre en place la reception d'une lecture NFC
     * la methode enableForegroundDispatch permet de dire que l'Activité doit être au premier plan
     * pour lire une carte NFC.
     */
    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = new Intent(this,RFIDActivity.class);
        intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        nfcAdapter.enableForegroundDispatch(this,pendingIntent, null,null);
    }


    /**
     * Quand une carte est lue et détectée cette méthode est lancé
     * @param intent Contient les éléments de la carte NFC
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        /*
         * Pour ne pas bloquer l'interface utilisateur, le traitement est lancé en tâche de fond.
         * Une tâche asynchrone est lancé avec un l'intent de la carte en argument
         */
        new RFIDActivity.TraitementAsynchrone().execute(intent);
    }


    /**
     * Désactive l'option enableForegroundDispatch
     * Il est nécessaire de le faire lorsque l'application est mise en pause.
     */
    @Override
    protected void onPause() {;
        super.onPause();
        nfcAdapter.disableForegroundDispatch(this);
    }

    /**
     * Méthode servant à récupérer l'identifiant d'une carte étudiante
     *
     * @param tag Il s'agit ici de l'identifiant de lecture de la carte qui va être
     *            utilisé pour créer l'identifiant unique
     * @return L'identifiant unique de la carte
     */
    private String dumpTagData(Tag tag) {
        byte[] id = tag.getId();;
        return toReversedHex(id);
    }

    /**
     * Méthode calculant l'identifiant d'une carte étudiante
     *
     * @param bytes id de lecture de la carte converti en octet
     * @return l'identifiant de la carte qui correspond au code hexadéciaml inversé.
     */
    private String toReversedHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; ++i) {
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
        }
        return sb.toString();
    }


    public class TraitementAsynchrone extends AsyncTask<Intent,Void,String> {

        /**
         * Traitement qui se fait dans un thread de tâche de fond, afin que le thread de
         * l'interface utilisateur soit toujours utilisable
         *
         * @param intents Contient l'intent de la carte
         * @return l'id de la carte étudiante
         */
        @Override
        protected String doInBackground(Intent... intents) {

            /*
             * Récupérer l'intent
             */
            Intent intent = intents[0];

            Tag tagId =  intent.getParcelableExtra(NfcAdapter.EXTRA_TAG); //Récupération du Tag permettant d'avoir l'identifiant

            String idEtudiant = dumpTagData(tagId);
            //Log.e("Identifiant carte Etu",idEtudiant);

            /*
             * Permet d'aller dans la méthode onPostExecute
             */
            return idEtudiant;
        }


        /**
         * Méthode appliqué quand le numéro de la carte ayant badgé a finalement été obtenue,
         *   Créé la requête pour envoyé le numéro à la base de données et affiché les informations
         *   relatifs à la requête .
         * @param s : contient le numéro de la carte de l'étudiant ayant badgé.
         */
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            /*
             * Recherche le TextView utilisé pour afficher les infromations relatives au badgeage
             */
            final TextView textView = (TextView) findViewById(R.id.textViewRFIDActivity);

            /*
             * Appelle le ServiceGenerator pour générer la requête.
             */
            Client client = ServiceGenerator.createService(Client.class);

            /*
             * Prépare l'envoie de la requête en instanciant le Call avec la méthode approprié pour la requête voulue,
             * ici l'envoie du numéro de la carte d'un étudiant.
             */
            Call<ReponseRequete> call_Post = client.EnvoieNumCarte(s);

            /*
             * Méthode envoyant la requête asynchronement à la base de données et stockant la
             * réponse obtenue (erreur ou réussite) dans CallBack
             * Ici le traitement de CallBack est directement appliqué :
             *   onResponse si la requête est considérée réussite.
             *  onFailure si la requête est considérée ratée.
             */
            call_Post.enqueue(new Callback<ReponseRequete>() {
                /*
                 * Méthode étant appliqué lorsque la requête est reçu par la base de données.
                 * Mais attention, il peut toujours y avoir des problèes ayant occurés.
                 */
                @Override
                public void onResponse(Call<ReponseRequete> call, Response<ReponseRequete> reponse) {

                    /*
                     * Test si la requête a réussi ( Aucune erreur comme l'erreur 404 ou 500).
                     */
                    if (reponse.isSuccessful()) {
                        /*
                         * Change le message du TextView pour informer les utilisateurs du résultat du badgeage
                         *  (Manque de place dans la séance, badgeage réussi, ...).
                         */
                        String Result = reponse.body().getReponse();
                        textView.setText(Result);

                        /*
                         * Réinitialise le compteur pour pouvoir le relancer.
                         */
                        compteur = 0;

                        /*
                         * Créé et lance un compteur permettant de remettre le message du TextView affiché
                         * à celui indiquant de badger après qu'un temps donnée ce soit passer.
                         */
                        new CountDownTimer(4000, 1000) {

                            /*
                             * Méthode s'aplliquantà chaque tick, soit à chaque miliseconde,
                             * ici augmentant la valeur du compteur de 1.
                             */
                            @Override
                            public void onTick(long millisUntilFinished) {
                                /*
                                 * Augmente le compteur de 1 pour atteindre la limite.
                                 */
                                compteur++;
                            }

                            /*
                             * Méthode s'appliquant lorsque le compteur arrive au temps fixé.
                             */
                            public void onFinish() {
                                /*
                                 * Remet le message du TextView à sa valeur de base indiquant de badger.
                                 */
                                String texteAffichage = "Veuillez Badger" ;
                                textView.setText(texteAffichage);
                            }
                        }.start();
                    } else {
                        /*
                         * Affiche dans le log d'erreur le statut de la requête, soit quelle type
                         * d'erreur a été rencontré comme l'erreur 404 ou 500.
                         */
                        Log.e(TAG, "status Code: " + reponse.code());
                    }
                }

                /**
                 * Méthode étant appliqué losque des problèmes sont apparus lors de  :
                 *   - la connexion au serveur,
                 *   - la création de la requête,
                 *   - la transformation de la réponse en objet java.
                 * @Param call : La requête provoquant le onFailure.
                 * @Param t    : objet contenant le message et le code d'erreur provoqué par la requête.
                 */
                @Override
                public void onFailure(Call<ReponseRequete> call, Throwable t) {
                    ServiceGenerator.Message(RFIDActivity.this, TAG, t);
                }
            });


        }
    }

}
