package com.example.rajat.jsonparser;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView lv1;
    String myJSON;
    ArrayList<String> arrayList;
    ArrayAdapter<String> adapter;
    String roll_nu,name,email,phone;
    private static final String TAG_RESULT="result";
    private static final String TAG_ROLL="Roll";
    private static final String TAG_NAME="Name";
    private static final String TAG_EMAIL="email";
    private static final String TAG_PHONE="phone";
    JSONArray people=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv1= (ListView) findViewById(R.id.mylist);
        arrayList=new ArrayList<>();
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList);
        lv1.setAdapter(adapter);
        getData();
    }

    public void getData()
    {
        class GetJSONData extends AsyncTask<String,String,String>
        {
            ProgressDialog pd;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd=new ProgressDialog(MainActivity.this);
                pd.setMessage("Downloading...");
                pd.setCanceledOnTouchOutside(false);
                pd.show();
            }

            @Override
            protected String doInBackground(String... strings)
            {
                DefaultHttpClient defaultHttpClient=new DefaultHttpClient(new BasicHttpParams());
                HttpPost httpPost = new HttpPost("https://rajatpanda008.000webhostapp.com/getdata.php");
                //URL of the getdata php file.
                httpPost.setHeader("Content-type","application/json");
                //this defines the content type of the URL
                InputStream inputStream=null;
                //to store the data received
                String result=null;
                try
                {
                    HttpResponse response = defaultHttpClient.execute(httpPost);
                    HttpEntity entity = response.getEntity();
                    //this will return a JSON array (Entity stores data in Key-Value pair)
                    inputStream = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"),8);
                    StringBuilder sb=new StringBuilder();
                    String line=null;
                    while ((line=reader.readLine())!=null)
                    {
                        sb.append(line+'\n');
                    }
                    result = sb.toString();
                }
                catch (Exception obj)
                {

                }
                finally {
                    try
                    {
                        if(inputStream!=null)
                        {
                            inputStream.close();
                        }
                    }
                    catch (Exception e)
                    {

                    }
                }
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                pd.dismiss();
                myJSON=s;
                show();
            }
        }
        GetJSONData obj=new GetJSONData();
        obj.execute();
    }

    public void show()
    {
        try
        {
            //Important
            JSONObject jsonObject=new JSONObject(myJSON);
            people=jsonObject.getJSONArray(TAG_RESULT);
            for (int i=0;i<people.length();i++)
            {
                JSONObject o = people.getJSONObject(i);
                roll_nu=o.getString(TAG_ROLL);
                name=o.getString(TAG_NAME);
                email=o.getString(TAG_EMAIL);
                phone=o.getString(TAG_PHONE);
                arrayList.add("Roll: "+roll_nu+"\nName: "+name+"\nEmail: "+email+"\nPhone: "+phone+"\n");
                adapter.notifyDataSetChanged();
            }
        }
        catch (Exception obj)
        {

        }
    }
}
