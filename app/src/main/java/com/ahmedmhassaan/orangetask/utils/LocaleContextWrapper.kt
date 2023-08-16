package com.ahmedmhassaan.orangetask.utils

import android.annotation.TargetApi
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.os.Build
import java.util.Locale


class LocaleContextWrapper(base: Context) : ContextWrapper(base) {
    companion object {
        fun wrap(context: Context, language: String): LocaleContextWrapper {
            val config: Configuration = context.resources.configuration

            val systemLocal: Locale = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                getSystemLocale(config)
            } else {
                getSystemLocaleLegacy(config)
            }

            if (systemLocal.language != language) {
                val locale = if (language.isEmpty()) Locale.getDefault() else Locale(language)
                Locale.setDefault(locale)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setSystemLocale(config, locale);
                } else {
                    setSystemLocaleLegacy(config, locale);
                }

            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                context = context.createConfigurationContext(config);

                val createdContext = context.createConfigurationContext(config);
                return LocaleContextWrapper(createdContext)
            } else {
                context.resources.updateConfiguration(config, context.resources.displayMetrics);
                return LocaleContextWrapper(context)
            }


        }

        @SuppressWarnings("deprecation")
        private fun getSystemLocaleLegacy(config: Configuration): Locale {
            return config.locale
        }

        @TargetApi(Build.VERSION_CODES.N)
        fun getSystemLocale(config: Configuration): Locale {
            return config.locales[0]
        }

        @Suppress("deprecation")
        fun setSystemLocaleLegacy(config: Configuration, locale: Locale) {
            config.locale = locale
        }

        @TargetApi(Build.VERSION_CODES.N)
        fun setSystemLocale(config: Configuration, locale: Locale) {
            config.setLocale(locale)
        }

    }
}