package com.artem_obrazumov.mycity.data.api

import com.artem_obrazumov.mycity.ui.instructions.models.InstructionSlide
import com.artem_obrazumov.mycity.ui.instructions.models.InstructionsScript

class Defaults {
    companion object {
        val defaultInstruction = InstructionsScript(
            slides = arrayListOf(
                InstructionSlide(
                    "1",
                    "https://sun1-90.userapi.com/s/v1/ig2/-x2FE2wGEl7q1DLL56_U0iQy5zxX8YLqIBmMsGfohACS3wpofDq_pHkDuPTffN0mx2szpgqWKbPBjXqMTtbKjWOh.jpg?size=50x0&quality=96&crop=0,0,992,992&ava=1",
                    "Убивать убивать убивать убивать убивать убивать убивать убивать убивать убивать убивать убивать убивать убивать"
                ),
                InstructionSlide(
                    "2",
                    "https://sun1-90.userapi.com/s/v1/ig2/-x2FE2wGEl7q1DLL56_U0iQy5zxX8YLqIBmMsGfohACS3wpofDq_pHkDuPTffN0mx2szpgqWKbPBjXqMTtbKjWOh.jpg?size=50x0&quality=96&crop=0,0,992,992&ava=1",
                    "хаха шлепа)"
                ),
                InstructionSlide(
                    "3",
                    "https://sun1-90.userapi.com/s/v1/ig2/-x2FE2wGEl7q1DLL56_U0iQy5zxX8YLqIBmMsGfohACS3wpofDq_pHkDuPTffN0mx2szpgqWKbPBjXqMTtbKjWOh.jpg?size=50x0&quality=96&crop=0,0,992,992&ava=1",
                    "3"
                ),
            )
        )
    }
}