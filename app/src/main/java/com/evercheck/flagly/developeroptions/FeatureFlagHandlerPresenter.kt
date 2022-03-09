package com.evercheck.flagly.developeroptions

import kotlinx.coroutines.*
import com.evercheck.flagly.featureflag.DynamicFeatureFlagHandler
import com.evercheck.flagly.featureflag.FeatureFlag
import com.evercheck.flagly.featureflag.FeatureFlagHandler
import com.evercheck.flagly.featureflag.FeatureFlagProvider
import com.evercheck.flagly.utils.CoroutineContextProvider
import java.util.Locale
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class FeatureFlagHandlerPresenter @Inject constructor(
    private val featureFlagProvider: FeatureFlagProvider,
    private val remoteFeatureFlagHandler: FeatureFlagHandler,
    private val localFeatureflagHandler: DynamicFeatureFlagHandler,
    private val coroutineContextProvider: CoroutineContextProvider
) : FeatureFlagActivityContract.Presenter, CoroutineScope {

    private val job: Job = SupervisorJob()
    private var search: String = ""

    override var view: FeatureFlagActivityContract.View? = null

    override val coroutineContext: CoroutineContext
        get() = coroutineContextProvider.mainDispatcher + job

    override fun bind(view: FeatureFlagActivityContract.View) {
        this.view = view
    }

    override fun onViewReady() {
        view?.setup()
    }

    override fun onLoadFeatureFlagValues(search: String) {
        this.search = search
        this.setupFeatureFlagValues()
    }

    private fun setupFeatureFlagValues() {
        launch {
            val values =
                withContext(coroutineContextProvider.backgroundDispatcher) {
                    featureFlagProvider.provideAppSupportedFeatureflags()
                        .filter {
                            search.isEmpty() ||
                                    it.name.toLowerCase(Locale.ROOT).contains(search)
                        }
                        .map { featureFlag ->
                            getFeatureFlagValue(featureFlag)
                        }
                }

            view?.showReatureFlagValues(values)
        }
    }

    override fun unBind() {
        view = null
    }

    override fun onFeatureFlagValueChanged(featureFlag: FeatureFlag, value: Boolean) {
        localFeatureflagHandler.setValue(featureFlag, value)
    }

    override fun onOverrideValueChange(
        featureFlag: FeatureFlag,
        override: Boolean,
        remoteValue: Boolean
    ) {
        if (override) {
            localFeatureflagHandler.setValue(featureFlag, remoteValue)
        } else {
            localFeatureflagHandler.removeOverridenValue(featureFlag)
        }
        setupFeatureFlagValues()
    }

    private fun getFeatureFlagValue(featureFlag: FeatureFlag): FeatureFlagValue {
        val isOverride = localFeatureflagHandler.isValueOverriden(featureFlag)
        return FeatureFlagValue(
            featureFlag,
            isOverride,
            getLocalValue(featureFlag, getLocalValue(featureFlag, isOverride)),
            remoteFeatureFlagHandler.isFeatureEnabled(featureFlag)
        )
    }

    private fun getLocalValue(featureFlag: FeatureFlag, isOverride: Boolean): Boolean =
        if (isOverride) {
            localFeatureflagHandler.isFeatureEnabled(featureFlag)
        } else {
            false
        }

}