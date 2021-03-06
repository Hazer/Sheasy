package de.jensklingenberg.sheasy.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import de.jensklingenberg.sheasy.App
import de.jensklingenberg.sheasy.R
import de.jensklingenberg.sheasy.data.usecase.ShareUseCase
import de.jensklingenberg.sheasy.model.SideMenuEntry
import de.jensklingenberg.sheasy.ui.files.FilesFragmentDirections
import de.jensklingenberg.sheasy.utils.PermissionUtils
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : AppCompatActivity(), Drawer.OnDrawerItemClickListener {

    val compositeDisposable = CompositeDisposable()

    lateinit var mainActivityDrawer: MainActivityDrawer
    private lateinit var navController: NavController

    @Inject
    lateinit var permissionUtils: PermissionUtils

    private val REQUEST_FILE_PERMISSION = 1

    @Inject
    lateinit var shareUseCaseProvider: ShareUseCase

    init {
        initializeDagger()
    }

    private fun initializeDagger() = App.appComponent.inject(this)


    /******************************************  Lifecycle methods  */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        setupNavigation()
        handleIntent(intent)
        mainActivityDrawer = MainActivityDrawer(this)
        permissionUtils.requestPermission(this, REQUEST_FILE_PERMISSION)

    }

    override fun onDestroy() {
        super.onDestroy()
        mainActivityDrawer.onDestroy()
    }


    /******************************************  Listener methods  */

    override fun onItemClick(view: View?, position: Int, drawerItem: IDrawerItem<*, *>?): Boolean {
        when (val item = drawerItem?.tag) {

            is SideMenuEntry -> {

                if (item.navId == -1) {
                    when (item.title) {
                        R.string.share_this_app -> {
                            shareUseCaseProvider.shareSheasyApk()
                            return true
                        }
                        R.string.menu_help -> {
                            val browserIntent =
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(getString(R.string.help_sheasy_wiki))
                                )
                            startActivity(browserIntent)
                        }

                    }
                } else {
                    mainActivityDrawer.closeDrawer()

                    navController.navigate(item.navId)
                }

            }
        }

        return true
    }

    /******************************************  Class methods  */


    fun handleIntent(intent: Intent) {
        intent.let {
            val action = it.action
            val type = it.type


            val clipData = it.clipData
            if (clipData != null) {


                for (i in 0..clipData.itemCount) {
                    val item = clipData.getItemAt(i)
                    val uri = item.uri

                    val filePath = FilesFragmentDirections.actionFilesFragmentSelf(uri.toString())
                    navController.navigate(R.id.filesFragment, filePath.arguments)
                    break
                }
            }


        }
    }


    private fun setupNavigation() {
        navController = findNavController(R.id.mainNavigationFragment)
        setupActionBarWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            supportActionBar?.setDisplayShowHomeEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)

        }
    }


}