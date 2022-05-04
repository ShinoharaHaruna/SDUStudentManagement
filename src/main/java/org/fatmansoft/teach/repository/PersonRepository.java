package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Person;
import org.fatmansoft.teach.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.naming.ldap.PagedResultsControl;
import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Integer> {
}
