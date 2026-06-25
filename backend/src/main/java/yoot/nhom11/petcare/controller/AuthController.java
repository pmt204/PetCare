package yoot.nhom11.petcare.controller;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import yoot.nhom11.petcare.dto.request.LoginRequest;
import yoot.nhom11.petcare.dto.request.SignupRequest;
import yoot.nhom11.petcare.dto.request.TokenRefreshRequest;
import yoot.nhom11.petcare.dto.response.JwtResponse;
import yoot.nhom11.petcare.dto.response.MessageResponse;
import yoot.nhom11.petcare.dto.response.TokenRefreshResponse;
import yoot.nhom11.petcare.entity.AppUser;
import yoot.nhom11.petcare.entity.RefreshToken;
import yoot.nhom11.petcare.entity.UserRole;
import yoot.nhom11.petcare.repository.AppUserRepository;
import yoot.nhom11.petcare.security.JwtUtils;
import yoot.nhom11.petcare.security.UserDetailsImpl;
import yoot.nhom11.petcare.service.RefreshTokenService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private AppUserRepository appUserRepository;

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private RefreshTokenService refreshTokenService;

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		String jwt = jwtUtils.generateJwtToken(authentication);

		RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

		String role = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.findFirst()
				.orElse("ROLE_OWNER")
				.replace("ROLE_", "");

		return ResponseEntity.ok(new JwtResponse(jwt,
				refreshToken.getToken(),
				userDetails.getId(),
				userDetails.getUsername(),
				userDetails.getEmail(),
				userDetails.getFullName(),
				role));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (appUserRepository.existsByUsername(signUpRequest.getUsername())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is already taken!");
		}

		if (appUserRepository.existsByEmail(signUpRequest.getEmail())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is already in use!");
		}

		// Create new user's account
		AppUser user = AppUser.builder()
				.username(signUpRequest.getUsername())
				.email(signUpRequest.getEmail())
				.passwordHash(encoder.encode(signUpRequest.getPassword()))
				.fullName(signUpRequest.getFullName())
				.active(true)
				.build();

		String strRole = signUpRequest.getRole();
		UserRole role;

		if (strRole == null) {
			role = UserRole.OWNER;
		} else {
			switch (strRole.toUpperCase()) {
				case "ADMIN":
					role = UserRole.ADMIN;
					break;
				case "VET":
				case "DOCTOR":
					role = UserRole.VET;
					break;
				default:
					role = UserRole.OWNER;
			}
		}

		user.setRole(role);
		appUserRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	@PostMapping("/refresh")
	public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
		String requestRefreshToken = request.getRefreshToken();

		return refreshTokenService.findByToken(requestRefreshToken)
				.map(refreshTokenService::verifyExpiration)
				.map(RefreshToken::getUser)
				.map(user -> {
					UserDetailsImpl userDetails = UserDetailsImpl.build(user);
					Authentication authentication = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					String token = jwtUtils.generateJwtToken(authentication);
					return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
				})
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token is not in database!"));
	}
}
