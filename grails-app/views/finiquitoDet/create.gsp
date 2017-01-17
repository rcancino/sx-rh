<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="createForm"/>
	<g:set var="entity" value="${finiquitoDetInstance}" scope="request" />
	<g:set var="editable" value="${false}" scope="request" />
	<g:set var="imprimible" value="${false}" scope="request" />
	<title>Partida de finiquito (${finiquito.id})</title>
</head>
<body>

	<content tag="header">
		Agregar Percepción/Retención a Finiquito de : 
		${finiquito.empleado}
	</content>
	
	<content tag="backButton">
		<g:link controller="finiquito" action="show" class="btn btn-outline btn-primary" id="${finiquito.id}" >
    	<i class="fa fa-step-backward"></i> Regresar
		</g:link> 

	</content>
	
	<content tag="formFields">
		<f:with bean="${entity }">

			<g:hiddenField name="finiquito" value="${finiquito.id}"></g:hiddenField>
			<f:field property="tipo" widget-class="form-control"/>
			<f:field property="concepto" widget-class="form-control"/>
			<f:field property="importeGravado" widget="number" widget-class="form-control"/>
			<f:field property="importeExcento" widget="number" widget-class="form-control"/>
			
		</f:with>

		<!-- <script type="text/javascript">
			$(function(){
				
			});
		</script> -->
	</content>

	
	
	
	
</body>
</html>