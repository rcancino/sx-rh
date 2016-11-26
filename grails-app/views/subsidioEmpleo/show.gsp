<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="showForm"/>
	<g:set var="entity" value="${subsidioEmpleoInstance}" scope="request" />
	<g:set var="entityName" value="Incidencia" scope="request" />
	<g:set var="editable" value="${true}" scope="request" />
	<g:set var="imprimible" value="${false}" scope="request" />
	<g:set var="delete" value="${true}" scope="request" />
	<title>Subsidio al empleo</title>
</head>
<body>

<content tag="header">
	Tarifa ISR : ${subsidioEmpleoInstance.id}
</content>



<content tag="form">
	<f:with bean="subsidioEmpleoInstance">
		<f:display property="ejercicio" />
		<f:display property="desde" />
		<f:display property="hasta" />
		<f:display property="subsidio" />
	</f:with>
</content>

	
	
</body>
</html>
