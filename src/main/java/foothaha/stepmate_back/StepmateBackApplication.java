package foothaha.stepmate_back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// Spring Boot 4.0 부터 @EntityScan 이 spring-boot-persistence 모듈로 이동했다.
// 구버전(3.x 이하): org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * StepMate 백엔드 진입점.
 *
 * 컴포넌트/엔티티/리포지토리가 foothaha 패키지 하위 곳곳에 흩어져 있으므로
 * scan 범위를 foothaha 로 명시한다.
 */
@SpringBootApplication(scanBasePackages = "foothaha")
@EntityScan(basePackages = "foothaha")
@EnableJpaRepositories(basePackages = "foothaha")
public class StepmateBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(StepmateBackApplication.class, args);
	}

}
