<html>
<head>
<meta charset="UTF-8">
<meta name="layout" content="reportes"/>
<g:set var="entity" value="${reportCommand}" scope="request" />
<g:set var="entityName" value="Impuesto sobre nominas" scope="request" />
<g:set var="action" value="impuestoSobreNominas" scope="request" />
<title>Impuesto sobre nóminas</title>
</head>
<body>

	<content tag="reporteTitle">
		3% Impuesto sobre nóminas
	</content>

	<content tag="formFields">
		<f:with bean="${reportCommand}">
			<f:field property="ejercicio" widget-class="form-control chosen-select"/>
			<f:field property="mes" widget-class="form-control chosen-select"/>
		</f:with>
	</content>
	
</body>

</html>