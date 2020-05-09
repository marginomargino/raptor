package io.fluidsonic.raptor


@RaptorDsl
interface RaptorComponent {

	@RaptorDsl
	val extensions: RaptorComponentExtensionSet


	companion object


	@RaptorDsl
	abstract class Base<out Self : Typed<Self>> : Typed<Self> {

		@RaptorDsl
		final override val extensions: RaptorComponentExtensionSet = DefaultRaptorComponentExtensionSet()


		@RaptorDsl
		@Suppress("UNCHECKED_CAST")
		final override fun configure(action: Self.() -> Unit) {
			(this as Self).action()
		}
	}


	interface Typed<out Self : Typed<Self>> : RaptorComponent, RaptorComponentSet<Self>
}
