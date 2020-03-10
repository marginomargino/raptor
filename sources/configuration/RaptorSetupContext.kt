package io.fluidsonic.raptor

import kotlin.reflect.*


// FIXME
class RaptorSetupContext internal constructor() {

	fun <Setup : Any> collection(setupClass: KClass<Setup>): RaptorSetupComponentCollection<Setup> {
		TODO()
	}


	fun <Setup : Any> configure(setupClass: KClass<Setup>, config: Setup.() -> Unit) {
		TODO()
	}


	fun <Setup : Any> register(setup: Setup, setupClass: KClass<in Setup>, vararg tags: Any = emptyArray(), config: Setup.() -> Unit = {}) {
		TODO()
	}
}


inline fun <reified Setup : Any> RaptorSetupContext.collection() =
	collection(setupClass = Setup::class)


inline fun <reified Setup : Any> RaptorSetupContext.configure(noinline config: Setup.() -> Unit) =
	configure(setupClass = Setup::class, config = config)


inline fun <reified Setup : Any> RaptorSetupContext.register(
	setup: Setup,
	vararg tags: Any = emptyArray(),
	noinline config: Setup.() -> Unit = {}
) =
	register(
		setup = setup,
		setupClass = Setup::class,
		tags = *tags,
		config = config
	)
