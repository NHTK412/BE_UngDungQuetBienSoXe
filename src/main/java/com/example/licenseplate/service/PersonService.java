package com.example.licenseplate.service;

import com.example.licenseplate.model.Person;
import com.example.licenseplate.repository.PersonRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }
    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }
    public Optional<Person> getPersonById(String id) {
        return personRepository.findById(id);
    }
    public Person createPerson(Person person) {
        return personRepository.save(person);
    }
    public Person updatePerson(String id, Person updatedPerson) {
        return personRepository.findById(id).map(person -> {
            person.setFullName(updatedPerson.getFullName());
            person.setBirthDate(updatedPerson.getBirthDate());
            person.setGender(updatedPerson.getGender());
            person.setAddress(updatedPerson.getAddress());
            person.setPhoneNumber(updatedPerson.getPhoneNumber());
            return personRepository.save(person);
        }).orElseThrow(() -> new RuntimeException("Person not found"));
    }
    public void deletePerson(String id) {
        personRepository.deleteById(id);
    }
}
