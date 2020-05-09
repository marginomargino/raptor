package io.fluidsonic.raptor


internal class DefaultRaptorCoreComponent : RaptorComponent.Base<RaptorCoreComponent>(),
	RaptorCoreComponent,
	RaptorFeatureFinalizationScope,
	RaptorFeatureInstallationScope {

	private val featureKeys: MutableSet<FeatureKey<*, *>> = mutableSetOf()

	override val componentRegistry = DefaultRaptorComponentRegistry()
	override val propertyRegistry = DefaultRaptorPropertyRegistry()


	init {
		componentRegistry.register(Key, component = this)
	}


	internal fun finalize(): Raptor {
		componentRegistry.finalize()

		for (key in featureKeys)
			componentRegistry.one(key).runFinalization(scope = this)

		return DefaultRaptor(properties = propertyRegistry.toPropertySet())
	}


	@Suppress("UNCHECKED_CAST")
	override fun <Feature : RaptorConfigurableFeature<RootComponent>, RootComponent : RaptorComponent> install(
		feature: Feature,
		configure: RootComponent.() -> Unit
	) {
		val key = FeatureKey(feature)

		featureKeys.add(key)

		val component = componentRegistry.oneOrNull(key) ?: run {
			DefaultRaptorFeatureInstallationComponent(feature, componentRegistry).also { component ->
				componentRegistry.register(key, component)

				component.runInstallation(scope = this)
			}
		}

		component.runConfiguration(configure)
	}


	override fun toString() =
		"default core"


	private data class FeatureKey<Feature : RaptorConfigurableFeature<RootComponent>, RootComponent : RaptorComponent>(
		val feature: Feature
	) : RaptorComponentKey<DefaultRaptorFeatureInstallationComponent<Feature, RootComponent>> {

		override fun toString() =
			feature.toString()
	}


	private object Key : RaptorComponentKey<DefaultRaptorCoreComponent> {

		override fun toString() = "core"
	}
}
