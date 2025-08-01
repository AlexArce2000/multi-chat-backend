package org.arc.chat_backend.user;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // JpaRepository<User, Long> -> Gestiona entidades User cuya clave es de tipo Long
    // Spring Data JPA leerá el nombre de este método y creará
    // la consulta "SELECT * FROM usuarios WHERE username = ?" automáticamente.
    Optional<User> findByUsername(String username);
}