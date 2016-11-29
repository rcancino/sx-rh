<html>
<head>
<meta charset="UTF-8">
<meta name="layout" content="reportes"/>
<g:set var="entity" value="${reportCommand}" scope="request" />
<g:set var="entityName" value="Acu concepto" scope="request" />
<g:set var="action" value="acumuladoDeNominasPorConcepto" scope="request" />
<title>Detalle por concepto</title>
</head>
<body>

	<content tag="reporteTitle">
		Detalle por concepto de nómina
	</content>
	<content tag="reportForm">
		<g:hiddenField name="reportName" value="AcumuladoDeNominasPorConcepto"/>
		<g:form action="acumuladoDeNominasPorConcepto" class="form-horizontal">
			<g:hiddenField name="reportName" value="AcumuladoDeNominasPorConcepto"/>
			<fieldset>
				<legend> Parámetros</legend>
				<f:with bean="${reportCommand}">
					<f:field property="ejercicio" widget-class="form-control"/>
					<f:field property="mes" widget-class="form-control"/>
					<f:field property="periodicidad" widget-class="form-control"/>
					<f:field property="concepto" widget-class="form-control"/>
					%{-- <div class="form-group">
					<label class="col-sm-2 control-label">Concepto</label>
					<g:select class="form-control"  
						name="concepto"
						from="${com.luxsoft.sw4.rh.ConceptoDeNomina.findAll().sort{it.tipo}}" 
						optionKey="id"/>
					</div> --}%
					
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
		%{-- <f:with bean="${reportCommand}">
			<f:field property="ejercicio" widget-class="form-control"/>
			<f:field property="mes" widget-class="form-control"/>
			<f:field property="periodicidad" widget-class="form-control"/>
			<div class="form-group">
				<label class="col-sm-2 control-label">Concepto</label>
				<div class="col-sm-10">
					<g:select class="form-control chosen-select"  
						name="concepto"
						from="${com.luxsoft.sw4.rh.ConceptoDeNomina.findAll().sort{it.tipo}}" 
						optionKey="id"/>
				</div>
			</div>
			
		</f:with> --}%
	</content>
	
</body>

</html>