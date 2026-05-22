package foothaha.stepmate_back.user.service;

import foothaha.stepmate_back.user.entity.AuthProvider;
import foothaha.stepmate_back.user.entity.User;
import foothaha.stepmate_back.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * Google 사용자 정보를 받아 DB에 upsert.
     *
     * - 같은 (provider, providerId) 가 이미 있으면: 이름/사진을 최신값으로 갱신.
     * - 같은 email 만 있으면: 기존 행에 provider 정보를 붙여 연결.
     * - 둘 다 없으면: 새 사용자 생성.
     */
    @Transactional
    public User upsertGoogleUser(String providerId, String email, String name, String pictureUrl) {
        // 1) provider + providerId 매칭 우선
        return userRepository.findByProviderAndProviderId(AuthProvider.GOOGLE, providerId)
                .map(u -> updateProfile(u, email, name, pictureUrl))
                .orElseGet(() ->
                        // 2) email 매칭 → 기존 계정에 Google 연결
                        userRepository.findByEmail(email)
                                .map(u -> linkGoogle(u, providerId, name, pictureUrl))
                                // 3) 신규 생성
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
}
