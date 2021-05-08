<<<<<<< HEAD:shared/src/commonMain/kotlin/com/lindenlabs/scorebook/shared/common/viewmodels/victory/entities.kt
package com.lindenlabs.scorebook.shared.common.viewmodels.victory

import com.lindenlabs.scorebook.shared.common.viewmodels.BaseInteraction
=======
package com.lindenlabs.scorebook.shared.common.entities.victory
>>>>>>> Use passed in constructor values for viewmodel:shared/src/commonMain/kotlin/com/lindenlabs/scorebook/shared/common/entities/victory/entities.kt

data class VictoryState(val victoryText: String)

sealed class VictoryInteraction {
    object GoHome : VictoryInteraction()
}

sealed class VictoryViewEvent {
    object None : VictoryViewEvent()
    object GoHome : VictoryViewEvent()
}