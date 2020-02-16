package com.chavau.univ_angers.univemarge.view.activities;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.chavau.univ_angers.univemarge.MainActivity;
import android.widget.Toast;
import com.chavau.univ_angers.univemarge.R;
import com.chavau.univ_angers.univemarge.database.DatabaseHelper;
import com.chavau.univ_angers.univemarge.database.dao.EtudiantDAO;
import com.chavau.univ_angers.univemarge.intermediaire.Etudiant;
import com.chavau.univ_angers.univemarge.intermediaire.Personnel;
import com.chavau.univ_angers.univemarge.view.adapters.AdapterEvenements;
import com.chavau.univ_angers.univemarge.view.adapters.AdapterMusculation;
import com.chavau.univ_angers.univemarge.view.adapters.AdapterViewPager;
import com.chavau.univ_angers.univemarge.intermediaire.MusculationData;
import com.chavau.univ_angers.univemarge.view.fragment.Configuration_dialog;
import com.fasterxml.jackson.core.io.DataOutputAsStream;

import java.util.ArrayList;
import java.util.Random;

public class Musculation extends AppCompatActivity {

    private ViewPager viewpager;
    private AdapterViewPager adapterviewpager;
    private AdapterMusculation adaptermusculation;
    private RecyclerView recyclerview;
    private MusculationData mdata;
    private ArrayList<Personnel> presences = new ArrayList<>();
    private TabLayout tabLayout;

    ArrayList<Etudiant> liste_etudiant_inscrit = new ArrayList<>();

    private EtudiantDAO etuDAO;

    /**
     * Capacité d'accueil du cours.
     */

    private int capacity = 0;


    /**
     * Durée minimale du cours.
     */

    private String duration = "00:00";

    /**
     * Méthode Retournant la capacité de la séance
     * @return Capacité de la sénace
     */

    public int capacity() { return capacity; }


    /**
     * Méthode Retournant la durée de la séance
     * @return Durée de la sénace
     */
    public String duration() { return duration; }


    private static String PERSONNELS_PRESENTS = "presences";

    private final int MY_PERMISSIONS_REQUEST_NFC = 1;
    /**
     * L'adaptateur gérant l'intéraction avec le téléphone pour le NFC.
     */
    private NfcAdapter nfcAdapter;
    /**
     * Contient le son à utiliser lorsque quelqu'un ayant badger est accepté par la base de données.
     */
    public MediaPlayer mp_son_approuver;
    /**
     * Contient le son à utiliser lorsque quelqu'un ayant badge n'est pas dans la base de données.
     */
    public MediaPlayer mp_son_refuser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musculation);

        setTitle("SUAPS : Musculation");

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewpager = findViewById(R.id.viewpager);
        recyclerview = findViewById(R.id.recyclerview_musculation);

        // Recuperation de presences si y'a une sauvegarde

        /*etuDAO = new EtudiantDAO(new DatabaseHelper(this));

        liste_etudiant_inscrit = etuDAO.listeEtudiantInscritCours(getIntent().getIntExtra(AdapterEvenements.getNomAct(),0));*/
        presences = creerPers();

        // Affectation du nombre de presents dans la salle
        mdata = creerMuscuData(presences.size());
        adaptermusculation = new AdapterMusculation(this, presences, mdata,getIntent().getIntExtra(AdapterEvenements.getNomAct(),0));

        ItemTouchHelper.SimpleCallback ihscb = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
                int position = (int) viewHolder.itemView.getTag();
                // Suppression d'un personnel et mise à jour du recyclerview
                adaptermusculation.enlever(position);
                // Mise à jour du viewpager
                mdata.setOccupation(adaptermusculation.getItemCount(), mdata.getCapacite());
                View v = viewpager.findViewWithTag("OcuppationValue");
                TextView tv = v.findViewById(R.id.id_details_value);
                tv.setText(mdata.getOccupation());
            }
        };
        new ItemTouchHelper(ihscb).attachToRecyclerView(recyclerview);


        // RFID
        mp_son_approuver = MediaPlayer.create(Musculation.this, R.raw.bonbadgeaccepter);
        mp_son_refuser = MediaPlayer.create(Musculation.this, R.raw.mauvaisbadgenonaccepter);

        /*
         * Initialize NFCAdapter
         */
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null) {
            /*
             * Matériel NFC absent
             */
            Toast.makeText(this, "NFC absent sur le téléphone, fermeture de l'application ", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if (!nfcAdapter.isEnabled()) {
            /*
             * Si le NFC n'est pas activé alors on ouvre le menu pour
             * que l'utilisateur puisse activer le module NFC
             */
            Toast.makeText(this, "Il faut activer le NFC", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
        }

        // Si les autorisations du NFC ont été refusés
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.NFC)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.NFC}, MY_PERMISSIONS_REQUEST_NFC);
        }

        // TODO: Suppression du son par default
