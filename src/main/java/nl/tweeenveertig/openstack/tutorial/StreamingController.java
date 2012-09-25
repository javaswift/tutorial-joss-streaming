package nl.tweeenveertig.openstack.tutorial;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import nl.tweeenveertig.openstack.client.Account;
import nl.tweeenveertig.openstack.client.Container;
import nl.tweeenveertig.openstack.client.StoredObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

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
    public static final String DEFAULT_BINARY_CONTENT_TYPE = "application/octet-stream";
    /**
     * The storage provider we'll use. This logs in and provides us with the logged in account.
     */
    private StorageProvider storageProvider;

    /**
     * Create a streaming controller.
     *
     * @param storageProvider the storage provider to use
     */
    @Autowired
    public StreamingController(StorageProvider storageProvider) {

        this.storageProvider = storageProvider;
    }

    /**
     * Show the index page.
     *
     * @return a view for the index page
     */
    @RequestMapping("/")
    public ModelAndView showIndexPage() {

        Container container = getTutorialContainer();
        StoredObject useCase2Object = container.getObject(StorageProvider.USE_CASE_2_OBJECT);
        StoredObject useCase3Object1 = container.getObject(StorageProvider.USE_CASE_3_DEST_OBJECT_1);
        StoredObject useCase3Object2 = container.getObject(StorageProvider.USE_CASE_3_DEST_OBJECT_2);

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("useCase2_ObjectExists", useCase2Object.exists());
        model.put("useCase3_Object1Exists", useCase3Object1.exists());
        model.put("useCase3_Object2Exists", useCase3Object2.exists());

        return new ModelAndView("index", model);
    }

    private Container getTutorialContainer() {

        Account account = storageProvider.getAccount();
        return account.getContainer(StorageProvider.TUTORIAL_CONTAINER);
    }

    /**
     * Use case 1: stream content from storage to the browser. This method also helps for the other use cases.
     *
     * @param objectName the name of the object to download
     * @param response   the response to send the data to
     * @throws IOException when streaming the content fails
     */
    @RequestMapping("/download/{objectName:.+}")
    public void downloadContent(@PathVariable String objectName, HttpServletResponse response) throws IOException {

        // Get the object to download.

        Container container = getTutorialContainer();
        StoredObject storedObject = container.getObject(objectName);

        if (storedObject.exists()) {

            streamObject(storedObject, response);
        } else {

            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void streamObject(StoredObject storedObject, HttpServletResponse response) throws IOException {

        // Get the content type and the data stream.

        String contentType = storedObject.getContentType();
        if (contentType == null) {
            contentType = DEFAULT_BINARY_CONTENT_TYPE;
        }
        InputStream dataStream = storedObject.downloadObjectAsInputStream();

        // Stream the data.

        OutputStream responseStream = null;
        try {

            response.setContentType(contentType);
            responseStream = response.getOutputStream();
            FileCopyUtils.copy(dataStream, responseStream);
        } finally {

            if (responseStream != null) {

                responseStream.close();
            }
        }
    }

    /**
     * Use case 2: stream content from the browser to storage.
     *
     * @param file the file to stream
     * @return a redirect to the index page
     */
    @RequestMapping("/upload")
    public View uploadContent(@RequestParam MultipartFile file) throws IOException {

        Container container = getTutorialContainer();

        StoredObject newUpload = container.getObject(StorageProvider.USE_CASE_2_OBJECT);
        storageProvider.streamUpload(newUpload, file.getInputStream());

        return redirectToIndexPage();
    }

    private View redirectToIndexPage() {

        return new RedirectView("/", true);
    }

    /**
     * Use case 3: generate content. This is the mean and bones of the use case.
     *
     * @return a redirect to the index page
     */
    @RequestMapping(value = "/generate")
    public View generateContent() throws IOException {

        Container container = getTutorialContainer();

        // Load the original image.

        StoredObject source = container.getObject(StorageProvider.USE_CASE_3_SRC_OBJECT);
        InputStream readingInputStream = source.downloadObjectAsInputStream();
        BufferedImage originalImage = ImageIO.read(readingInputStream);

        // Store as-is as first generated image.

        StoredObject dest1 = container.getObject(StorageProvider.USE_CASE_3_DEST_OBJECT_1);
        InputStream uploadSourceStream1 = createInputStream(originalImage);
        storageProvider.streamUpload(dest1, uploadSourceStream1);

        // Generate a thumbnail from it and store that as second generated image.

        BufferedImage resizedImage = ImageUtils.scaleImage(originalImage, 200, 200);

        StoredObject dest2 = container.getObject(StorageProvider.USE_CASE_3_DEST_OBJECT_2);
        InputStream uploadSourceStream2 = createInputStream(resizedImage);
        storageProvider.streamUpload(dest2, uploadSourceStream2);

        return redirectToIndexPage();
    }

    private InputStream createInputStream(BufferedImage image) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);

        return new ByteArrayInputStream(baos.toByteArray());
    }

    /**
     * Use case 3: clear the generated content. This is a helper method.
     *
     * @return a redirect to the index page
     */
    @RequestMapping(value = "/reset")
    public View clearGeneratedContent() {

        Container container = getTutorialContainer();
        deleteIfPresent(container, StorageProvider.USE_CASE_2_OBJECT);
        deleteIfPresent(container, StorageProvider.USE_CASE_3_DEST_OBJECT_1);
        deleteIfPresent(container, StorageProvider.USE_CASE_3_DEST_OBJECT_2);

        return redirectToIndexPage();
    }

    private void deleteIfPresent(Container container, String objectName) {

        StoredObject object1 = container.getObject(objectName);
        if (object1.exists()) {
            object1.delete();
        }
    }
}
