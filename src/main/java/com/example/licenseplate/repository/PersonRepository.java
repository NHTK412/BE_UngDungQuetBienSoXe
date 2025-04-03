package com.example.licenseplate.repository;

import com.example.licenseplate.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
