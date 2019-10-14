package com.moengage.segmentdemo;

import android.app.Application;
import com.moengage.core.Logger;
import com.moengage.core.MoEngage;
import com.segment.analytics.Analytics;
import com.segment.analytics.android.integrations.moengage.MoEngageIntegration;
import com.squareup.leakcanary.LeakCanary;

import static com.moengage.core.Logger.VERBOSE;

/**
 * @author Umang Chamaria
 */
public class SegmentDemoApplication extends Application {
  @Override
  public void onCreate() {
    super.onCreate();
    if (LeakCanary.isInAnalyzerProcess(this)) {
      // This process is dedicated to LeakCanary for heap analysis.
      // You should not init your app in this process.
      return;
    }
    LeakCanary.install(this);
    Analytics analytics = new Analytics.Builder(getApplicationContext(),
            "WRITEKEY")//use your own write key
    .logLevel(Analytics.LogLevel.VERBOSE)// should be added only in debug builds. Make sure this
    // is removed before a signed apk is generated.
    .use(MoEngageIntegration.FACTORY)//enable MoEngage integration
    .build();
    Analytics.setSingletonInstance(analytics);//recommended as creating a new instance every time
    // is expensive in terms of resources used

    // this is the instance of the application class and "XXXXXXXXXXX" is the APP ID from the dashboard.
    MoEngage moEngage = new MoEngage.Builder(this, "XXXXXXXXXX")
        .enableSegmentIntegration()
        .setLogLevel(VERBOSE)
        .setNotificationSmallIcon(R.drawable.icon)
        .setNotificationLargeIcon(R.drawable.ic_launcher)
        .enableLocationServices()
        .build();
    MoEngage.initialise(moEngage);
  }
}
