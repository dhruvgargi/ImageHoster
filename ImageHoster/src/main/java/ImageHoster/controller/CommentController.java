package ImageHoster.controller;
/*
---------------------------------------------------------------------------------------------------------------------------------------
 Version         Modification Date                Developer                Modifications
---------------------------------------------------------------------------------------------------------------------------------------
 *@ 1.0.0.1         30-Dec-2018                  Dhruv Sharma              Functionality Upgrade: Users can add comments for any image.
*/
import ImageHoster.model.Comments;
import ImageHoster.model.User;
import ImageHoster.service.CommentService;
import ImageHoster.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class CommentController {

    @Autowired
    ImageService imageService;

    @Autowired
    CommentService commentService;

    @RequestMapping(value = "/image/{imageId}/{imageTitle}/comment",method = RequestMethod.POST)
    public String addComments(Model model, @PathVariable("imageId") Integer imageId, @PathVariable("imageTitle") String imageTitle, Comments comments, @RequestParam("comment") String comment, HttpSession session){
        User user = (User) session.getAttribute("loggeduser");
        comments.setimage(imageService.getImage(imageId));
        comments.setText(comment);
        comments.setCreatedDate(new Date());
        comments.setUser(user);
        commentService.addComments(comments);
        model.addAttribute("comments",comments);
        return "redirect:/images"+"/"+imageId+"/"+imageTitle;
    }
}
