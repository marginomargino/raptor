import io.fluidsonic.gradle.*

plugins {
	id("io.fluidsonic.gradle") version "1.1.26"
}

fluidLibrary(name = "raptor", version = "0.12.2", prefixName = false) {
	allModules {
		language {
			withExperimentalApi("io.fluidsonic.raptor.RaptorInternalApi")
		}
	}
}
