package io.fluidsonic.raptor


interface RaptorComponentRegistry {

	fun <Component : RaptorComponent> configure(key: RaptorComponentKey<out Component>): RaptorComponentSet<Component>
	fun isEmpty(): Boolean
	fun <Component : RaptorComponent> oneOrNull(key: RaptorComponentKey<out Component>): Component?
	fun <Component : RaptorComponent> many(key: RaptorComponentKey<out Component>): List<Component>
	fun <Component : RaptorComponent> register(key: RaptorComponentKey<in Component>, component: Component)
	override fun toString(): String


	companion object {

		fun default(): RaptorComponentRegistry =
			DefaultRaptorComponentRegistry()
	}


	object ChildRegistryComponentExtensionKey : RaptorComponentExtensionKey<RaptorComponentRegistry> {

		override fun toString() = "child registry"
	}
}


// https://kotlinlang.slack.com/archives/C0B9K7EP2/p1588740913072200
fun <Component : RaptorComponent> RaptorComponentRegistry.configure(
	key: RaptorComponentKey<Component>,
	action: Component.() -> Unit
) {
	configure(key = key).configure(action)
}


fun <Component : RaptorComponent> RaptorComponentRegistry.one(key: RaptorComponentKey<Component>): Component =
	oneOrNull(key) ?: error("Expected a component to be registered for key '$key'.")


inline fun <Component : RaptorComponent> RaptorComponentRegistry.oneOrRegister(
	key: RaptorComponentKey<Component>,
	create: () -> Component
): Component =
	oneOrNull(key) ?: create().also { register(key, it) }


@RaptorDsl
val RaptorComponentContainer.childComponentRegistry
	get() = extensions[RaptorComponentRegistry.ChildRegistryComponentExtensionKey]
		?: error("Cannot access the child component registry of a component that hasn't been registered yet.")
