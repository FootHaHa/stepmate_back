package foothaha.stepmate_back.user.service;

import foothaha.stepmate_back.run.repository.RunSessionRepository;
import foothaha.stepmate_back.user.dto.StreakResponse;
import foothaha.stepmate_back.user.dto.UserResponse;
import foothaha.stepmate_back.user.entity.AuthProvider;
import foothaha.stepmate_back.user.entity.User;
import foothaha.stepmate_back.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TreeSet;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RunSessionRepository runSessionRepository;

    @Transactional
    public User upsertGoogleUser(String providerId, String email, String name, String pictureUrl) {
        return userRepository.findByProviderAndProviderId(AuthProvider.GOOGLE, providerId)
                .map(u -> updateProfile(u, email, name, pictureUrl))
                .orElseGet(() ->
                        userRepository.findByEmail(email)
                                .map(u -> linkGoogle(u, providerId, name, pictureUrl))
                                .orElseGet(() -> createNew(providerId, email, name, pictureUrl))
                );
    }

    private User updateProfile(User user, String email, String name, String pictureUrl) {
        user.setEmail(email);
        if (name != null) user.setName(name);
        if (pictureUrl != null) user.setProfileImageUrl(pictureUrl);
        return userRepository.save(user);
    }

    private User linkGoogle(User user, String providerId, String name, String pictureUrl) {
        user.setProvider(AuthProvider.GOOGLE);
        user.setProviderId(providerId);
        if (name != null) user.setName(name);
        if (pictureUrl != null) user.setProfileImageUrl(pictureUrl);
        return userRepository.save(user);
    }

    private User createNew(String providerId, String email, String name, String pictureUrl) {
        User user = User.builder()
                .provider(AuthProvider.GOOGLE)
                .providerId(providerId)
                .email(email)
                .name(name)
                .profileImageUrl(pictureUrl)
                .build();
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public UserResponse findMyInfo(String email) {
        User user = findByEmail(email);
        return UserResponse.from(user);
    }

    @Transactional
    public UserResponse updateBodyInfo(String email, Double weightKg, Double heightCm) {
        User user = findByEmail(email);
        user.setWeightKg(weightKg);
        user.setHeightCm(heightCm);
        return UserResponse.from(user);
    }

    @Transactional
    public StreakResponse getStreak(String email) {
        User user = findByEmail(email);

        List<LocalDateTime> rawDates = runSessionRepository.findFinishedStartedAtByUser(user);

        // 날짜만 추출 후 중복 제거, 내림차순 정렬
        TreeSet<LocalDate> dates = new TreeSet<>((a, b) -> b.compareTo(a));
        for (LocalDateTime dt : rawDates) {
            dates.add(dt.toLocalDate());
        }
        List<LocalDate> sortedDates = List.copyOf(dates);

        LocalDate today = LocalDate.now();
        boolean ranToday = sortedDates.contains(today);
        int currentStreak = calcCurrentStreak(sortedDates, today);
        int maxStreak = Math.max(calcMaxStreak(sortedDates), user.getMaxStreak());

        user.setCurrentStreak(currentStreak);
        user.setMaxStreak(maxStreak);

        return StreakResponse.builder()
                .currentStreak(currentStreak)
                .maxStreak(maxStreak)
                .ranToday(ranToday)
                .build();
    }

    private int calcCurrentStreak(List<LocalDate> sortedDatesDesc, LocalDate today) {
        if (sortedDatesDesc.isEmpty()) return 0;

        LocalDate cursor = sortedDatesDesc.get(0);
        // 오늘 또는 어제부터 시작하지 않으면 streak 없음
        if (!cursor.equals(today) && !cursor.equals(today.minusDays(1))) return 0;

        int streak = 0;
        for (LocalDate date : sortedDatesDesc) {
            if (date.equals(cursor)) {
                streak++;
                cursor = cursor.minusDays(1);
            } else {
                break;
            }
        }
        return streak;
    }

    private int calcMaxStreak(List<LocalDate> sortedDatesDesc) {
        if (sortedDatesDesc.isEmpty()) return 0;

        int max = 1, current = 1;
        for (int i = 1; i < sortedDatesDesc.size(); i++) {
            if (sortedDatesDesc.get(i).equals(sortedDatesDesc.get(i - 1).minusDays(1))) {
                current++;
                if (current > max) max = current;
            } else {
                current = 1;
            }
        }
        return max;
    }

    private User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

}
