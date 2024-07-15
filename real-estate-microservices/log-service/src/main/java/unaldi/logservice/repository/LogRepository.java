package unaldi.logservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import unaldi.logservice.entity.Log;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 15.07.2024
 */
@Repository
public interface LogRepository extends MongoRepository<Log, String> {

}
