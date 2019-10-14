# SegmentDemo
This is a sample for Segment-MoEngage Integration

To get up and running with MoEngage on Android, there a couple of steps we will walk you through.

To enable its full functionality (like Push Notifications, InApp Messaging), there are still a couple of steps that you have to take care of in your Android app.

#### Adding MoEngage Dependency:

Along with the segment dependency add the below dependency in your build.gradle file.

```groovy
 compile('com.moengage:moengage-segment-integration:+') {
        transitive = true
    }
```

#### How to Initialise MoEngage SDK:
Get APP ID from the [Settings Page](http://app.moengage.com/v3/#/settings/0/0) on the MoEngage dashboard and initialise the MoEngage SDK in the `Application` class's `onCreate()`

```java
// this is the instance of the application class and "XXXXXXXXXXX" is the APP ID from the dashboard.
MoEngage moEngage = new MoEngage.Builder(this, "XXXXXXXXXXX")
            .enableSegmentIntegration()
            .build();
    MoEngage.initialise(moEngage);
```

#### Install/Update Differentiation
This is solely required for migration to MoEngage Platform. We need your help to tell the SDK whether the user is a new user for on your app or an existing user who has updated to the latest version.

If the user was already using your application and has just updated to a new version which has MoEngage SDK it is an updated call the below API

```java
MoEHelper.getInstance(getApplicationContext()).setAppStatus(AppStatus.UPDATE)
```

In case it is a fresh install call the below API

```java
MoEHelper.getInstance(getApplicationContext()).setAppStatus(AppStatus.INSTALL)
```

**This code should be done in your Application class and should be called only once.**

**If this code is not added Smart Trigger InApp/Push Campaigns on INSTALL event will not work.**

#### How To - Push Notifications:
##### 1. Adding meta information for push notification.

Along with the App Id and the notification small icon large icon and sender id(only if using GCM library) to the builder.

```java
MoEngage moEngage =
        new MoEngage.Builder(this, "XXXXXXXXXX")
            .setSenderId("xxxxxxx")
            .setNotificationSmallIcon(R.drawable.icon)
            .setNotificationLargeIcon(R.drawable.ic_launcher)
            .enableSegmentIntegration()
            .build();
    MoEngage.initialise(moEngage);
```
##### 2. Push Token

By default the SDK registers for push token. In case your application already has a mechanism to
register for push token disable push token registration by using the opt-out as shown below

```java
MoEngage moEngage =
        new MoEngage.Builder(this, "XXXXXXXXXX")
            .setNotificationSmallIcon(R.drawable.icon)
            .setNotificationLargeIcon(R.drawable.ic_launcher)
            .optOutTokenRegistration()
            .enableSegmentIntegration()
            .build();
    MoEngage.initialise(moEngage);
```

Once you have opted out of push token registration pass the token to MoEngage SDK,
the application should pass the token to MoEngage SDK from the Registration Service used for getting the push token.

```java
PushManager.getInstance().refreshToken(getApplicationContext(), token);
```

In case you are allowing MoEngage SDK to register for push but also want the token for
internal usage you can get the token by implementing `PushManager.OnTokenReceivedListener` in your **Application class**

```java
public class DemoApp extends Application implements PushManager.OnTokenReceivedListener{
  public void onCreate() {
    PushManager.getInstance().setTokenObserver(this);
  }
  @Override public void onTokenReceived(String token) {
		//save token for your use
	}
}
```
##### 3. Configure FCM

 [Configuring FCM](https://docs.moengage.com/docs/configuring-fcm)


##### 4. Configure Geo-fence

By default SDK does not track location neither geo-fence campaigns work by default.
To track location and run geo-fence campaigns you need to opt-in for location service in the MoEngage initialiser.
To initialise call the below opt-in API.

```java
    MoEngage moEngage =
        new MoEngage.Builder(this, "XXXXXXXXXXX")
            .enableLocationServices()
            .enableSegmentIntegration()
            .build();
    MoEngage.initialise(moEngage);
```

**Note:** For Geo-fence pushes to work your Application should have location permission and Play Services' Location Library should be included.

For more details on refer to the [Configuring Geo-Fence](https://docs.moengage.com/docs/geo-fence-push) section in the MoEngage documentation.

##### 5. Declaring & configuring Rich Landing Activity:
Add the following snippet and replace `[PARENT_ACTIVITY_NAME]` with the name of the parent
 activity; `[ACTIVITY_NAME]` with the activity name which should be the parent of the Rich Landing Page

 ```xml
 <activity
    android:name="com.moe.pushlibrary.activities.MoEActivity"
    android:label="[ACTIVITY_NAME]"
    android:parentActivityName="[PARENT_ACTIVITY_AME]" >
    <!-- Parent activity meta-data to support 4.0 and lower -->
    <meta-data
        android:name="android.support.PARENT_ACTIVITY"
        android:value="[PARENT_ACTIVITY_AME]" />
 </activity>
 ```

Now log on to [MoEngage Dashboard](http://app.moengage.com/login) and go to `Push Platform Settings` section. Add the GCM API Key which you have generated from the Google Developer console API Access for GCM Module. Also update the app package name.

You are now all setup to receive push notifications from MoEngage. For more information on features provided in MoEngage Android SDK refer to following links:

 * [Push Notifications](http://docs.moengage.com/docs/push-configuration)

 * [In-App messaging](http://docs.moengage.com/docs/configuring-in-app-nativ)

 * [Notification Center](http://docs.moengage.com/docs/notification-center)

 * [Advanced Configuration](https://docs.moengage.com/docs/advanced-integration)

 * [API Reference](https://moengage.github.io/MoEngage-Android-SDK/)

 * [GDPR Compliance](https://docs.moengage.com/docs/gdpr-compliance)


#### Identify
Use [Identify](https://segment.com/docs/sources/mobile/android/#identify) to track user specific attributes. It equivalent to tracking [user attributes](http://docs.moengage.com/docs/identifying-user) on MoEngage. MoEngage supports traits supported by Segment as well as custom traits. If you set traits.id, we set that as the Unique ID for that user.

#### Track
Use [track](https://segment.com/docs/sources/mobile/android/#track) to track events and user behaviour in your app.
This will send the event to MoEngage with the associated properties. Tracking events is essential and will help you create segments for engaging users.

#### Reset
If your app supports the ability for a user to logout and login with a new identity, then you’ll need to call reset for the Analytics client.

#### Migrating from older version(One time process)
Please refer to [this](http://docs.moengage.com/docs/migrating-to-7xxx) link to migrate from SDK version less than 2.0.00

