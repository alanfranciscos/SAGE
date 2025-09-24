package com.sage.util;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

/**
 * Utility class for generating unique tokens for caregivers.
 * 
 * @author SAGE System
 * @version 1.0
 */
public class TokenGenerator {
    
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom random = new SecureRandom();
    
    /**
     * Generates a unique token with 8 alphanumeric characters.
     * All characters in the token will be unique (no repeated characters).
     * 
     * @return A string with 8 unique alphanumeric characters
     */
    public static String generateUniqueToken() {
        Set<Character> usedChars = new HashSet<>();
        StringBuilder token = new StringBuilder();
        
        while (token.length() < 8) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            
            // Only add if character hasn't been used yet
            if (!usedChars.contains(randomChar)) {
                usedChars.add(randomChar);
                token.append(randomChar);
            }
        }
        
        return token.toString();
    }
}