<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="createForm"/>
	<title>Alta de modificación</title>
	<g:set var="entity" value="${modificacionInstance}" scope="request" />
	<g:set var="entityName" value="Modificacion Salarial" scope="request" />
	<g:set var="editable" value="${true}" scope="request" />
	<g:set var="imprimible" value="${false}" scope="request" />
</head>
<body>
		<content tag="formTitle">
			
		</content>
		<content tag="formFields">
			<g:render template="createForm"/>
		</content>
	
</body>
</html>