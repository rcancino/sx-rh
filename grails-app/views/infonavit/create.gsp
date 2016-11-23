<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="createForm"/>
	<g:set var="entity" value="${infonavitInstance}" scope="request" />
	<g:set var="editable" value="${false}" scope="request" />
	<g:set var="imprimible" value="${false}" scope="request" />
	<title>INFONAVIT (alta)</title>
	
</head>
<body>
	<content tag="header">
		Alta de crédito INFONAVIT
	</content>
	
	<content tag="formTitle">
		Crédito INFONAVIT
	</content>
	
	<content tag="formFields">
		<f:with bean="${infonavitInstance}">
			<f:field property="empleado" />
			<f:field property="alta" />
			<f:field property="numeroDeCredito" widget-class="form-control"/>
			<f:field property="tipo" widget-class="form-control chosen-select"/>
			<f:field property="cuotaFija" widget="numeric" label="Descuento"/>
			<f:field property="comentario" widget-class="form-control"/>
		</f:with>
	</content>
	
</body>
</html>