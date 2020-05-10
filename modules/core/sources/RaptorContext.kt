package io.fluidsonic.raptor


interface RaptorContext {

	val parent: RaptorContext?
	val properties: RaptorPropertySet

	fun asScope(): RaptorScope
	override fun toString(): String


	companion object
}


val RaptorContext.root: RaptorContext
	get() = parent?.root ?: this
