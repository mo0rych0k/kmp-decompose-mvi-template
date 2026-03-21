import SwiftUI
import ComposeApp

@main
struct iOSApp: App {

    init() {
        KoinHelperKt.bootstrapPlatformKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
