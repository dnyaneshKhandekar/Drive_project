package com.dnyanesh.google_drive_project.Repo;

import com.dnyanesh.google_drive_project.Entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {
}
