package foothaha.stepmate_back.run.service;

import foothaha.stepmate_back.run.repository.RunSessionRepository;
import foothaha.stepmate_back.run.repository.SessionSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RunSessionService {

    private final RunSessionRepository runSessionRepository;
    private final SessionSummaryRepository sessionSummaryRepository;
}