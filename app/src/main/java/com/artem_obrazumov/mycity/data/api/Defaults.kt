package com.artem_obrazumov.mycity.data.api

import com.artem_obrazumov.mycity.ui.instructions.models.InstructionSlide
import com.artem_obrazumov.mycity.ui.instructions.models.InstructionsScript

class Defaults {
    companion object {
        val defaultInstruction = InstructionsScript(
            slides = arrayListOf(
                InstructionSlide(
                    "Изучайте новые места и новых людей!",
                    "https://firebasestorage.googleapis.com/v0/b/mycity-f9e4c.appspot.com/o/kisspng-city-euclidean-vector-green-clip-art-promotional-material-green-city-background-5a81a467793700.6695648015184456714965.png?alt=media&token=de18a482-3ecb-4f60-bc7b-84be4dba8820",
                    "На главной странице вы увидите интересные места и популярных людей своего города."
                ),
                InstructionSlide(
                    "Создайте свой аккаунт!",
                    "https://cdn.icon-icons.com/icons2/933/PNG/512/user-account-box_icon-icons.com_72491.png",
                    "Нажав на иконку профиля, вы сможете создать свой аккаунт и начать писать отзывы!"
                ),
                InstructionSlide(
                    "Добавляйте места в избранное!",
                    "https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/f/0493b5fb-a59a-42d0-8e72-de4ac41277e0/d8vbil5-3ea91196-b93a-4607-8650-ad8b8ce80c57.png?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1cm46YXBwOjdlMGQxODg5ODIyNjQzNzNhNWYwZDQxNWVhMGQyNmUwIiwiaXNzIjoidXJuOmFwcDo3ZTBkMTg4OTgyMjY0MzczYTVmMGQ0MTVlYTBkMjZlMCIsIm9iaiI6W1t7InBhdGgiOiJcL2ZcLzA0OTNiNWZiLWE1OWEtNDJkMC04ZTcyLWRlNGFjNDEyNzdlMFwvZDh2YmlsNS0zZWE5MTE5Ni1iOTNhLTQ2MDctODY1MC1hZDhiOGNlODBjNTcucG5nIn1dXSwiYXVkIjpbInVybjpzZXJ2aWNlOmZpbGUuZG93bmxvYWQiXX0.LzXDOQ2dqnN7v3DzLa8orWMkgjlXDZntKt9mn9wjrdM",
                    "Вы сможете добавить место в избранное, чтобы вернуться к нему позже"
                ),
            )
        )
    }
}