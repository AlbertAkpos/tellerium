package me.alberto.tellerium.data.domain.usecase

import me.alberto.tellerium.data.domain.farmer.FarmerRepository
import me.alberto.tellerium.data.domain.model.Farmer
import javax.inject.Inject

class GetFarmersUseCase @Inject constructor (private val farmerRepository: FarmerRepository) :
    UseCase<Unit, Unit>() {
    override suspend fun buildUseCase(params: Unit?) {
         farmerRepository.getRemoteFarmers()
    }
}