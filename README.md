# Github Search


If you type in a search query and click enter, a search is implemented.  You can sort and order the searches for each specific search api.  It checks for internet connectivity and handles any errors by displaying them to the end user.  Click on a result item to open the url associated with it.


## Here are some of the bullet points about the app

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

## Classes to check out
* **Observe** - Class that extends ObservableField (a reactive data binding class that allows for subscribing to object changes).  This class customizes the process, and allows for easy transitions between RXJava2 Observables, Data Binding, LifeCycle Observers, and Kotlin Delegates.  Gives the ability to customize setters, getters, listen for changes all while be inline and lifecycle aware!

##### Example
``` Kotlin
class ResultsActivity : BaseActivity() {

    //Start with an observe Item if you would like to be able to subscribe to it
    val itemObserve = "".observe
            // Optional getter modifier in lambda form (Example says if text is 'CLEAR_ME' then return '',
            // else return text).
            // Similar option for preSetter as well
            .customGetter {
                if (it == "CLEAR_ME")
                    ""
                else
                    it
            }
            // onChanged event, subscribed here with a CompositeDisposable that is being implemented
            // in the BaseActivity automatically - meaning this is lifecycle aware!
            .onChanged {
                "onChanged ran for value $it".timber.d
            }

    //Now take the observe item and apply it as a delegate, so any changes to 'item' will trigger
    // the 'itemObserve' onChanged event!
    var item by itemObserve
    
}
```

* **Action** - trigger actions - like OnClickListeners - Using either `Observe` or `LiveData` classes.  Allows for lifecycle awareness again

* **CustomExtensions** - a ton of different Kotlin items, such as extensions, inline functions, reified generics, operators, infix functions, and custom classes.   Really shows the capabilities of Kotlin

* **bindings** - Custom Data Bindings that can be applied in the Layout XML to attach objects to the views.  Can be one way or two way bindings for updates both from data side and ui side.  Almost acts an auto generated presenter.

## To give it some added features, I made the following enhancements

* Added a parallax collapsing toolbar
* Added a slide down and fade in animation for the Github logo at startup
* Put in a dev option to either show the urls in a Custom Tab Chrome implementation or in a web view (just to show that I could implement either one)
* Created launch icons
* Overrode cache headers on successful api results to cache them for five minutes. If you go back and forth between tabs or pages then it won't constantly re-pull the same data (especially since the api has a per minute limit without authentication).
* Added a parallax collapsing toolbar
* Added a slide down and fade in animation for the Github logo at startup
* Put in a dev option to either show the urls in a Custom Tab Chrome implementation or in a web view (just to show that I could implement either one)
* Created launch icons
* Overrode cache headers on successful api results to cache them for five minutes. If you go back and forth between tabs or pages then it won't constantly re-pull the same data (especially since the api has a per minute limit without authentication).