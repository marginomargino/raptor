package io.fluidsonic.raptor.domain.mongo

import io.fluidsonic.raptor.*
import io.fluidsonic.raptor.bson.*
import io.fluidsonic.raptor.cqrs.*


internal object RaptorAggregateEventIdBson : RaptorPlugin {

	override fun RaptorPluginInstallationScope.install() {
		bson.definition {
			decode {
				RaptorEventId(reader.objectId().toString())
			}

			encode { value ->
				writer.value(ObjectIdOrNull(value.toString()) ?: error("Invalid aggregate event id: $value"))
			}
		}
	}
}
