package ra.edu.ptit_cntt2_it210_project.service;

import org.springframework.stereotype.Service;
import ra.edu.ptit_cntt2_it210_project.repository.BorrowingDetailRepository;
import ra.edu.ptit_cntt2_it210_project.repository.BorrowingRecordRepository;
import ra.edu.ptit_cntt2_it210_project.repository.MentoringSessionRepository;

import java.util.HashMap;
import java.util.Map;

@Service
public class OverviewServiceImpl implements OverviewService{
    private final MentoringSessionRepository sessionRepository;
    private final BorrowingDetailRepository detailRepository;

    public OverviewServiceImpl(MentoringSessionRepository sessionRepository, BorrowingDetailRepository detailRepository){
        this.sessionRepository = sessionRepository;
        this.detailRepository = detailRepository;
    }


    @Override
    public Map<String, Object> getOverviewStats() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalSessions", sessionRepository.count());

        stats.put("pendingRequests", sessionRepository.countByStatus("PENDING"));

        Integer borrowedCount = detailRepository.sumQuantityByBorrowingStatus("COMPLETED");
        stats.put("borrowedEquipmentsCount", borrowedCount != null ? borrowedCount : 0);

        return stats;
    }
}
