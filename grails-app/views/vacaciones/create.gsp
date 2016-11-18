<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="createForm"/>
	<title>Vacaciones</title>
	<g:set var="entity" value="${vacacionesInstance}" scope="request" />
	<g:set var="editable" value="${true}" scope="request" />
	<g:set var="imprimible" value="${false}" scope="request" />
</head>
<body>

<content tag="header">
	Solicitud de vacaciones
</content>

<content tag="formFields">
	<f:with bean="vacacionesInstance">
		<f:field property="empleado" />
		<f:field property="comentario" widget-class="form-control" />
	</f:with>
</content>
	
</body>
</html>