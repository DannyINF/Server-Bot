package serverbot.statistics;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@Order(10)
public class StatisticsDataInitializer {
    private final StatisticsRepository statisticsRepository;

    public StatisticsDataInitializer(StatisticsRepository statisticsRepository) {
        this.statisticsRepository = statisticsRepository;
    }

    @Transactional
    public void initialize() {
        Statistics statistics = new Statistics("277746420281507841", "541212279329259532", 0L, 0L, 0L, 0L, LocalDateTime.now(),
                LocalDateTime.now(), null, 0L, 0L, 0L);
        statisticsRepository.save(statistics);
    }
}
