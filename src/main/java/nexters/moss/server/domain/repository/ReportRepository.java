package nexters.moss.server.domain.repository;

import nexters.moss.server.domain.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
    void deleteByUser_Id(Long userId);
}
