package com.example.officeservice.repository;

import com.example.officeservice.model.Desk;
import org.springframework.data.jpa.repository.JpaRepository;

// Extending JpaRepository gives us findAll(), findById(), save(), delete()
// etc. for free, backed by real SQL — no manual query code needed.
public interface DeskRepository extends JpaRepository<Desk, String> {
}
