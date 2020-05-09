package tests

import io.fluidsonic.raptor.*
import kotlin.test.*


class DebuggingTests {

	@Test
	fun testEmptyRaptorToString() {
		val raptor = raptor {}

		assertEquals(
			expected = "[raptor] -> (empty)",
			actual = raptor.toString()
		)
	}


	@Test
	fun testEmptyRegistriesToString() {
		assertEquals(
			expected = "[component registry] -> (empty)",
			actual = DefaultRaptorComponentRegistry()
				.also { it.configure(DummyComponent.Key) }
				.toString()
		)

		assertEquals(
			expected = "[property registry] -> (empty)",
			actual = DefaultRaptorPropertyRegistry().toString()
		)
	}


	@Test
	fun testRegistriesToString() {
		raptor {
			install(TextCollectionFeature)
			textCollection {
				append("foo")
			}

			install(CounterFeature) {
				increment()

				extensions[AnyRaptorComponentExtensionKey] = "counter extension"
			}

			install(NodeFeature) {
				node("a") {
					node("a1")
					node("a2")

					extensions[AnyRaptorComponentExtensionKey] = "node extension"
				}
				node("b")
				node("c")
			}

			install(object : RaptorFeature {

				override fun RaptorFeatureFinalizationScope.finalize() {
					assertEquals(
						expected = """
							[component registry] ->
								[core] -> default core
								[counter] ->
									counter (1) -> 
										[any] -> counter extension
								[counter feature] -> default feature component
								[debugging tests feature] -> default feature component
								[debugging tests feature root] -> default feature root component
								[dummy] ->
									dummy (z)
									dummy (1) -> 
										[any] -> dummy extension
									dummy (a)
								[node] ->
									node (root) -> 
										[component registry] ->
											[node] ->
												node (a) -> 
													[any] -> node extension
													[component registry] ->
														[node] ->
															node (a1)
															node (a2)
												node (b)
												node (c)
								[node feature] -> default feature component
								[text collection] -> text collection (foo)
								[text collection feature] -> default feature component
								[text collection feature root] -> default feature root component
						""".trimIndent(),
						actual = componentRegistry.toString()
					)

					assertEquals(
						expected = """
							[property registry] ->
								[count] -> 1
								[root node] ->
									node(root) ->
										node(a) ->
											node(a1)
											node(a2)
										node(b)
										node(c)
								[text] -> foo
						""".trimIndent(),
						actual = propertyRegistry.toString()
					)
				}


				override fun RaptorFeatureInstallationScope.install() {
					componentRegistry.register(DummyComponent.Key, DummyComponent("z"))
					componentRegistry.register(DummyComponent.Key, DummyComponent("1").apply {
						extensions[AnyRaptorComponentExtensionKey] = "dummy extension"
					})
					componentRegistry.register(DummyComponent.Key, DummyComponent("a"))
				}


				override fun toString() = "debugging tests feature"
			})
		}
	}


	@Test
	fun testRaptorToString() {
		val raptor = raptor {
			install(TextCollectionFeature)
			textCollection {
				append("foo")
			}

			install(CounterFeature) {
				increment()
			}
		}

		assertEquals(
			expected = """
				[raptor] ->
					[property set] ->
						[count] -> 1
						[text] -> foo
			""".trimIndent(),
			actual = raptor.toString()
		)
	}
}
