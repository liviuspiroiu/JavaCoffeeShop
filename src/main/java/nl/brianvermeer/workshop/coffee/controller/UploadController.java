package nl.brianvermeer.workshop.coffee.controller;

import nl.brianvermeer.workshop.coffee.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;

@Controller
public class UploadController {

    @Autowired
    private PersonService personService;

    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";

    @GetMapping("/uploadimage")
    public String displayUploadForm() {
        return "person/upload";
    }

    @PostMapping("/uploadimage")
    public String uploadImage(Model model, @RequestParam("multipart") MultipartFile multipart, Principal principal) throws IOException {
        var name = multipart.getOriginalFilename().replace(" ", "_");
        var fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, name);

        final File file = fileNameAndPath.toFile();
        if (!file.getCanonicalPath().startsWith(UPLOAD_DIRECTORY))  {
            throw new IOException("Could not upload image");
        }

        Files.write(fileNameAndPath, multipart.getBytes());
        model.addAttribute("msg", "Uploaded images: " + name);

        if (principal == null) {
            model.addAttribute("message", "ERROR");
            return "person/upload";
        }

        var user = principal.getName();
        var person = personService.findByUsername(user);

        person.setProfilePic(name);
        personService.savePerson(person);
        return "person/upload";
    }
}
