package io.pylyp.core.navigation

public sealed interface AppFeature {
    public data object Coffee : AppFeature
    public data object Cover : AppFeature
    public data object Weather : AppFeature
    public data object SkyTrack : AppFeature
}

