package com.sage.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Unit tests for TokenGenerator utility class.
 */
public class TokenGeneratorTest {

    @Test
    public void testGenerateUniqueTokenLength() {
        String token = TokenGenerator.generateUniqueToken();
        assertEquals(8, token.length(), "Token should be exactly 8 characters long");
    }

    @Test
    public void testGenerateUniqueTokenAllCharactersUnique() {
        String token = TokenGenerator.generateUniqueToken();
        Set<Character> uniqueChars = new HashSet<>();
        
        for (char c : token.toCharArray()) {
            uniqueChars.add(c);
        }
        
        assertEquals(8, uniqueChars.size(), "All 8 characters in token should be unique");
    }

    @Test
    public void testGenerateUniqueTokenContainsOnlyAlphanumeric() {
        String token = TokenGenerator.generateUniqueToken();
        
        for (char c : token.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c), 
                "Token should contain only alphanumeric characters, found: " + c);
        }
    }

    @Test
    public void testGenerateUniqueTokenMultipleCallsProduceDifferentTokens() {
        String token1 = TokenGenerator.generateUniqueToken();
        String token2 = TokenGenerator.generateUniqueToken();
        String token3 = TokenGenerator.generateUniqueToken();
        
        assertNotEquals(token1, token2, "Multiple calls should produce different tokens");
        assertNotEquals(token1, token3, "Multiple calls should produce different tokens");
        assertNotEquals(token2, token3, "Multiple calls should produce different tokens");
    }
}