<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="createForm"/>
	<g:set var="entity" value="${conceptoInstance}" scope="request" />
	<g:set var="entityName" value="Incidencia" scope="request" />
	<g:set var="editable" value="${false}" scope="request" />
	<g:set var="imprimible" value="${false}" scope="request" />
	<title>Alta  de concepto</title>
</head>
<body>

<content tag="header">
	Alta de incidencia
</content>



<content tag="formFields">
	<f:with bean="conceptoInstance">
		<f:field property="clave" widget-class="form-control" />
		<f:field property="descripcion" widget-class="form-control" />
		<f:field property="clase" widget-class="form-control" />
		<f:field property="claveSat" widget-class="form-control" />
		<f:field property="tipo" widget-class="form-control" />
		<f:field property="general" widget-class="form-control" />
	</f:with>
</content>

	
