import SwiftUI

@main
struct iOSApp: App {
    init() {
        KoinHelperKt.initKoinInPlatform()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}