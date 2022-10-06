package io.fluidsonic.raptor

import io.fluidsonic.raptor.di.*
import io.fluidsonic.raptor.ktor.*


public object RaptorEntitiesPlugin : RaptorPlugin {

	override fun RaptorPluginCompletionScope.complete() {
		require(RaptorKtorPlugin)

		val resolverTypes = componentRegistry.oneOrNull(RaptorEntitiesComponent.key)?.resolverTypes.orEmpty()

		// FIXME
		configure(RaptorDIPlugin) {
			di.provide<RaptorEntityResolver<RaptorEntity, RaptorEntityId>> {
				RaptorAnyEntityResolver(context = get(), resolverTypes = resolverTypes)
			}
		}

		// FIXME
		//			graphs.definitions(
		//				RaptorEntity.graphDefinition(),
		////				RaptorEntityId.graphDefinition()
		//			)
		// }
	}


	override fun RaptorPluginInstallationScope.install() {
		require(RaptorDIPlugin)
	}
}


@RaptorDsl
public val RaptorAssemblyScope.entities: RaptorEntitiesComponent
	get() = componentRegistry.oneOrRegister(RaptorEntitiesComponent.key, ::RaptorEntitiesComponent)
