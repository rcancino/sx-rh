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
	<content tag="formFields">
		<g:hiddenField name="reportName" value="AcumuladoDeNominasPorConcepto"/>
		<f:with bean="${reportCommand}">
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
			
		</f:with>
	</content>
	
</body>

</html>