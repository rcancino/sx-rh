<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="createForm"/>
	title>Alta de calendario</title>
	<g:set var="entity" value="${calendarioInstance}" scope="request" />
	<g:set var="editable" value="${true}" scope="request" />
	<g:set var="imprimible" value="${false}" scope="request" />
</head>
<body>

<content tag="header">
	Alta de calendario (Ejercicio${session.ejercicio })
</content>

<content tag="formFields">
	<f:with bean="calendarioInstance">
		<f:field property="ejercicio" widget-class="form-control" />
		<f:field property="tipo" widget-class="form-control" />
		<f:field property="comentario" widget-class="form-control" />
	</f:with>
</content>
	
</body>
</html>
