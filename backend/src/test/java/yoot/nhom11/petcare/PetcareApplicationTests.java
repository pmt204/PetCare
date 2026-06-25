package yoot.nhom11.petcare;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Disabled("Docker environment not available")
@Import(TestcontainersConfiguration.class)
@SpringBootTest
class PetcareApplicationTests {

	@Test
	void contextLoads() {
	}

}
