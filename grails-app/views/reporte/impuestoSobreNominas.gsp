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

	<content tag="reportForm">
		<g:form action="impuestoSobreNominas" class="form-horizontal">
			<fieldset>
				<legend> Parámetros</legend>
				<f:with bean="${reportCommand}">
					<f:field property="ejercicio" widget-class="form-control" value="${session.ejercicio}"/>
					<f:field property="mes" widget-class="form-control"/>
				</f:with>
			</fieldset>
			<div class="form-group">
		    	<div class="col-sm-offset-2 col-sm-3">
		      		<button type="submit" class="btn btn-default">
		      			<span class="glyphicon glyphicon-cog"></span> Ejecutar
		      		</button>
		    	</div>
		  	</div>
		</g:form>
	</content>
	
</body>

</html>