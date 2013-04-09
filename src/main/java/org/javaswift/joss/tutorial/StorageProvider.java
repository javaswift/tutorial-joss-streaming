package org.javaswift.joss.tutorial;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.ResourceBundle;

import eu.medsea.mimeutil.MimeUtil;
import org.javaswift.joss.client.factory.AccountConfig;
import org.javaswift.joss.client.factory.AccountFactory;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.StoredObject;
import org.springframework.stereotype.Service;

/**
 * Service to login to our cloud storage and provide the logged in account.
 *
 * @author <a href="mailto:oscar.westra@42.nl">Oscar Westra van Holthe - Kind</a>
 */
@Service
public class StorageProvider {

    /**
     * The name of the container we'll use for this tutorial.
     */
    public static final String TUTORIAL_CONTAINER = "tutorial-joss-streaming";
    /**
     * The name of the resource to upload for use case 1.
     */
    public static final String USE_CASE_1_RESOURCE = "/Cloud-Computing.jpg";
    /**
     * The name of the stored object for use case 1.
     */
    public static final String USE_CASE_1_OBJECT = "testObject1";
    /**
     * The name of the stored object for use case 2.
     */
    public static final String USE_CASE_2_OBJECT = "testObject2";
    /**
     * The name of the source object for use case 3.
     */
    public static final String USE_CASE_3_SRC_OBJECT = "testObject3-i";
    /**
     * The name of the first destination object for use case 3.
     */
    public static final String USE_CASE_3_DEST_OBJECT_1 = "testObject3-o1";
    /**
     * The name of the second destination object for use case 3.
     */
    public static final String USE_CASE_3_DEST_OBJECT_2 = "testObject3-o2";
    /**
     * Buffer size for &quot;MIME magic&quot;, i.e. determining the actual MIME type of the content.
     */
    public static final int MIME_MAGIC_BUFFER_SIZE = 32;
    /**
     * The account (logged in), lazily initialized.
     */
    private Account account;

    /**
     * Create a {@code StorageProvider}.
     */
    public StorageProvider() {

        MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
    }

    /**
     * Get our cloud storage account.
     *
     * @return our account
     */
    public Account getAccount() {

        if (account == null) {

            ResourceBundle credentials = ResourceBundle.getBundle("credentials");
            account = new AccountFactory()
                    .setTenant(credentials.getString("tenant"))
                    .setUsername(credentials.getString("username"))
                    .setPassword(credentials.getString("password"))
                    .setAuthUrl(credentials.getString("auth_url"))
                    .setMock(true)
                    .createAccount();
        }
        return account;
    }

    /**
     * Stream a file to a {@code StoredObject}. Also sets the content type based upon the stream.
     *
     * @param storedObject the stored object to upload to
     * @param stream       the data stream tp upload
     */
    public void streamUpload(StoredObject storedObject, InputStream stream) throws IOException {

        // A content type can be determined from the first 32 bytes. Get them while ensuring the InputStream remains complete.

        byte[] mimeMagicBuffer = new byte[MIME_MAGIC_BUFFER_SIZE];
        PushbackInputStream pushbackInputStream = new PushbackInputStream(stream, MIME_MAGIC_BUFFER_SIZE);

        int magicBytesRead = pushbackInputStream.read(mimeMagicBuffer);
        pushbackInputStream.unread(mimeMagicBuffer, 0, magicBytesRead);

        // Determine the MIME type.

        String contentType = MimeUtil.getMostSpecificMimeType(MimeUtil.getMimeTypes(mimeMagicBuffer)).toString();

        // Do the actual upload.

        storedObject.uploadObject(pushbackInputStream);
        storedObject.setContentType(contentType);
    }
}
