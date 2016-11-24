<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="editForm"/>
	<g:set var="entity" value="${incapacidadInstance}" scope="request" />
	<g:set var="entityName" value="Incapacidad" scope="request" />
	<g:set var="editable" value="${false}" scope="request" />
	<g:set var="imprimible" value="${false}" scope="request" />
	<title>Incapacidad ${incapacidadInstance.id} </title>
</head>
<body>

<content tag="header">
	Incapacidades de: ${incapacidadInstance.empleado}
</content>



<content tag="formFields">
	<f:with bean="incapacidadInstance">
		<f:field property="empleado" />
		<f:field property="referenciaImms" widget-class="form-control" />
		<f:field property="tipo" widget-class="form-control" />
		<f:field property="fechaInicial" />
		<f:field property="fechaFinal" />
		<f:field property="comentario" widget-class="form-control" />
		<f:field property="porcentaje" widget="porcentaje" />
		<f:field property="tipoRiesgo" widget-class="form-control" />
		<f:field property="secuela" widget-class="form-control" />
		<f:field property="control" widget-class="form-control" />
	</f:with>
</content>

	
	
</body>
</html>