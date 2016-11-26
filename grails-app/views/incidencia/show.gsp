<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="showForm"/>
	<g:set var="entity" value="${incidenciaInstance}" scope="request" />
	<g:set var="entityName" value="Incidencia" scope="request" />
	<g:set var="editable" value="${true}" scope="request" />
	<g:set var="imprimible" value="${false}" scope="request" />
	<g:set var="delete" value="${true}" scope="request" />
	<title>Incidencia ${incidenciaInstance.id} </title>
</head>
<body>

<content tag="header">
	Incidencia de: ${incidenciaInstance.empleado}
</content>



<content tag="form">
	<f:with bean="incidenciaInstance">
		<f:display property="empleado" />
		<f:display property="fechaInicial" />
		<f:display property="fechaFinal" />
		<f:display property="tipo" widget-class="form-control" />
		<f:display property="pagado" widget-class="form-control" label="Con goce"/>
		<f:display property="comentario" widget-class="form-control" />
	</f:with>
</content>

	
	
</body>
</html>