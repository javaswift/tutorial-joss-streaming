Tutorial: JOSS Streaming
========================

This is a working project to demonstrate JOSS for streaming content.

It is built using Spring MVC, and features 3 use cases:

1. Streaming content from storage to the browser
2. Streaming content from the browser to storage
3. Generating content, e.g. by creaeting thumbnails from previously uploaded files


To see this project in action:
------------------------------

1. Checkout the code
2. Run this command (it starts a container in the foreground):
    mvn jetty:run
3. Point your browser to [http://localhost:8081/](http://localhost:8081/)


To see how you can do all this yourself, have a look at the class `nl.tweeenveertig.openstack.tutorial.StreamingController`.
