package edu.msu.leemyou1.project1.Cloud;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import edu.msu.leemyou1.project1.ActiveGames;
import edu.msu.leemyou1.project1.Cloud.Models.ActiveGameResult;
import edu.msu.leemyou1.project1.Cloud.Models.Catalog;
import edu.msu.leemyou1.project1.Cloud.Models.GameResult;
import edu.msu.leemyou1.project1.Cloud.Models.Item;
import edu.msu.leemyou1.project1.Cloud.Models.RegisterResult;
import edu.msu.leemyou1.project1.Cloud.Models.LoginResult;
import edu.msu.leemyou1.project1.Cloud.Models.TurnResult;

import edu.msu.leemyou1.project1.R;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

@SuppressWarnings("deprecation")
public class Cloud {
    private static final Cloud ourInstance = new Cloud();

    public static Cloud getInstance() {
        return ourInstance;
    }

    //
    //  Be sure to change the following user and password to yours.
    //
    // change to your database
    private static final String USER = "behmnoah";
    private static final String PASSWORD = "Jangofet1999!";
    //
    //  Change the following BASE_URL to point to your web site
    //
    // need to change
    private static final String BASE_URL = "https://webdev.cse.msu.edu/~behmnoah/cse476/proj2/";
    public static final String REGISTER_PATH = "register.php";
    public static final String CATALOG_PATH = "battleship-cat.php";
    public static final String LOGIN_PATH = "login.php";
    public static final String GAME_PATH = "theGame.php";
    public static final String ActiveGame_PATH = "turnResult.php";
    public static final String ACTIVE_GAMES_PATH = "activeGame.php";
    public static final String TURN_PATH = "turnResult.php";
    private static final String UTF8 = "UTF-8";

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build();

    public String register(String username, String password) {
        BattleshipService service = retrofit.create(BattleshipService.class);
        try {
            RegisterResult theResult = service.Register(username.trim(), password.trim()).execute().body();
            String theStatusMessage = theResult.getStatus();
            String theResultMessage = theResult.getMessage();
            if (theResult.getMessage() == null) {
                return theStatusMessage;
            }
            return theResultMessage;
        } catch (IOException e) {
            return "Error";
        }
    }

    public String login(String username, String password) {
        BattleshipService service = retrofit.create(BattleshipService.class);
        try {
            LoginResult theResult = service.Login(username.trim(), password.trim()).execute().body();
            String theStatusMessage = theResult.getStatus();
            String theResultMessage = theResult.getMessage();
            if (theResult.getMessage() == null) {
                return theStatusMessage;
            }
            return theResultMessage;
        } catch (IOException e) {
            return "Error";
        }
    }

    public GameResult Game() {
        BattleshipService service = retrofit.create(BattleshipService.class);
        try {
            return service.Game().execute().body();
        } catch (IOException e) {
            return null;
        }
    }

    public TurnResult turnresult() {
        BattleshipService service = retrofit.create(BattleshipService.class);
        try {
            return service.turnresult().execute().body();
        } catch (IOException e) {
            return null;
        }
    }

    public ActiveGameResult ActiveGames() {
        BattleshipService service = retrofit.create(BattleshipService.class);
        try{
            return service.ActiveGameResult().execute().body();
        } catch(IOException e){
            return null;
        }
    }


    public Catalog getCatalog() throws IOException, RuntimeException {


        BattleshipService service = retrofit.create(BattleshipService.class);

        Response response = service.getCatalog(USER, PASSWORD).execute();
        // check if request failed
        if (!response.isSuccessful()) {
            Log.e("getCatalog", "Failed to get catalog, response code is =" + response.code());
            return new Catalog("no", new ArrayList<Item>(), "Server error" + response.code());
        }
        Catalog catalog = (Catalog) response.body();
        if (catalog.getStatus().equals("no")) {
            String string = "Failed to get catalog, msg is =" + catalog.getMessage();
            Log.e("getCatalog", string);
            return new Catalog("no", new ArrayList<Item>(), string);

        }

        if (catalog.getItems() == null) {
            catalog.setItems(new ArrayList<Item>());
        }
        return catalog;
    }

    public String getUsername() { return USER; }

    public class CatalogAdapter extends BaseAdapter {


        /**
         * The items we display in the list box. Initially this is
         * null until we get items from the server.
         */
        private Catalog catalog = new Catalog("", new ArrayList(), "");

        /**
         * Constructor
         */
        public CatalogAdapter(final View view) {
            // Create a thread to load the catalog
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        catalog = getCatalog();

                        if (catalog.getStatus().equals("no")) {
                            String msg = "Loading catalog returned status 'no'! Message is = '" + catalog.getMessage() + "'";
                            throw new Exception(msg);
                        }
                        if (catalog.getItems().isEmpty()) {
                            String msg = "Catalog does not contain any hattings.";
                            throw new Exception(msg);
                        }

                        view.post(new Runnable() {

                            @Override
                            public void run() {
                                // Tell the adapter the data set has been changed
                                notifyDataSetChanged();
                            }

                        });
                    } catch (Exception e) {
                        // Error condition! Something went wrong
                        Log.e("CatalogAdapter", "Something went wrong when loading the catalog", e);
                        view.post(new Runnable() {
                            @Override
                            public void run() {
                                String string;
                                // Make sure there is a message in teh catalog
                                // If there isn't use the message from the exception
                                if (catalog.getMessage() == null) {
                                    string = e.getMessage();
                                } else {
                                    string = catalog.getMessage();
                                }
                                Toast.makeText(view.getContext(), string, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }).start();
        }

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return catalog.getItems();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.catalog_item, parent, false);
            }

            TextView tv = (TextView) view.findViewById(R.id.textItem);
            tv.setText(catalog.getItems().get(position).getName());


            return view;
        }
    }

}