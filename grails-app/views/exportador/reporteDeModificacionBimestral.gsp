<html>
<head>
<meta charset="UTF-8">
<meta name="layout" content="exportadores"/>
<title>Reporte Modificacion Bimestral de Salario SUA</title>
</head>
<body>

	<content tag="reporteTitle">
		Reporte de Modificacion Bimestral de Salario SUA
	</content>
	<content tag="reportForm">
		<g:hasErrors bean="${reportCommand}">
            <div class="alert alert-danger">
                <g:renderErrors bean="${reportCommand}" as="list" />
            </div>
        </g:hasErrors>
		<div class="col-sm-6">
		
		<g:form action="reportePorBimestre" class="form-horizontal">
			<g:hiddenField name="reportName" value="ModificacionBimestral"/>
			<fieldset>
				<legend> Parámetros</legend>
				<f:with bean="${reportCommand}">
					<f:field property="ejercicio" widget-class="form-control" wrapper="bootstrap3"/>
					<f:field property="bimestre" widget-class="form-control" wrapper="bootstrap3"/>
					<f:field property="tipo" widget-class="form-control" wrapper="bootstrap3"/>
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
		</div>
	</content>
	
</body>

</html>