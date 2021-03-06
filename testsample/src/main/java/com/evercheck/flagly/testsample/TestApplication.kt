package com.evercheck.flagly.testsample

import android.app.Application
import com.evercheck.flagly.developeroptions.FeatureHandleResourceProvider
import com.evercheck.flagly.data.DynamicFeatureFlagHandler
import com.evercheck.flagly.domain.model.FeatureFlag
import com.evercheck.flagly.data.FeatureFlagHandler
import com.evercheck.flagly.data.FeatureFlagProvider

class TestApplication : Application(), FeatureHandleResourceProvider {

    private val featureFlagHandler = object : FeatureFlagHandler, DynamicFeatureFlagHandler {
        private val featureflagMap = HashMap<String, Boolean>()

        override fun setValue(featureFlag: FeatureFlag, value: Boolean) {
            featureflagMap[featureFlag.name] = value
        }

        override fun isFeatureEnabled(featureFlag: FeatureFlag): Boolean {
            return featureflagMap[featureFlag.name] ?: false
        }

        override fun isValueOverriden(featureFlag: FeatureFlag): Boolean =
            featureflagMap.containsKey(featureFlag.name)

        override fun removeOverridenValue(featureFlag: FeatureFlag) {
            featureflagMap.remove(featureFlag.name)
        }
    }

    override fun getFeatureFlagProvider(): FeatureFlagProvider = object : FeatureFlagProvider {

        override fun provideAppSupportedFeatureflags(): Collection<FeatureFlag> {
            return setOf(
                FeatureFlagOne,
                FeatureFlagTwo,
                FeatureFlagThree,
                FeatureFlagFour,
                FeatureFlagFive
            )
        }
    }

    override fun getRemoteFeatureFlagHandler(): FeatureFlagHandler = object : FeatureFlagHandler {
        override fun isFeatureEnabled(featureFlag: FeatureFlag): Boolean {
            return featureFlag is FeatureFlagOne ||  featureFlag is FeatureFlagTwo
        }
    }

    override fun getLocalFeatureflagHandler(): DynamicFeatureFlagHandler = featureFlagHandler
}
