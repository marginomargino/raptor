package io.fluidsonic.raptor.domain.mongo

import io.fluidsonic.raptor.*
import io.fluidsonic.raptor.bson.*
import io.fluidsonic.raptor.cqrs.*
import io.fluidsonic.raptor.mongo.*


public object RaptorDomainMongoPlugin : RaptorPlugin {

	override fun RaptorPluginCompletionScope.complete() {
		configure(RaptorBsonPlugin) {
			require(RaptorDomainPlugin) { domain ->
				bson.definitions(
					RaptorAggregateEventBson.bson(definitions = domain.aggregates.definitions),
					RaptorAggregateEventBson.idBson(),
				)
			}
		}
	}


	override fun RaptorPluginInstallationScope.install() {
		require(RaptorDomainPlugin)
		require(RaptorMongoPlugin)
	}


	override fun toString(): String = "domain-mongo"
}