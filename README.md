# TwitterSearchFramework

Android library that lets you search hashtags in Twitter.

Using Volley for Async HTTP requests.

### Integration

you can add this Lib from [GitPack.io](https://jitpack.io/#yamin-elmakis/TwitterSearcher)

Gradle:

``` xml

allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
        }
    }
   
    dependencies {
        ...
        compile 'com.github.yamin-elmakis:TwitterSearchFramework:V2.0'
    }
```

### Usage

init the searcher 
``` java

    public class App extends Application {

        @Override
        public void onCreate() {
            super.onCreate();
            TwitterManager.getInstance().init(this);
            ...
        }
    }  
```

authenticate and search 

``` java
    TwitterManager.getInstance().authenticate(TWITTER_KEY, TWITTER_SECRET, twitterAuthListener);
    
    TwitterManager.getInstance().search(twitterSearchListener, hashtag);
```