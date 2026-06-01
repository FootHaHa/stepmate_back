package foothaha.stepmate_back.run.service;

import foothaha.stepmate_back.run.dto.RunSessionStartResponse;
import foothaha.stepmate_back.run.entity.RunSession;
import foothaha.stepmate_back.run.entity.RunSessionStatus;
import foothaha.stepmate_back.run.repository.RunSessionRepository;
import foothaha.stepmate_back.run.repository.SessionSummaryRepository;
import foothaha.stepmate_back.user.entity.User;
import foothaha.stepmate_back.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RunSessionService {

    private final RunSessionRepository runSessionRepository;
    private final SessionSummaryRepository sessionSummaryRepository;
    private final UserRepository userRepository;

    @Transactional
    public RunSessionStartResponse startRun(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        RunSession session = RunSession.builder()
                .user(user)
                .startedAt(LocalDateTime.now())
                .status(RunSessionStatus.IN_PROGRESS)
                .build();

        return RunSessionStartResponse.from(runSessionRepository.save(session));
    }

    @Transactional
    public void finishRun(String email, Long runSessionId) {
        RunSession session = runSessionRepository.findById(runSessionId)
                .orElseThrow(() -> new IllegalArgumentException("세션을 찾을 수 없습니다."));

        if (!session.getUser().getEmail().equals(email)) {
            throw new IllegalArgumentException("본인의 세션만 종료할 수 있습니다.");
        }

        session.finish(LocalDateTime.now());

    }
}