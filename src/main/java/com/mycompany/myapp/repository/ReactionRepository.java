package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Reaction;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Reaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long>, JpaSpecificationExecutor<Reaction> {
}
