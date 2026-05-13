package foothaha.auth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Google 이 발급한 ID Token 의 서명 / audience / 만료 여부를 검증한다.
 *
 * 검증에 성공하면 페이로드(이메일, 이름, picture, sub 등)를 반환한다.
 * 실패 시 null 또는 예외.
 */
@Slf4j
@Service
public class GoogleTokenVerifier {

    /**
     * application.properties 의 google.oauth.client-ids 값.
     * 콤마로 여러 ID 허용 (Android Client ID, Web Client ID 둘 다 등록 권장).
     */
    @Value("${google.oauth.client-ids:}")
    private String clientIdsRaw;

    private GoogleIdTokenVerifier verifier;

    @PostConstruct
    void init() {
        List<String> clientIds = Arrays.stream(clientIdsRaw.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        if (clientIds.isEmpty()) {
            log.warn("[GoogleTokenVerifier] google.oauth.client-ids 가 비어 있음. "
                    + "ID Token 검증이 거부됩니다. application.properties 에 OAuth Client ID 를 등록하세요.");
        }

        this.verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance())
                .setAudience(clientIds)
                .build();
    }

    /**
     * @param idTokenString 프론트엔드가 넘겨준 Google ID Token
     * @return 검증된 payload. 검증 실패 시 null.
     */
    public GoogleIdToken.Payload verify(String idTokenString) {
        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            return idToken == null ? null : idToken.getPayload();
        } catch (GeneralSecurityException | java.io.IOException e) {
            log.warn("Google ID Token 검증 실패: {}", e.getMessage());
            return null;
        }
    }
}
