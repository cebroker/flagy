package com.evercheck.flagly.testsample

import com.evercheck.flagly.featureflag.FeatureFlag

object FeatureFlagOne : FeatureFlag{
    override val name: String
        get() = "one"
}

object FeatureFlagTwo : FeatureFlag{
    override val name: String
        get() = "two"
}


object FeatureFlagThree : FeatureFlag{
    override val name: String
        get() = "three"
}

object FeatureFlagFour : FeatureFlag{
    override val name: String
        get() = "four"
}