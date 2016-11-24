<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="createForm"/>
	<title>Vacaciones grupo</title>
	<g:set var="entity" value="${vacacionesGrupoInstance}" scope="request" />
	<g:set var="editable" value="${true}" scope="request" />
	<g:set var="imprimible" value="${false}" scope="request" />
</head>
<body>

<content tag="header">
	Solicitud de vacaciones en grupo
</content>

<content tag="formFields">
	<f:with bean="vacacionesGrupoInstance">
		<f:field property="comentario" widget-class="form-control" />
		<f:field property="fechaInicial" />
		<f:field property="fechaFinal" />
		<f:field property="calendarioDet" />
	</f:with>
</content>
	
</body>
</html>