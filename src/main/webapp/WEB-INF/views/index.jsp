<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <c:set var="path" value="${pageContext.request.contextPath}"/>
    <style type="text/css">
        body {
            font-size: .9em;
            margin: 0 auto;
            max-width: 60em;
        }

        section {
            border: 2px groove gray;
            margin-bottom: 2em;
            padding: 1em;
        }

        section h1 {
            margin-top: 0;
            padding-top: 0;
            font-size: 1.5em;
        }

        section p.footnote {
            border-top: thin dashed gray;
            margin-top: 2em;
            padding-top: 1em;
            font-size: 80%;
        }
    </style>
    <link rel="icon" href="${path}/images/favicon.ico" type="image/x-icon"/>
    <title>JOSS Tutorial - Streaming</title>
</head>
<body>

<h1>JOSS Tutorial - Streaming</h1>

<section id="usecase1">
    <h1>Use case 1: downloading content</h1>
    <p>
        This use case is about streaming content from the OpenStack Storage to the browser via your application. Although this is not a sensible approach for
        public containers &mdash; you can then simply point the browser to the Storage directly &mdash; it <em>is</em> a common scenario for private containers.
    </p>
    <p>
        As things stand now, there is no way to get an access token that is authorized only to read a specific container. If you have a token, you can do
        anything. Even remove all containers. Needless to say, your token may not leave your application. Thus this use case.
    </p>
    <p>
        Start this use case (i.e. download a file) by clicking this link:
        <a href="${path}/download/testObject1">download</a>
    </p>
</section>

<section id="usecase2">
    <h1>Use case 2: uploading content</h1>
    <p>
        You cannot let a browser access stored content, nor can you stream content from storage, if there is no content. This use case is about the simple case
        of providing content: streaming files (unchanged) directly into storage.
    </p>
    <p>
        Start this use case by submitting a file with the following form:
    </p>

    <form method="post" action="${path}/upload" enctype="multipart/form-data">
        <p>
            <label>File: <input type="file" name="file" size="60"/></label> <input type="submit" value="Upload"/>
        </p>
    </form>

    <%--@elvariable id="useCase2_ObjectExists" type="java.lang.Boolean"--%>
    <c:if test="${useCase2_ObjectExists}">
        <ul>
            <li>View the uploaded object: <a href="${path}/download/testObject2">download</a></li>
        </ul>
    </c:if>

    <p class="footnote">
        There are two ways to get the file type attached to the object in our cloud storage: by file extension or manually. This tutorial uses the manual
        method, by using &quot;MIME magic&quot;, i.e. pattern matching on the first 32 bytes of the file. For more information in the various options, you can
        read <a href="http://www.rgagnon.com/javadetails/java-0487.html">Real's Howto</a>.
    </p>
</section>

<section id="usecase3">
    <h1>Use case 3: generating content</h1>
    <p>
        Sometimes, you want more than just pass along files. For example, you may want to generate thumbnails. For such a case it's usually a good idea to
        store images in a queue somewhere (this may be a database). You can then retrieve and process them at your leasure. This use case demonstrates this for
        a single image.
    </p>

    <form method="post" action="${path}/generate">
        <p>
            This use case can be started any way you like. Here, we use a button: <input type="submit" value="Generate"/>
        </p>
        <%--@elvariable id="useCase3_Object1Exists" type="java.lang.Boolean"--%>
        <%--@elvariable id="useCase3_Object2Exists" type="java.lang.Boolean"--%>
        <ul>
            <li>View the source object: <a href="${path}/download/testObject3-i">download</a></li>
            <c:if test="${useCase3_Object1Exists}">
                <li>View the first generated object: <a href="${path}/download/testObject3-o1">download</a></li>
            </c:if>
            <c:if test="${useCase3_Object2Exists}">
                <li>View the second generated object: <a href="${path}/download/testObject3-o2">download</a></li>
            </c:if>
        </ul>
    </form>
</section>

<section id="reset">
    <h1>Reset Data</h1>

    <form method="post" action="${path}/reset">
        <p>
            This is not a use case, but a button to clear all uploaded data: <input type="submit" value="Reset"/>
        </p>
    </form>
</section>

</body>
</html>
