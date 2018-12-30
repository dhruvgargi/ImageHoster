package ImageHoster.repository;
/*
--------------------------------------------------------------------------------------------------------------------------------
 Version         Modification Date                Developer                Modifications
--------------------------------------------------------------------------------------------------------------------------------
 *@ 1.0.0.1         22-Dec-2018                  Dhruv Sharma              Fix for Image Upload Issue.
 *@ 1.0.0.2         29-Dec-2018                  Dhruv Sharma              Bug Fix: Owner of the image can edit/delete the image.
*/
import ImageHoster.model.Comments;
import ImageHoster.model.Image;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;

//The annotation is a special type of @Component annotation which describes that the class defines a data repository
@Repository
public class ImageRepository {

    //Get an instance of EntityManagerFactory from persistence unit with name as 'imageHoster'
    @PersistenceUnit(unitName = "imageHoster")
    private EntityManagerFactory emf;


    //The method receives the Image object to be persisted in the database
    //Creates an instance of EntityManager
    //Starts a transaction
    //The transaction is committed if it is successful
    //The transaction is rolled back in case of unsuccessful transaction
    public Image uploadImage(Image newImage) {

        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            em.persist(newImage);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
        return newImage;
    }

    //The method creates an instance of EntityManager
    //Executes JPQL query to fetch all the images from the database
    //Returns the list of all the images fetched from the database
    public List<Image> getAllImages() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Image> query = em.createQuery("SELECT i from Image i", Image.class);
        List<Image> resultList = query.getResultList();

        return resultList;
    }

    //The method creates an instance of EntityManager
    //Executes JPQL query to fetch the image from the database with corresponding title
    //Returns the image in case the image is found in the database
    //Returns null if no image is found in the database
    public Image getImageByTitle(String title,int id) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Image> typedQuery = null; //Added by Dhruv Sharma. Fix for image upload issue.
        try {
            //typedQuery = (TypedQuery<Image>) em.createQuery("SELECT i from Image i where i.title =:title , Image.class)setParameter("title",title);;//Comments added by Dhruv Sharma. Fix for image upload issue.
            typedQuery = (TypedQuery<Image>) em.createQuery("SELECT i from Image i where i.title =:title and i.id =:id", Image.class); //Added by Dhruv Sharma. Fix for image upload issue.
            //Start: Added by Dhruv Sharma. Fix for image upload issue.
            typedQuery.setParameter("title",title);
            typedQuery.setParameter("id",id);
            //End: Added by Dhruv Sharma. Fix for image upload issue.
            return typedQuery.getSingleResult();
        } catch (NoResultException nre) {
           return null;
        }
    }

    //The method creates an instance of EntityManager
    //Executes JPQL query to fetch the image from the database with corresponding id
    //Returns the image fetched from the database
    public Image getImage(Integer imageId) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Image> typedQuery = null;
        Image image = null;
        try {
             typedQuery = em.createQuery("SELECT i from Image i where i.id =:imageId", Image.class).setParameter("imageId", imageId);
             image = typedQuery.getSingleResult();

        }catch(Exception ex){
         System.out.println("Exception: in getImage() "+ex.toString());
        }finally{
            typedQuery = null;
            em.close();
        }
        return image;
    }

    //The method receives the Image object to be updated in the database
    //Creates an instance of EntityManager
    //Starts a transaction
    //The transaction is committed if it is successful
    //The transaction is rolled back in case of unsuccessful transaction
    public void updateImage(Image updatedImage) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            em.merge(updatedImage);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
    }

    //The method receives the Image id of the image to be deleted in the database
    //Creates an instance of EntityManager
    //Starts a transaction
    //Get the image with corresponding image id from the database
    //This changes the state of the image model from detached state to persistent state, which is very essential to use the remove() method
    //If you use remove() method on the object which is not in persistent state, an exception is thrown
    //The transaction is committed if it is successful
    //The transaction is rolled back in case of unsuccessful transaction
    public void deleteImage(Integer imageId) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            Image image = em.find(Image.class, imageId);
            em.remove(image);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
    }

    // Start: Added by Dhruv Sharma. Bug Fix: Owner of the image can edit/delete the image.
    public boolean validateUser(Integer loggedUserId, Integer imageId){
        EntityManager em = emf.createEntityManager();
        TypedQuery<Image> typedQuery = null;
        Image image = null;
        Integer imageUserId = null;
        boolean bool = false;
        try {
            //Retrieve the image based upon the image id.
            typedQuery = em.createQuery("SELECT i from Image i where i.id = :imageId", Image.class).setParameter("imageId", imageId);
            image = typedQuery.getSingleResult();
            imageUserId = (Integer) image.getUser().getId();
            //Compare the user id for the image and the logged in user and in case of a match, true is returned to the controller.
            if(loggedUserId == imageUserId){ bool = true;}
        }catch(Exception ex){
         System.out.println("Exception: "+ex);  //Echo the exception to the console in case of an undesirable experience.
        }finally{
            typedQuery = null;
            em.close();
        }
        return bool;
    }
    // End: Added by Dhruv Sharma. Bug Fix: Owner of the image can edit/delete the image.

}
