package com.mycompany.myapp;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Service;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

class KafkaArchTest {

    @Test
    void consumersShouldBeInTheirPackage() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.mycompany.myapp");

        classes()
            .that()
            .haveSimpleNameEndingWith("Consumer")
            .should()
            .resideInAPackage("com.mycompany.myapp.service.kafka.consumer")
            .because("Consumers have to be gathered")
            .check(importedClasses);
    }

    @Test
    void producersShouldBeInTheirPackage() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.mycompany.myapp");

        classes()
            .that()
            .haveSimpleNameEndingWith("Producer")
            .should()
            .resideInAPackage("com.mycompany.myapp.service.kafka.producer")
            .because("Producers have to be gathered")
            .check(importedClasses);
    }

    @Test
    void serializersShouldBeInTheirPackage() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.mycompany.myapp");

        classes()
            .that()
            .haveSimpleNameEndingWith("Serializer")
            .should()
            .resideInAPackage("com.mycompany.myapp.service.kafka.serde")
            .because("Serializers have to be gathered")
            .check(importedClasses);
    }

    @Test
    void deserializersShouldBeInTheirPackage() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.mycompany.myapp");

        classes()
            .that()
            .haveSimpleNameEndingWith("Deserializer")
            .should()
            .resideInAPackage("com.mycompany.myapp.service.kafka.serde")
            .because("Deserializers have to be gathered")
            .check(importedClasses);
    }

    @Test
    void serdesShouldBeInTheirPackage() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.mycompany.myapp");

        classes()
            .that()
            .haveSimpleNameEndingWith("Serde")
            .should()
            .resideInAPackage("com.mycompany.myapp.service.kafka.serde")
            .because("Serdes have to be gathered")
            .check(importedClasses);
    }

    @Test
    void consumersShouldExtendGenericConsumer() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.mycompany.myapp.service.kafka.consumer");

        final ArchCondition<? super JavaClass> extendGenericConsumer = new ArchCondition<JavaClass>("extend GenericConsumer") {
            @Override
            public void check(JavaClass javaClass, ConditionEvents conditionEvents) {
                if (!javaClass.getSuperClass().isPresent() || !javaClass.getSuperClass().get().getName().equals("GenericConsumer")) {
                    final String message = String.format(
                        "Class %s does not extend GenericConsumer", javaClass.getName());
                    conditionEvents.add(SimpleConditionEvent.violated(javaClass, message));
                }
            }
        };

        classes()
            .that()
            .doNotHaveSimpleName("GenericConsumer")
            .and()
            .haveSimpleNameEndingWith("Consumer")
            .should(extendGenericConsumer)
            .because("Consumers have to extend GenericConsumer")
            .check(importedClasses);
    }

    @Test
    void consumersShouldBeServices() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.mycompany.myapp.service.kafka.consumer");

        classes()
            .that()
            .doNotHaveSimpleName("GenericConsumer")
            .and()
            .haveSimpleNameEndingWith("Consumer")
            .should()
            .beAnnotatedWith(Service.class)
            .because("Consumers have to be services")
            .check(importedClasses);
    }

    @Test
    void producersShouldBeServices() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.mycompany.myapp.service.kafka.producer");

        classes()
            .that()
            .haveSimpleNameEndingWith("Producer")
            .should()
            .beAnnotatedWith(Service.class)
            .because("Producers have to be services")
            .check(importedClasses);
    }
}
