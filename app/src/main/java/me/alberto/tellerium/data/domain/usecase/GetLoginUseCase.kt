package me.alberto.tellerium.data.domain.usecase

import me.alberto.tellerium.data.domain.repository.LoginRepository
import javax.inject.Inject

class GetLoginUseCase @Inject constructor(private val loginRepository: LoginRepository) :
    UseCase<Boolean, Unit>() {
    override suspend fun buildUseCase(params: Unit?): Boolean {
        return loginRepository.getLogin()
    }
}