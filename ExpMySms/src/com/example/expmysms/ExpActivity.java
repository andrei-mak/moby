package com.example.expmysms;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ExpActivity extends FragmentActivity {

	/* viewPager */
	private static final int NUM_PAGES = 3;
	Fragment f = null;

	/* Threads */
	private ProgressBar mProgressBar;
	private Handler mHandler;

	/* Settings/Preferences */
	private String mBgImage;

	/**
	 * The pager widget, which handles animation and allows swiping horizontally
	 * to access previous and next wizard steps.
	 */
	private ViewPager mPager;

	/**
	 * The pager adapter, which provides the pages to the view pager widget.
	 */
	private PagerAdapter mPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		/* Read theme from "settings" menu */
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		if (prefs.contains("bg_list")) {
			mBgImage = prefs
					.getString("bg_list", "bg_summer_green_grass_large");

		}
		else {
			mBgImage = "bg_summer_green_grass_large";
		}

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exp);
		
		/* Set BG image */
		ImageView mImageView = (ImageView) findViewById(R.id.bgImageMain);
		int mBgImageResourceId = this.getResources().getIdentifier(
				mBgImage, "drawable", this.getPackageName());
		mImageView.setImageResource(mBgImageResourceId);

		// Instantiate a ViewPager and a PagerAdapter.
		mPager = (ViewPager) findViewById(R.id.pager);
		mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(mPagerAdapter);

		mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
		mHandler = new Handler();

	}
	
	@Override
	protected void onResume() {
		/* Read theme from "settings" menu */
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		if (prefs.contains("bg_list")) {
			mBgImage = prefs
					.getString("bg_list", "bg_summer_green_grass_large");

		}
		else {
			mBgImage = "bg_summer_green_grass_large";
		}
		
		super.onResume();
		
		/* Set BG image */
		ImageView mImageView = (ImageView) findViewById(R.id.bgImageMain);
		int mBgImageResourceId = this.getResources().getIdentifier(
				mBgImage, "drawable", this.getPackageName());
		mImageView.setImageResource(mBgImageResourceId);
		
		
	}

	/* Menu define */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.exp, menu);
		return true;
	}
	
	/* Menu handle events */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.action_settings:
	        	startActivity(new Intent(this, SettingsActivity.class));
	            return true;
	        /*case R.id.help:
	            showHelp();
	            return true;*/
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	/**
	 * Button listener - Step 1
	 */
	public void onClickExportMySms(View v) {
		// Go to next fragment
		mPager.setCurrentItem(mPager.getCurrentItem() + 1, true);
	}

	/**
	 * Button listener - Step 3
	 */
	public void onClickExit(View v) {
		// Go to next fragment
		mPager.setCurrentItem(mPager.getCurrentItem() - 1, true);

		mPager.setCurrentItem(mPager.getCurrentItem() - 1, true);
	}

	/**
	 * Button listener - Step 2 - Start Export
	 */
	public void onClickExportStart(View v) {
		// Go to next fragment
		mPager.setCurrentItem(mPager.getCurrentItem() + 1, true);

		/*
		 * // Start lengthy operation in a background thread new Thread(new
		 * Runnable() { public void run() { // Update the progress bar by
		 * Handler - start mHandler.post(new Runnable() {// This thread runs in
		 * the UI public void run() { mProgressBar.setVisibility(0); } }); // Do
		 * background staff try { String mSmsData = null; mSmsData =
		 * getSms(getApplicationContext());
		 * 
		 * saveDataExStorage(mSmsData); } catch (Exception e) { // TODO: handle
		 * exception Log.d("thread", "Thread run... exception"); }
		 * 
		 * 
		 * // Update the progress bar by Handler - end mHandler.post(new
		 * Runnable() {// This thread runs in the UI public void run() {
		 * mProgressBar.setVisibility(1); } }); } }).start();
		 */
	}

	/**
	 * A simple pager adapter that represents 5 ScreenSlidePageFragment objects,
	 * in sequence.
	 */
	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		public ScreenSlidePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// return new ExpFragment1(); // in case all same

			switch (position) {
			case 0:
				f = new ExpFragment1();
				break;
			case 1:
				f = new ExpFragment2();
				break;
			case 2:
				f = new ExpFragment3();
				break;
			}
			return f;

		}

		@Override
		public int getCount() {
			return NUM_PAGES;
		}
	}

	/**
	 * Read all SMS
	 * 
	 * @param context
	 */
	private String getSms(Context context) {

		String msgData = "";

		Cursor cursor = getContentResolver().query(
				Uri.parse("content://sms/inbox"), null, null, null, null);

		cursor.moveToFirst();

		if (cursor != null && cursor.moveToFirst()) {
			do {
				for (int idx = 0; idx < cursor.getColumnCount(); idx++) {
					msgData += " " + cursor.getColumnName(idx) + ":"
							+ cursor.getString(idx);
				}
			} while (cursor.moveToNext());
		}

		return msgData;
	}

	/**
	 * Read all SMS with progress count
	 * 
	 * @param context
	 */
	/*
	 * private int getSmsProgress(Context context) {
	 * 
	 * int counter = 0;
	 * 
	 * while (counter <= 100) { counter++; switch (counter) { case 10: return
	 * 10; case 20: return 20; case 30: return 30;
	 * 
	 * default: break; } } return 100; }
	 */

	/**
	 * Save file to external storage (available throw USB connection. SD card
	 * for example)
	 * 
	 * @param dataToSave
	 *            //data that should be saved in file
	 */
	private void saveDataExStorage(String dataToSave) {

		/*
		 * Check storage availability
		 */
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// We can read and write the media
			mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// We can only read the media
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
		} else {
			// Something else is wrong. It may be one of many other states, but
			// all we need
			// to know is we can neither read nor write
			mExternalStorageAvailable = mExternalStorageWriteable = false;
		}

		/*
		 * Write file
		 */
		if (mExternalStorageWriteable) {
			File path = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
			File mFile = new File(path, "mySMSsave.txt");

			try {
				// Make sure the Pictures directory exists.
				path.mkdirs();

				// if file doesnt exists, then create it
				if (!mFile.exists()) {
					mFile.createNewFile();
				}

				FileWriter fw = new FileWriter(mFile.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(dataToSave);
				bw.close();

				/*
				 * 
				 * // Create file file.createNewFile();
				 * 
				 * // Very simple code to copy a picture from the application's
				 * // resource into the external file. Note that this code does
				 * // no error checking, and assumes the picture is small (does
				 * not // try to copy it in chunks). Note that if external
				 * storage is // not currently mounted this will silently fail.
				 * //InputStream is =
				 * getResources().openRawResource(R.drawable.balloons);
				 * OutputStream os = new FileOutputStream(file); byte[] data =
				 * dataToSave.getBytes(Charset.forName("UTF-8")); //String to
				 * bytes //is.read(data); os.write(data); //is.close();
				 * os.close();
				 */

				Toast.makeText(getApplicationContext(), "File saved " + path,
						Toast.LENGTH_SHORT).show();

			} catch (IOException e) {
				// Unable to create file, likely because external storage is
				// not currently mounted.
				Log.w("ExternalStorage", "Error writing " + mFile, e);
				Toast.makeText(getApplicationContext(), "Save failed" + e,
						Toast.LENGTH_LONG).show();
			}

		} else {
			Toast.makeText(getApplicationContext(),
					R.string.err_cant_write_sdcard, Toast.LENGTH_SHORT).show();
		}
	}

}
