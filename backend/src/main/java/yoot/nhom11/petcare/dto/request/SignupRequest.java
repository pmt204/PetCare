package yoot.nhom11.petcare.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
	@NotBlank(message = "Username cannot be blank")
	@Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
	private String username;

	@NotBlank(message = "Email cannot be blank")
	@Size(max = 50)
	@Email(message = "Email must be a valid email address")
	private String email;

	@NotBlank(message = "Password cannot be blank")
	@Size(min = 6, max = 40, message = "Password must be between 6 and 40 characters")
	private String password;

	@NotBlank(message = "Full name cannot be blank")
	@Size(max = 100)
	private String fullName;

	@NotBlank(message = "Role cannot be blank")
	private String role; // e.g. "OWNER", "VET", "ADMIN"
}
