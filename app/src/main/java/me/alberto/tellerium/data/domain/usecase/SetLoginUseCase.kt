package me.alberto.tellerium.data.domain.usecase

import me.alberto.tellerium.data.domain.repository.LoginRepository
import javax.inject.Inject

class SetLoginUseCase @Inject constructor(private val loginRepository: LoginRepository) :
    UseCase<Unit, Boolean>() {
    override suspend fun buildUseCase(params: Boolean?) {
        requireNotNull(params) { "User cannot be null" }
        loginRepository.setLogin(params)
    }
}