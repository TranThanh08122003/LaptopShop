// package com.TCL.example.repository;


// import java.lang.annotation.Target;

// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.Pageable;
// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.stereotype.Repository;

// @Repository
// public interface TargetRepository extends JpaRepository<Target, Long> {
//     Target findByCode(String code);
//     Page<Target> findByActive(Boolean active, Pageable pageable);
//     void save(com.TCL.example.domain.Target target);
// }
