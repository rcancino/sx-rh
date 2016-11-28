<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="editForm"/>
	<g:set var="entity" value="${calendarioDetInstance}" scope="request" />
	<g:set var="entityName" value="Periodo" scope="request" />
	<g:set var="editable" value="${false}" scope="request" />
	<g:set var="imprimible" value="${false}" scope="request" />
	<title>Calendario</title>
</head>
<body>

<content tag="header">
	{calendarioDetInstance.calendario}
</content>



<content tag="formFields">
	<f:with bean="calendarioDetInstance">
		<f:display property='folio' />
		<f:field property='mes' input-class="form-control">
		  <g:select id="mesField" class="form-control"  
              name="mes" 
              value="${calendarioDetInstance.mes}"
              noSelection="['':'-Seleccione un mes-']"
              from="${com.luxsoft.sw4.Mes.getMeses().collect{it.nombre}}"/>
		</f:field>

		<f:field property='bimestre' input-class="form-control">
			<g:select id="mesField" class="form-control"  
            	name="bimestre" 
            	value="${calendarioDetInstance.bimestre}"
            	noSelection="['':'-Seleccione un bimestre-']"
            	from="${(1..6)}"/>
		</f:field>
		<f:field property='inicio' input-class="form-control"/>
		<f:field property='fin' input-class="form-control"/>
		<f:field property='fechaDePago' input-class="form-control" label="Pago"/>
		<fieldset>
		    <legend><small>Asistencia:</small></legend>
		    <f:field property='asistencia.fechaInicial' input-class="form-control"/>
		    <f:field property='asistencia.fechaFinal' input-class="form-control"/>
		</fieldset>
	</f:with>
</content>

	
	
</body>
</html>

