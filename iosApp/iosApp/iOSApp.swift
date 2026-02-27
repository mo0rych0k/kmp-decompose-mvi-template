import SwiftUI

@main
struct iOSApp: App {
    init() {
        KoinHelperKt.iOsInitKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}