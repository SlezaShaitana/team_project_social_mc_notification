//package com.social.mcnotification.services;
//
//import com.social.mcnotification.client.dto.Pageable;
//import com.social.mcnotification.dto.*;
//import com.social.mcnotification.enums.NotificationType;
//import com.social.mcnotification.exceptions.NotificationNotFoundException;
//import com.social.mcnotification.exceptions.NotificationSettingNotFoundException;
//import com.social.mcnotification.model.NotificationEntity;
//import com.social.mcnotification.model.NotificationSettingEntity;
//import com.social.mcnotification.repository.NotificationRepository;
//import com.social.mcnotification.repository.NotificationSettingRepository;
//import com.social.mcnotification.security.jwt.JwtTokenFilter;
//import com.social.mcnotification.security.jwt.JwtUtils;
//import com.social.mcnotification.security.jwt.User;
//import com.social.mcnotification.services.helper.Mapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.test.context.support.WithMockUser;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//
//@ExtendWith(MockitoExtension.class)
//@WithMockUser(username = "testuser", roles = {"USER"})
//public class NotificationServiceImplTest {
//    // jwt token filter ??
//
//    @MockBean
//    private SecurityContext securityContext;
//
//    @MockBean
//    private JwtTokenFilter jwtTokenFilter;
//
//    @MockBean
//    private NotificationRepository notificationRepository;
//
//    @MockBean
//    private NotificationSettingRepository notificationSettingRepository;
//
//    @MockBean
//    private Mapper mapper;
//
//    @InjectMocks
//    private NotificationServiceImpl service;
//
//    @MockBean
//    private User user;
//
//    @MockBean
//    private Authentication authentication;
//
//    private final UUID id = UUID.randomUUID();
//
//
//
//    @BeforeEach
//    void setUp() {
//        List<String> roles = new ArrayList<>();
//        roles.add("USER");
//        user = new User(id, "hgujc9gtyfjhugv", "alisa@gmail.com", roles);
//
//        MockitoAnnotations.openMocks(this);
//
//
//        securityContext = Mockito.mock(SecurityContext.class);
//        authentication = Mockito.mock(Authentication.class);
//        notificationSettingRepository = Mockito.mock(NotificationSettingRepository.class);
//        jwtTokenFilter = Mockito.mock(JwtTokenFilter.class);
//        jwtTokenFilter.setUser(user);
//        mapper = Mockito.mock(Mapper.class);
//
//        when(jwtTokenFilter.getUser().getId()).thenReturn(id);
//
////        when(jwtTokenFilter.getUser().getId()).thenReturn(id);
//
//
//
////        service = Mockito.mock(NotificationServiceImpl.class);
//
//        service = new NotificationServiceImpl(jwtTokenFilter, notificationRepository, notificationSettingRepository);
//
//        // Устанавливаем мок для SecurityContext
//        SecurityContextHolder.setContext(securityContext);
////        when(securityContext.getAuthentication()).thenReturn(authentication);
////        when(authentication.getName()).thenReturn("testUser");
//
//        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
//        lenient().when(authentication.getName()).thenReturn("testUser");
//
//
//
//    }
//
//
//    @Test
////    @WithMockUser(roles = {"USER"})
////    @WithMockUser(username = "testuser", roles = {"USER"})
//    public void testGetNotificationSettings() {
////        UUID id = UUID.randomUUID();
//        NotificationSettingEntity entity = new NotificationSettingEntity();
//        entity.setId(id);
//
//        assertEquals(id, entity.getId());
//
//        NotificationSettingDto dto = new NotificationSettingDto();
//        dto.setId(id);
//
////        when(jwtTokenFilter.getUser()).thenReturn(user);
//        when(notificationSettingRepository.findById(id)).thenReturn(Optional.of(entity).get());
//        when(mapper.mapToNotificationSettingDto(entity)).thenReturn(dto);
//
//
//        NotificationSettingDto result = service.getNotificationSettings();
//
////        assertEquals(id, result.getId());
//
////        assertNotNull(result);
//        assertEquals(result.getId(), entity.getId());
//        verify(notificationSettingRepository, times(2)).findById(id);
//        verify(mapper, times(2)).mapToNotificationSettingDto(entity);
//
////        List<String> sort = new ArrayList<>();
////        int page = 1;
////        int size = 10;
////        UUID id = UUID.randomUUID();
////        NotificationSettingEntity mockSettingEntity = new NotificationSettingEntity();
////        mockSettingEntity.setId(id);
////
////        when(notificationSettingRepository.findById(id.toString())).thenReturn(Optional.of(mockSettingEntity));
//////        NotificationDto notificationDto = (NotificationDto) service.getNotifications(page, size, sort);
////
////        NotificationSettingDto notificationSettingDto = (NotificationSettingDto) service.getNotifications(page, size, sort);
////
////        assertEquals(id, notificationSettingDto.getId());
////
////        verify(notificationRepository, times(1)).findById("1");
//    }
//
//
//
//    @Test
//    @WithMockUser(roles = {"USER"})
//    public void UpdateNotificationSettings() {
//        NotificationSettingEntity mockSettingEntity = new NotificationSettingEntity();
//        mockSettingEntity.setId(id);
//        notificationSettingRepository.save(mockSettingEntity);
//        lenient().when(notificationSettingRepository.findById(user.getId())).thenReturn(Optional.of(mockSettingEntity).get());
////        when(jwtTokenFilter.getUser()).thenReturn(id);
//
//        NotificationUpdateDto updateDto = new NotificationUpdateDto( false, NotificationType.MESSAGE);
//
////        notificationService.updateNotificationSettings(updateDto);
//
//        verify(notificationSettingRepository).save(mockSettingEntity);
//    }
//
//
////    @Test
////    public void markAllEventsAsRead() {}
////
////
////    @Test
////    public Boolean createNotificationSettings(UUID id) {
////
////        return null;
////    }
////
////    @Test
////    public void createNotification(EventNotificationDto eventNotificationDto) {}
////
//
//
//    @Test
//    @WithMockUser(roles = {"USER"})
//    public void testGetNotifications() {
//    }
//
//
//    @Test
//    @WithMockUser(roles = {"USER"})
//    public void testGetEventsCount() {
//        List<NotificationEntity> mockNotifications = new ArrayList<>();
//        NotificationEntity notification = new NotificationEntity();
//        notification.setIsReaded(false);
//        mockNotifications.add(notification);
//        when(notificationRepository.findByAuthorId(user.getId())).thenReturn(mockNotifications);
//
////        NotificationCountDto result = new NotificationCountDto();
//
////        assertNotNull(result);
////        assertEquals(1, result.getData().getCount());
//
//    }
//
//
//
//
//    @Test
//    @WithMockUser(roles = {"USER"})
//    public void testGetEventsCountNotFound() {
//        when(notificationRepository.findByAuthorId(user.getId())).thenReturn(new ArrayList<>());
//
////        assertThrows(NotificationNotFoundException.class, () -> notificationService.getEventsCount());
//    }
//
//
//}
