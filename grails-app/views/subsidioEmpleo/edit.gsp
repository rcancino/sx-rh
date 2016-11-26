<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="editForm"/>
	<g:set var="entity" value="${subsidioEmpleoInstance}" scope="request" />
	<g:set var="entityName" value="Incidencia" scope="request" />
	<g:set var="editable" value="${true}" scope="request" />
	<g:set var="imprimible" value="${false}" scope="request" />
	<g:set var="delete" value="${true}" scope="request" />
	<title>Subsidio al empleo ${entity.id}</title>
</head>
<body>

<content tag="header">
	Tarifa ISR : ${subsidioEmpleoInstance.id}
</content>



<content tag="formFields">
	<f:with bean="subsidioEmpleoInstance">
		<f:field property="ejercicio" widget-class="form-control"/>
		<f:field property="desde" widget="numeric"/>
		<f:field property="hasta" widget="numeric"/>
		<f:field property="subsidio" widget="numeric"/>
	</f:with>
</content>

	
	
</body>
</html>
