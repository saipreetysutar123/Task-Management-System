package com.saipreety.taskmanagement.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomAccessDeniedHandlerTest {

    @Test
    void handle_ShouldReturn403Forbidden() throws Exception {

        CustomAccessDeniedHandler handler =
                new CustomAccessDeniedHandler();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        AccessDeniedException exception = mock(AccessDeniedException.class);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(printWriter);

        handler.handle(request, response, exception);

        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
        verify(response).setContentType("application/json");

        printWriter.flush();

        String result = stringWriter.toString();

        assertTrue(result.contains("\"status\":403"));
        assertTrue(result.contains(
                "Forbidden - You do not have permission to access this resource"
        ));
    }
}
