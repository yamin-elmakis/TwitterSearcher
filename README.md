# TwitterSearchFramework

Android library that lets you search hashtags in Twitter.

Using Volley for Async HTTP requests.

# Integration

Gradle:

    allprojects {
        repositories {
            ...
            maven { url "https://jitpack.io" }
        }
    }
   
    dependencies {
        ...
        compile 'com.github.yamin-elmakis:TwitterSearchFramework:1.0.0'
    }

# Usage

init the searcher 
    
    public class App extends Application {

        @Override
        public void onCreate() {
            super.onCreate();
            TwitterManager.getInstance().init(this);
            ...
        }
    }  

authenticate and search 
    
    TwitterManager.getInstance().authenticate(TWITTER_KEY, TWITTER_SECRET, twitterAuthListener);
    
    TwitterManager.getInstance().search(twitterSearchListener, hashtag);
