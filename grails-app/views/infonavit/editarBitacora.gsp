<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="editForm"/>
	<g:set var="entity" value="${infonavitInstance}" scope="request" />
	<g:set var="editable" value="${false}" scope="request" />
	<g:set var="imprimible" value="${false}" scope="request" />
	<g:set var="action" value="updateBitacora" scope="request" />
	<title>INFONAVIT ${infonavitInstance.id }</title>
</head>
<body>
	<content tag="header">
		CréditoINFONAVIT de: ${infonavitInstance.empleado}
	</content>
	
	<content tag="formTitle">
		Crédito INFONAVIT
	</content>
	
	<content tag="formFields">
		<f:with bean="${infonavitInstance}">
			<f:display property="alta" />
			<f:display property="numeroDeCredito" widget-class="form-control" />
			<f:display property="tipo" widget-class="form-control" />
			<f:display property="cuotaFija"  label="Descuento" />
			<f:field property="activo"  widget-class="form-control " />
			<f:field property="suspension" widget-type="text" widget-class="form-control " />
			<f:field property="reinicio" widget-type="text" widget-class="form-control " />
			<f:field property="modificacionTipo" widget-type="text" widget-class="form-control " />
			<f:field property="modificacionValor" widget-type="text" widget-class="form-control " />
			<f:field property="modificacionNumero" widget-type="text" widget-class="form-control " />
			<f:field property="comentario" widget-class="form-control"/>
		</f:with>
	</content>
	
</body>
</html>