package com.ramya.zillowwebtech;

import java.util.Locale;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.UiLifecycleHelper;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class Activitytwo extends Activity implements ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v13.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	private ImageView imageSwitcher;
	private int index = 0;

	private String address;

	private Facebook facebook;

	private JSONObject object;

	private String jsonString;

	private UiLifecycleHelper uiHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activitytwo);
		uiHelper = new UiLifecycleHelper(this, null);
		uiHelper.onCreate(savedInstanceState);
		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));

		}

		facebook = new Facebook(getString(R.string.facebook_app_id));

	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			switch (position) {
			case 0:
				return new infofragment() {
					@Override
					public void onStart() {
						super.onStart();
						setcontent();
					}
				};
			case 1:
				return new zestimatefragment() {

					@Override
					public void onStart() {
						super.onStart();
						setzestimatecontents();
						Button next = (Button) findViewById(R.id.nextbutton);
						next.setClickable(true);
						next.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								if (index == 2) {
									index = 0;
								} else {
									index++;
								}
								imageSwitcher.setImageDrawable(getImage(index));
								((TextView) findViewById(R.id.zestimateheader))
										.setText(getHeader(index));
							}
						});

						Button previous = (Button) findViewById(R.id.previousbutton);
						previous.setClickable(true);
						previous.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								if (index == 0) {
									index = 2;
								} else {
									index--;
								}
								imageSwitcher.setImageDrawable(getImage(index));
								((TextView) findViewById(R.id.zestimateheader))
										.setText(getHeader(index));
							}
						});

					}
				};

			default:
				return null;
			}

		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			}
			return null;
		}
	}

	void setcontent() {
		String jsonString = getIntent().getStringExtra("jsonString");
		JSONParser parser = new JSONParser();
		try {
			object = (JSONObject) parser.parse(jsonString);
			object = (JSONObject) object.get("result");
			((TextView) findViewById(R.id.propTypeV)).setText(object.get(
					"usecode").toString());
			((TextView) findViewById(R.id.yearbuiltvalue)).setText(object.get(
					"yearbuilt").toString());
			((TextView) findViewById(R.id.lotsizevalue)).setText(object.get(
					"lotsize").toString());
			((TextView) findViewById(R.id.finareavalue)).setText(object.get(
					"finishedarea").toString());
			((TextView) findViewById(R.id.bathroomsvalue)).setText(object.get(
					"bathrooms").toString());
			((TextView) findViewById(R.id.bedroomsvalue)).setText(object.get(
					"bedrooms").toString());
			((TextView) findViewById(R.id.taxassessvalue)).setText(object.get(
					"taxassessmentyear").toString());
			((TextView) findViewById(R.id.taxvalue)).setText(object.get(
					"taxassessment").toString());
			((TextView) findViewById(R.id.lastsoldprice)).setText(object.get(
					"lastsoldprice").toString());
			((TextView) findViewById(R.id.lastdatevalue)).setText(object.get(
					"lastsolddate").toString());

			((TextView) findViewById(R.id.propestimate))
					.setText("Zestimate\u00AE Property Estimate as of "
							+ object.get("propertyestimate").toString());

			((TextView) findViewById(R.id.propestimatevalue)).setText(object
					.get("propertyestvalue").toString());

			((TextView) findViewById(R.id.proprangevalue)).setText(object.get(
					"rangelow1").toString()
					+ "-" + object.get("rangehigh1").toString());

			((TextView) findViewById(R.id.rentest))
					.setText("Rent Zestimate\u00AE Valuation as of "
							+ object.get("rentdate").toString());

			((TextView) findViewById(R.id.rentestvalue)).setText(object.get(
					"rentestval").toString());
			((TextView) findViewById(R.id.rentchangevalue)).setText(object.get(
					"rentvaluechangeval").toString());

			((TextView) findViewById(R.id.rentrangevalue)).setText(object.get(
					"rangelow2").toString()
					+ "-" + object.get("rangehigh2").toString());

			String link = "<a href='" + object.get("homedetails").toString()
					+ "'>" + object.get("street").toString() + ","
					+ object.get("city").toString() + ","
					+ object.get("state").toString() + "-"
					+ object.get("zipcode").toString() + "</a>";

			((TextView) findViewById(R.id.propertyUrl)).setText(Html
					.fromHtml(link));
			((TextView) findViewById(R.id.propertyUrl))
					.setMovementMethod(LinkMovementMethod.getInstance());

			String zillow = "<a href='http://www.zillow.com/corp/Terms.htm'>Terms of Use</a>";
			String zestimate = "<a href='http://www.zillow.com/zestimate/'>What's a zestimate</a>";
			String use = " Use is subject to ";

			((TextView) findViewById(R.id.textView1)).setText(Html
					.fromHtml("\u00a9Zillow, Inc., 2006-2014" + "<br/>" + use
							+ zillow + "<br/>" + zestimate));
			((TextView) findViewById(R.id.textView1))
					.setMovementMethod(LinkMovementMethod.getInstance());

			if (object.get("valuechangeval").toString().contains("N/A")) {
				((TextView) findViewById(R.id.overallchangevalue))
						.setText(object.get("valuechangeval").toString());

			} else if (object.get("estimatevaluechangesign").toString()
					.contains("+")) {

				String image = "<img src = '" + (String) object.get("imgu")
						+ "'>" + object.get("valuechangeval").toString();
				((TextView) findViewById(R.id.overallchangevalue)).setText(Html
						.fromHtml(image, new ImageGetter() {

							@Override
							public Drawable getDrawable(String source) {
								return MainActivity.image1;
							}

						}, null));
			} else if (object.get("estimatevaluechangesign").toString()
					.contains("-")) {
				String image = "<img src = '" + (String) object.get("imgd")
						+ "'>" + object.get("valuechangeval").toString();
				((TextView) findViewById(R.id.overallchangevalue)).setText(Html
						.fromHtml(image, new ImageGetter() {

							@Override
							public Drawable getDrawable(String source) {
								return MainActivity.image2;
							}

						}, null));
			}

			if (object.get("rentvaluechangeval").toString().contains("N/A")) {
				((TextView) findViewById(R.id.rentchangevalue)).setText(object
						.get("rentvaluechangeval").toString());

			} else if (object.get("restimatevaluechangesign").toString()
					.contains("+")) {

				String image = "<img src = '" + (String) object.get("imgu")
						+ "'>" + object.get("rentvaluechangeval").toString();
				((TextView) findViewById(R.id.rentchangevalue)).setText(Html
						.fromHtml(image, new ImageGetter() {

							@Override
							public Drawable getDrawable(String source) {
								return MainActivity.image1;
							}

						}, null));
			} else if (object.get("restimatevaluechangesign").toString()
					.contains("-")) {
				String image = "<img src = '" + (String) object.get("imgd")
						+ "'>" + object.get("rentvaluechangeval").toString();
				((TextView) findViewById(R.id.rentchangevalue)).setText(Html
						.fromHtml(image, new ImageGetter() {

							@Override
							public Drawable getDrawable(String source) {
								return MainActivity.image2;
							}

						}, null));
			}

			Button share = (Button) findViewById(R.id.fbButton);
			share.setClickable(true);
			share.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					postdialog();

				}

			});

		} catch (org.json.simple.parser.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	void setzestimatecontents() {
		jsonString = getIntent().getStringExtra("jsonString");
		JSONParser parser = new JSONParser();
		JSONObject object = null;
		try {
			object = (JSONObject) parser.parse(jsonString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		object = (JSONObject) object.get("result");
		address = object.get("street").toString() + ","
				+ object.get("city").toString() + ","
				+ object.get("state").toString() + "-"
				+ object.get("zipcode").toString();
		((TextView) findViewById(R.id.zestimateaddr)).setText(address);

		String zillow = "<a href='http://www.zillow.com/corp/Terms.htm'>Terms of Use</a>";
		String zestimate = "<a href='http://www.zillow.com/zestimate/'>What's a zestimate</a>";
		String use = " Use is subject to ";

		((TextView) findViewById(R.id.label_zest)).setText(Html
				.fromHtml("\u00a9Zillow, Inc., 2006-2014" + "<br/>" + use
						+ zillow + "<br/>" + zestimate));
		((TextView) findViewById(R.id.label_zest))
				.setMovementMethod(LinkMovementMethod.getInstance());

		imageSwitcher = (ImageView) findViewById(R.id.imageSwitcher1);

		imageSwitcher.setScaleType(ImageView.ScaleType.FIT_XY);
		((TextView) findViewById(R.id.zestimateheader))
				.setText(getHeader(index));

		imageSwitcher.setImageDrawable(getImage(0));
	}

	private Drawable getImage(int i) {
		if (i == 0)
			return MainActivity.chart1;
		else if (i == 1)
			return MainActivity.chart2;
		else if (i == 2)
			return MainActivity.chart3;

		return null;
	}

	private String getHeader(int i) {
		if (i == 0)
			return "Historical Zestimate for the past 1 year";
		else if (i == 1)
			return "Historical Zestimate for the past 5 years";
		else if (i == 2)
			return "Historical Zestimate for the past 10 years";

		return null;
	}

	public void postdialog() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Post to Facebook");
		alert.setPositiveButton("Post Property Details",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						share();
					}
				});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Toast.makeText(Activitytwo.this, "Post Cancelled",
								Toast.LENGTH_SHORT).show();
					}
				});

		alert.show();

	}

	@SuppressWarnings("deprecation")
	public void share() {
		JSONParser parser = new JSONParser();
		JSONObject objectc = null;
		try {
			objectc = (JSONObject) parser.parse(jsonString);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		objectc = (JSONObject) objectc.get("chart");
		JSONObject objecty = (JSONObject) objectc.get("year1");

		Bundle bundle = new Bundle();
		bundle.putString("name", address);
		bundle.putString("caption", "Property Information from Zillow.com");
		bundle.putString(
				"description",
				"Last Sold Price: " + object.get("lastsoldprice").toString()
						+ ", 30 Days Overall Change: "
						+ object.get("estimatevaluechangesign").toString()
						+ object.get("valuechangeval").toString());
		bundle.putString("link", object.get("homedetails").toString());
		bundle.putString("picture", objecty.get("url").toString());

		facebook.dialog(Activitytwo.this, "feed", bundle, new DialogListener() {

			@Override
			public void onFacebookError(FacebookError e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onError(DialogError e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onComplete(Bundle values) {
				// TODO Auto-generated method stub

				// When the story is posted, echo the success
				// and the post Id.
				final String postId = values.getString("post_id");
				if (postId != null) {
					Toast.makeText(Activitytwo.this,
							"Posted story, id: " + postId, Toast.LENGTH_SHORT)
							.show();
				} else {
					// User clicked the Cancel button
					Toast.makeText(Activitytwo.this, "Publish cancelled",
							Toast.LENGTH_SHORT).show();
				}

			}

			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				Toast.makeText(Activitytwo.this, "Post cancelled",
						Toast.LENGTH_SHORT).show();
			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu( Menu menu )
	{
		getMenuInflater().inflate( R.menu.activitytwo, menu );
		return true;
	}

	@Override
	public boolean onOptionsItemSelected( MenuItem item )
	{
		int id = item.getItemId();
		
		if ( id == R.id.fb_logout )
		{
			Session session = Session.getActiveSession();
			if ( session != null )
			{
				session.close();
				session.closeAndClearTokenInformation();
			}
			Toast.makeText( Activitytwo.this, "Facebook Logout Successfull", Toast.LENGTH_SHORT ).show();
			return true;
		}
		
		
		return super.onOptionsItemSelected( item );
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		facebook.authorizeCallback(requestCode, resultCode, data);
	}
}
