package com.example.myapplication;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.material.navigation.NavigationView;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
       implements NavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //toolbar
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, myToolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        getNews getNews = new getNews();
        getNews.execute("https://feeds.bbci.co.uk/news/world/us_and_canada/rss.xml");
    }
    // async
    private class getNews  extends AsyncTask<String, Integer, ArrayList<object>> {
        ArrayList<object> newsList = new ArrayList<>();
        ProgressBar progressBar = findViewById(R.id.progressBar);

        public ArrayList<object> doInBackground(String... args) {
            try {
                URL url = new URL(args[0]);

                //imitate real connection
                publishProgress(0);
                Thread.sleep(100);
                publishProgress(25);


                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                InputStream response = conn.getInputStream();
                //get data from RSS string
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(response, "UTF-8");

                //imitate real connection
                Thread.sleep(150);
                publishProgress(50);

                ArrayList<String> ls = new ArrayList<>();
                boolean item = false;
                while (xpp.getEventType() != xpp.END_DOCUMENT) {
                    String title;
                    String link;
                    String description;
                    String date;
                    String guid;
                    if (xpp.getEventType() == xpp.START_TAG) {
                        String tagName = xpp.getName();
                        xpp.next();
                        if (tagName.equals("item")) {
                            item = true;
                        }
                        if (tagName.equals("title") && item) {
                            title = xpp.getText();
                            ls.add(title);
                            System.out.println("--------title: " + title);
                        }
                        if (tagName.equals("link") && item) {
                            link = xpp.getText();
                            ls.add(link);
                        }
                        if (tagName.equals("description") && item) {
                            description = xpp.getText();
                            ls.add(description);
                        }
                        if (tagName.equals("guid")) {
                            guid = xpp.getText();
                            ls.add(guid);
                        }
                        if (tagName.equals("pubDate")) {
                            date = xpp.getText();
                            ls.add(date);
                            if ( ls.size() >= 5) {
                                newsList.add(new object(ls.get(0), ls.get(1), ls.get(2), ls.get(3), ls.get(4)));
                                ls.clear();
                            }
                        }
                    }
                    xpp.next();
                }
                //close stream and connection
                if (response != null) { response.close();}
                conn.disconnect();
                //Imitate real connection
                Thread.sleep(100);
                publishProgress(75);
                Thread.sleep(50);
                publishProgress(100);

            } catch (Exception e) {
                System.out.println("...");
            }
            return newsList;
        }
        public void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
        }

        public void onPostExecute(ArrayList<object> newsList) {
            class MyListAdapter extends BaseAdapter {
                public int getCount() {
                    return newsList.size();}
                public Object getItem(int position) {
                    return position;}
                public long getItemId(int position) {
                    return position;}
                public View getView(int position, View old, ViewGroup parent) {
                    View newView = old;
                    LayoutInflater inflater = getLayoutInflater();
                    if (newView == null) {
                        newView = inflater.inflate(R.layout.row_layout, parent, false);
                    }
                    TextView tView = newView.findViewById(R.id.textGoesHere);
                    tView.setText(newsList.get(position).title);
                    return newView;
                }
            }

            ListView listView = findViewById(R.id.listView);
            MyListAdapter myAdapter = new MyListAdapter();
            listView.setAdapter(myAdapter);

            listView.setOnItemClickListener( (parent, view, position, id) -> {
                object selectedNews = newsList.get(position);
                Intent intent = new Intent(MainActivity.this, Detail.class);
                intent.putExtra("title",selectedNews.title);
                intent.putExtra("description", selectedNews.description);
                intent.putExtra("link", selectedNews.link);
                intent.putExtra("date", selectedNews.date);
                startActivity(intent);

            });
        }
    }

        //menu and nav
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } if (id == R.id.fav) {
            Intent intent = new Intent(this, Favorite.class);
            startActivity(intent);
        } else if (id == R.id.contact) {
            Intent intent = new Intent(this, ContactUs.class);
            startActivity(intent);
        } else if (id == R.id.exit) {
            finishAffinity();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.homeIcon) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.helpIcon) {
            AlertDialog.Builder aDialog = new AlertDialog.Builder(this);
            String message = getResources().getString(R.string.note2);
            aDialog.setMessage(message)
                    .setTitle(R.string.howTo)
                    .setNeutralButton(R.string.ok, (click, arg) -> {
                    })
                    .create()
                    .show();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return true;
    }

}