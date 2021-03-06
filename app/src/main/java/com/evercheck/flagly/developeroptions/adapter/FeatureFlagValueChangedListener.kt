package com.evercheck.flagly.developeroptions.adapter

import com.evercheck.flagly.domain.model.FeatureFlag


interface FeatureFlagValueChangedListener {

    fun onFeatureFlagValueChanged(featureFlag: FeatureFlag, value: Boolean)

    fun onOverrideValueChange(featureFlag: FeatureFlag, override: Boolean, remoteValue: Boolean)
}
