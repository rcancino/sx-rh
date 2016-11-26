<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="editForm"/>
	<g:set var="entity" value="${incidenciaInstance}" scope="request" />
	<g:set var="entityName" value="Incapacidad" scope="request" />
	<g:set var="editable" value="${false}" scope="request" />
	<g:set var="imprimible" value="${false}" scope="request" />
	<title>Incidencia ${incidenciaInstance.id} </title>
</head>
<body>

<content tag="header">
	Incidencia de: ${incidenciaInstance.empleado}
</content>



<content tag="formFields">
	<f:with bean="incidenciaInstance">
		<f:field property="empleado" />
		<f:field property="fechaInicial" />
		<f:field property="fechaFinal" />
		<f:field property="tipo" widget-class="form-control" />
		<f:field property="pagado" widget-class="form-control" label="Con goce"/>
		<f:field property="comentario" widget-class="form-control" />
	</f:with>
</content>

	
	
</body>
</html>