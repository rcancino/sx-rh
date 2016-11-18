<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="showForm"/>
	<title>Operacion Generica ${operacionGenericaInstance.id}</title>
	<g:set var="entity" value="${operacionGenericaInstance}" scope="request" />
	<g:set var="editable" value="${false}" scope="request" />
	<g:set var="imprimible" value="${false}" scope="request" />
</head>
<body>

<content tag="header">
	Operación genérica ${operacionGenericaInstance.id}  (${operacionGenericaInstance.empleado})
</content>
	
<content tag="form">
	<f:with bean="${operacionGenericaInstance }">
		<f:display property="tipo" widget-class="form-control"/>
		<f:display property="concepto" widget-class="form-control"/>
		<f:display property="importeGravado" widget-class="form-control" input-type="text"/>
		<f:display property="importeExcento" widget-class="form-control" input-type="text"/>
		<f:display property="calendarioDet" widget-class="form-control"/>
		<f:display property="comentario" widget-class="form-control"/>
	</f:with>
</content>
	
</body>
</html>