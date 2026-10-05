package com.nivor.workspace;
import java.util.List;import java.util.Optional;import org.springframework.data.jpa.repository.JpaRepository;
public interface WorkspaceRepository extends JpaRepository<Workspace,Long>{List<Workspace> findAllByUser_IdOrderByNameAsc(Long id);Optional<Workspace> findByIdAndUser_Id(Long id,Long userId);}
