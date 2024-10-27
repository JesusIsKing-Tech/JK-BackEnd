package com.api.jesus_king_tech.domain.usuario.repository;

import com.api.jesus_king_tech.domain.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

//    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM Usuario u WHERE u.email = :email")
//    boolean existsByEmail(@Param("email") String email );

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);
}
