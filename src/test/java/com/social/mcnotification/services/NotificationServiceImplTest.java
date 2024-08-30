package com.social.mcnotification.services;

import com.social.mcnotification.dto.*;
import com.social.mcnotification.enums.NotificationType;
import com.social.mcnotification.model.NotificationEntity;
import com.social.mcnotification.model.NotificationSettingEntity;
import com.social.mcnotification.repository.NotificationRepository;
import com.social.mcnotification.repository.NotificationSettingRepository;
import com.social.mcnotification.security.jwt.JwtTokenFilter;
import com.social.mcnotification.security.jwt.UserModel;
import com.social.mcnotification.services.helper.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
//@WithMockUser(username = "testuser", roles = {"USER"})
public class NotificationServiceImplTest {

    @MockBean
    private SecurityContext securityContext;
    @MockBean
    private Authentication authentication;
    @MockBean
    private NotificationRepository notificationRepository;
    @MockBean
    private NotificationSettingRepository notificationSettingRepository;
    @MockBean
    private Mapper mapper;
    @MockBean
    private UserModel user;
    @InjectMocks
    private NotificationServiceImpl service;

    @BeforeEach
    void setUp() {
        List<String> roles = new ArrayList<>();
        roles.add("USER");
        user = new UserModel(UUID.fromString("aa59a222-5452-41f8-86e9-0d7631621d6d"), "hgujc9gtyfjhugv", "alisa@gmail.com", roles);

        MockitoAnnotations.openMocks(this);

        securityContext = Mockito.mock(SecurityContext.class);
        authentication = Mockito.mock(Authentication.class);
        notificationSettingRepository = Mockito.mock(NotificationSettingRepository.class);
        notificationRepository = Mockito.mock(NotificationRepository.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        mapper = Mockito.mock(Mapper.class);
        service = new NotificationServiceImpl(notificationRepository, notificationSettingRepository, mapper);

        // Устанавливаем мок для SecurityContext
        SecurityContextHolder.setContext(securityContext);

        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        lenient().when(authentication.getName()).thenReturn("testUser");
    }

    @Test
    public void testGetCurrentUser() {
        List<String> roles = new ArrayList<>();
        roles.add("USER");
        UserModel userModel = new UserModel(user.getId(), "hgujc9gtyfjhugv", "alisa@gmail.com", roles);
        when(authentication.getPrincipal()).thenReturn(userModel);
        UserModel result = service.getCurrentUser();
        assertEquals(userModel, result);
    }


    @Test
    public void testGetNotificationSettings() {
        NotificationSettingEntity entity = new NotificationSettingEntity();
        entity.setUserId(user.getId());

        NotificationSettingDto resultDto = new NotificationSettingDto();
        resultDto.setUserId(user.getId());
        when(authentication.getPrincipal()).thenReturn(user);
        when(service.getCurrentUser()).thenReturn(user);
        when(notificationSettingRepository.findByUserId(user.getId())).thenReturn(Optional.of(entity).get());
        when(mapper.mapToNotificationSettingDto(entity)).thenReturn(resultDto);

        NotificationSettingDto result = service.getNotificationSettings();

        assertEquals(result.getUserId(), resultDto.getUserId());
        verify(notificationSettingRepository, times(1)).findByUserId(user.getId());
        verify(mapper, times(1)).mapToNotificationSettingDto(entity);
    }

    @Test
    public void UpdateNotificationSettings() {
        NotificationType type = NotificationType.POST;
        NotificationUpdateDto updateDto = new NotificationUpdateDto(false, type);

        NotificationSettingEntity entity = new NotificationSettingEntity();
        entity.setUserId(user.getId());

        when(authentication.getPrincipal()).thenReturn(user);
        when(service.getCurrentUser()).thenReturn(user);
        when(notificationSettingRepository.findByUserId(user.getId())).thenReturn(entity);

        service.updateNotificationSettings(updateDto);

        verify(notificationSettingRepository, times(1)).save(any(NotificationSettingEntity.class));
        verify(notificationSettingRepository, times(1)).findByUserId(user.getId());

    }

    @Test
    public void markAllEventsAsRead() {
        List<NotificationEntity> entityList = new ArrayList<>();

        for (int i = 1; i <= 5; i ++) {
            NotificationEntity entity = new NotificationEntity();
            entity.setReceiverId(user.getId());
            entity.setAuthorId(UUID.randomUUID());
            entityList.add(entity);
        }

        when(authentication.getPrincipal()).thenReturn(user);
        when(service.getCurrentUser()).thenReturn(user);
        when(notificationRepository.findByReceiverId(user.getId())).thenReturn(entityList);

        service.markAllEventsAsRead();

        verify(notificationRepository, times(1)).saveAll(entityList);
        verify(notificationRepository, times(1)).findByReceiverId(user.getId());
    }


    @Test
    public void createNotificationSettings() {
        UUID idUser = user.getId();
        NotificationSettingEntity notificationSetting = new NotificationSettingEntity();
        notificationSetting.setUserId(idUser);
        when(mapper.mapToSettingEntity(any(NotificationSettingDto.class))).thenReturn(notificationSetting);
        Boolean result = service.createNotificationSettings(idUser);

        assertTrue(result);
        verify(notificationSettingRepository, times(1)).save(any(NotificationSettingEntity.class));
    }



    @Test
    public void createNotification() {

        EventNotificationDto eventNotificationDto = new EventNotificationDto(
                UUID.randomUUID(),
                user.getId(),
                NotificationType.MESSAGE,
                "Новое сообщение!");

        when(authentication.getPrincipal()).thenReturn(user);
        when(service.getCurrentUser()).thenReturn(user);

        service.createNotification(eventNotificationDto);

        verify(notificationRepository, times(1)).save(any(NotificationEntity.class));
    }


    @Test
    public void testGetEventsCount() {
        List<NotificationEntity> entityList = new ArrayList<>();

        for (int i = 1; i <= 5; i ++) {
            NotificationEntity entity = new NotificationEntity();
            entity.setReceiverId(user.getId());
            entity.setAuthorId(UUID.randomUUID());
            if (i <= 2) {
            entity.setIsReaded(true);
            } else {
                entity.setIsReaded(false);
            }
            entityList.add(entity);
        }

        when(authentication.getPrincipal()).thenReturn(user);
        when(service.getCurrentUser()).thenReturn(user);
        when(notificationRepository.findByReceiverId(user.getId())).thenReturn(entityList);

        NotificationCountDto result = service.getEventsCount();

        assertEquals(result.getData().getCount(), 3);
        verify(notificationRepository, times(1)).findByReceiverId(user.getId());
    }

    // Ниже методы которые осталось дописать !!!!!!!!!!!!!!!!!!!!!!



//    @Test
//    public void testGetNotifications() {
//    }

}
