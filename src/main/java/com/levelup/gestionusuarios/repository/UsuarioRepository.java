package com.levelup.gestionusuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.levelup.gestionusuarios.entity.UsuarioEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    
    // Query Methods (consultas derivadas del nombre del método)
    Optional<UsuarioEntity> findByEmail(String email);
    
    List<UsuarioEntity> findByActivo(Boolean activo);
    
    List<UsuarioEntity> findByEsMayorEdad(Boolean esMayorEdad);
    
    List<UsuarioEntity> findByTieneDescuentoDuoc(Boolean tieneDescuento);
    
    Boolean existsByEmail(String email);
    
    // Query Nativa - SQL real
    @Query(value = "SELECT * FROM usuarios WHERE puntos_levelup >= :puntos", nativeQuery = true)
    List<UsuarioEntity> findUsuariosConPuntosMayoresA(@Param("puntos") Integer puntos);
    
    // Query Objetal - JPQL
    @Query("SELECT u FROM UsuarioEntity u WHERE u.nombre LIKE %:nombre% OR u.apellido LIKE %:apellido%")
    List<UsuarioEntity> buscarPorNombreOApellido(@Param("nombre") String nombre, @Param("apellido") String apellido);
    
    // Query Nativa con JOIN
    @Query(value = "SELECT u.* FROM usuarios u " +
                   "INNER JOIN usuario_roles ur ON u.id = ur.usuario_id " +
                   "INNER JOIN roles r ON ur.rol_id = r.id " +
                   "WHERE r.nombre = :nombreRol", nativeQuery = true)
    List<UsuarioEntity> findUsuariosPorRol(@Param("nombreRol") String nombreRol);
    
    // Query Objetal para contar usuarios activos
    @Query("SELECT COUNT(u) FROM UsuarioEntity u WHERE u.activo = true")
    Long contarUsuariosActivos();
}
