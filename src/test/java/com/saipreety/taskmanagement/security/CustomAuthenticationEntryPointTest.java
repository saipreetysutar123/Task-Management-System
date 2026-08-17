package com.saipreety.taskmanagement.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.AuthenticationException;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomAuthenticationEntryPointTest {

    @Test
    void commence_ShouldReturn401Unauthorized() throws Exception {

        CustomAuthenticationEntryPoint entryPoint =
                new CustomAuthenticationEntryPoint();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        AuthenticationException exception = mock(AuthenticationException.class);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(printWriter);

        entryPoint.commence(request, response, exception);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json");

        printWriter.flush();

        String result = stringWriter.toString();

        assertTrue(result.contains("\"status\":401"));
        assertTrue(result.contains("Unauthorized - Please login first"));
    }
}
