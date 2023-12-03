package ru.practicum.shareit.booking;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoLite;
import ru.practicum.shareit.booking.model.Booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {
    @Autowired
    ObjectMapper mapper;
    @MockBean
    BookingService bookingService;
    @Autowired
    private MockMvc mvc;
    private BookingDto bookingDto;
    private BookingDtoLite bookingDtoL;

    @BeforeEach
    void setUp() {
        UserDto booker = new UserDto(1L, "John", "john.doe@mail.com");
        UserDto owner = new UserDto(2L, "John2", "john2.doe@mail.com");
        ItemDto item = new ItemDto(1L, "Item", "description", true, owner, 2L, bookingDtoL, bookingDtoL, null);
        bookingDto = BookingDto.builder().id(1L).start(LocalDateTime.of(2023, 11, 30, 1, 1, 1))
                .end(LocalDateTime.of(2023, 12, 30, 1, 1, 1)).item(item).booker(booker).status(BookingStatus.WAITING)
                .build();
        bookingDtoL = BookingDtoLite.builder().id(1L).start(LocalDateTime.of(2023, 11, 30, 1, 1, 1))
                .end(LocalDateTime.of(2023, 12, 30, 1, 1, 1)).itemId(1L).status(BookingStatus.WAITING).build();
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void createBooking() throws Exception {
        when(bookingService.add(anyLong(), eq(bookingDtoL)))
        .thenReturn(bookingDto);

        mvc.perform(post("/bookings")
                .header("X-Sharer-User-Id", 2L)
                .content(mapper.writeValueAsString(bookingDtoL))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(bookingDto.getId().intValue())))
        .andExpect(jsonPath("$.start", is(bookingDto.getStart().toString())))
        .andExpect(jsonPath("$.end", is(bookingDto.getEnd().toString())))
        .andExpect(jsonPath("$.item.name", is(bookingDto.getItem().getName())))
        .andExpect(jsonPath("$.booker.name", is(bookingDto.getBooker().getName())))
        .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())));
        verify(bookingService).add(anyLong(), any());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void updateBooking() throws Exception {
        when(bookingService.update(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingDto);

        mvc.perform(patch("/bookings/{bookingId}", 1L)
                        .param("approved", String.valueOf(true))
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId().intValue())))
                .andExpect(jsonPath("$.start", is(bookingDto.getStart().toString())))
                .andExpect(jsonPath("$.end", is(bookingDto.getEnd().toString())))
                .andExpect(jsonPath("$.item.name", is(bookingDto.getItem().getName())))
                .andExpect(jsonPath("$.booker.name", is(bookingDto.getBooker().getName())))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())));
        verify(bookingService).update(anyLong(), anyLong(), anyBoolean());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void getBookingById() throws Exception {
        when(bookingService.get(anyLong(), anyLong()))
                .thenReturn(bookingDto);

        mvc.perform(get("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId().intValue())))
                .andExpect(jsonPath("$.start", is(bookingDto.getStart().toString())))
                .andExpect(jsonPath("$.end", is(bookingDto.getEnd().toString())))
                .andExpect(jsonPath("$.item.name", is(bookingDto.getItem().getName())))
                .andExpect(jsonPath("$.booker.name", is(bookingDto.getBooker().getName())))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())));
        verify(bookingService).get(anyLong(), anyLong());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void getUserBookings() throws Exception {
        when(bookingService.getBookingsByBooker(anyLong(), anyString(), any()))
                .thenReturn(List.of(bookingDto));

        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId().intValue())))
                .andExpect(jsonPath("$[0].start", is(bookingDto.getStart().toString())))
                .andExpect(jsonPath("$[0].end", is(bookingDto.getEnd().toString())))
                .andExpect(jsonPath("$[0].item.name", is(bookingDto.getItem().getName())))
                .andExpect(jsonPath("$[0].booker.name", is(bookingDto.getBooker().getName())))
                .andExpect(jsonPath("$[0].status", is(bookingDto.getStatus().toString())));
        verify(bookingService).getBookingsByBooker(anyLong(), anyString(), any());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void getItemBookings() throws Exception {
        when(bookingService.getBookingsByItem(anyLong(), any()))
                .thenReturn(List.of(bookingDto));

        mvc.perform(get("/bookings/item")
                        .header("X-Sharer-Item-Id", 1L)
                        .param("state", "ALL")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId().intValue())))
                .andExpect(jsonPath("$[0].start", is(bookingDto.getStart().toString())))
                .andExpect(jsonPath("$[0].end", is(bookingDto.getEnd().toString())))
                .andExpect(jsonPath("$[0].item.name", is(bookingDto.getItem().getName())))
                .andExpect(jsonPath("$[0].booker.name", is(bookingDto.getBooker().getName())))
                .andExpect(jsonPath("$[0].status", is(bookingDto.getStatus().toString())));
        verify(bookingService).getBookingsByItem(anyLong(), any());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void getOwnerBookings() throws Exception {
        when(bookingService.getBookingsByOwner(anyLong(), anyString(), any()))
                .thenReturn(List.of(bookingDto));

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId().intValue())))
                .andExpect(jsonPath("$[0].start", is(bookingDto.getStart().toString())))
                .andExpect(jsonPath("$[0].end", is(bookingDto.getEnd().toString())))
                .andExpect(jsonPath("$[0].item.name", is(bookingDto.getItem().getName())))
                .andExpect(jsonPath("$[0].booker.name", is(bookingDto.getBooker().getName())))
                .andExpect(jsonPath("$[0].status", is(bookingDto.getStatus().toString())));
        verify(bookingService).getBookingsByOwner(anyLong(), anyString(), any());
    }

    @Test
    void getOwnerBookingsError() throws Exception {
        when(bookingService.getBookingsByOwner(anyLong(), eq("ALL"), any()))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(status().isBadRequest());
    }

    @Test
    void bookingControllerErrorsTest() throws Exception {
        mvc.perform(get("/bookings/owner?from=-1&size=2").header("X-Sharer-User-Id", 1L).param("state", "ALL")
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError())
                .andExpect(status().isBadRequest());
        mvc.perform(get("/bookings/item?from=-1&size=2").header("X-Sharer-User-Id", 1L).param("state", "ALL")
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError())
                .andExpect(status().isBadRequest());
        mvc.perform(get("/bookings?from=-1&size=2").header("X-Sharer-User-Id", 1L).param("state", "ALL")
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError())
                .andExpect(status().isBadRequest());
    }
}