package com.evercheck.flagly.developeroptions

import com.evercheck.flagly.developeroptions.adapter.FeatureFlagValueChangedListener

interface FeatureFlagActivityContract {

    interface View {

        fun setup()

        fun showReatureFlagValues(featureFlagValues: List<FeatureFlagValue>)
    }

    interface Presenter : FeatureFlagValueChangedListener {

        var view: View?

        fun bind(view: View)

        fun onViewReady()

        fun unBind()
    }
}
