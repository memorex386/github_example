package com.example.bradleythome.githubserach.search

import android.app.Application
import android.content.Context
import android.databinding.ObservableField
import android.net.ConnectivityManager
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.ViewGroup
import com.example.bradleythome.githubserach.R
import com.example.bradleythome.githubserach.databinding.CommitItemBinding
import com.example.bradleythome.githubserach.databinding.GithubRepoItemBinding
import com.example.bradleythome.githubserach.databinding.IssuesItemBinding
import com.example.bradleythome.githubserach.databinding.UsersItemBinding
import com.example.bradleythome.githubserach.models.*
import com.example.bradleythome.githubserach.network.GithubRepository
import com.example.bradleythome.githubserach.results.ResultsAdapter
import com.example.bradleythome.githubserach.results.ResultsItemViewHolder
import com.example.bradleythome.githubserach.results.fragment.*
import com.example.bradleythome.githubserach.uitl.ActionItem
import com.example.bradleythome.githubserach.uitl.asyncMain
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable


/**
 * Created by bradley.thome on 10/17/17.
 */

enum class Order(val order: String, val title: String) {
    ASC("asc", "Ascending"),
    DESC("desc", "Descending")
}

/**
 * The different github api options in enum form, including a SearchViewContainer constructor for
 * each one (using a 'when' statement which means that each one is required to have an
 * associated searchViewContainer)
 */
enum class SearchEnum(val title: String, val sortOptions: HashMap<String, String>) {
    REPOSITORIES("Repos",
            hashMapOf(Pair("stars", "Stars"), Pair("forks", "Forks"), Pair("updated", "Updated"), Pair("", "Best Match"))),
    COMMITS("Commits",
            hashMapOf(Pair("author-date", "Author Date"), Pair("committer-date", "Committer Date"), Pair("", "Best Match"))),
    ISSUES("Issues",
            hashMapOf(Pair("comments", "Comments"), Pair("created", "Created"), Pair("updated", "Updated"), Pair("", "Best Match"))),
    USERS("Users",
            hashMapOf(Pair("followers", "Followers"), Pair("repositories", "Repositories"), Pair("joined", "Joined"), Pair("", "Best Match")));

    fun toLowerCase() = this.toString().toLowerCase()

    fun noResults(context: Context, filter: String): String = context.getString(R.string.no_results, title, filter)

    fun startSearch(context: Context): String = context.getString(R.string.start_search, title)
}

fun SearchEnum.fragment(): BaseResultsFragment<*, *> = when (this) {
    SearchEnum.REPOSITORIES -> RepoFragment()
    SearchEnum.ISSUES -> IssuesFragment()
    SearchEnum.COMMITS -> CommitsFragment()
    SearchEnum.USERS -> UsersFragment()
}

fun SearchEnum.searchViewContainer(app: Application, baseResultsViewModel: BaseResultsViewModel<*>, compositeDisposable: CompositeDisposable, githubRepository: GithubRepository): SearchViewContainer<*> = when (this) {

    SearchEnum.REPOSITORIES -> object : SearchViewContainer<GithubRepo>(app, baseResultsViewModel, githubRepository, compositeDisposable, SearchEnum.REPOSITORIES) {

        override fun resultsItemViewHolder(parent: ViewGroup?, resultsItem: GithubRepo): ResultsItemViewHolder<GithubRepo> {
            return ResultsItemViewHolder.config(GithubRepoItemBinding::class.java, R.layout.github_repo_item, parent, resultsItem) { binding, resultItem ->
                binding.apply {
                    viewModel = getThis()
                    githubRepo = resultItem
                }
            }
        }

        override fun api(githubRepository: GithubRepository, searchOptions: SearchOptions): Single<out Results<GithubRepo>> {
            return githubRepository.getRepos(searchOptions)
        }
    }

    SearchEnum.COMMITS -> object : SearchViewContainer<CommitItem>(app, baseResultsViewModel, githubRepository, compositeDisposable, SearchEnum.COMMITS) {

        override fun resultsItemViewHolder(parent: ViewGroup?, resultsItem: CommitItem): ResultsItemViewHolder<CommitItem> {
            return ResultsItemViewHolder.config(CommitItemBinding::class.java, R.layout.commit_item, parent, resultsItem) { binding, resultItem ->
                binding.apply {
                    viewModel = getThis()
                    commitItem = resultItem
                }
            }
        }

        override fun api(githubRepository: GithubRepository, searchOptions: SearchOptions): Single<out Results<CommitItem>> {
            return githubRepository.getCommits(searchOptions)
        }
    }

    SearchEnum.ISSUES -> object : SearchViewContainer<IssueItem>(app, baseResultsViewModel, githubRepository, compositeDisposable, SearchEnum.ISSUES) {

        override fun resultsItemViewHolder(parent: ViewGroup?, resultsItem: IssueItem): ResultsItemViewHolder<IssueItem> {
            return ResultsItemViewHolder.config(IssuesItemBinding::class.java, R.layout.issues_item, parent, resultsItem) { binding, resultItem ->
                binding.apply {
                    viewModel = getThis()
                    issueItem = resultItem
                }
            }
        }

        override fun api(githubRepository: GithubRepository, searchOptions: SearchOptions): Single<out Results<IssueItem>> {
            return githubRepository.getIssues(searchOptions)
        }
    }

    SearchEnum.USERS -> object : SearchViewContainer<UserItem>(app, baseResultsViewModel, githubRepository, compositeDisposable, SearchEnum.USERS) {

        override fun resultsItemViewHolder(parent: ViewGroup?, resultsItem: UserItem): ResultsItemViewHolder<UserItem> {
            return ResultsItemViewHolder.config(UsersItemBinding::class.java, R.layout.users_item, parent, resultsItem) { binding, resultItem ->
                binding.apply {
                    viewModel = getThis()
                    userItem = resultItem
                }
            }
        }

        override fun api(githubRepository: GithubRepository, searchOptions: SearchOptions): Single<out Results<UserItem>> {
            return githubRepository.getUsers(searchOptions)
        }

    }


}

