package com.moengage.segmentdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.segment.analytics.Analytics;
import com.segment.analytics.Properties;
import com.segment.analytics.Traits;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    Analytics.with(MainActivity.this)
        .identify("user attributes", new Traits().putFirstName("Umang").putGender("Male"), null);
    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        Snackbar.make(view, "Email Button Clicked", Snackbar.LENGTH_LONG)
            .setAction("Action", null)
            .show();
        //track event with attributes
        Analytics.with(MainActivity.this)
            .track("Email button Click", new Properties().putValue("email", "opened"));
      }
    });
    //track user attributes
    trackUserInfo(getBaseContext(),"25","Male","Bengaluru");
    //track event without attributes
    Analytics.with(MainActivity.this).track("only event");
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  // Method to track User Attributes
  private void trackUserInfo(Context context, String age, String gender, String city) {
    //userId is mapped to USER_ATTRIBUTE_UNIQUE_ID of MoEngage SDK
    String userId = "umang@moengage.com";
    String firstName = "Umang";
    String lastName = "Chamaria";
    String fullName = firstName + " " + lastName;
    String email = "umang@moengage.com";
    try {
      Traits userTraits = new Traits().putFirstName(firstName)
          .putLastName(lastName)
          .putName(fullName)
          .putEmail(email);
      if (age != null && !age.isEmpty()) {
        try {
          int parsedAge = Integer.parseInt(age);
          userTraits.putAge(parsedAge);
        } catch (NumberFormatException e) {
        }
      }
      if (gender != null && !gender.isEmpty()) {
        userTraits.putGender(gender);
      }
      if (city != null && !city.isEmpty()) {
        userTraits.put("user_location", city);
      }
      // Method provided by Segment for identification of users
      Analytics.with(context).identify(userId, userTraits, null);
    } catch (Exception e) {
    }
  }
}
