package unaldi.photoservice.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import unaldi.photoservice.entity.Photo;
import unaldi.photoservice.entity.dto.request.MultipleUploadRequest;
import unaldi.photoservice.entity.dto.request.PhotoIdsRequest;
import unaldi.photoservice.entity.dto.request.SingleUploadRequest;
import unaldi.photoservice.entity.dto.response.DownloadResponse;
import unaldi.photoservice.entity.dto.response.PhotoResponse;
import unaldi.photoservice.repository.PhotoRepository;
import unaldi.photoservice.service.Impl.PhotoServiceImpl;
import unaldi.photoservice.utils.exception.PhotoNameEmptyException;
import unaldi.photoservice.utils.exception.PhotoNotFoundException;
import unaldi.photoservice.utils.rabbitMQ.dto.LogDTO;
import unaldi.photoservice.utils.rabbitMQ.producer.LogProducer;
import unaldi.photoservice.utils.result.DataResult;
import unaldi.photoservice.utils.result.Result;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PhotoServiceTest {
    private static SingleUploadRequest singleUploadRequest;
    private static MultipleUploadRequest multipleUploadRequest;
    private static PhotoResponse photoResponse;
    private static DownloadResponse downloadResponse;
    private static Photo photo;
    private static List<Photo> photoList;

    @Mock
    private PhotoRepository photoRepository;

    @Mock
    private LogProducer logProducer;

    @InjectMocks
    private PhotoServiceImpl photoService;

    @BeforeAll
    static void setUp() {
        singleUploadRequest = ObjectFactory.getInstance().getSingleUploadRequest();
        multipleUploadRequest = ObjectFactory.getInstance().getMultipleUploadRequest();
        photoResponse = ObjectFactory.getInstance().getPhotoResponse();
        downloadResponse = ObjectFactory.getInstance().getDownloadResponse();
        photo = ObjectFactory.getInstance().getPhoto();
        photoList = ObjectFactory.getInstance().getPhotoList();
    }

    @BeforeEach
    public void setUps() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);
    }

    @Test
    void givenSingleUploadRequest_whenSingleUpload_thenPhotoShouldBeSaved() throws IOException {
        when(photoRepository.existsByPhotoName(anyString())).thenReturn(false);
        when(photoRepository.save(any(Photo.class))).thenReturn(photo);

        DataResult<PhotoResponse> result = photoService.singleUpload(singleUploadRequest);

        assertTrue(result.getSuccess(), "Photo should be saved");
        assertEquals(photoResponse, result.getData(), "The photo response should match");

        verify(photoRepository, times(1)).save(any(Photo.class));
        verify(logProducer, times(1)).sendToLog(any(LogDTO.class));
    }

    @Test
    void givenMultipleUploadRequest_whenMultipleUpload_thenPhotosShouldBeUploaded() {
        when(photoRepository.existsByPhotoName(anyString())).thenReturn(false);
        when(photoRepository.save(any(Photo.class))).thenReturn(photo);

        DataResult<List<PhotoResponse>> result = photoService.multipleUpload(multipleUploadRequest);

        assertTrue(result.getSuccess(), "Photos should be uploaded");
        assertEquals(List.of(photoResponse), result.getData(), "The photo response list should match");

        verify(photoRepository, times(1)).save(any(Photo.class));
    }

    @Test
    void givenFindAllRequest_whenFindAll_thenAllPhotosShouldBeReturned() {
        when(photoRepository.findAll()).thenReturn(photoList);

        DataResult<List<PhotoResponse>> result = photoService.findAll();

        assertTrue(result.getSuccess(), "Photos should be found");
        assertEquals(photoList.size(), result.getData().size(), "The photo response list size should match");

        verify(photoRepository, times(1)).findAll();
        verify(logProducer, times(1)).sendToLog(any(LogDTO.class));
    }

    @Test
    void givenPhotoIdsRequest_whenFindByPhotoIds_thenPhotosShouldBeReturned() {
        List<String> photoIds = List.of(photo.getId());
        when(photoRepository.findByPhotoIds(photoIds)).thenReturn(photoList);

        PhotoIdsRequest photoIdsRequest = new PhotoIdsRequest(photoIds);
        DataResult<List<PhotoResponse>> result = photoService.findByPhotoIds(photoIdsRequest);

        assertTrue(result.getSuccess(), "Photos by IDs should be found");
        assertEquals(photoList.size(), result.getData().size(), "The photo response list size should match");

        verify(photoRepository, times(1)).findByPhotoIds(photoIds);
        verify(logProducer, times(1)).sendToLog(any(LogDTO.class));
    }

    @Test
    void givenPhotoId_whenFindById_thenPhotoShouldBeFound() {
        when(photoRepository.findById(photo.getId())).thenReturn(Optional.of(photo));

        DataResult<PhotoResponse> result = photoService.findById(photo.getId());

        assertTrue(result.getSuccess(), "Photo should be found");
        assertEquals(photoResponse, result.getData(), "The photo response should match");

        verify(photoRepository, times(1)).findById(photo.getId());
        verify(logProducer, times(1)).sendToLog(any(LogDTO.class));
    }


    @Test
    void givenPhotoId_whenDownloadById_thenDownloadResponseShouldBeReturned() {
        when(photoRepository.findById(photo.getId())).thenReturn(Optional.of(photo));

        DataResult<DownloadResponse> result = photoService.downloadById(photo.getId());

        assertTrue(result.getSuccess(), "Photo should be downloadable");
        assertEquals(downloadResponse, result.getData(), "The download response should match");

        verify(photoRepository, times(1)).findById(photo.getId());
        verify(logProducer, times(1)).sendToLog(any(LogDTO.class));
    }

    @Test
    void givenPhotoId_whenDeleteById_thenPhotoShouldBeDeleted() {
        when(photoRepository.findById(photo.getId())).thenReturn(Optional.of(photo));

        Result result = photoService.deleteById(photo.getId());

        assertTrue(result.getSuccess(), "Photo should be deleted");

        verify(photoRepository, times(1)).deleteById(photo.getId());
        verify(logProducer, times(1)).sendToLog(any(LogDTO.class));
    }

    @Test
    void givenInvalidSingleUploadRequest_whenSingleUpload_thenExceptionShouldBeThrown() {
        MultipartFile invalidFile = new MockMultipartFile("photo", "", "image/jpeg", "test data".getBytes());
        SingleUploadRequest invalidRequest = new SingleUploadRequest(invalidFile);

        assertThrows(PhotoNameEmptyException.class, () -> {
            photoService.singleUpload(invalidRequest);
        });
    }

    @Test
    void givenNonExistentPhotoId_whenFindById_thenPhotoNotFoundExceptionShouldBeThrown() {
        when(photoRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(PhotoNotFoundException.class, () -> {
            photoService.findById("non-existent-id");
        });
    }

    @Test
    void givenNonExistentPhotoId_whenDownloadById_thenPhotoNotFoundExceptionShouldBeThrown() {
        when(photoRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(PhotoNotFoundException.class, () -> {
            photoService.downloadById("non-existent-id");
        });
    }

    @Test
    void givenNonExistentPhotoId_whenDeleteById_thenPhotoNotFoundExceptionShouldBeThrown() {
        when(photoRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(PhotoNotFoundException.class, () -> {
            photoService.deleteById("non-existent-id");
        });
    }
}
