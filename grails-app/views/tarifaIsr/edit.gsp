<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="editForm"/>
	<g:set var="entity" value="${tarifaIsrInstance}" scope="request" />
	<g:set var="entityName" value="Incidencia" scope="request" />
	<g:set var="editable" value="${true}" scope="request" />
	<g:set var="imprimible" value="${false}" scope="request" />
	<g:set var="delete" value="${true}" scope="request" />
	<title>Tarifa ISR</title>
</head>
<body>

<content tag="header">
	Tarifa ISR : ${tarifaIsrInstance.id}
</content>

<content tag="formFields">
	<f:with bean="tarifaIsrInstance">
		<f:field property="ejercicio" widget-class="form-control" wrapper="bootstrap3"/>
		<f:field property="tipo" widget-class="form-control" wrapper="bootstrap3"/>
		<f:field property="limiteInferior" widget="numeric"  wrapper="bootstrap3"/>
		<f:field property="limiteSuperior" widget="numeric" wrapper="bootstrap3"/>
		<f:field property="cuotaFija" widget="numeric" wrapper="bootstrap3"/>
		<f:field property="porcentaje" widget="numeric" wrapper="bootstrap3"/>
	</f:with>
</content>

	
	
</body>
</html>