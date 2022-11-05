package com.pointlessapps.datastoreroom.processor

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

internal class DataStoreEntityProcessorProvider : SymbolProcessorProvider {

    override fun create(environment: SymbolProcessorEnvironment) = DataStoreEntityProcessor(
        codeGenerator = environment.codeGenerator,
    )
}