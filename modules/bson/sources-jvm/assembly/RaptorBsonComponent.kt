@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package io.fluidsonic.raptor.bson

import io.fluidsonic.raptor.*
import io.fluidsonic.raptor.bson.RaptorBsonDefinition.*
import java.util.*
import kotlin.internal.*
import org.bson.codecs.*
import org.bson.codecs.configuration.*


private val bsonPropertyKey = RaptorPropertyKey<RaptorBson>("bson")


public class RaptorBsonComponent internal constructor() : RaptorComponent.Base<RaptorBsonComponent>(RaptorBsonPlugin) {

	private val definitionsByPriority: MutableMap<Priority, MutableList<RaptorBsonDefinition>> = EnumMap(Priority::class.java)
	private var includesDefaultDefinitions = false


	@RaptorDsl
	public fun codecs(vararg codecs: Codec<*>, priority: Priority = Priority.normal) {
		codecs(codecs.asIterable(), priority = priority)
	}


	@RaptorDsl
	public fun codecs(codecs: Iterable<Codec<*>>, priority: Priority = Priority.normal) {
		definitions(codecs.map(RaptorBsonDefinition::of), priority = priority)
	}


	@RaptorDsl
	public inline fun <reified Value : Any> definition(
		priority: Priority = Priority.normal,
		noinline configure: RaptorBsonDefinitionBuilder<@NoInfer Value>.() -> Unit,
	) {
		definitions(raptor.bson.definition<Value>(configure), priority = priority)
	}


	@RaptorDsl
	public fun definitions(vararg definitions: RaptorBsonDefinition, priority: Priority = Priority.normal) {
		definitions(definitions.asIterable(), priority = priority)
	}


	@RaptorDsl
	public fun definitions(definitions: Iterable<RaptorBsonDefinition>, priority: Priority = Priority.normal) {
		definitionsByPriority.getOrPut(priority, ::mutableListOf).addAll(definitions)
	}


	@RaptorDsl
	public fun includeDefaultDefinitions() {
		if (includesDefaultDefinitions)
			return

		includesDefaultDefinitions = true
	}


	@RaptorDsl
	public fun providers(vararg providers: CodecProvider, priority: Priority = Priority.normal) {
		providers(providers.asIterable(), priority = priority)
	}


	@RaptorDsl
	public fun providers(providers: Iterable<CodecProvider>, priority: Priority = Priority.normal) {
		definitions(providers.map(RaptorBsonDefinition::of), priority = priority)
	}


	override fun RaptorComponentConfigurationEndScope<RaptorBsonComponent>.onConfigurationEnded() {
		val definitions = listOfNotNull(
			definitionsByPriority[Priority.high],
			definitionsByPriority[Priority.normal],
			RaptorBsonDefinition.raptorDefaults.takeIf { includesDefaultDefinitions },
			definitionsByPriority[Priority.low],
			// These codecs must come last to allow all other codecs to override default behavior.
			// Also, MongoDB would freak out with StackOverflowError if their own codecs don't come before these!
			RaptorBsonDefinition.bsonDefaults.takeIf { includesDefaultDefinitions },
		).flatten()

		propertyRegistry.register(bsonPropertyKey, DefaultRaptorBson(context = lazyContext, definitions = definitions))
	}
}


@RaptorDsl
public fun RaptorAssemblyQuery<RaptorBsonComponent>.codecs(
	vararg codecs: Codec<*>,
	priority: Priority = Priority.normal,
) {
	codecs(codecs.asIterable(), priority = priority)
}


@RaptorDsl
public fun RaptorAssemblyQuery<RaptorBsonComponent>.codecs(
	codecs: Iterable<Codec<*>>,
	priority: Priority = Priority.normal,
) {
	definitions(codecs.map(RaptorBsonDefinition::of), priority = priority)
}


@RaptorDsl
public inline fun <reified Value : Any> RaptorAssemblyQuery<RaptorBsonComponent>.definition(
	@BuilderInference noinline configure: RaptorBsonDefinitionBuilder<Value>.() -> Unit,
) {
	definitions(raptor.bson.definition<Value>(configure))
}


@RaptorDsl
public fun RaptorAssemblyQuery<RaptorBsonComponent>.definitions(
	vararg definitions: RaptorBsonDefinition,
	priority: Priority = Priority.normal,
) {
	definitions(definitions.asIterable(), priority = priority)
}


@RaptorDsl
public fun RaptorAssemblyQuery<RaptorBsonComponent>.definitions(
	definitions: Iterable<RaptorBsonDefinition>,
	priority: Priority = Priority.normal,
) {
	this {
		definitions(definitions, priority = priority)
	}
}


@RaptorDsl
public fun RaptorAssemblyQuery<RaptorBsonComponent>.includeDefaultDefinitions() {
	this {
		includeDefaultDefinitions()
	}
}


@RaptorDsl
public fun RaptorAssemblyQuery<RaptorBsonComponent>.providers(
	vararg providers: CodecProvider,
	priority: Priority = Priority.normal,
) {
	providers(providers.asIterable(), priority = priority)
}


@RaptorDsl
public fun RaptorAssemblyQuery<RaptorBsonComponent>.providers(
	providers: Iterable<CodecProvider>,
	priority: Priority = Priority.normal,
) {
	definitions(providers.map(RaptorBsonDefinition::of), priority = priority)
}


public val RaptorContext.bson: RaptorBson
	get() = properties[bsonPropertyKey] ?: throw RaptorPluginNotInstalledException(RaptorBsonPlugin)
