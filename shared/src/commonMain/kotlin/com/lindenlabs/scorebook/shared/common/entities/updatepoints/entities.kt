<<<<<<< HEAD:shared/src/commonMain/kotlin/com/lindenlabs/scorebook/shared/common/viewmodels/updatepoints/entities.kt
package com.lindenlabs.scorebook.shared.common.viewmodels.updatepoints

import com.lindenlabs.scorebook.shared.common.viewmodels.BaseInteraction
=======
package com.lindenlabs.scorebook.shared.common.entities.updatepoints

>>>>>>> Use passed in constructor values for viewmodel:shared/src/commonMain/kotlin/com/lindenlabs/scorebook/shared/common/entities/updatepoints/entities.kt
import com.lindenlabs.scorebook.shared.common.raw.Game
import com.lindenlabs.scorebook.shared.common.raw.Player

sealed class UpdatePointsInteraction {
    data class ScoreIncreaseBy(val point: String) : UpdatePointsInteraction()
    data class ScoreLoweredBy(val point: String) : UpdatePointsInteraction()
}

sealed class UpdatePointsViewState {
    object Loading : UpdatePointsViewState()
    data class ScreenOpened(val game: Game, val player: Player) : UpdatePointsViewState()
}

sealed class UpdatePointsViewEvent {
    object Loading : UpdatePointsViewEvent()

    data class AlertNoTextEntered(val errorText: String = "Enter a score") : UpdatePointsViewEvent()

    data class ScoreUpdated(val player: Player, val game: Game) : UpdatePointsViewEvent()
}