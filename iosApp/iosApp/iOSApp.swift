import SwiftUI
import ComposeApp

@main
struct iOSApp: App {

    init() {
        KoinHelperKt.bootstrapPlatformKoin()
        IosWeatherNotifications.shared.onAppLaunch()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
