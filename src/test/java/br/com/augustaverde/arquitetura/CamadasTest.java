package br.com.augustaverde.arquitetura;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

/**
 * Regras do MVC: o controller fala com o service, o service fala com o repository,
 * e o model não depende de ninguém.
 */
@AnalyzeClasses(packages = "br.com.augustaverde", importOptions = ImportOption.DoNotIncludeTests.class)
class CamadasTest {

    @ArchTest
    static final ArchRule camadas = layeredArchitecture()
            .consideringOnlyDependenciesInLayers()
            .layer("Controller").definedBy("..controller..")
            .layer("Service").definedBy("..service..")
            .layer("Repository").definedBy("..repository..")
            .layer("Model").definedBy("..model..")
            .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
            .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller")
            .whereLayer("Repository").mayOnlyBeAccessedByLayers("Service")
            .whereLayer("Model").mayOnlyBeAccessedByLayers("Controller", "Service", "Repository");

    @ArchTest
    static final ArchRule modelNaoConheceSpringWeb = noClasses()
            .that().resideInAPackage("..model..")
            .should().dependOnClassesThat().resideInAnyPackage("org.springframework.web..", "org.springframework.ui..");

    @ArchTest
    static final ArchRule controllersFicamNoPacoteCerto = classes()
            .that().areAnnotatedWith(org.springframework.stereotype.Controller.class)
            .should().resideInAPackage("..controller..");

    @ArchTest
    static final ArchRule repositoriesFicamNoPacoteCerto = classes()
            .that().areAssignableTo(org.springframework.data.repository.Repository.class)
            .should().resideInAPackage("..repository..");
}
