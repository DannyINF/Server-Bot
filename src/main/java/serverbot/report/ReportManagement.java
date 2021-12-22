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
}
