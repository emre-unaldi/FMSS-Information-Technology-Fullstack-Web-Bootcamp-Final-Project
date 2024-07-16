package unaldi.orderservice.utils.configuration.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import unaldi.orderservice.entity.Package;
import unaldi.orderservice.repository.PackageRepository;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 16.07.2024
 */
@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final PackageRepository packageRepository;

    @Autowired
    public DatabaseInitializer(PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        addPackageIfNotFound();
    }

    private void addPackageIfNotFound() {
        if (packageRepository.count() == 0) {
            Package pkg = Package.builder()
                    .advertCount(10)
                    .packageTime(2592000L)
                    .price(500.00)
                    .build();

            packageRepository.save(pkg);
        }
    }

}
