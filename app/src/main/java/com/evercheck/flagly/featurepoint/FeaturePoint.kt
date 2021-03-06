package com.evercheck.flagly.featurepoint

interface FeaturePoint<out Feature,in Params> {

    val factories: Collection<FeatureFactory<Feature, Params>>

    fun createFeatures(params: Params): List<Feature>
}