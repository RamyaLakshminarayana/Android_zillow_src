package com.ramya.zillowwebtech;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {
	private EditText streetaddress;
	private EditText city;
	private Spinner spinner;
	private TextView addresslb;
	private TextView citylb;
	private TextView spinnerlb;
	private boolean addressfl = true;
	private boolean cityfl = true;
	private boolean statefl = true;
	private boolean addressflag = false;
	private boolean cityflag = false;
	private boolean stateflag = false;
	private Button search;
	String url = "http://zillowsearchproperty-env.elasticbeanstalk.com/?";
	protected String spinnervalue;
	private String empty="";
	private String jresult;
	protected int position;
	public static Drawable image1 = null;
	public static Drawable image2 = null;
	public static Drawable chart1 = null;
	public static Drawable chart2 = null;
	public static Drawable chart3 = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		handlers();
		
		ImageView img = (ImageView)findViewById(R.id.image1);
	       img.setOnClickListener(new View.OnClickListener(){
	           public void onClick(View v){
	               Intent intent = new Intent();
	               intent.setAction(Intent.ACTION_VIEW);
	               intent.addCategory(Intent.CATEGORY_BROWSABLE);
	               intent.setData(Uri.parse("http://www.zillow.com/"));
	               startActivity(intent);
	           }
	       });
	}

	private void handlers() {
		// TODO Auto-generated method stub
		streetaddress = (EditText) findViewById(R.id.street);
		city = (EditText) findViewById(R.id.city);
		spinner = (Spinner) findViewById(R.id.spinner1);
		addresslb = (TextView) findViewById(R.id.addressl);
		citylb = (TextView) findViewById(R.id.cityl);
		spinnerlb = (TextView) findViewById(R.id.spinnerl);
		search = (Button) findViewById(R.id.button1);

		streetaddress.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				//if (addressflag) {
					validateStreet();
//				} else {
//					addressflag = true;
//				}

			}

		});

		city.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
//				if (cityflag) {
					validateCity();
//				} else {
//					cityflag = true;
//				}
			}


		});

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {

				spinnervalue = parent.getItemAtPosition(position)
						.toString();
				MainActivity.this.position = position;
				validateState(position);

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		search.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				validateCity();
				validateStreet();
				validateState(position);
				if (addressfl == false && cityfl == false && statefl == false && stateflag==true /*&& cityflag==true && addressflag==true*/) {

					new test().execute(url
							+ "streetaddress="
							+ streetaddress.getText().toString()
									.replace(" ", "%20") + "&city="
							+ city.getText().toString().replace(" ", "%20")
							+ "&state=" + spinnervalue.replace(" ", "%20"));

				}
				
				
			}
		});

	}
	
	private void validateCity() {
		if (city.getText() == null
				|| city.getText().toString().trim().length() <= 0) {
			citylb.setVisibility(View.VISIBLE);
			cityfl = true;
		} else {
			citylb.setVisibility(View.INVISIBLE);
			cityfl = false;
		}
	}

	private void validateStreet() {
		if (streetaddress.getText() == null
				|| streetaddress.getText().toString().trim()
						.length() <= 0) {
			addresslb.setVisibility(View.VISIBLE);
			addressfl = true;
		}

		else {
			addresslb.setVisibility(View.INVISIBLE);
			addressfl = false;
		}
	}
	public class test extends AsyncTask<String, Void, String> {

	

		protected String doInBackground(String... urls) {
			try {
				downloadurl(urls[0]);
				if(jresult.contains("false"))
				{
					empty="no match";
				}
				else
				{
					empty="";
				JSONParser parser = new JSONParser();
				JSONObject object= (JSONObject)parser.parse(jresult);
				JSONObject objectr= (JSONObject)object.get("result");
				JSONObject objectc= null;
				objectc =(JSONObject)object.get("chart");
				JSONObject objecty1 = (JSONObject)objectc.get("year1");
				JSONObject objecty2 = (JSONObject)objectc.get("year5");
				JSONObject objecty3 = (JSONObject)objectc.get("year10");
				
				chart1= new ImageGetter() {
					
					@Override
					public Drawable getDrawable(String source) {
						// TODO Auto-generated method stub
						return imageload(source,true);
					}
				}.getDrawable((String)objecty1.get("url"));
				
				chart2= new ImageGetter() {
					
					@Override
					public Drawable getDrawable(String source) {
						// TODO Auto-generated method stub
						return imageload(source,true);
					}
				}.getDrawable((String)objecty2.get("url"));
				
				chart3= new ImageGetter() {
					
					@Override
					public Drawable getDrawable(String source) {
						// TODO Auto-generated method stub
						return imageload(source,true);
					}
				}.getDrawable((String)objecty3.get("url"));
				
				
				 //String image = "<img src = '" +(String) objectr.get("imgu") + "'/>";
							image1= imageload((String) objectr.get("imgu"),false);
							image2= imageload((String) objectr.get("imgd"),false);
				
				}

			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			return null;
		}

		protected void onPostExecute(String result) {
			if (empty=="no match") {
				TextView textview = (TextView) findViewById(R.id.textView8);
				textview.setVisibility(View.VISIBLE);
			} else {

				TextView textview = (TextView) findViewById(R.id.textView8);
				textview.setVisibility(View.INVISIBLE);
				super.onPostExecute(result);
				jparse();

			}

		}
	}

	private void downloadurl(String myurl) throws IOException {

		InputStream inputstream = null;
		jresult = "";

		try {

			// create HttpClient
			HttpClient httpclient = new DefaultHttpClient();

			// make GET request to the given URL
			HttpResponse httpResponse = httpclient.execute(new HttpGet(myurl));

			// receive response as inputStream
			inputstream = httpResponse.getEntity().getContent();

			// convert inputstream to string
			if (inputstream != null)
				jresult = readIt(inputstream);
			else
				jresult = "Did not work!";

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String readIt(InputStream inputstream) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputstream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputstream.close();
		return result;
	}

	void jparse() {

		Intent intent = new Intent(getApplicationContext(), Activitytwo.class);
		intent.putExtra("jsonString", jresult);
		startActivity(intent);

	}

	private Drawable imageload(String source, boolean isChart) {
		Drawable drawable = null;
		URL sourceURL;
		try {
			sourceURL = new URL(source);
			URLConnection urlConnection = sourceURL.openConnection();
			urlConnection.connect();
			InputStream inputStream = urlConnection.getInputStream();
			BufferedInputStream bufferedInputStream = new BufferedInputStream(
					inputStream);
			Bitmap bm = BitmapFactory.decodeStream(bufferedInputStream);
			drawable = new BitmapDrawable(getResources(), bm);
			if (isChart) {
				drawable.setBounds(0, 0, 600, 550);

			} else {
				drawable.setBounds(0, 0, 30, 42);
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return drawable;
	}

	private void validateState(int position) {
		if (stateflag) {
			// TODO Auto-generated method stub
			if (position == 0) {

				spinnerlb.setVisibility(View.VISIBLE);
				statefl = true;

			} else {
				spinnerlb.setVisibility(View.INVISIBLE);
				statefl = false;
			}
		} else {
			stateflag = true;
		}
	}

}
