package com.pointlessapps.datastoreroom.processor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.validate
import com.pointlessapps.datastoreroom.annotations.DataStoreEntity
import com.pointlessapps.datastoreroom.processor.generators.DataStoreEntityGenerator

internal class DataStoreEntityProcessor(codeGenerator: CodeGenerator) : SymbolProcessor {

    private val entityGenerator = DataStoreEntityGenerator(codeGenerator)

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver
            .getSymbolsWithAnnotation(DataStoreEntity::class.qualifiedName!!)
            .filterIsInstance<KSClassDeclaration>()

        if (!symbols.iterator().hasNext()) return emptyList()

        val currentlyUsedNames = mutableSetOf<String>()
        symbols.forEach { symbol ->
            getDataStoreEntityName(symbol).let {
                if (currentlyUsedNames.contains(it)) {
                    throw IllegalStateException(
                        "There are multiple DataStore entities with the same name `$it`!"
                    )
                }
                currentlyUsedNames.add(it)
            }
            symbol.accept(DataStoreEntityVisitor(), Unit)
        }

        return symbols.filterNot(KSClassDeclaration::validate).toList()
    }

    inner class DataStoreEntityVisitor : KSVisitorVoid() {
        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) =
            entityGenerator.generate(classDeclaration)
    }
}