//        nfcAdapter.enableReaderMode(this,
//                tag -> System.out.println(tag),
//            NfcAdapter.FLAG_READER_NFC_A |
//            NfcAdapter.FLAG_READER_NFC_B |
//            NfcAdapter.FLAG_READER_NFC_F |
//            NfcAdapter.FLAG_READER_NFC_V |
//            NfcAdapter.FLAG_READER_NFC_BARCODE |
//            NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS,
//            null);

    }

    public MusculationData creerMuscuData(int presences) {
        MusculationData m_data = new MusculationData(33);
        m_data.setTempsMinimum(1, 30);
        m_data.setOccupation(presences, m_data.getCapacite());
        return m_data;
    }

    @Override
    protected void onResume() {
        super.onResume();

        adapterviewpager = new AdapterViewPager(mdata, this);
        viewpager.setAdapter(adapterviewpager);

        tabLayout.setupWithViewPager(viewpager, true);

        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(adaptermusculation);

        adaptermusculation.set_listener(new AdapterMusculation.Listener() {
            @Override
            public void onClick(int position) {
                // Suppression d'un personnel et mise à jour du recyclerview
                adaptermusculation.enlever(position);
                // Mise à jour du viewpager
                mdata.setOccupation(adaptermusculation.getItemCount(), mdata.getCapacite());
                View v = viewpager.findViewWithTag("OcuppationValue");
                TextView tv = v.findViewById(R.id.id_details_value);
                tv.setText(mdata.getOccupation());
            }
        });

        // RFID
        Intent intent = new Intent(this, Musculation.class);
        intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        IntentFilter[] intentFilters = new IntentFilter[]{
                new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED),
        };
        String[][] techLists = new String[][]{
                new String[]{
                        NfcA.class.getName()
                },
        };

        try {
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, techLists);
        } catch (Exception e) {
            // TODO
            System.out.println(e.toString());
        }

    }

    public ArrayList<Personnel> creerPers() {
        ArrayList<Personnel> personnels = new ArrayList<>();
        Random rand = new Random();
        personnels.add(new Personnel("ALLON", "LEVY"));
        personnels.add(new Personnel("BACARD", "HUGO"));
        personnels.add(new Personnel("BAKER", "MATTHEW"));
        personnels.add(new Personnel("BALWE", "CHETAN"));
        personnels.add(new Personnel("BELAIR", "LUC"));
        personnels.add(new Personnel("CEBALLOS", "CESAR"));
        personnels.add(new Personnel("FAVRE", "CHARLES"));
        personnels.add(new Personnel("BYSZEWSKI", "DYLAN"));
        personnels.add(new Personnel("CHEN", "CHRISTIAN"));
        personnels.add(new Personnel("FEHM", "ARNO"));
        personnels.add(new Personnel("GARCIA", "LUIS"));
        personnels.add(new Personnel("FARGUES", "LAURENT"));
        personnels.add(new Personnel("HERBLOT", "MATHILDE"));
        personnels.add(new Personnel("LI", "WEN-WEI"));

        for (Personnel p : personnels) {
            int heure = rand.nextInt(3);
            int minute = rand.nextInt(60);
            p.setHeurePassee(heure, minute);
        }
        return personnels;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putParcelableArrayList(PERSONNELS_PRESENTS, presences);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_configuration,menu);
        return(super.onCreateOptionsMenu(menu));

    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.setting:
                configurerCours(null);
                return true;
       }
        return super.onOptionsItemSelected(item);

    }

    //appel dialog de configuration de la durée et capacité
    public void configurerCours(View view) {
        new Configuration_dialog().show(getSupportFragmentManager(),"configClasse");

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_NFC:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    System.out.println("Granted");
                } else {
                    // permission denied
                    System.out.println("denied");
                    Toast.makeText(Musculation.this, "[NFC] Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * Quand une carte est lue et détectée cette méthode est lancé
     *
     * @param intent Contient les éléments de la carte NFC
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        /*
         * Pour ne pas bloquer l'interface utilisateur, le traitement est lancé en tâche de fond.
         * Une tâche asynchrone est lancé avec un l'intent de la carte en argument
         */
        new Musculation.TraitementAsynchrone().execute(intent);
    }

    /**
     * Désactive l'option enableForegroundDispatch
     * Il est nécessaire de le faire lorsque l'application est mise en pause.
     */
    @Override
    protected void onPause() {
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
        byte[] id = tag.getId();
        return toReversedHex(id);
    }

    /**
     * Méthode calculant l'identifiant d'une carte étudiante
     *
     * @param bytes id de lecture de la carte converti en octet
     * @return l'identifiant de la carte qui correspond au code hexadéciaml inversé. // TODO : voir si vraiment utile
     */
    private String toReversedHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            int b = aByte & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b).toUpperCase());
        }
        return sb.toString();
    }

    /**
     * Lorsqu'une carte est lue elle se fait dans le thread d'interface, hors il déconseillé de faire
     * un quelconque traitement dans ce thread. Cette tâche asynchrone à pour but de faire le traitement
     * de lecture dans un nouveau thread.
     */
    public class TraitementAsynchrone extends AsyncTask<Intent, Void, String> {

        /**
         * Traitement qui se fait dans un thread de tâche de fond, afin que le thread de
         * l'interface utilisateur soit toujours utilisable
         *
         * @param intents Contient l'intent de la carte
         * @return l'id de la carte étudiante
         */
        @Override
        protected String doInBackground(Intent... intents) {
            System.out.println("doInBackground");
            /*
             * Récupérer l'intent
             */
            Intent intent = intents[0];

            Tag tagId = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG); //Récupération du Tag permettant d'avoir l'identifiant

            String idEtudiant = dumpTagData(tagId);
            /*
             * Permet d'aller dans la méthode onPostExecute
             */
            return idEtudiant;
        }


        /**
         * Méthode appliqué quand le numéro de la carte ayant badgé a finalement été obtenue,
         * Créé la requête pour envoyer le numéro à la base de données puis affiche les informations
         * relatifs à la requête, joue un son selon le réultat de la requête.
         *
         * @param s : contient le numéro de la carte de l'étudiant ayant badgé.
         */
        /*
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            boolean deja_present = false;

            System.out.println("MIFARE=" + s);
            Toast.makeText(Musculation.this, "MIFARE:\n" + s, Toast.LENGTH_LONG).show();

            for (int i = 0 ; i < adaptermusculation.getItemCount(); i++){
                AdapterMusculation.Personne p = adaptermusculation.getPersonne(i);
                if(p.getMiFare().equals(s)) {
                    adaptermusculation.enlever(i);
                    deja_present = true;
                }
            }

            if (!deja_present) // s'il est pas deja présent
                for (Etudiant e : liste_etudiant_inscrit){
                    if(e.getNo_mifare().equals(s)) { //TODO a tester
                        mp_son_approuver.start();
                        adaptermusculation.addPersonne(e);
                    }
                    else
                        mp_son_refuser.start();
                }



        }
        */

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            System.out.println("MIFARE=" + s);
//            Toast.makeText(Musculation.this, "MIFARE:\n" + s, Toast.LENGTH_LONG).show();
//            if () mp_son_approuver.start(); else mp_son_refuser.start();

            if (demo) {
                Toast.makeText(Musculation.this, "Vincent LE QUEC", Toast.LENGTH_LONG).show();
                adaptermusculation.setPresenceDemo();
                adaptermusculation.notifyDataSetChanged();
                mp_son_approuver.start();
                demo = !demo;
            } else {
                adaptermusculation.enlever(14);
                demo = !demo;
            }

        }

    }
    //TODO: Demo
    private static boolean demo = true;


}


