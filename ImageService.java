package ImageHoster.service;
/*
--------------------------------------------------------------------------------------------------------------------------------
 Version         Modification Date                Developer                Modifications
--------------------------------------------------------------------------------------------------------------------------------
 *@ 1.0.0.1         29-Dec-2018                  Dhruv Sharma              Bug Fix: Owner of the image can edit/delete the image.
*/
import ImageHoster.model.Image;
import ImageHoster.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;

    //Call the getAllImages() method in the Repository and obtain a List of all the images in the database
    public List<Image> getAllImages() {
        return imageRepository.getAllImages();
    }


    //The method calls the createImage() method in the Repository and passes the image to be persisted in the database
    public void uploadImage(Image image) {
        imageRepository.uploadImage(image);
    }


    //The method calls the getImageByTitle() method in the Repository and passes the title of the image to be fetched
    //public Image getImageByTitle(String title) { return imageRepository.getImageByTitle(title); }
    public Image getImageByTitle(String title,int id) { return imageRepository.getImageByTitle(title,id); }//Dhruv

    //The method calls the getImage() method in the Repository and passes the id of the image to be fetched
    public Image getImage(Integer imageId) {
        return imageRepository.getImage(imageId);
    }

    //The method calls the updateImage() method in the Repository and passes the Image to be updated in the database
    public void updateImage(Image updatedImage) {
        imageRepository.updateImage(updatedImage);
    }

    //The method calls the deleteImage() method in the Repository and passes the Image id of the image to be deleted in the database
    public void deleteImage(Integer imageId) { imageRepository.deleteImage(imageId);
    }

    //Start: Added by Dhruv Sharma. Bug Fix: Owner of the image can edit/delete the image.
    public boolean validateUser(Integer loggedUserId, Integer imageId){
        return imageRepository.validateUser(loggedUserId,imageId);
    }
    //End: Added by Dhruv Sharma. Bug Fix: Owner of the image can edit/delete the image.
}
