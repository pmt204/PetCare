package yoot.nhom11.petcare.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JwtResponse {
	private String token;
	private String type = "Bearer";
	private String refreshToken;
	private Long id;
	private String username;
	private String email;
	private String fullName;
	private String role;

	public JwtResponse(String accessToken, String refreshToken, Long id, String username, String email, String fullName, String role) {
		this.token = accessToken;
		this.refreshToken = refreshToken;
		this.id = id;
		this.username = username;
		this.email = email;
		this.fullName = fullName;
		this.role = role;
	}
}
