package org.example;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class ServerTest {
    Server server = Mockito.mock(Server.class);

    @Test
    void run() {
        assertNull(server.in);
        assertNull(server.out);
    }

    @Test
    void endSocket() {
    }

    @Test
    void logServer() {
    }
}