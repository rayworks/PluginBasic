package com.rayworks.hostdroid

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager.GET_RESOLVED_FILTER
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.rayworks.commlib.Comm
import dalvik.system.DexClassLoader
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()

            callPlugin()
        }
    }

    private fun callPlugin() {
        var intent = Intent("com.rayworks.plugindroid")
        val plugins: List<ResolveInfo> = packageManager.queryIntentActivities(intent, GET_RESOLVED_FILTER)


        var rinfo = plugins.get(0)
        var activityInfo: ActivityInfo = rinfo.activityInfo

        var pkgName = activityInfo.packageName
        var dexPath = activityInfo.applicationInfo.sourceDir

        var dexOutputDir = applicationInfo.dataDir
        var libPath = activityInfo.applicationInfo.nativeLibraryDir

        var classLoader = DexClassLoader(dexPath, dexOutputDir, libPath, javaClass.classLoader)

        // retrieve the plugin version
        val resources = packageManager.getResourcesForApplication(pkgName)
        val id = resources.getIdentifier("plugin_version", "string", pkgName)
        val version: String = resources.getString(id)

        try {
            val loadClass = classLoader.loadClass(pkgName + ".PluginClass");
            var comm: Comm = loadClass.newInstance() as Comm
            val i = 12
            val j = 34
            val result = comm.add(i, j)

            println(">>> result " + result)
            Toast.makeText(this, "Result is " + result, Toast.LENGTH_SHORT).show()

            text_content.text = String.format(Locale.ENGLISH, ">>> Operation result for %d + %d is " +
                    "%d, powered by Plugin with version %s", i, j, result, version)

        } catch (e: Exception) {
            e.printStackTrace()

            Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
