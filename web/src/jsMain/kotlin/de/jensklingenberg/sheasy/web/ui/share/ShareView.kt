package de.jensklingenberg.sheasy.web.ui.share

import components.materialui.Button
import components.materialui.FormControl
import components.materialui.icons.SendIcon
import de.jensklingenberg.sheasy.model.Status
import de.jensklingenberg.sheasy.web.components.materialui.Input
import de.jensklingenberg.sheasy.web.model.SourceItem
import de.jensklingenberg.sheasy.web.model.StringRes
import de.jensklingenberg.sheasy.web.model.render
import kotlinx.html.InputType
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.EventTarget
import react.*
import react.dom.div


interface FileViewState : RState {
    var status: Status
    var openMenu: Boolean
    var anchor: EventTarget?
    var item: ArrayList<SourceItem>
    var messageFieldInput: String
    var activeConnection: String

}


class ShareView : RComponent<RProps, FileViewState>(), ShareContract.View {
    private var presenter: ShareContract.Presenter? = SharePresenter(this)


    /****************************************** React Lifecycle methods  */

    override fun FileViewState.init() {

        status = Status.LOADING
        openMenu = false
        anchor = null
        item = arrayListOf()
        messageFieldInput = ""
        activeConnection = StringRes.NOT_CONNECTED

    }

    override fun componentDidMount() {
        presenter?.componentDidMount()

    }

    override fun RBuilder.render() {

        div {
            +state.activeConnection
        }

        FormControl {
            Input {
                attrs {
                    type = InputType.search.realValue
                    name = "search"
                    placeholder = "Input Message"
                    onChange = {
                        onSearchInputChanged((it.target as HTMLInputElement).value)
                    }
                }
            }
        }

        Button {
            SendIcon {}
            +"Send"
            attrs {
                variant = "outlined"
                component = "span"

                onClick = {
                    // presenter.navigateUp()
                    presenter?.send(state.messageFieldInput)
                }

            }

        }

        state.item.render(this)

    }


    /****************************************** Presenter methods  */

    override fun setData(items: List<SourceItem>) {
        setState {
            this.item.clear()
            this.item.addAll(items)
        }
    }

    override fun setConnectedMessage(message: String) {
        setState {
            activeConnection = message
        }

    }

    override fun showMessage(notificationOptions: SourceItem) {
        setState {
            item.add(notificationOptions)
        }

    }


    /****************************************** Class methods  */


    private fun onSearchInputChanged(value: String) {
        state.messageFieldInput = value
    }

}


