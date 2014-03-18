Tutorial: JOSS Streaming
========================

This is a working project to demonstrate JOSS for streaming content.

It is built using Spring MVC, and features 3 use cases:

1. Streaming content from storage to the browser
2. Streaming content from the browser to storage
3. Generating content, e.g. by creating thumbnails from previously uploaded files


To see this project in action:
------------------------------

1. Checkout the code
2. Put your account information in src/main/resources/application-context.xml, in the cloudConfig bean
3. Run this command (it starts a container in the foreground):
    mvn tomcat7:run
4. Point your browser to [http://localhost:8080/](http://localhost:8080/)


To see how you can do all this yourself, have a look at the class `org.javaswift.joss.tutorial.StreamingController`.