/**
 * Container to store all the api variables
 */
class SearchOptions(val searchEnum: SearchEnum, var query: String = "", var page: Int = 1, var sort: Pair<String, String> = Pair("", "Best Match"), val order: Order = Order.DESC) : Parcelable {
    constructor(parcel: Parcel) : this(
            SearchEnum.valueOf(parcel.readString()),
            parcel.readString(),
            parcel.readInt(),
            Pair(parcel.readString(), parcel.readString()),
            Order.valueOf(parcel.readString()))


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(searchEnum.name)
        parcel.writeString(query)
        parcel.writeInt(page)
        parcel.writeString(sort.first)
        parcel.writeString(sort.second)
        parcel.writeString(order.name);
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SearchOptions> {
        override fun createFromParcel(parcel: Parcel): SearchOptions {
            return SearchOptions(parcel)
        }

        override fun newArray(size: Int): Array<SearchOptions?> {
            return arrayOfNulls(size)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (!(other is SearchOptions)) return false
        val search = other
        return searchEnum == search.searchEnum &&
                query == search.query &&
                page == search.page &&
                sort.first == search.sort.first &&
                order == search.order
    }
}

/**
 * A View Model or Presenter of sorts to build the necessary tools needed using a generic type for various parts of the app
 */
abstract class SearchViewContainer<T : ResultsItem>(val app: Application, val baseResultsViewModel: BaseResultsViewModel<*>, val githubRepository: GithubRepository, val compositeDisposable: CompositeDisposable, val searchEnum: SearchEnum) {

    val hasResults = ObservableField<Boolean>(false)
    val loading = ObservableField<Boolean>(false)
    val itemClickedAction = ActionItem<T>()
    val recyclerAdapter = ObservableField<ResultsAdapter<T>>(ResultsAdapter(this))
    val noResultsText = ObservableField<String>(searchEnum.startSearch(app))
    val errorMessage = ObservableField<String?>(null)
    abstract fun resultsItemViewHolder(parent: ViewGroup?, resultsItem: T): ResultsItemViewHolder<T>

    abstract fun api(githubRepository: GithubRepository, searchOptions: SearchOptions): Single<out Results<T>>

    fun getThis(): SearchViewContainer<T> = this

    fun search(searchOptions: SearchOptions, success: () -> Unit = {}, fail: (Throwable?) -> Unit = {}) {
        Log.e("search", searchEnum.title)

        if (searchOptions.query.isNullOrBlank()) {
            noResultsText.set(searchEnum.startSearch(app))
            errorMessage.set(null)
            hasResults.set(false)
            fail(null)
            return
        }

        fun error(throwable: Throwable) {
            errorMessage.set(throwable.localizedMessage)
            fail(throwable)
            hasResults.set(false)
            loading.set(false)
        }

        val cm = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = cm.activeNetworkInfo
        val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting

        if (!isConnected) {
            error(Throwable("No Internet Connection"))
            return
        }


        noResultsText.set(searchEnum.noResults(app, searchOptions.query))


        loading.set(true)
        compositeDisposable.asyncMain(api(githubRepository, searchOptions), { results ->
            baseResultsViewModel.totalPages.set(Math.ceil((results.totalCount ?: 1) / 30.0).toInt())
            results.items?.let {
                baseResultsViewModel.lastSearch = searchOptions
                recyclerAdapter.get().swapData(it)
                success()
                errorMessage.set(null)
                hasResults.set(it.size > 0)
                loading.set(false)
            }
        },
                { throwable ->
                    error(throwable)
                })

    }

    fun onItemClicked(resultsItem: T) {
        itemClickedAction.actionOccurred(resultsItem)
    }


}




