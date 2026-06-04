package foothaha.stepmate_back.user.service;

import foothaha.stepmate_back.user.dto.UserResponse;
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

    private User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

}
