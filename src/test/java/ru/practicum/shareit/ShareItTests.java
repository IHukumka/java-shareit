package ru.practicum.shareit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ShareItTests {

    public ShareItApp app = new ShareItApp();

    @Test
    public void main() {
        app.main(new String[] {});
    }

    @Test
    void contextLoads() {
        assertThat(app).isNotNull();
    }

}
