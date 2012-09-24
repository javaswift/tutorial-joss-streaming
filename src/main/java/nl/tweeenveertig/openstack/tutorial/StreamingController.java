package nl.tweeenveertig.openstack.tutorial;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller to support streaming from &amp; to JOSS.
 *
 * @author <a href="mailto:oscar.westra@42.nl">Oscar Westra van Holthe - Kind</a>
 */
@Controller
public class StreamingController {

    /**
     * Content type to use when none is given.
     */
    static final String DEFAULT_BINARY_CONTENT_TYPE = "application/octet-stream";

    /**
     * Use case 1: stream content from storage to the browser.
     *
     * @param response the response to send the data to
     * @throws IOException when streaming the content fails
     */
    @RequestMapping("download")
    public void downloadContent(HttpServletResponse response) throws IOException {

        throw new UnsupportedOperationException("This operation is not yet implemented.");
    }

    /**
     * Use case 2: stream content from the browser to storage.
     *
     * @param file the file to stream
     * @return the index page
     */
    @RequestMapping("upload")
    public ModelAndView uploadContent(@RequestParam MultipartFile file) {

        throw new UnsupportedOperationException("This operation is not yet implemented.");
    }

    /**
     * Use case 3: generate content. This is the mean and bones of the use case.
     *
     * @return the index page
     */
    @RequestMapping(value = "generate", params = { "action=generate" })
    public ModelAndView generateContent() {

        throw new UnsupportedOperationException("This operation is not yet implemented.");
    }

    /**
     * Use case 3: clear the generated content. This is a helper method.
     *
     * @return the index page
     */
    @RequestMapping(value = "generate", params = { "action=clear" })
    public ModelAndView clearGeneratedContent() {

        throw new UnsupportedOperationException("This operation is not yet implemented.");
    }
}
