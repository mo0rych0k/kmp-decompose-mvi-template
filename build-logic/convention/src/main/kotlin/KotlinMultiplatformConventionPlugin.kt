import io.pylyp.build.logic.Constants
import io.pylyp.build.logic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

@Suppress("unused")
class KotlinMultiplatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {

        with(pluginManager) {
            apply(libs.findPlugin("kotlinMultiplatform").get().get().pluginId)
            apply(libs.findPlugin("kotlinMultiplatformLibrary").get().get().pluginId)
        }

        extensions.configure<KotlinMultiplatformExtension> {
            explicitApi = ExplicitApiMode.Strict

            iosArm64()
            iosSimulatorArm64()

            targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
                binaries.all {
                    // Each -linker-option forwards exactly one flag to the Apple linker; version must be a separate -linker-option.
                    freeCompilerArgs += listOf(
                        "-linker-option",
                        "-ios_version_min",
                        "-linker-option",
                        Constants.IOS_MIN_VERSION,
                    )
                }
            }

            jvm()

            compilerOptions {
                freeCompilerArgs.add("-Xexpect-actual-classes")
            }

            sourceSets.all {
                languageSettings {
                    optIn("kotlin.ExperimentalStdlibApi")
                    optIn("kotlin.time.ExperimentalTime")
                    optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
                }
            }
        }
    }
}
