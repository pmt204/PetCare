package yoot.nhom11.petcare.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import yoot.nhom11.petcare.entity.TestResult;

public interface TestResultRepository extends JpaRepository<TestResult, Long> {
}
