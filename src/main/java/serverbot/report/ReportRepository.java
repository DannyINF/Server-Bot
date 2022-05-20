package serverbot.report;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends CrudRepository<Report, ReportId> {
    Streamable<Report> findAll();

    Streamable<Report> findByUserIdAndRulingType(String userId, RulingType rulingType);

    @Query(value = "SELECT r FROM Report r WHERE r.userId = ?1 AND r.rulingType in (?2,?3)")
    Streamable<Report> findUnfinishedReportsByUserId(String userId, RulingType rulingType1, RulingType rulingType2);
}
