package com.rainy.mvi.di

import com.rainy.mvi.model.repository.HomeRepository
import org.koin.dsl.module

/**
 * @author jiangshiyu
 * @date 2023/1/11
 */


val repoModule = module {
    single { HomeRepository() }
}



val appModule = listOf(repoModule)