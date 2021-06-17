package io.fluidsonic.raptor


@JvmInline
public value class AccessToken(public val value: String) {

	override fun toString(): String =
		"••••••"


	public companion object {

		public fun graphDefinition(): RaptorGraphDefinition = graphScalarDefinition {
			parseString(::AccessToken)
			serialize(AccessToken::value)
		}
	}
}
