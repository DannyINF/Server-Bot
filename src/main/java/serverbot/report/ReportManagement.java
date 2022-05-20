package serverbot.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ReportManagement {

    @Autowired
    ReportRepository reportRepository;

    public Streamable<Report> findAll() {
        return reportRepository.findAll();
    }

    public void save(Report report) {
        reportRepository.save(report);
    }

    public Optional<Report> findById(ReportId reportId) {
        return reportRepository.findById(reportId);
    }

    public Streamable<Report> findByUserIdAndRulingType(String userId, RulingType rulingType) {
        return reportRepository.findByUserIdAndRulingType(userId, rulingType);
    }

    public Streamable<Report> findUnfinishedReportsByUserId(String userId, RulingType rulingType1, RulingType rulingType2) {
        return reportRepository.findUnfinishedReportsByUserId(userId, rulingType1, rulingType2);
    }
}
