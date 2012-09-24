<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <c:set var="path" value="${pageContext.request.contextPath}"/>
    <link rel="icon" href="${path}/images/favicon.ico" type="image/x-icon"/>
    <link rel="shortcut icon" href="${path}/images/favicon.ico" type="image/x-icon"/>
    <title>JOSS Tutorial - Streaming</title>
</head>
<body>

<h1>JOSS Tutorial - Streaming</h1>


<h2>Use case 1: downloading content</h2>

<p>
    This use case is about streaming content from the OpenStack Storage to the browser via your application. Although this is not a sensible approach for
    public containers (you can then simply point the browser to the Storage directly), it <em>is</em> a common scenario for private containers. There are two
    important reasons for this:
</p>
<ol>
    <li>The security model is quite coarse grained: permissions are only set on a container.</li>
    <li>Accessing a private contaier required an access token, which is always valid for 24 hours. You want to keep that private.</li>
</ol>
<p>
    Start this use case (i.e. download a file) by clicking this link:
    <a href="${path}/download">.../download</a>
</p>


<h2>Use case 2: uploading content</h2>

<p>
    [description of the use case]
</p>
<form method="post" action="${path}/upload" enctype="multipart/form-data">
    <p>
        Start this use case submitting a file with the following form:

        <label>File: <input type="file" name="file" size="60"/></label><br/>
        <input type="submit" value="Upload"/>
    </p>
</form>


<h2>Use case 3: generating content</h2>

<p>
    [description of the use case]
</p>
<form method="post" action="${path}/generate">
    <p>
        This use case can be started any way you like. Here, we use simple two-button form.

        <button type="submit" name="action" value="generate">Generate</button>
        &nbsp;&nbsp;&nbsp;
        <button type="submit" name="action" value="clear">Clear</button>
    </p>
</form>

</body>
</html>
