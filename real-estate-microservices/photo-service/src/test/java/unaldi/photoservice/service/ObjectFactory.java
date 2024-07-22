package unaldi.photoservice.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import unaldi.photoservice.entity.Photo;
import unaldi.photoservice.entity.dto.request.MultipleUploadRequest;
import unaldi.photoservice.entity.dto.request.SingleUploadRequest;
import unaldi.photoservice.entity.dto.response.DownloadResponse;
import unaldi.photoservice.entity.dto.response.PhotoResponse;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ObjectFactory {
    private static ObjectFactory instance;
    private SingleUploadRequest singleUploadRequest;
    private MultipleUploadRequest multipleUploadRequest;
    private PhotoResponse photoResponse;
    private DownloadResponse downloadResponse;
    private Photo photo;
    private List<Photo> photoList;

    public static synchronized ObjectFactory getInstance() {
        if (instance == null) {
            instance = new ObjectFactory();
        }
        return instance;
    }

    public SingleUploadRequest getSingleUploadRequest() {
        if (singleUploadRequest == null) {
            MultipartFile mockFile = new MockMultipartFile("photo", "test.jpg", "image/jpeg", "test data".getBytes());
            singleUploadRequest = new SingleUploadRequest(mockFile);
        }
        return singleUploadRequest;
    }

    public MultipleUploadRequest getMultipleUploadRequest() {
        if (multipleUploadRequest == null) {
            multipleUploadRequest = new MultipleUploadRequest(
                    new MultipartFile[]{singleUploadRequest.getPhoto()}
            );
        }
        return multipleUploadRequest;
    }

    public PhotoResponse getPhotoResponse() {
        if (photoResponse == null) {
            photoResponse = new PhotoResponse("1", "test.jpg", "http://localhost/api/v1/photos/download/1", "image/jpeg", getSingleUploadRequest().getPhoto().getSize());
        }
        return photoResponse;
    }

    public DownloadResponse getDownloadResponse() {
        if (downloadResponse == null) {
            Resource resource = new ByteArrayResource("test data".getBytes());
            downloadResponse = new DownloadResponse(resource, "test.jpg");
        }
        return downloadResponse;
    }

    public Photo getPhoto() {
        if (photo == null) {
            photo = new Photo(); // create a new Photo instance
            photo.setId("1");
            photo.setName("test.jpg");
            photo.setType("image/jpeg");
            photo.setSourceData("test data".getBytes());
        }
        return photo;
    }

    public List<Photo> getPhotoList() {
        if (photoList == null) {
            photoList = List.of(getPhoto());
        }
        return photoList;
    }
}
