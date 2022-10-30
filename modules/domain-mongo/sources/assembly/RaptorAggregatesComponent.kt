package io.fluidsonic.raptor.domain.mongo

import io.fluidsonic.mongo.*
import io.fluidsonic.raptor.*
import io.fluidsonic.raptor.domain.*
import kotlinx.datetime.*


@RaptorDsl
public fun RaptorAssemblyQuery<RaptorAggregatesComponent>.mongoEventFactory(clock: Clock) {
	each {
		eventFactory(RaptorMongoAggregateEventFactory(clock = clock))
	}
}


@RaptorDsl
public fun RaptorAssemblyQuery<RaptorAggregatesComponent>.mongoStore(client: MongoClient, databaseName: String, collectionName: String) {
	each {
		store(RaptorAggregateStore.mongo(client = client, databaseName = databaseName, collectionName = collectionName))
	}
}
