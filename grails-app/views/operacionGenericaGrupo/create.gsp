<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="createForm"/>
	<title>Alta de Operacion Generica en grupo</title>
	<g:set var="entity" value="${operacionGenericaGrupoInstance}" scope="request" />
	<g:set var="editable" value="${false}" scope="request" />
	<g:set var="imprimible" value="${false}" scope="request" />
</head>
<body>

	<content tag="header">
		Alta de operaciones genéricas en grupo
	</content>
	
	<!-- <content tag="formTitle">Empleado</content> -->
	
	<content tag="formFields">
		<f:with bean="${operacionGenericaGrupoInstance }">
			<f:field property="tipo" widget-class="form-control"/>
			<f:field property="concepto" widget-class="form-control"/>
			<f:field property="importeGravado" widget-class="form-control money" widget-type="text"/>
			<f:field property="importeExcento" widget-class="form-control money" widget-type="text"/>
			<f:field property="calendarioDet" />
			<f:field property="comentario" widget-class="form-control"/>
		</f:with>
	</content>
	
</body>
</html>