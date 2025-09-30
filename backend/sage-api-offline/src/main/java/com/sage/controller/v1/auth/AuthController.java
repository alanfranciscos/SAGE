package com.sage.controller.v1.auth;

import com.sage.config.security.TokenService;
import com.sage.dto.v1.auth.LoginRequestDTO;
import com.sage.dto.v1.auth.RegisterRequestDTO;
import com.sage.dto.v1.auth.ResponseDTO;
import com.sage.dto.v1.caregiver.request.CreateCaregiverRequestDto;
import com.sage.dto.v1.caregiver.response.CaregiverResponseDto;
import com.sage.dto.v1.caregiver.response.CaregiverResponseFromPasswordTableDto;
import com.sage.services.caregiver.CaregiverServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final CaregiverServiceImpl caregiverService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO body){
        CaregiverResponseDto user = this.caregiverService.findByEmailAndReturnsCaregiverResponseDto(body.email()).orElseThrow(() -> new RuntimeException("User not found for email"));
        CaregiverResponseFromPasswordTableDto userResponseFromPasswordTableDto = this.caregiverService.getCaregiverFromPasswordTable(user.id()).orElseThrow(() -> new RuntimeException("User not found for uuid"));
        String hash = passwordEncoder.encode("123456");
        System.out.println(hash);
        if(passwordEncoder.matches(body.password(), userResponseFromPasswordTableDto.caregiver_password())) {
            System.out.println("IF");
            String token = this.tokenService.generateToken(user);
            return ResponseEntity.ok(new ResponseDTO(user.fullName(), token));
        } else {
            System.out.println("ELSE");
        }
        return ResponseEntity.badRequest().build();
    }


    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterRequestDTO body){
        Optional<CaregiverResponseDto> optionalUser = this.caregiverService.findByEmailAndReturnsCaregiverResponseDto(body.email());

        if(optionalUser.isPresent()) {
            return ResponseEntity.badRequest().body("Usuário já cadastrado com este e-mail.");
        }

        UUID caregiverId = caregiverService.createCaregiver(
                body.fullName(),
                body.cpf(),
                body.email(),
                body.phone(),
                "chieff"
        );

        System.out.println("CAREGIVER ID: " + caregiverId);

        String hashedPassword = passwordEncoder.encode(body.caregiver_password());

        UUID passwordId = caregiverService.createPassword(caregiverId, hashedPassword);
        System.out.println("PASSWORD ID: " + passwordId);

        CaregiverResponseDto newUser = caregiverService.getCaregiverById(caregiverId);

        String token = this.tokenService.generateToken(newUser);

        return ResponseEntity.ok(new ResponseDTO(newUser.fullName(), token));
    }
}