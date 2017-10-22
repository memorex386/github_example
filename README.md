# Github Search


If you type in a search query and click enter, a search is implemented.  You can sort and order the searches for each specific search api.  It checks for internet connectivity and handles any errors by displaying them to the end user.  Click on a result item to open the url associated with it.


##Here are some of the bullet points about the app

* I wrote the whole app in Kotlin
* Two way view data binding with custom bindings for clean, efficient view presenting
* It uses Dagger 2 for dependency injection (a bit much for an app this small, but I just wanted to treat this app like it's a normal sized app)
* RxJava for observables and async
* OKHttp and Retrofit coupled with RxJava for async http requests
* Uses latest android arch components like ViewModel, LiveData, and Lifecycles to make it lifecycle aware
* Picasso with Okhttp for image downloading
* Recycler View for displaying the items list
* Moshi (comparable to GSON) for auto converting JSON to POJO (POKO for Kotlin?) coupled with OKHttp
* Pulled most of the view logic into custom data bindings, hopefully making unit tests easier to implement in the future for being able to run a unit test without having to inflate views
* Built with Android Studio 3.0 RC1 using the latest Gradle build

##To give it some added features, I made the following enhancements

* Added a parallax collapsing toolbar
* Added a slide down and fade in animation for the Github logo at startup
* Put in a dev option to either show the urls in a Custom Tab Chrome implementation or in a web view (just to show that I could implement either one)
* Created launch icons
* Overrode cache headers on successful api results to cache them for five minutes. If you go back and forth between tabs or pages then it won't constantly re-pull the same data (especially since the api has a per minute limit without authentication).