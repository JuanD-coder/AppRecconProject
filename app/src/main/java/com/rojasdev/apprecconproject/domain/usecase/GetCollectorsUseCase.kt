package com.rojasdev.apprecconproject.domain.usecase

import com.rojasdev.apprecconproject.data.repository.CollectorRepository
import com.rojasdev.apprecconproject.legacy.data.entities.RecolectoresEntity
import javax.inject.Inject

class GetCollectorsUseCase @Inject constructor(
    private val repository: CollectorRepository
) {
    suspend operator fun invoke(): List<RecolectoresEntity> {
        return repository.getCollectors()
    }
}
