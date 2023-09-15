package com.chrxw.purenga

import android.app.AlertDialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.Keep
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat


/**
 * 主界面
 */
class MainActivity : AppCompatActivity() {

    companion object {

        /**
         * 检测模块启用状态
         */
        @JvmStatic
        @Keep
        fun isModuleActive(): Boolean {
            return false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        supportFragmentManager.beginTransaction().replace(R.id.settings, SettingsFragment()).commit()
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        private var runningStatusPref: Preference? = null

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.pref_settings, rootKey)
            findPreference<Preference>("version")?.summary = BuildConfig.VERSION_NAME
            runningStatusPref = findPreference("running_status")
        }

        override fun onPreferenceTreeClick(preference: Preference): Boolean {
            val prefKey = preference.key

            when (prefKey) {
                "hide_icon" -> {
                    val ctx = context
                    if (ctx != null) {
                        hideAppIcon(ctx)
                    }
                    return true
                }
                "plugin_setting" -> {
                    AlertDialog.Builder(activity).run {
                        setView(R.layout.plugin_setting)
                        setNegativeButton("关闭", null)
                        show()
                    }
                    return true
                }
                "open_nga" -> {
                    try {
                        startActivity(
                            Intent(Intent.ACTION_MAIN).setComponent(
                                ComponentName(
                                    Constant.NGA_PACKAGE_NAME,
                                    "com.donews.nga.activitys.MainActivity"
                                )
                            )//.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
                        )
                    } catch (e: Throwable) {

                        toast("打开 NGA 失败")
                    }
                    return true
                }
                else -> {
                    val intent = when (prefKey) {
                        "version" -> {
                            Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.github_url)))
                        }

                        "author" -> {
                            Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.author_url)))
                        }

                        "donate" -> {
                            Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.donate_url)))
                        }

                        else -> null
                    }

                    if (intent != null) {
                        startActivity(intent)
                    }

                    return true
                }
            }

        }

        override fun onResume() {
            super.onResume()
            runningStatusPref?.setSummary(if (isModuleActive()) R.string.module_enabled else R.string.module_disabled)
        }

        private fun hideAppIcon(context: Context) {
            val componentName = ComponentName(context, "com.chrxw.purenga.MainActivity")
            val packageManager = context.packageManager
            val state = packageManager.getComponentEnabledSetting(componentName)
            if (state != PackageManager.COMPONENT_ENABLED_STATE_DISABLED) {
                packageManager.setComponentEnabledSetting(
                    componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP
                )
            }
        }

        private fun toast(text: String, duration: Int = Toast.LENGTH_LONG) {
            val t = Toast.makeText(this.context, text, duration)
            t.show()
        }
    }
}