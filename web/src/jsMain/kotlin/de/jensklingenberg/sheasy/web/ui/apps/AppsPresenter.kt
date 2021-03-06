package de.jensklingenberg.sheasy.web.ui.apps

import de.jensklingenberg.sheasy.model.SheasyError
import de.jensklingenberg.sheasy.web.data.FileDataSource
import de.jensklingenberg.sheasy.web.model.response.App
import kodando.rxjs.subscribeBy
import org.w3c.dom.events.Event

class AppsPresenter(private val view: AppsContract.View, private val fileDataSource: FileDataSource) :
    AppsContract.Presenter {


    private var appsResult = listOf<App>()

    /****************************************** React Lifecycle methods  */
    override fun componentWillUnmount() {}

    override fun componentDidMount() {

    }

    /****************************************** Presenter methods  */
    override fun onSearch(query: String) {
        appsResult
            .filter {
                it.name.contains(query, true)
            }.map { respo ->
                AppSourceItem(respo,
                    { },
                    { event: Event -> view.handleClickListItem(event, respo) })
            }
            .run(view::setData)
    }


    override fun getApps() {

        fileDataSource.getApps()
            .subscribeBy(
                next = {
                    appsResult = it

                    appsResult
                        .map { respo ->
                            AppSourceItem(respo,
                                { },
                                { event: Event -> view.handleClickListItem(event, respo) })
                        }.run {
                            view.setData(this)
                        }
                }, error = {
                    if (it is SheasyError) {
                        view.showError(it)
                    }
                }
            )


    }


    override fun downloadApk(app: App?) {
        fileDataSource.downloadApk(app)
    }
}
